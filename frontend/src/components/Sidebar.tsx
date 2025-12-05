"use client";

import { FileText, Receipt, FileSpreadsheet, Sparkles, Settings } from "lucide-react";
import type {JSX} from "react";

interface SidebarProps {
    onSelect: (title: string) => void;
}

export default function Sidebar({ onSelect }: SidebarProps): JSX.Element {
    return (
        <aside className="w-64 hidden md:flex flex-col gap-4 p-4 border-r border-sidebar-border bg-sidebar-background text-sidebar-foreground">
            <div className="text-lg font-semibold font-sans tracking-wide flex items-center">
                DocHubAi
                <Sparkles size={18} className="ml-2 h-4 w-4 text-yellow-500" />
                {/*<Sparkles size={18} className="ml-2 text-red-600" />*/}
            </div>

            <nav className="flex flex-col gap-1 mt-4">
                <button
                    onClick={() => onSelect("Verträge")}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-left font-medium"
                >
                    <FileText size={18} className="ml-2"/>
                    Verträge
                </button>

                <button
                    onClick={() => onSelect("Zahlungsbelege")}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-left font-medium"
                >
                    <Receipt size={18} className="ml-2"/>
                    Zahlungsbelege
                </button>

                <button
                    onClick={() => onSelect("Rechnungen")}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-left font-medium"
                >
                    <FileSpreadsheet size={18} className="ml-2"/>
                    Rechnungen
                </button>

                <button
                    onClick={() => onSelect("Einstellungen")}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-left font-medium"
                >
                    <Settings size={18} className="ml-2"/>
                    Einstellungen
                </button>
            </nav>

            <div className="mt-auto text-sm text-muted-foreground flex items-center gap-2">
                © {new Date().getFullYear()}
            </div>
        </aside>
    );
}
