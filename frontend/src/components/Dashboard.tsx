"use client";

import { useState } from "react";
import Sidebar from "./Sidebar";
import Header from "./Header";
import ContractPage from "@/pages/ContractPage";

export default function Dashboard() {
    const [title, setTitle] = useState("Verträge");

    return (
        <div className="min-h-screen flex bg-background text-foreground">
            <Sidebar onSelect={setTitle} />

            <div className="flex-1 flex flex-col">
                <Header title={title} />

                <main className="p-4 flex-1">
                    {title === "Verträge" && <ContractPage />}

                    {title === "Zahlungsbelege" && (
                        <div>Zahlungsbelege werden hier angezeigt…</div>
                    )}

                    {title === "Rechnungen" && (
                        <div>Rechnungen werden hier angezeigt…</div>
                    )}

                    {title === "Einstellungen" && (
                        <div>Einstellungen werden hier angezeigt…</div>
                    )}
                </main>
            </div>
        </div>
    );
}
