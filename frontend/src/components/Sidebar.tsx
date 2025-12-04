"use client";

import { FileText, Receipt, FileSpreadsheet, Sparkles } from "lucide-react";

interface SidebarProps {
    onSelect: (title: string) => void;
}

export default function Sidebar({ onSelect }: SidebarProps) {
    return (
        <aside className="w-64 hidden md:flex flex-col gap-4 p-4 border-r border-sidebar-border bg-sidebar-background text-sidebar-foreground">
            <div className="text-lg font-semibold font-sans tracking-wide">DochubAi ✨</div>

            <nav className="flex flex-col gap-1 mt-4">
                <button
                    onClick={() => onSelect("Verträge")}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-left font-medium"
                >
                    <FileText size={18} />
                    Verträge
                </button>

                <button
                    onClick={() => onSelect("Zahlungsbelege")}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-left font-medium"
                >
                    <Receipt size={18} />
                    Zahlungsbelege
                </button>

                <button
                    onClick={() => onSelect("Rechnungen")}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-left font-medium"
                >
                    <FileSpreadsheet size={18} />
                    Rechnungen
                </button>
            </nav>

            <div className="mt-auto text-sm text-muted-foreground flex items-center gap-2">
                © {new Date().getFullYear()} DochubDashBoard
                <Sparkles size={16} />
            </div>
        </aside>
    );
}
