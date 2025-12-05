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

import { Button } from "@/components/ui/button.tsx";

import {
    Table,
    TableHeader,
    TableRow,
    TableHead,
    TableBody,
    TableCell,
} from "@/components/ui/table.tsx";
import { Badge } from "@/components/ui/badge";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip.tsx";
import type { Contract } from "@/types/Contract.tsx";
import ContractDialog from "@/components/contract/ContractDialog.tsx";

// Testdaten
const data: Contract[] = [
    { id: "1", title: "Mietvertrag", startDate: "2024-01-15", endDate: "2025-01-15", description: "Mietvertrag für Büro- oder Wohnräume.", ai: 1 },
    { id: "2", title: "Arbeitsvertrag", startDate: "2023-11-03", endDate: "2024-11-03", description: "Unbefristeter Arbeitsvertrag mit Standardklauseln.", ai: 2 },
    { id: "3", title: "Dienstleistungsvertrag", startDate: "2024-02-20", endDate: "2025-02-20", description: "Vertrag über externe Dienstleistungen.", ai: 1 },
    { id: "4", title: "Kaufvertrag", startDate: "2022-08-09", endDate: "2023-08-09", description: "Einmaliger Kaufvertrag für Waren oder Geräte.", ai: 3 },
    { id: "5", title: "Versicherungsvertrag", startDate: "2023-12-11", endDate: "2024-12-11", description: "Standard-Versicherungspolice mit jährlicher Laufzeit.", ai: 1 },
    { id: "6", title: "Leasingvertrag", startDate: "2022-06-01", endDate: "2025-06-01", description: "Leasingvertrag für technische Geräte oder Fahrzeuge.", ai: 2 },
    { id: "7", title: "Wartungsvertrag", startDate: "2024-03-01", endDate: "2025-03-01", description: "Regelmäßige Wartung von IT-Systemen.", ai: 1 },
    { id: "8", title: "Hostingvertrag", startDate: "2023-09-14", endDate: "2024-09-14", description: "Webhosting oder Serverhosting-Leistungen.", ai: 1 },
    { id: "9", title: "Supportvertrag", startDate: "2024-01-01", endDate: "2025-01-01", description: "Technischer Support und Helpdesk.", ai: 1 },
    { id: "10", title: "Kooperationsvertrag", startDate: "2022-11-22", endDate: "2023-11-22", description: "Partnerschaft für gemeinsame Projekte.", ai: 2 },
    { id: "11", title: "Liefervertrag", startDate: "2023-01-10", endDate: "2024-01-10", description: "Regelmäßige Lieferung von Waren.", ai: 1 },
    { id: "12", title: "Agenturvertrag", startDate: "2024-02-01", endDate: "2025-02-01", description: "Marketing- oder Vermittlungsagenturvertrag.", ai: 1 },
    { id: "13", title: "Lizenzvertrag", startDate: "2024-01-20", endDate: "2025-01-20", description: "Lizenz zur Nutzung geistigen Eigentums.", ai: 1 },
    { id: "14", title: "Bauvertrag", startDate: "2022-05-15", endDate: "2024-05-15", description: "Vertrag über Bauleistungen.", ai: 3 },
    { id: "15", title: "Projektvertrag", startDate: "2023-03-11", endDate: "2024-03-11", description: "Befristeter Projektvertrag mit definiertem Umfang.", ai: 1 },
    { id: "16", title: "Beratungsvertrag", startDate: "2023-12-02", endDate: "2024-12-02", description: "Externe Unternehmensberatung.", ai: 1 },
    { id: "17", title: "Darlehensvertrag", startDate: "2021-09-30", endDate: "2026-09-30", description: "Kreditvereinbarung mit langfristiger Laufzeit.", ai: 2 },
    { id: "18", title: "Schulungsvertrag", startDate: "2024-02-22", endDate: "2025-02-22", description: "Vertrag über Weiterbildungs- oder Schulungsleistungen.", ai: 1 },
    { id: "19", title: "Cloud-Service-Vertrag", startDate: "2023-10-05", endDate: "2024-10-05", description: "Cloud-basierte IT-Services.", ai: 1 },
    { id: "20", title: "IT-Rahmenvertrag", startDate: "2024-01-03", endDate: "2026-01-03", description: "Übergeordneter Vertrag über IT-Dienstleistungen.", ai: 1 },
    { id: "21", title: "Werkvertrag", startDate: "2022-12-12", endDate: "2024-12-12", description: "Herstellung eines bestimmten Werkergebnisses.", ai: 2 },
    { id: "22", title: "ADV-Vertrag", startDate: "2023-11-22", endDate: "2024-11-22", description: "Vertrag zur Auftragsdatenverarbeitung gemäß DSGVO.", ai: 1 },
    { id: "23", title: "Telekommunikationsvertrag", startDate: "2022-03-09", endDate: "2023-03-09", description: "Mobilfunk oder Internet-Vertrag.", ai: 3 },
    { id: "24", title: "Providervertrag", startDate: "2023-07-12", endDate: "2024-07-12", description: "Vertrag mit Internet- oder Service-Provider.", ai: 1 },
    { id: "25", title: "Abovertrag", startDate: "2024-02-14", endDate: "2025-02-14", description: "Wiederkehrendes Abonnement für Dienstleistungen.", ai: 1 },
    { id: "26", title: "Überlassungsvertrag", startDate: "2021-11-01", endDate: "2024-11-01", description: "Überlassung von Personal oder Gegenständen.", ai: 2 },
    { id: "27", title: "Forschungsvertrag", startDate: "2023-05-18", endDate: "2024-05-18", description: "Kooperative Forschungstätigkeiten.", ai: 1 },
    { id: "28", title: "Kreditvertrag", startDate: "2022-01-28", endDate: "2027-01-28", description: "Langfristiger Kreditvertrag mit festen Konditionen.", ai: 3 },
    { id: "29", title: "Vertriebsvertrag", startDate: "2023-08-30", endDate: "2024-08-30", description: "Vertrieb von Produkten oder Dienstleistungen.", ai: 1 },
    { id: "30", title: "Lizenzverlängerung", startDate: "2024-02-25", endDate: "2025-02-25", description: "Verlängerung einer bestehenden Lizenz.", ai: 1 },
    { id: "31", title: "SaaS-Vertrag", startDate: "2023-09-01", endDate: "2024-09-01", description: "Software-as-a-Service-Nutzungslizenz.", ai: 1 },
    { id: "32", title: "Partnerschaftsvertrag", startDate: "2024-03-12", endDate: "2026-03-12", description: "Langfristige Partnerschaft zwischen Unternehmen.", ai: 1 },
    { id: "33", title: "Wartungsvertrag Premium", startDate: "2022-10-10", endDate: "2024-10-10", description: "Erweiterter Wartungs- und Servicevertrag.", ai: 2 },
];

const columns: ColumnDef<Contract>[] = [
    {
        accessorKey: "title",
        header: ({ column }) => (
            <Button variant="ghost" className="justify-start pl-6" onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>Titel<ArrowUpDown className="ml-2 h-4 w-4" /></Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("title")}</div>,
    },
    {
        accessorKey: "startDate",
        header: ({ column }) => (
            <Button variant="ghost" className="justify-start pl-6" onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>Vertragsbeginn<ArrowUpDown className="ml-2 h-4 w-4" /></Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("startDate")}</div>,
    },
    {
        accessorKey: "endDate",
        header: ({ column }) => (
            <Button variant="ghost" className="justify-start pl-6" onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>Vertragsende<ArrowUpDown className="ml-2 h-4 w-4" /></Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("endDate")}</div>,
    },
    {
        accessorKey: "description",
        header: () => (
            <div className="pl-6 font-medium">
                Beschreibung
            </div>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("description")}</div>,
    },
    {
        accessorKey: "ai",
        header: ({ column }) => (
            <Button variant="ghost" className="pl-6 flex items-center" onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>
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

            const description = {
                1: "Ist einwandfrei",
                2: "Sollte überprüft werden",
                3: "Weist kritische Abweichungen auf",
            }[value] ?? "Keine Daten";

            return (
                <div className="pl-6 flex items-center">
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

export default function ContractTable() {
    const [sorting, setSorting] = React.useState<SortingState>([]);
    const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([]);
    const [columnVisibility, setColumnVisibility] = React.useState<VisibilityState>({});
    const [rowSelection, setRowSelection] = React.useState({});

    // Neue States
    const [dialogOpen, setDialogOpen] = React.useState(false);
    const [selectedContract, setSelectedContract] = React.useState<Contract | null>(null);

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
        state: { sorting, columnFilters, columnVisibility, rowSelection },
    });

    return (
        <div className="w-full">
            {/* Vertrag anlegen */}
            {/* Vertrag anlegen */}
            <div className="flex items-center py-4">
                <div className="ml-auto">
                    <Badge className="cursor-pointer bg-blue-500 text-white rounded-full px-4 py-1"
                           onClick={() => {
                               setSelectedContract({
                                   id: "",
                                   title: "",
                                   startDate: "",
                                   endDate: "",
                                   description: "",
                                   ai: 1,
                               });
                               setDialogOpen(true);
                           }}
                    >
                        Neuen Vertrag anlegen
                    </Badge>
                </div>
            </div>


            {/* Tabelle */}
            <div className="rounded-md border overflow-hidden">
                <Table>
                    <TableHeader>
                        {table.getHeaderGroups().map((headerGroup) => (
                            <TableRow key={headerGroup.id}>
                                {headerGroup.headers.map((header) => (
                                    <TableHead key={header.id}>{header.isPlaceholder ? null : flexRender(header.column.columnDef.header, header.getContext())}</TableHead>
                                ))}
                            </TableRow>
                        ))}
                    </TableHeader>

                    <TableBody>
                        {table.getRowModel().rows?.length ? (
                            table.getRowModel().rows.map((row) => (
                                <TableRow
                                    key={row.id}
                                    className="cursor-pointer hover:bg-muted/50"
                                    onClick={() => {
                                        setSelectedContract(row.original);
                                        setDialogOpen(true);
                                    }}
                                >
                                    {row.getVisibleCells().map((cell) => (
                                        <TableCell key={cell.id}>{flexRender(cell.column.columnDef.cell, cell.getContext())}</TableCell>
                                    ))}
                                </TableRow>
                            ))
                        ) : (
                            <TableRow>
                                <TableCell colSpan={columns.length} className="h-24 text-center">Keine Ergebnisse.</TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </div>

            {/* Pagination */}
            <div className="flex items-center justify-end space-x-2 py-4">
                <div className="space-x-2">
                    <Button variant="outline" size="sm" onClick={() => table.previousPage()} disabled={!table.getCanPreviousPage()}>Zurück</Button>
                    <Button variant="outline" size="sm" onClick={() => table.nextPage()} disabled={!table.getCanNextPage()}>Weiter</Button>
                </div>
            </div>

            {/* DETAILS-DIALOG */}
            <ContractDialog
                open={dialogOpen}
                onOpenChange={setDialogOpen}
                contract={selectedContract}
            />

        </div>
    );
}
