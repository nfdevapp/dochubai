"use client";

import * as React from "react";
import ContractTable from "@/components/contract/ContractTable";
import ContractTableDialog from "@/components/contract/ContractTableDialog";
import type {Contract} from "@/model/Contract";
import {getAllContracts} from "@/api/ContractService";

export default function ContractPage() {
    const [dialogOpen, setDialogOpen] = React.useState(false); // Dialog öffnen/schließen
    const [selectedContractId, setSelectedContractId] = React.useState<string | null>(null); // ausgewählter Vertrag
    const [contracts, setContracts] = React.useState<Contract[]>([]); // zentraler State aller Verträge
    const [loading, setLoading] = React.useState(true); // Ladezustand

    // Verträge beim Mounten laden
    React.useEffect(() => {
        const fetchContracts = async () => {
            try {
                const data = await getAllContracts();
                setContracts(data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchContracts();
    }, []);

    // Öffne Dialog für ausgewählten Vertrag
    const handleSelectContract = (id: string) => {
        setSelectedContractId(id);
        setDialogOpen(true);
    };

    // Vertrag löschen: aktualisiert State + schließt Dialog
    const handleDelete = (deletedId: string) => {
        setContracts((prev) => prev.filter((c) => c.id !== deletedId));
        setDialogOpen(false);
    };

    // Vertrag speichern oder neu anlegen: aktualisiert State + schließt Dialog
    const handleSave = (updatedContract: Contract) => {
        setContracts((prev) => {
            const index = prev.findIndex((c) => c.id === updatedContract.id);
            if (index !== -1) {
                const newArr = [...prev];
                newArr[index] = updatedContract;
                return newArr;
            } else {
                return [updatedContract, ...prev]; // neuer Vertrag oben
            }
        });
        setDialogOpen(false);
    };


    return (
        <div className="space-y-6">
            {/* Tabelle mit Verträgen */}
            <ContractTable
                contracts={contracts} // Daten aus Parent State
                loading={loading}      // Ladeanzeige
                onSelectContract={handleSelectContract} // öffnet Dialog
            />

            {/* Dialog für Vertrag bearbeiten / neu anlegen */}
            <ContractTableDialog
                open={dialogOpen}
                onOpenChange={setDialogOpen}
                contractId={selectedContractId}
                onDelete={handleDelete} // Callback nach Löschen
                onSave={handleSave}     // Callback nach Speichern
            />

            {/* Platz für weitere Komponenten */}
        </div>
    );
}
