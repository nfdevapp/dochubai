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
import type { Contract } from "@/model/Contract.tsx";


// Test Data
const data: Contract[] = [
    { id: "1", title: "Mietvertrag", startDate: "15.01.2024", endDate: "15.01.2025", description: "Mietvertrag für Büro- oder Wohnräume.", aiLevel: 1 },
    { id: "2", title: "Arbeitsvertrag", startDate: "03.11.2023", endDate: "03.11.2024", description: "Unbefristeter Arbeitsvertrag mit Standardklauseln.", aiLevel: 2 },
    { id: "3", title: "Dienstleistungsvertrag", startDate: "20.02.2024", endDate: "20.02.2025", description: "Vertrag über externe Dienstleistungen.", aiLevel: 1 },
    { id: "4", title: "Kaufvertrag", startDate: "09.08.2022", endDate: "09.08.2023", description: "Einmaliger Kaufvertrag für Waren oder Geräte.", aiLevel: 3 },
    { id: "5", title: "Versicherungsvertrag", startDate: "11.12.2023", endDate: "11.12.2024", description: "Standard-Versicherungspolice mit jährlicher Laufzeit.", aiLevel: 1 },
    { id: "6", title: "Leasingvertrag", startDate: "01.06.2022", endDate: "01.06.2025", description: "Leasingvertrag für technische Geräte oder Fahrzeuge.", aiLevel: 2 },
    { id: "7", title: "Wartungsvertrag", startDate: "01.03.2024", endDate: "01.03.2025", description: "Regelmäßige Wartung von IT-Systemen.", aiLevel: 1 },
    { id: "8", title: "Hostingvertrag", startDate: "14.09.2023", endDate: "14.09.2024", description: "Webhosting oder Serverhosting-Leistungen.", aiLevel: 1 },
    { id: "9", title: "Supportvertrag", startDate: "01.01.2024", endDate: "01.01.2025", description: "Technischer Support und Helpdesk.", aiLevel: 1 },
    { id: "10", title: "Kooperationsvertrag", startDate: "22.11.2022", endDate: "22.11.2023", description: "Partnerschaft für gemeinsame Projekte.", aiLevel: 2 },
    { id: "11", title: "Liefervertrag", startDate: "10.01.2023", endDate: "10.01.2024", description: "Regelmäßige Lieferung von Waren.", aiLevel: 1 },
    { id: "12", title: "Agenturvertrag", startDate: "01.02.2024", endDate: "01.02.2025", description: "Marketing- oder Vermittlungsagenturvertrag.", aiLevel: 1 },
    { id: "13", title: "Lizenzvertrag", startDate: "20.01.2024", endDate: "20.01.2025", description: "Lizenz zur Nutzung geistigen Eigentums.", aiLevel: 1 },
    { id: "14", title: "Bauvertrag", startDate: "15.05.2022", endDate: "15.05.2024", description: "Vertrag über Bauleistungen.", aiLevel: 3 },
    { id: "15", title: "Projektvertrag", startDate: "11.03.2023", endDate: "11.03.2024", description: "Befristeter Projektvertrag mit definiertem Umfang.", aiLevel: 1 },
    { id: "16", title: "Beratungsvertrag", startDate: "02.12.2023", endDate: "02.12.2024", description: "Externe Unternehmensberatung.", aiLevel: 1 },
    { id: "17", title: "Darlehensvertrag", startDate: "30.09.2021", endDate: "30.09.2026", description: "Kreditvereinbarung mit langfristiger Laufzeit.", aiLevel: 2 },
    { id: "18", title: "Schulungsvertrag", startDate: "22.02.2024", endDate: "22.02.2025", description: "Vertrag über Weiterbildungs- oder Schulungsleistungen.", aiLevel: 1 },
    { id: "19", title: "Cloud-Service-Vertrag", startDate: "05.10.2023", endDate: "05.10.2024", description: "Cloud-basierte IT-Services.", aiLevel: 1 },
    { id: "20", title: "IT-Rahmenvertrag", startDate: "03.01.2024", endDate: "03.01.2026", description: "Übergeordneter Vertrag über IT-Dienstleistungen.", aiLevel: 1 },
    { id: "21", title: "Werkvertrag", startDate: "12.12.2022", endDate: "12.12.2024", description: "Herstellung eines bestimmten Werkergebnisses.", aiLevel: 2 },
    { id: "22", title: "ADV-Vertrag", startDate: "22.11.2023", endDate: "22.11.2024", description: "Vertrag zur Auftragsdatenverarbeitung gemäß DSGVO.", aiLevel: 1 },
    { id: "23", title: "Telekommunikationsvertrag", startDate: "09.03.2022", endDate: "09.03.2023", description: "Mobilfunk oder Internet-Vertrag.", aiLevel: 3 },
    { id: "24", title: "Providervertrag", startDate: "12.07.2023", endDate: "12.07.2024", description: "Vertrag mit Internet- oder Service-Provider.", aiLevel: 1 },
    { id: "25", title: "Abovertrag", startDate: "14.02.2024", endDate: "14.02.2025", description: "Wiederkehrendes Abonnement für Dienstleistungen.", aiLevel: 1 },
    { id: "26", title: "Überlassungsvertrag", startDate: "01.11.2021", endDate: "01.11.2024", description: "Überlassung von Personal oder Gegenständen.", aiLevel: 2 },
    { id: "27", title: "Forschungsvertrag", startDate: "18.05.2023", endDate: "18.05.2024", description: "Kooperative Forschungstätigkeiten.", aiLevel: 1 },
    { id: "28", title: "Kreditvertrag", startDate: "28.01.2022", endDate: "28.01.2027", description: "Langfristiger Kreditvertrag mit festen Konditionen.", aiLevel: 3 },
    { id: "29", title: "Vertriebsvertrag", startDate: "30.08.2023", endDate: "30.08.2024", description: "Vertrieb von Produkten oder Dienstleistungen.", aiLevel: 1 },
    { id: "30", title: "Lizenzverlängerung", startDate: "25.02.2024", endDate: "25.02.2025", description: "Verlängerung einer bestehenden Lizenz.", aiLevel: 1 },
    { id: "31", title: "SaaS-Vertrag", startDate: "01.09.2023", endDate: "01.09.2024", description: "Software-as-a-Service-Nutzungslizenz.", aiLevel: 1 },
    { id: "32", title: "Partnerschaftsvertrag", startDate: "12.03.2024", endDate: "12.03.2026", description: "Langfristige Partnerschaft zwischen Unternehmen.", aiLevel: 1 },
    { id: "33", title: "Wartungsvertrag Premium", startDate: "10.10.2022", endDate: "10.10.2024", description: "Erweiterter Wartungs- und Servicevertrag.", aiLevel: 2 },
];


const columns: ColumnDef<Contract>[] = [
    {
        accessorKey: "title",
        header: ({ column }) => (
            <Button variant="ghost" className="justify-start pl-6" onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>
                Titel<ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("title")}</div>,
    },
    {
        accessorKey: "startDate",
        header: ({ column }) => (
            <Button variant="ghost" className="justify-start pl-6" onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>
                Vertragsbeginn<ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <div className="pl-6">{row.getValue("startDate")}</div>,
    },
    {
        accessorKey: "endDate",
        header: ({ column }) => (
            <Button variant="ghost" className="justify-start pl-6" onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>
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
            <Button variant="ghost" className="pl-6 flex items-center gap-2" onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>
                <span className="font-medium">AI-Analyse</span>
                <Sparkles className="h-4 w-4" />
                <ArrowUpDown className="h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => {
            const value = Number(row.getValue("aiLevel"));
            const color = { 1: "bg-green-500", 2: "bg-yellow-400", 3: "bg-red-500" }[value] ?? "bg-gray-400";
            const description = { 1: "Ist einwandfrei", 2: "Sollte überprüft werden", 3: "Weist kritische Abweichungen auf" }[value] ?? "Keine Daten";

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
    const [sorting, setSorting] = React.useState<SortingState>([]);
    const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([]);
    const [columnVisibility, setColumnVisibility] = React.useState<VisibilityState>({});
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
        state: { sorting, columnFilters, columnVisibility, rowSelection },
    });

    return (
        <div className="w-full">
            <div className="flex items-center py-4">
                <div className="ml-auto">
                    <Badge
                        className="cursor-pointer bg-blue-500 text-white rounded-full px-4 py-1"
                        onClick={() => onSelectContract?.({ id: "", title: "", startDate: "", endDate: "", description: "", aiLevel: 0 })}
                    >
                        Neuen Vertrag anlegen
                    </Badge>
                </div>
            </div>

            <div className="rounded-md border overflow-hidden">
                <Table>
                    <TableHeader>
                        {table.getHeaderGroups().map(headerGroup => (
                            <TableRow key={headerGroup.id}>
                                {headerGroup.headers.map(header => (
                                    <TableHead key={header.id}>
                                        {header.isPlaceholder ? null : flexRender(header.column.columnDef.header, header.getContext())}
                                    </TableHead>
                                ))}
                            </TableRow>
                        ))}
                    </TableHeader>
                    <TableBody>
                        {table.getRowModel().rows.length ? (
                            table.getRowModel().rows.map(row => (
                                <TableRow key={row.id} className="cursor-pointer hover:bg-muted/50" onClick={() => onSelectContract?.(row.original)}>
                                    {row.getVisibleCells().map(cell => (
                                        <TableCell key={cell.id}>{flexRender(cell.column.columnDef.cell, cell.getContext())}</TableCell>
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
                    <Button variant="outline" size="sm" onClick={() => table.previousPage()} disabled={!table.getCanPreviousPage()}>
                        Zurück
                    </Button>
                    <Button variant="outline" size="sm" onClick={() => table.nextPage()} disabled={!table.getCanNextPage()}>
                        Weiter
                    </Button>
                </div>
            </div>
        </div>
    );
}
