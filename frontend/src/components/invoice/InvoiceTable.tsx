"use client";

import * as React from "react";
import {
    type ColumnDef,
    type ColumnFiltersState,
    type SortingState,
    type VisibilityState,
    getCoreRowModel,
    getPaginationRowModel,
    getSortedRowModel,
    getFilteredRowModel,
    useReactTable,
    flexRender,
} from "@tanstack/react-table";

import { ArrowUpDown } from "lucide-react";
import { cn } from "@/lib/utils"
import { Skeleton } from "@/components/ui/skeleton";
import { Button } from "@/components/ui/button";
import {
    Table,
    TableHeader,
    TableRow,
    TableHead,
    TableBody,
    TableCell,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";

import type { Invoice } from "@/model/Invoice";
import InvoiceTableDialog from "@/components/invoice/InvoiceTableDialog";
import { getAllInvoices } from "@/api/InvoiceService";

// -------------------------
// Haupt-Komponente
// -------------------------
export default function InvoiceTable() {
    const [invoices, setInvoices] = React.useState<Invoice[]>([]);
    const [loading, setLoading] = React.useState(true);

    const [sorting, setSorting] = React.useState<SortingState>([]);
    const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([]);
    const [columnVisibility, setColumnVisibility] = React.useState<VisibilityState>({});
    const [rowSelection, setRowSelection] = React.useState({});

    const [dialogOpen, setDialogOpen] = React.useState(false);
    const [selectedInvoice, setSelectedInvoice] = React.useState<string | null>(null);

    // Invoices laden
    React.useEffect(() => {
        const fetchInvoices = async () => {
            try {
                const data = await getAllInvoices();
                setInvoices(data);
            } catch (err) {
                console.error("Fehler beim Laden der Abrechnungen:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchInvoices();
    }, []);

    const openNewInvoice = () => {
        setSelectedInvoice(null);
        setDialogOpen(true);
    };

    const openEditInvoice = (id: string) => {
        setSelectedInvoice(id);
        setDialogOpen(true);
    };

    const handleSave = (invoice: Invoice) => {
        setInvoices((prev) => {
            const exists = prev.some((c) => c.id === invoice.id);
            if (exists) return prev.map((c) => (c.id === invoice.id ? invoice : c));
            return [invoice, ...prev];
        });
        setDialogOpen(false);
    };

    const handleDelete = (id: string) => {
        setInvoices((prev) => prev.filter((c) => c.id !== id));
        setDialogOpen(false);
    };

    const table = useReactTable({
        data: invoices,
        columns,
        onSortingChange: setSorting,
        onColumnFiltersChange: setColumnFilters,
        onColumnVisibilityChange: setColumnVisibility,
        onRowSelectionChange: setRowSelection,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
        getSortedRowModel: getSortedRowModel(),
        getFilteredRowModel: getFilteredRowModel(),
        state: { sorting, columnFilters, columnVisibility, rowSelection },
    });

    if (loading) {
        return (
            <div className="w-full overflow-hidden rounded-md border">
                <table className="w-full border-collapse">
                    <thead>
                    <tr>
                        {columns.map((_, i) => (
                            <th key={i} className="p-2 border-b">
                                <Skeleton className="h-4 w-24" />
                            </th>
                        ))}
                    </tr>
                    </thead>
                    <tbody>
                    {Array.from({ length: 3 }).map((_, idx) => (
                        <tr key={idx}>
                            {columns.map((_, i) => (
                                <td key={i} className="p-2 border-b">
                                    <Skeleton className="h-4 w-full" />
                                </td>
                            ))}
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        );
    }

    return (
        <div className="w-full">
            <div className="flex items-center gap-4 py-4">
                <input
                    placeholder="Suche..."
                    className="px-3 py-2 rounded-md border border-input bg-transparent w-64"
                    value={(table.getColumn("purpose")?.getFilterValue() as string) ?? ""}
                    onChange={(e) => table.getColumn("purpose")?.setFilterValue(e.target.value)}
                />
                <div className="ml-auto">
                    <Badge
                        className="cursor-pointer bg-blue-500 text-white rounded-full px-4 py-1"
                        onClick={openNewInvoice}
                    >
                        Neue Abrechnung anlegen
                    </Badge>
                </div>
            </div>

            <div className="rounded-md border overflow-hidden">
                <Table>
                    <TableHeader>
                        {table.getHeaderGroups().map((hg) => (
                            <TableRow key={hg.id}>
                                {hg.headers.map((header) => (
                                    <TableHead key={header.id}>
                                        {header.isPlaceholder
                                            ? null
                                            : flexRender(header.column.columnDef.header, header.getContext())}
                                    </TableHead>
                                ))}
                            </TableRow>
                        ))}
                    </TableHeader>
                    <TableBody>
                        {table.getRowModel().rows.length ? (
                            table.getRowModel().rows.map((row) => (
                                <TableRow
                                    key={row.id}
                                    className="cursor-pointer hover:bg-muted/50"
                                    onClick={() => openEditInvoice(row.original.id)}
                                >
                                    {row.getVisibleCells().map((cell) => (
                                        <TableCell key={cell.id}>
                                            {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            ))
                        ) : (
                            <TableRow>
                                <TableCell colSpan={columns.length} className="text-center py-6">
                                    Keine Ergebnisse.
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </div>

            <div className="w-full mt-2 flex items-center">
                <div className="text-sm text-muted-foreground flex-1 text-center">
                    Seite {table.getState().pagination.pageIndex + 1} von {table.getPageCount()}
                </div>
                <div className="flex justify-end space-x-2">
                    <Button variant="outline" size="sm" onClick={() => table.previousPage()} disabled={!table.getCanPreviousPage()}>
                        Zurück
                    </Button>
                    <Button variant="outline" size="sm" onClick={() => table.nextPage()} disabled={!table.getCanNextPage()}>
                        Weiter
                    </Button>
                </div>
            </div>

            <InvoiceTableDialog
                open={dialogOpen}
                onOpenChange={setDialogOpen}
                invoiceId={selectedInvoice}
                onSave={handleSave}
                onDelete={handleDelete}
            />
        </div>
    );
}

// -------------------------
// Spalten Definition
// -------------------------
const columns: ColumnDef<Invoice>[] = [
    {
        accessorKey: "docNumber",
        header: ({ column }) => (
            <Button
                variant="ghost"
                className="justify-start pl-6"
                onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
            >
                Belegnummer<ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("docNumber")}</div>,
    },
    {
        accessorKey: "date",
        header: ({ column }) => (
            <Button
                variant="ghost"
                className="justify-start pl-6"
                onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
            >
                Datum<ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("date")}</div>,
    },
    {
        accessorKey: "purpose",
        header: () => <div className="pl-6 font-medium">Verwendungszweck</div>,
        cell: ({ row }) => <div className="pl-6">{row.getValue("purpose")}</div>,
    },
    {
        accessorKey: "amount",
        header: ({ column }) => (
            <Button
                variant="ghost"
                className="pl-4 justify-start"
                onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
            >
                Betrag
                <ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => {
            const amount = row.getValue<number>("amount");
            const isInvoice = row.original.isInvoice;

            return (
                <div className="pl-4">
                    <div
                        className={cn(
                            "w-[70px] text-right font-medium tabular-nums",
                            isInvoice === true && "text-green-600",
                            isInvoice === false && "text-red-600"
                        )}
                    >
                        {amount.toFixed(2).replace(".", ",")} €
                    </div>
                </div>
            );
        },
    },
];


