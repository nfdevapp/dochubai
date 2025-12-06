"use client";

import * as React from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { CalendarIcon, Sparkles } from "lucide-react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { format } from "date-fns";
import type { Contract } from "@/model/Contract";
import { Textarea } from "@/components/ui/textarea";

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
    // Hooks immer oben aufrufen
    const [startDate, setStartDate] = React.useState<Date | undefined>(contract?.startDate ? new Date(contract.startDate) : undefined);
    const [endDate, setEndDate] = React.useState<Date | undefined>(contract?.endDate ? new Date(contract.endDate) : undefined);
    const [startOpen, setStartOpen] = React.useState(false); // Popover Startdatum
    const [endOpen, setEndOpen] = React.useState(false);     // Popover Enddatum

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

                    {/* Startdatum */}
                    <div className="grid gap-3">
                        <Label>Startdatum</Label>
                        <Popover open={startOpen} onOpenChange={setStartOpen}>
                            <PopoverTrigger asChild>
                                <Button variant="outline" className="w-full justify-start text-left font-normal">
                                    <CalendarIcon className="mr-2 h-4 w-4" />
                                    {startDate ? format(startDate, "dd.MM.yyyy") : "Datum wählen"}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar
                                    mode="single"
                                    selected={startDate}
                                    onSelect={(date) => {
                                        setStartDate(date);
                                        setStartOpen(false);
                                    }}
                                />
                            </PopoverContent>
                        </Popover>
                    </div>

                    {/* Enddatum */}
                    <div className="grid gap-3">
                        <Label>Enddatum</Label>
                        <Popover open={endOpen} onOpenChange={setEndOpen}>
                            <PopoverTrigger asChild>
                                <Button variant="outline" className="w-full justify-start text-left font-normal">
                                    <CalendarIcon className="mr-2 h-4 w-4" />
                                    {endDate ? format(endDate, "dd.MM.yyyy") : "Datum wählen"}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar
                                    mode="single"
                                    selected={endDate}
                                    onSelect={(date) => {
                                        setEndDate(date);
                                        setEndOpen(false);
                                    }}
                                />
                            </PopoverContent>
                        </Popover>
                    </div>

                    <div className="grid gap-3">
                        <Label htmlFor="description">Beschreibung</Label>
                        <Textarea
                            id="description"
                            name="description"
                            defaultValue={contract.description}
                            rows={4}
                            className="resize-none"
                        />
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
