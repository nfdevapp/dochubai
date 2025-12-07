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
import { Textarea } from "@/components/ui/textarea";
import type { Contract } from "@/model/Contract.ts";

// Hilfsfunktion dd.MM.yyyy → Date
const parseDate = (dateStr: string): Date | undefined => {
    if (!dateStr) return undefined;
    const [day, month, year] = dateStr.split(".").map(Number);
    return new Date(year, month - 1, day);
};

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
    const [title, setTitle] = React.useState("");
    const [description, setDescription] = React.useState("");
    const [startDate, setStartDate] = React.useState<Date | undefined>();
    const [endDate, setEndDate] = React.useState<Date | undefined>();
    const [startOpen, setStartOpen] = React.useState(false);
    const [endOpen, setEndOpen] = React.useState(false);

    React.useEffect(() => {
        if (contract) {
            setTitle(contract.title);
            setDescription(contract.description);
            setStartDate(contract.startDate ? parseDate(contract.startDate) : undefined);
            setEndDate(contract.endDate ? parseDate(contract.endDate) : undefined);
        } else {
            setTitle("");
            setDescription("");
            setStartDate(undefined);
            setEndDate(undefined);
        }
    }, [contract]);

    if (!contract) return null;

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log({
            title,
            description,
            startDate: startDate ? format(startDate, "dd.MM.yyyy") : "",
            endDate: endDate ? format(endDate, "dd.MM.yyyy") : "",
        });
        onOpenChange(false);
    };

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="sm:max-w-[500px]">
                <DialogHeader>
                    <DialogTitle className="text-center w-full">Vertrag bearbeiten</DialogTitle>
                </DialogHeader>

                <form className="space-y-5 mt-4" onSubmit={handleSubmit}>
                    <div className="grid gap-3">
                        <Label htmlFor="title" className="font-bold">Titel</Label>
                        <Input id="title" value={title} onChange={(e) => setTitle(e.target.value)} />
                    </div>

                    <div className="grid gap-3">
                        <Label htmlFor="startDate" className="font-bold">Startdatum</Label>
                        <Popover open={startOpen} onOpenChange={setStartOpen}>
                            <PopoverTrigger asChild>
                                <Button variant="outline" className="w-full justify-start text-left font-normal">
                                    <CalendarIcon className="mr-2 h-4 w-4" />
                                    {startDate ? format(startDate, "dd.MM.yyyy") : "Datum wählen"}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar mode="single" selected={startDate} onSelect={(date) => { setStartDate(date); setStartOpen(false); }} />
                            </PopoverContent>
                        </Popover>
                    </div>

                    <div className="grid gap-3">
                        <Label htmlFor="endDate" className="font-bold">Enddatum</Label>
                        <Popover open={endOpen} onOpenChange={setEndOpen}>
                            <PopoverTrigger asChild>
                                <Button variant="outline" className="w-full justify-start text-left font-normal">
                                    <CalendarIcon className="mr-2 h-4 w-4" />
                                    {endDate ? format(endDate, "dd.MM.yyyy") : "Datum wählen"}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar mode="single" selected={endDate} onSelect={(date) => { setEndDate(date); setEndOpen(false); }} />
                            </PopoverContent>
                        </Popover>
                    </div>

                    <div className="grid gap-3">
                        <Label htmlFor="description" className="font-bold">Beschreibung</Label>
                        <Textarea id="description" value={description} onChange={(e) => setDescription(e.target.value)} rows={4} className="resize-none" />
                    </div>

                    <div className="grid gap-3">
                        <Label className="flex items-center gap-1 font-bold">
                            AI-Analyse
                            <Sparkles className="h-5 w-5 inline-block" />
                        </Label>
                        <span className={`${aiColorMap[Number(contract.aiLevel) || 0] || "text-gray-500"} font-semibold`}>
                                {aiDescriptionMap[Number(contract.aiLevel) || 0] || "Keine Daten"}
                        </span>
                    </div>


                    <DialogFooter>
                        <Button variant="outline" onClick={() => onOpenChange(false)}>Abbrechen</Button>
                        <Button type="submit">Speichern</Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}
