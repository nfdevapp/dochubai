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
import { getAllContracts } from "@/api/ContractService";

// Spalten
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
                <span className="font-medium">AI-Analyse</span>
                <Sparkles className="h-4 w-4" />
                <ArrowUpDown className="h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => {
            const value = Number(row.getValue("aiLevel"));
            const color = { 1: "bg-green-500", 2: "bg-yellow-400", 3: "bg-red-500" }[value] ?? "bg-gray-400";
            const description = {
                1: "Ist einwandfrei",
                2: "Sollte überprüft werden",
                3: "Weist kritische Abweichungen auf",
            }[value] ?? "Keine Daten";

            return (
                <div className="w-full flex justify-center items-center">
                    <TooltipProvider>
                        <Tooltip>
                            <TooltipTrigger>
                                <div className={`h-3 w-3 rounded-full ${color}`} />
                            </TooltipTrigger>
                            <TooltipContent>
                                <p>{description}</p>
                            </TooltipContent>
                        </Tooltip>
                    </TooltipProvider>
                </div>
            );
        },
    },
];

type ContractTableProps = {
    onSelectContract?: (contract: Contract) => void;
};

export default function ContractTable({ onSelectContract }: ContractTableProps) {
    const [contracts, setContracts] = React.useState<Contract[]>([]);
    const [loading, setLoading] = React.useState(true);
    const [sorting, setSorting] = React.useState<SortingState>([]);
    const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([]);
    const [columnVisibility, setColumnVisibility] = React.useState<VisibilityState>({});
    const [rowSelection, setRowSelection] = React.useState({});

    React.useEffect(() => {
        const fetchContracts = async () => {
            try {
                const data = await getAllContracts();
                setContracts(data);
            } catch (error) {
                console.error("Fehler beim Laden der Verträge:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchContracts();
    }, []);

    const table = useReactTable({
        data: contracts,
        columns,
        onSortingChange: setSorting,
        onColumnFiltersChange: setColumnFilters,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
        getSortedRowModel: getSortedRowModel(),
        getFilteredRowModel: getFilteredRowModel(),
        onColumnVisibilityChange: setColumnVisibility,
        onRowSelectionChange: setRowSelection,
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
                    {Array.from({ length: columns.length }).map((_, idx) => (
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
                {/* Suchfeld links */}
                <input
                    placeholder="Suche..."
                    className="px-3 py-2 rounded-md border border-input bg-transparent w-64"
                    value={(table.getColumn("title")?.getFilterValue() as string) ?? ""}
                    onChange={(e) => table.getColumn("title")?.setFilterValue(e.target.value)}
                />

                <div className="ml-auto">
                    <Badge
                        className="cursor-pointer bg-blue-500 text-white rounded-full px-4 py-1"
                        onClick={() =>
                            onSelectContract?.({
                                id: "",
                                title: "",
                                startDate: "",
                                endDate: "",
                                description: "",
                                aiLevel: 0,
                                aiAnalysisText: "",
                            })
                        }
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
                                        {header.isPlaceholder ? null : flexRender(header.column.columnDef.header, header.getContext())}
                                    </TableHead>
                                ))}
                            </TableRow>
                        ))}
                    </TableHeader>

                    <TableBody>
                        {table.getRowModel().rows.length ? (
                            table.getRowModel().rows.map((row) => (
                                <TableRow key={row.id} className="cursor-pointer hover:bg-muted/50" onClick={() => onSelectContract?.(row.original)}>
                                    {row.getVisibleCells().map((cell) => (
                                        <TableCell key={cell.id}>{flexRender(cell.column.columnDef.cell, cell.getContext())}</TableCell>
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
        </div>
    );
}
