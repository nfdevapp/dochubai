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

import { ArrowUpDown, Sparkles } from "lucide-react";
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
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";

import type { Contract } from "@/model/Contract";
import ContractTableDialog from "@/components/contract/ContractTableDialog";
import { getAllContracts } from "@/api/ContractService";

// -------------------------
// Haupt-Komponente
// -------------------------
export default function ContractTable() {
    const [contracts, setContracts] = React.useState<Contract[]>([]);
    const [loading, setLoading] = React.useState(true);

    const [sorting, setSorting] = React.useState<SortingState>([]);
    const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([]);
    const [columnVisibility, setColumnVisibility] = React.useState<VisibilityState>({});
    const [rowSelection, setRowSelection] = React.useState({});

    const [dialogOpen, setDialogOpen] = React.useState(false);
    const [selectedContract, setSelectedContract] = React.useState<string | null>(null);

    // Contracts laden
    React.useEffect(() => {
        const fetchContracts = async () => {
            try {
                const data = await getAllContracts();
                setContracts(data);
            } catch (err) {
                console.error("Fehler beim Laden der Verträge:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchContracts();
    }, []);

    const openNewContract = () => {
        setSelectedContract(null);
        setDialogOpen(true);
    };

    const openEditContract = (id: string) => {
        setSelectedContract(id);
        setDialogOpen(true);
    };

    const handleSave = (contract: Contract) => {
        setContracts((prev) => {
            const exists = prev.some((c) => c.id === contract.id);
            if (exists) return prev.map((c) => (c.id === contract.id ? contract : c));
            return [contract, ...prev];
        });
        setDialogOpen(false);
    };

    const handleDelete = (id: string) => {
        setContracts((prev) => prev.filter((c) => c.id !== id));
        setDialogOpen(false);
    };

    const table = useReactTable({
        data: contracts,
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
                    value={(table.getColumn("title")?.getFilterValue() as string) ?? ""}
                    onChange={(e) => table.getColumn("title")?.setFilterValue(e.target.value)}
                />
                <div className="ml-auto">
                    <Badge
                        className="cursor-pointer bg-blue-500 text-white rounded-full px-4 py-1"
                        onClick={openNewContract}
                    >
                        Neuen Vertrag anlegen
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
                                    onClick={() => openEditContract(row.original.id)}
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

            <ContractTableDialog
                open={dialogOpen}
                onOpenChange={setDialogOpen}
                contractId={selectedContract}
                onSave={handleSave}
                onDelete={handleDelete}
            />
        </div>
    );
}

// -------------------------
// Spalten Definition
// -------------------------
const columns: ColumnDef<Contract>[] = [
    {
        accessorKey: "title",
        header: ({ column }) => (
            <Button
                variant="ghost"
                className="justify-start pl-6"
                onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
            >
                Titel<ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("title")}</div>,
    },
    {
        accessorKey: "startDate",
        header: ({ column }) => (
            <Button
                variant="ghost"
                className="justify-start pl-6"
                onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
            >
                Vertragsbeginn<ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("startDate")}</div>,
    },
    {
        accessorKey: "endDate",
        header: ({ column }) => (
            <Button
                variant="ghost"
                className="justify-start pl-6"
                onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
            >
                Vertragsende<ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("endDate")}</div>,
    },
    {
        accessorKey: "description",
        header: () => <div className="pl-6 font-medium">Beschreibung</div>,
        cell: ({ row }) => <div className="pl-6">{row.getValue("description")}</div>,
    },
    {
        accessorKey: "aiLevel",
        header: ({ column }) => (
            <Button
                variant="ghost"
                className="pl-6 flex items-center gap-2"
                onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
            >
                <span className="font-medium">KI-Analyse</span>
                <Sparkles className="h-4 w-4" />
                <ArrowUpDown className="h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <AICell value={Number(row.getValue("aiLevel"))} />,
    },
];

// -------------------------
// AI-Level Hilfsdaten
// -------------------------
const AI_LEVELS = {
    1: { color: "bg-green-500", label: "Ist einwandfrei" },
    2: { color: "bg-yellow-400", label: "Sollte überprüft werden" },
    3: { color: "bg-red-500", label: "Weist kritische Abweichungen auf" },
} as const;

// Hilfskomponente für AI-Level-Zelle
const AICell = ({ value }: { value: number }) => {
    // Typ casten, damit TypeScript weiß, dass value ein gültiger Schlüssel ist
    const level = AI_LEVELS[value as keyof typeof AI_LEVELS] ?? { color: "bg-gray-400", label: "Keine Daten" };
    return (
        <div className="w-full flex justify-center items-center">
            <TooltipProvider>
                <Tooltip>
                    <TooltipTrigger>
                        <div className={`h-3 w-3 rounded-full ${level.color}`} />
                    </TooltipTrigger>
                    <TooltipContent>
                        <p>{level.label}</p>
                    </TooltipContent>
                </Tooltip>
            </TooltipProvider>
        </div>
    );
};


