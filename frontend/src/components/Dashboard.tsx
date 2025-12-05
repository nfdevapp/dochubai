"use client";

import { useState } from "react";
import Sidebar from "@/components/Sidebar";
import Header from "@/components/Header";
import ContractPage from "@/pages/ContractPage";

export default function Dashboard() {
    const [title, setTitle] = useState("Verträge");

    const pages: Record<string, React.ReactNode> = {
        "Verträge": <ContractPage />,
        "Zahlungsbelege": <div>Zahlungsbelege werden hier angezeigt…</div>,
        "Rechnungen": <div>Rechnungen werden hier angezeigt…</div>,
        "Einstellungen": <div>Einstellungen werden hier angezeigt…</div>,
    };

    return (
        <div className="min-h-screen flex bg-background text-foreground">
            <Sidebar onSelect={setTitle} />

            <div className="flex-1 flex flex-col">
                <Header title={title} />

                <main className="p-4 flex-1">
                    {pages[title] ?? <div>Seite nicht gefunden.</div>}
                </main>
            </div>
        </div>
    );
}
