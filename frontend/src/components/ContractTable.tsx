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

import { ArrowUpDown, ChevronDown, MoreHorizontal, Sparkles } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

import {
    DropdownMenu,
    DropdownMenuCheckboxItem,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

import {
    Table,
    TableHeader,
    TableRow,
    TableHead,
    TableBody,
    TableCell,
} from "@/components/ui/table";

import type { Contract } from "@/types/Contract";

// Testdaten aktualisiert (startDate + endDate)
const data: Contract[] = [
    { id: "1", title: "Mietvertrag", startDate: "2024-01-15", endDate: "2025-01-15", ai: 1 },
    { id: "2", title: "Arbeitsvertrag", startDate: "2023-11-03", endDate: "2024-11-03", ai: 2 },
    { id: "3", title: "Dienstleistungsvertrag", startDate: "2024-02-20", endDate: "2025-02-20", ai: 1 },
    { id: "4", title: "Kaufvertrag", startDate: "2022-08-09", endDate: "2023-08-09", ai: 3 },
    { id: "5", title: "Versicherungsvertrag", startDate: "2023-12-11", endDate: "2024-12-11", ai: 1 },
    { id: "6", title: "Leasingvertrag", startDate: "2022-06-01", endDate: "2025-06-01", ai: 2 },
    { id: "7", title: "Wartungsvertrag", startDate: "2024-03-01", endDate: "2025-03-01", ai: 1 },
    { id: "8", title: "Hostingvertrag", startDate: "2023-09-14", endDate: "2024-09-14", ai: 1 },
    { id: "9", title: "Supportvertrag", startDate: "2024-01-01", endDate: "2025-01-01", ai: 1 },
    { id: "10", title: "Kooperationsvertrag", startDate: "2022-11-22", endDate: "2023-11-22", ai: 2 },
    { id: "11", title: "Liefervertrag", startDate: "2023-01-10", endDate: "2024-01-10", ai: 1 },
    { id: "12", title: "Agenturvertrag", startDate: "2024-02-01", endDate: "2025-02-01", ai: 1 },
    { id: "13", title: "Lizenzvertrag", startDate: "2024-01-20", endDate: "2025-01-20", ai: 1 },
    { id: "14", title: "Bauvertrag", startDate: "2022-05-15", endDate: "2024-05-15", ai: 3 },
    { id: "15", title: "Projektvertrag", startDate: "2023-03-11", endDate: "2024-03-11", ai: 1 },
    { id: "16", title: "Beratungsvertrag", startDate: "2023-12-02", endDate: "2024-12-02", ai: 1 },
    { id: "17", title: "Darlehensvertrag", startDate: "2021-09-30", endDate: "2026-09-30", ai: 2 },
    { id: "18", title: "Schulungsvertrag", startDate: "2024-02-22", endDate: "2025-02-22", ai: 1 },
    { id: "19", title: "Cloud-Service-Vertrag", startDate: "2023-10-05", endDate: "2024-10-05", ai: 1 },
    { id: "20", title: "IT-Rahmenvertrag", startDate: "2024-01-03", endDate: "2026-01-03", ai: 1 },
    { id: "21", title: "Werkvertrag", startDate: "2022-12-12", endDate: "2024-12-12", ai: 2 },
    { id: "22", title: "ADV-Vertrag", startDate: "2023-11-22", endDate: "2024-11-22", ai: 1 },
    { id: "23", title: "Telekommunikationsvertrag", startDate: "2022-03-09", endDate: "2023-03-09", ai: 3 },
    { id: "24", title: "Providervertrag", startDate: "2023-07-12", endDate: "2024-07-12", ai: 1 },
    { id: "25", title: "Abovertrag", startDate: "2024-02-14", endDate: "2025-02-14", ai: 1 },
    { id: "26", title: "Überlassungsvertrag", startDate: "2021-11-01", endDate: "2024-11-01", ai: 2 },
    { id: "27", title: "Forschungsvertrag", startDate: "2023-05-18", endDate: "2024-05-18", ai: 1 },
    { id: "28", title: "Kreditvertrag", startDate: "2022-01-28", endDate: "2027-01-28", ai: 3 },
    { id: "29", title: "Vertriebsvertrag", startDate: "2023-08-30", endDate: "2024-08-30", ai: 1 },
    { id: "30", title: "Lizenzverlängerung", startDate: "2024-02-25", endDate: "2025-02-25", ai: 1 },
    { id: "31", title: "SaaS-Vertrag", startDate: "2023-09-01", endDate: "2024-09-01", ai: 1 },
    { id: "32", title: "Partnerschaftsvertrag", startDate: "2024-03-12", endDate: "2026-03-12", ai: 1 },
    { id: "33", title: "Wartungsvertrag Premium", startDate: "2022-10-10", endDate: "2024-10-10", ai: 2 },
];

const columns: ColumnDef<Contract>[] = [
    {
        accessorKey: "title",
        header: ({ column }) => (
            <Button
                variant="ghost"
                className="justify-start pl-6"
                onClick={() =>
                    column.toggleSorting(column.getIsSorted() === "asc")
                }
            >
                Titel
                <ArrowUpDown className="ml-2 h-4 w-4" />
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
                onClick={() =>
                    column.toggleSorting(column.getIsSorted() === "asc")
                }
            >
                Vertragsbeginn
                <ArrowUpDown className="ml-2 h-4 w-4" />
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
                onClick={() =>
                    column.toggleSorting(column.getIsSorted() === "asc")
                }
            >
                Vertragsende
                <ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("endDate")}</div>,
    },
    {
        accessorKey: "ai",
        header: ({ column }) => (
            <Button
                variant="ghost"
                className="pl-6 flex items-center"
                onClick={() =>
                    column.toggleSorting(column.getIsSorted() === "asc")
                }
            >
                <Sparkles className="h-4 w-4" />
                <ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => {
            const value = Number(row.getValue("ai"));

            const color = {
                1: "bg-green-500",
                2: "bg-yellow-400",
                3: "bg-red-500",
            }[value] ?? "bg-gray-400";

            return (
                <div className="pl-6 flex items-center">
                    <div className={`h-3 w-3 rounded-full ${color}`} />
                </div>
            );
        },
    },

    {
        id: "actions",
        enableHiding: false,
        cell: ({ row }) => {
            const contract = row.original;

            return (
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button variant="ghost" className="h-8 w-8 p-0">
                            <span className="sr-only">Open menu</span>
                            <MoreHorizontal className="h-4 w-4" />
                        </Button>
                    </DropdownMenuTrigger>

                    <DropdownMenuContent align="end">
                        <DropdownMenuLabel>Aktionen</DropdownMenuLabel>
                        <DropdownMenuItem
                            onClick={() => navigator.clipboard.writeText(contract.id)}
                        >
                            Vertrag-ID kopieren
                        </DropdownMenuItem>
                        <DropdownMenuSeparator />
                        <DropdownMenuItem>Vertrag öffnen</DropdownMenuItem>
                        <DropdownMenuItem>Herunterladen</DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            );
        },
    },
];

export default function ContractTable() {
    const [sorting, setSorting] = React.useState<SortingState>([]);
    const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([]);
    const [columnVisibility, setColumnVisibility] =
        React.useState<VisibilityState>({});
    const [rowSelection, setRowSelection] = React.useState({});

    const table = useReactTable({
        data,
        columns,
        onSortingChange: setSorting,
        onColumnFiltersChange: setColumnFilters,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
        getSortedRowModel: getSortedRowModel(),
        getFilteredRowModel: getFilteredRowModel(),
        onColumnVisibilityChange: setColumnVisibility,
        onRowSelectionChange: setRowSelection,
        state: {
            sorting,
            columnFilters,
            columnVisibility,
            rowSelection,
        },
    });

    return (
        <div className="w-full">
            <div className="flex items-center py-4">
                <Input
                    placeholder="Titel filtern..."
                    value={(table.getColumn("title")?.getFilterValue() as string) ?? ""}
                    onChange={(event) =>
                        table.getColumn("title")?.setFilterValue(event.target.value)
                    }
                    className="max-w-sm"
                />

                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button variant="outline" className="ml-auto">
                            Spalten <ChevronDown className="ml-2 h-4 w-4" />
                        </Button>
                    </DropdownMenuTrigger>

                    <DropdownMenuContent align="end">
                        {table
                            .getAllColumns()
                            .filter((column) => column.getCanHide())
                            .map((column) => (
                                <DropdownMenuCheckboxItem
                                    key={column.id}
                                    className="capitalize"
                                    checked={column.getIsVisible()}
                                    onCheckedChange={(value) =>
                                        column.toggleVisibility(!!value)
                                    }
                                >
                                    {column.id}
                                </DropdownMenuCheckboxItem>
                            ))}
                    </DropdownMenuContent>
                </DropdownMenu>
            </div>

            <div className="rounded-md border overflow-hidden">
                <Table>
                    <TableHeader>
                        {table.getHeaderGroups().map((headerGroup) => (
                            <TableRow key={headerGroup.id}>
                                {headerGroup.headers.map((header) => (
                                    <TableHead key={header.id}>
                                        {header.isPlaceholder
                                            ? null
                                            : flexRender(
                                                header.column.columnDef.header,
                                                header.getContext()
                                            )}
                                    </TableHead>
                                ))}
                            </TableRow>
                        ))}
                    </TableHeader>

                    <TableBody>
                        {table.getRowModel().rows?.length ? (
                            table.getRowModel().rows.map((row) => (
                                <TableRow key={row.id}>
                                    {row.getVisibleCells().map((cell) => (
                                        <TableCell key={cell.id}>
                                            {flexRender(
                                                cell.column.columnDef.cell,
                                                cell.getContext()
                                            )}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            ))
                        ) : (
                            <TableRow>
                                <TableCell colSpan={columns.length} className="h-24 text-center">
                                    Keine Ergebnisse.
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </div>

            <div className="flex items-center justify-end space-x-2 py-4">
                <div className="space-x-2">
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => table.previousPage()}
                        disabled={!table.getCanPreviousPage()}
                    >
                        Zurück
                    </Button>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => table.nextPage()}
                        disabled={!table.getCanNextPage()}
                    >
                        Weiter
                    </Button>
                </div>
            </div>
        </div>
    );
}
