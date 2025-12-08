"use client";

import * as React from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { CalendarIcon, Sparkles, Upload } from "lucide-react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { format } from "date-fns";
import { Textarea } from "@/components/ui/textarea";
import type { Contract } from "@/model/Contract.ts";
import { Card, CardContent } from "@/components/ui/card.tsx";
import { useUploadFile } from "@better-upload/client";
import { deleteContract } from "@/api/ContractService";

import {
    AlertDialog,
    AlertDialogTrigger,
    AlertDialogContent,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogCancel
} from "@/components/ui/alert-dialog";


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

// Neuer UploadButton, speichert Datei nur lokal
function UploadButton({ accept, onFileSelect }: { accept?: string; onFileSelect?: (file: File) => void }) {
    return (
        <Button type="button">
            <label className="flex items-center gap-2 cursor-pointer">
                <Upload className="size-4" />
                Datei auswählen
                <input
                    type="file"
                    accept={accept}
                    className="hidden"
                    onChange={(e) => {
                        const file = e.target.files?.[0];
                        if (file && onFileSelect) onFileSelect(file);
                    }}
                />
            </label>
        </Button>
    );
}

export default function ContractTableDialog({ open, onOpenChange, contract }: ContractTableDialogProps) {
    const [startOpen, setStartOpen] = React.useState(false);
    const [endOpen, setEndOpen] = React.useState(false);

    const [title, setTitle] = React.useState("");
    const [description, setDescription] = React.useState("");
    const [startDate, setStartDate] = React.useState<Date | undefined>();
    const [endDate, setEndDate] = React.useState<Date | undefined>();
    const [aiLevel, setAiLevel] = React.useState(0);
    const [aiAnalysisText, setAiAnalysisText] = React.useState("");
    const [fileName, setFileName] = React.useState("");
    const [file, setFile] = React.useState<File | null>(null);

    const { control } = useUploadFile({ route: "D:/" });

    React.useEffect(() => {
        if (contract) {
            setTitle(contract.title);
            setDescription(contract.description);
            setStartDate(contract.startDate ? parseDate(contract.startDate) : undefined);
            setEndDate(contract.endDate ? parseDate(contract.endDate) : undefined);
            setAiLevel(contract.aiLevel);
            setAiAnalysisText(contract.aiAnalysisText);
            setFile(null);                  // Reset Datei
            setFileName(contract.fileName || ""); // Aktuellen Dateinamen setzen
        } else {
            setTitle("");
            setDescription("");
            setStartDate(undefined);
            setEndDate(undefined);
            setAiLevel(0);
            setAiAnalysisText("");
            setFile(null);
            setFileName("");
        }
    }, [contract]);



    if (!contract) return null;

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        let uploadedFileName = fileName;

        if (file) {
            const result = await control.upload(file);
            uploadedFileName = result?.file?.name || file.name;
            setFileName(uploadedFileName); // State aktualisieren
        }

        console.log({
            title,
            description,
            startDate: startDate ? format(startDate, "dd.MM.yyyy") : "",
            endDate: endDate ? format(endDate, "dd.MM.yyyy") : "",
            aiLevel,
            aiAnalysisText,
            fileName: uploadedFileName,
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
                    <div className="flex items-center justify-between gap-4">
                        <Label htmlFor="title" className="font-bold whitespace-nowrap">
                            Titel:
                        </Label>
                        <Input id="title" value={title} onChange={(e) => setTitle(e.target.value)} className="w-[350px]" />
                    </div>

                    {/* Startdatum */}
                    <div className="flex items-center justify-between gap-4">
                        <Label htmlFor="startDate" className="font-bold">
                            Startdatum:
                        </Label>
                        <Popover open={startOpen} onOpenChange={setStartOpen}>
                            <PopoverTrigger asChild>
                                <Button variant="outline" className="w-[350px] justify-start text-left font-normal">
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
                    <div className="flex items-center justify-between gap-4">
                        <Label htmlFor="endDate" className="font-bold">
                            Enddatum:
                        </Label>
                        <Popover open={endOpen} onOpenChange={setEndOpen}>
                            <PopoverTrigger asChild>
                                <Button variant="outline" className="w-[350px] justify-start text-left font-normal">
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
                        <Label htmlFor="description" className="font-bold">
                            Beschreibung:
                        </Label>
                        <Textarea
                            id="description"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            rows={2}
                            className="resize-none"
                        />
                    </div>

                    {/* AI-Analyse */}
                    <div className="flex items-center justify-between gap-4">
                        <Label className="flex items-center gap-1 font-bold whitespace-nowrap">
                            AI-Analyse <Sparkles className="h-5 w-5 inline-block" />:
                        </Label>
                        <div className="w-[330px] text-left">
                            <span
                                className={`${aiColorMap[Number(contract.aiLevel) || 0] || "text-gray-500"} font-semibold`}
                            >
                                {aiDescriptionMap[Number(contract.aiLevel) || 0] || "Keine Daten"}
                            </span>
                        </div>
                    </div>

                    {/* AI Text */}
                    <Card className="mt-2 border border-gray-200 bg-gray-50">
                        <CardContent className="p-3">{contract.aiAnalysisText || "Keine weiteren Informationen"}</CardContent>
                    </Card>
                    {/* Upload */}
                    <div className="grid gap-3">
                        <Label className="font-bold">
                            Dokument für KI-Analyse hochladen (.pdf, .doc, .docx)
                        </Label>

                        <UploadButton
                            accept=".pdf,.doc,.docx"
                            onFileSelect={(file) => {
                                setFile(file);            // Datei speichern
                                setFileName(file.name);   // Dateiname für Anzeige setzen
                            }}
                        />

                        <p className="mt-2 text-sm text-gray-700">
                            {file
                                ? `Ausgewählt: ${file.name}`
                                : contract.fileName
                                    ? `Aktuell gespeichert: ${contract.fileName}`
                                    : "Kein Dokument vorhanden"}
                        </p>
                    </div>


                    {/* Buttons */}
                    <DialogFooter className="flex justify-between">
                        <div className="flex gap-2">
                            <Button variant="outline" onClick={() => onOpenChange(false)}>
                                Abbrechen
                            </Button>
                            <Button type="submit" variant="outline">
                                Speichern
                            </Button>
                        </div>
                        <AlertDialog>
                            <AlertDialogTrigger asChild>
                                <Button
                                    type="button"
                                    variant="outline"
                                    className="hover:bg-red-400"
                                >
                                    Löschen
                                </Button>
                            </AlertDialogTrigger>

                            <AlertDialogContent>
                                <AlertDialogHeader>
                                    <AlertDialogTitle>Vertrag wirklich löschen?</AlertDialogTitle>
                                    <AlertDialogDescription>
                                        Dieser Vorgang kann nicht rückgängig gemacht werden.
                                    </AlertDialogDescription>
                                </AlertDialogHeader>

                                <AlertDialogFooter>
                                    <AlertDialogCancel asChild>
                                        <Button variant="outline">Abbrechen</Button>
                                    </AlertDialogCancel>
                                    <Button
                                        variant="destructive"
                                        onClick={async () => {
                                            if (!contract) return;
                                            try {
                                                await deleteContract(contract.id);
                                                console.log("Vertrag gelöscht:", contract.id);
                                                onOpenChange(false); // Dialog schließen
                                            } catch (err) {
                                                console.error("Fehler beim Löschen:", err);
                                                alert(err instanceof Error ? err.message : "Unbekannter Fehler");
                                            }
                                        }}
                                    >
                                        Löschen bestätigen
                                    </Button>
                                </AlertDialogFooter>
                            </AlertDialogContent>
                        </AlertDialog>
                    </DialogFooter>

                </form>
            </DialogContent>
        </Dialog>
    );
}
