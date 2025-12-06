"use client";

import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { CalendarIcon } from "lucide-react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { format } from "date-fns";
import { Sparkles } from "lucide-react";
import type { Contract } from "@/types/Contract";

interface ContractTableDialogProps {
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

export default function ContractTableDialog({ open, onOpenChange, contract }: ContractTableDialogProps) {
    if (!contract) return null;

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="sm:max-w-[500px]">
                <DialogHeader>
                    <DialogTitle className="text-center w-full">Vertrag bearbeiten</DialogTitle>
                </DialogHeader>

                <form className="space-y-5 mt-4" onSubmit={(e) => e.preventDefault()}>
                    <div className="grid gap-3">
                        <Label htmlFor="title">Titel</Label>
                        <Input id="title" name="title" defaultValue={contract.title} />
                    </div>

                    <div className="grid gap-3">
                        <Label htmlFor="description">Beschreibung</Label>
                        <Input id="description" name="description" defaultValue={contract.description} />
                    </div>

                    <div className="grid gap-3">
                        <Label>Startdatum</Label>
                        <Popover>
                            <PopoverTrigger asChild>
                                <Button
                                    variant="outline"
                                    className="w-full justify-start text-left font-normal"
                                >
                                    <CalendarIcon className="mr-2 h-4 w-4" />
                                    {contract.startDate ? format(new Date(contract.startDate), "dd.MM.yyyy") : "Datum wählen"}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar
                                    mode="single"
                                    selected={contract.startDate ? new Date(contract.startDate) : undefined}
                                />
                            </PopoverContent>
                        </Popover>
                    </div>

                    <div className="grid gap-3">
                        <Label>Enddatum</Label>
                        <Popover>
                            <PopoverTrigger asChild>
                                <Button
                                    variant="outline"
                                    className="w-full justify-start text-left font-normal"
                                >
                                    <CalendarIcon className="mr-2 h-4 w-4" />
                                    {contract.endDate ? format(new Date(contract.endDate), "dd.MM.yyyy") : "Datum wählen"}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar
                                    mode="single"
                                    selected={contract.endDate ? new Date(contract.endDate) : undefined}
                                />
                            </PopoverContent>
                        </Popover>
                    </div>

                    <p className="flex items-center gap-2 pt-2">
                        <strong className="flex items-center gap-1">
                            AI-Analyse
                            <Sparkles className="h-5 w-5 inline-block" />:
                        </strong>
                        <span
                            className={`${aiColorMap[Number(contract.aiLevel)] || "text-gray-500"} font-semibold`}
                        >
              {aiDescriptionMap[Number(contract.aiLevel)] || "Keine Daten"}
            </span>
                    </p>

                    <DialogFooter>
                        <Button variant="outline" onClick={() => onOpenChange(false)}>
                            Abbrechen
                        </Button>
                        <Button type="submit">Speichern</Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}
