"use client";

import * as React from "react";
import ContractTable from "@/components/contract/ContractTable";
import ContractTableDialog from "@/components/contract/ContractTableDialog";

export default function ContractPage() {
    const [dialogOpen, setDialogOpen] = React.useState(false);
    const [selectedContractId, setSelectedContractId] = React.useState<string | null>(null);

    const handleSelectContract = (id: string) => {
        setSelectedContractId(id);
        setDialogOpen(true);
    };

    return (
        <div className="space-y-6">
            <ContractTable onSelectContract={handleSelectContract} />

            <ContractTableDialog
                open={dialogOpen}
                onOpenChange={setDialogOpen}
                contractId={selectedContractId}
            />

            {/* Platzhalter für andere Komponenten */}
            {/*
            1. Lokaler State pro Komponente: ContractTable holt sich selbst die Daten (Axios).
            <ContractTable />
            2. Daten von der Seite weiterreichen. ContractPage holt die Daten (axios) und gibt sie als Props an die Unterkomponenten
            <ContractTable data={contracts} />
           */}
        </div>
    );
}
