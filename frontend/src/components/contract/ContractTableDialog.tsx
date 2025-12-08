"use client";

import * as React from "react";
import {
    Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { CalendarIcon, Sparkles, Upload } from "lucide-react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { format } from "date-fns";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent } from "@/components/ui/card";
import { useUploadFile } from "@better-upload/client";
import { deleteContract, getContractById, createContract, updateContract } from "@/api/ContractService";
import type { Contract } from "@/model/Contract";

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

// Hilfsfunktion: Konvertiert dd.MM.yyyy → Date
const parseDate = (dateStr: string): Date | undefined => {
    if (!dateStr) return undefined;
    const [day, month, year] = dateStr.split(".").map(Number);
    return new Date(year, month - 1, day);
};

// Props der Haupt-Komponente
interface ContractTableDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    contractId?: string | null;  // optional: Wenn null → neuer Vertrag
    onDelete?: (id: string) => void; // neu
    onSave?: (contract: Contract) => void; // neu
}

// Haupt-Komponente für Vertrags-Dialog
export default function ContractTableDialog({
                                                open,
                                                onOpenChange,
                                                contractId,
                                                onSave,
                                                onDelete
                                            }: ContractTableDialogProps) {

    // STATES: Formularfelder, Upload-Datei, Ladezustand
    const [loading, setLoading] = React.useState(false);
    const [title, setTitle] = React.useState("");
    const [description, setDescription] = React.useState("");
    const [startDate, setStartDate] = React.useState<Date | undefined>();
    const [endDate, setEndDate] = React.useState<Date | undefined>();
    const [aiLevel, setAiLevel] = React.useState(0);
    const [aiAnalysisText, setAiAnalysisText] = React.useState("");
    const [fileName, setFileName] = React.useState("");
    const [file, setFile] = React.useState<File | null>(null);
    //speichern
    const [saving, setSaving] = React.useState(false);
    //löschvorgang anzigen
    const [deleting, setDeleting] = React.useState(false);

    const { control } = useUploadFile({ route: "D:/" }); // Datei-Upload Hook

    // STATES für Popover (Kalender öffnen/schließen)
    const [startPopoverOpen, setStartPopoverOpen] = React.useState(false);
    const [endPopoverOpen, setEndPopoverOpen] = React.useState(false);

    // Lade Vertragsdaten, wenn ID gesetzt und Dialog geöffnet
    React.useEffect(() => {
        const loadContract = async () => {
            if (!open) return;

            if (!contractId) {
                // Neues Formular leeren
                setTitle("");
                setDescription("");
                setStartDate(undefined);
                setEndDate(undefined);
                setAiLevel(0);
                setAiAnalysisText("");
                setFile(null);
                setFileName("");
                return;
            }

            setLoading(true);
            try {
                const contract: Contract = await getContractById(contractId);
                // Felder füllen
                setTitle(contract.title);
                setDescription(contract.description);
                setStartDate(parseDate(contract.startDate));
                setEndDate(parseDate(contract.endDate));
                setAiLevel(contract.aiLevel);
                setAiAnalysisText(contract.aiAnalysisText);
                setFile(null);
                setFileName(contract.fileName);
            } catch (err) {
                console.error("Fehler beim Laden des Vertrags:", err);
            } finally {
                setLoading(false);
            }
        };
        loadContract();
    }, [contractId, open]);

    // Wenn Dialog geschlossen => nichts rendern
    if (!open) return null;

    // Formular-Submit: Datei hochladen
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        setSaving(true); // Speichern beginnt

        let uploadedFileName = fileName;
        let fileBytes: Uint8Array | null = null;

        if (file) {
            const result = await control.upload(file);
            uploadedFileName = result?.file?.name || file.name;
            fileBytes = new Uint8Array(await file.arrayBuffer());
        }

        const contractData: Omit<Contract, "id"> = {
            title,
            description,
            startDate: startDate ? format(startDate, "dd.MM.yyyy") : "",
            endDate: endDate ? format(endDate, "dd.MM.yyyy") : "",
            aiLevel,
            aiAnalysisText,
            fileName: uploadedFileName,
            file: fileBytes
        };

        try {
            let savedContract: Contract;

            if (!contractId) {
                savedContract = await createContract(contractData as Contract);
            } else {
                savedContract = await updateContract(contractId, { ...contractData, id: contractId } as Contract);
            }

            onSave?.(savedContract); // Parent Callback → Tabelle aktualisieren
            onOpenChange(false); // Dialog schließen
        } catch (err) {
            console.error("Fehler beim Speichern:", err);
        } finally {
            setSaving(false); // Speichern abgeschlossen
        }
    };

    // RENDER
    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="sm:max-w-[500px]">

                {/* Dialog Header */}
                <DialogHeader>
                    <DialogTitle className="text-center w-full">
                        {contractId ? "Vertrag bearbeiten" : "Neuen Vertrag anlegen"}
                        {saving && <div className="text-center py-2 text-sm">Daten werden gespeichert...</div>}
                    </DialogTitle>
                </DialogHeader>

                {/* Ladeanzeige */}
                {loading ? (
                    <div className="text-center py-10">Lade Daten...</div>
                ) : (
                    <form className="space-y-5 mt-4" onSubmit={handleSubmit}>

                        {/* TITEL */}
                        <div className="flex items-center justify-between gap-4">
                            <Label className="font-bold">Titel:</Label>
                            <Input value={title} onChange={(e) => setTitle(e.target.value)} className="w-[350px]" />
                        </div>

                        {/* STARTDATUM */}
                        <div className="flex items-center justify-between gap-4">
                            <Label className="font-bold">Startdatum:</Label>
                            <Popover open={startPopoverOpen} onOpenChange={setStartPopoverOpen}>
                                <PopoverTrigger asChild>
                                    <Button variant="outline" className="w-[350px] justify-start">
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
                                            setStartPopoverOpen(false); // Popover schließen
                                        }}
                                    />
                                </PopoverContent>
                            </Popover>
                        </div>

                        {/* ENDDATUM */}
                        <div className="flex items-center justify-between gap-4">
                            <Label className="font-bold">Enddatum:</Label>
                            <Popover open={endPopoverOpen} onOpenChange={setEndPopoverOpen}>
                                <PopoverTrigger asChild>
                                    <Button variant="outline" className="w-[350px] justify-start">
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
                                            setEndPopoverOpen(false); // Popover schließen
                                        }}
                                    />
                                </PopoverContent>
                            </Popover>
                        </div>

                        {/* BESCHREIBUNG */}
                        <div className="grid gap-3">
                            <Label className="font-bold">Beschreibung:</Label>
                            <Textarea value={description} onChange={(e) => setDescription(e.target.value)} rows={2} />
                        </div>

                        {/* AI ANALYSE */}
                        <div className="flex items-center justify-between gap-4">
                            <Label className="flex items-center gap-1 font-bold whitespace-nowrap">
                                AI-Analyse <Sparkles className="h-5 w-5 inline-block" />:
                            </Label>
                            <div className="w-[330px] text-left">
                                <span className={`${
                                    aiLevel === 1 ? "text-green-500" :
                                        aiLevel === 2 ? "text-yellow-500" :
                                            aiLevel === 3 ? "text-red-500" : "text-gray-500"
                                } font-semibold`}>
                                    {aiLevel === 1 ? "Ist einwandfrei"
                                        : aiLevel === 2 ? "Sollte überprüft werden"
                                            : aiLevel === 3 ? "Weist kritische Abweichungen auf"
                                                : "Keine Daten"}
                                </span>
                            </div>
                        </div>

                        {/* AI Analyse Text */}
                        <Card className="mt-2 border border-gray-200 bg-gray-50">
                            <CardContent className="p-3">
                                {aiAnalysisText || "Keine weiteren Informationen"}
                            </CardContent>
                        </Card>

                        {/* UPLOAD */}
                        <div className="grid gap-3">
                            <Label className="font-bold">Dokument hochladen:</Label>
                            <Button asChild>
                                <label className="cursor-pointer flex gap-2 items-center">
                                    <Upload className="size-4" />
                                    Datei wählen
                                    <input
                                        type="file"
                                        accept=".pdf,.doc,.docx"
                                        className="hidden"
                                        onChange={(e) => {
                                            const f = e.target.files?.[0];
                                            if (!f) return;
                                            setFile(f);
                                            setFileName(f.name);
                                        }}
                                    />
                                </label>
                            </Button>
                            <p className="text-sm text-gray-700">
                                {file ? `Ausgewählt: ${file.name}` :
                                    fileName ? `Aktuelle Datei: ${fileName}` : "Keine Datei vorhanden"}
                            </p>
                        </div>

                        {/* BUTTONS */}
                        <DialogFooter className="flex justify-between">
                            <div className="flex gap-2">
                                <Button variant="outline" onClick={() => onOpenChange(false)}>Abbrechen</Button>
                                <Button type="submit" variant="outline" disabled={saving}>
                                    {saving ? "Speichern..." : "Speichern"}
                                </Button>
                            </div>

                            {/* Löschen-Dialog */}
                            {contractId && (
                                <AlertDialog>
                                    <AlertDialogTrigger asChild>
                                        <Button type="button" variant="outline" className="hover:bg-red-400">
                                            Löschen
                                        </Button>
                                    </AlertDialogTrigger>

                                    <AlertDialogContent>
                                        <AlertDialogHeader>
                                            <AlertDialogTitle>Vertrag löschen?</AlertDialogTitle>
                                            <AlertDialogDescription>
                                                {deleting ? "Daten werden gelöscht..." : "Dieser Vorgang kann nicht rückgängig gemacht werden."}
                                            </AlertDialogDescription>
                                        </AlertDialogHeader>

                                        <AlertDialogFooter>
                                            <AlertDialogCancel asChild>
                                                <Button variant="outline" disabled={deleting}>Abbrechen</Button>
                                            </AlertDialogCancel>
                                            <Button
                                                variant="destructive"
                                                disabled={deleting}
                                                onClick={async () => {
                                                    if (!contractId) return;
                                                    setDeleting(true);
                                                    try {
                                                        await deleteContract(contractId);
                                                        onDelete?.(contractId); // Parent Callback aufrufen
                                                        onOpenChange(false);    // Dialog schließen
                                                    } catch (err) {
                                                        console.error("Fehler beim Löschen:", err);
                                                    } finally {
                                                        setDeleting(false);
                                                    }
                                                }}
                                            >
                                                {deleting ? "Löschen..." : "Löschen bestätigen"}
                                            </Button>
                                        </AlertDialogFooter>
                                    </AlertDialogContent>

                                </AlertDialog>
                            )}
                        </DialogFooter>
                    </form>
                )}
            </DialogContent>
        </Dialog>
    );
}
