"use client";

import * as React from "react";
import ContractTable from "@/components/contract/ContractTable.tsx";
import ContractTableDialog from "@/components/contract/ContractTableDialog.tsx";
import type { Contract } from "@/model/Contract.tsx";

export default function ContractPage() {
    const [dialogOpen, setDialogOpen] = React.useState(false);
    const [selectedContract, setSelectedContract] = React.useState<Contract | null>(null);

    const handleSelectContract = (contract: Contract) => {
        setSelectedContract(contract);
        setDialogOpen(true);
    };

    return (
        <div className="space-y-6">
            <ContractTable onSelectContract={handleSelectContract} />
            <ContractTableDialog open={dialogOpen} onOpenChange={setDialogOpen} contract={selectedContract} />

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
