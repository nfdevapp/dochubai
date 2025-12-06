"use client";

import { useState } from "react";
import Sidebar from "@/components/Sidebar";
import Header from "@/components/Header";
import ContractPage from "@/pages/ContractPage";
import type { ReactNode } from "react";

export default function Dashboard() {
    const [title, setTitle] = useState<string>("Verträge");
    const [page, setPage] = useState<ReactNode>(<ContractPage />);

    return (
        <div className="min-h-screen flex bg-background text-foreground">
            {/* Sidebar */}
            <Sidebar setTitle={setTitle} setPage={setPage} />

            {/* Content Bereich */}
            <div className="flex-1 flex flex-col">
                {/* Header */}
                <Header title={title} />

                {/* Content */}
                <main className="p-4 flex-1">
                    {page}
                </main>
            </div>
        </div>
    );
}
