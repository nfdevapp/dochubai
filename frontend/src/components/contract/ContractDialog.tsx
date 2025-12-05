"use client";

import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button";
import type { Contract } from "@/types/Contract.tsx";
import {Sparkles} from "lucide-react";

interface ContractDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    contract: Contract | null;
}

const aiColorMap: Record<number, string> = {
    1: "text-green-500",
    2: "text-yellow-500",
    3: "text-red-500",
};

const aiDescriptionMap: Record<number, string> = {
    1: "Ist einwandfrei",
    2: "Sollte überprüft werden",
    3: "Weist kritische Abweichungen auf",
};


export default function ContractDialog({ open, onOpenChange, contract }: ContractDialogProps) {
    if (!contract) return null;

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Vertragsdetails</DialogTitle>
                </DialogHeader>

                <div className="space-y-3 mt-4">
                    <p><strong>ID:</strong> {contract.id}</p>
                    <p><strong>Titel:</strong> {contract.title}</p>
                    <p><strong>Startdatum:</strong> {contract.startDate}</p>
                    <p><strong>Enddatum:</strong> {contract.endDate}</p>
                    <p><strong>Beschreibung:</strong> {contract.description}</p>
                    <p className="flex items-center gap-2">
                        <strong className="flex items-center gap-1">
                            AI-Analyse
                            <Sparkles className="h-5 w-5 inline-block" />
                            :
                        </strong>
                        <span className={`${aiColorMap[Number(contract.aiLevel)] || "text-gray-500"} font-semibold`}>
                            {aiDescriptionMap[Number(contract.aiLevel)] || "Keine Daten"}
                        </span>
                    </p>
                </div>
                <DialogFooter>
                    <Button variant="outline" onClick={() => onOpenChange(false)}>
                        Abbrechen
                    </Button>

                    <Button onClick={() => console.log("Speichern gedrückt")}>
                        Speichern
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
