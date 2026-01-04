"use client";

import { FileText, Receipt, Sparkles, Settings } from "lucide-react";
import ContractPage from "@/pages/ContractPage";
import InvoicePage from "@/pages/InvoicePage.tsx";

interface SidebarProps {
    setTitle: (title: string) => void;
    setPage: (page: React.ReactNode) => void;
}

export default function Sidebar({ setTitle, setPage }: SidebarProps) {
    return (
        <aside className="w-64 hidden md:flex flex-col gap-4 p-4 border-r border-sidebar-border bg-sidebar-background text-sidebar-foreground">
            <div className="text-lg font-semibold font-sans tracking-wide flex items-center">
                DocHubAi
                <Sparkles size={18} className="ml-2 h-4 w-4 text-yellow-500" />
            </div>

            <nav className="flex flex-col gap-1 mt-4">
                <button
                    onClick={() => {
                        setTitle("Verträge");
                        setPage(<ContractPage />);
                    }}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground font-medium"
                >
                    <FileText size={18} /> Verträge
                </button>

                <button
                    onClick={() => {
                        setTitle("Abrechnungen");
                        setPage(<InvoicePage />);
                    }}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground font-medium"
                >
                    <Receipt size={18} /> Abrechnungen
                </button>

                <button
                    onClick={() => {
                        setTitle("KI-Assistent");
                        setPage(<div>TODO KI-Assistent…</div>);
                    }}
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground font-medium"
                >
                    <Settings size={18} /> KI-Assistent
                </button>
            </nav>

            {/*<div className="mt-auto text-sm text-muted-foreground">*/}
            {/*    © {new Date().getFullYear()}*/}
            {/*</div>*/}
        </aside>
    );
}
