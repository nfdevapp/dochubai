"use client";

import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button";
import type { Contract } from "@/types/Contract.tsx";

interface ContractDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    contract: Contract | null;
}

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
                    <p><strong>AI-Level:</strong> {contract.ai}</p>
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
