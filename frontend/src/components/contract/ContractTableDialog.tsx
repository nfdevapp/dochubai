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
import type { Contract } from "@/model/Contract";

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
    // Lokale States für alle Formularfelder
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
            setStartDate(contract.startDate ? new Date(contract.startDate) : undefined);
            setEndDate(contract.endDate ? new Date(contract.endDate) : undefined);
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
            startDate,
            endDate,
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
                    {/* Titel */}
                    <div className="grid gap-3">
                        <Label htmlFor="title">Titel</Label>
                        <Input
                            id="title"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                        />
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

                    {/* Beschreibung */}
                    <div className="grid gap-3">
                        <Label htmlFor="description">Beschreibung</Label>
                        <Textarea
                            id="description"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            rows={4}
                            className="resize-none"
                        />
                    </div>

                    {/* AI Analyse */}
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
