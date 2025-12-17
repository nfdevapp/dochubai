"use client";

import * as React from "react";
import {
    Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {CalendarIcon, Upload, Check, ChevronsUpDown} from "lucide-react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { format } from "date-fns";
import { Textarea } from "@/components/ui/textarea";
import { useUploadFile } from "@better-upload/client";
import { deleteInvoice, getInvoiceById, createInvoice, updateInvoice } from "@/api/InvoiceService";
import { cn } from "@/lib/utils"
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandItem,
    CommandList,
} from "@/components/ui/command"
import type { Invoice } from "@/model/Invoice";
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
interface InvoiceTableDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    invoiceId?: string | null;
    onDelete?: (id: string) => void;
    onSave?: (invoice: Invoice) => void;
}

// Haupt-Komponente
export default function InvoiceTableDialog({
                                                open,
                                                onOpenChange,
                                                invoiceId,
                                                onSave,
                                                onDelete
                                            }: InvoiceTableDialogProps) {

    const [loading, setLoading] = React.useState(false);
    const [docNumber, setDocNumber] = React.useState("");
    const [purpose, setPurpose] = React.useState("");
    const [date, setDate] = React.useState<Date | undefined>();
    const [isInvoice, setIsInvoice] = React.useState<boolean>();
    const [fileName, setFileName] = React.useState("");
    const [file, setFile] = React.useState<File | null>(null);
    const [saving, setSaving] = React.useState(false);
    const [deleting, setDeleting] = React.useState(false);
    // Betrag states => Eingaben double werte
    const [amount, setAmount] = React.useState<number | undefined>(undefined);
    const [amountInput, setAmountInput] = React.useState<string>("");

    // Abrechnung
    const [invoiceType, setInvoiceType] = React.useState<string>("Rechnung");
    const [invoiceTypeOpen, setInvoiceTypeOpen] = React.useState(false);

    const { control } = useUploadFile({ route: "D:/" });

    const [startPopoverOpen, setStartPopoverOpen] = React.useState(false);

    // Lade Vertragsdaten
    React.useEffect(() => {
        const loadInvoice = async () => {
            if (!open) return;

            if (!invoiceId) {
                setDocNumber("");
                setPurpose("");
                setDate(undefined);
                setAmount(0);
                setAmountInput("");
                setIsInvoice(undefined);
                setFile(null);
                setFileName("");
                return;
            }

            setLoading(true);
            try {
                const invoice: Invoice = await getInvoiceById(invoiceId);
                setDocNumber(invoice.docNumber);
                setPurpose(invoice.purpose);
                setDate(parseDate(invoice.date));
                setAmount(invoice.amount);
                setIsInvoice(invoice.isInvoice);
                setFile(null);
                setFileName(invoice.fileName);
                // Base64 sichern für Download
                (window as { __currentFileBase64?: string }).__currentFileBase64 = invoice.fileBase64 ?? undefined;
            } catch (err) {
                console.error("Fehler beim Laden des Vertrags:", err);
            } finally {
                setLoading(false);
            }
        };
        loadInvoice();
    }, [invoiceId, open]);

    if (!open) return null;

    // Base64-Konvertierung
    const fileToBase64 = (file: File): Promise<string> => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => resolve(reader.result as string);
            reader.onerror = (err) => reject(err);
            reader.readAsDataURL(file);
        });
    };

    const handleDownload = () => {
        if (!fileName) return;

        let blob: Blob;

        // Falls neue Datei hochgeladen → direkt File nutzen
        if (file) {
            blob = new Blob([file], { type: file.type });
        }
        // Falls Vertrag aus Backend → Base64 → Blob
        else {
            const base64 = (window as { __currentFileBase64?: string }).__currentFileBase64;
            if (!base64) {
                console.error("Keine Base64 Datei vorhanden");
                return;
            }

            // BASE64 → Bytearray
            const byteChars = atob(base64);
            const byteNumbers = new Array(byteChars.length);
            for (let i = 0; i < byteChars.length; i++) {
                byteNumbers[i] = byteChars.charCodeAt(i);
            }
            const byteArray = new Uint8Array(byteNumbers);

            const ext = fileName.split(".").pop()?.toLowerCase();

            const mime =
                ext === "pdf" ? "application/pdf" :
                    ext === "doc" ? "application/msword" :
                        ext === "docx" ? "application/vnd.openxmlformats-officedocument.wordprocessingml.document" :
                            "application/octet-stream";

            blob = new Blob([byteArray], { type: mime });
        }

        // Browser zeigt Speichern-unter-Dialog an
        const url = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = fileName;
        a.click();
        URL.revokeObjectURL(url);
    };

    // Formular-Submit
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setSaving(true);

        try {
            let uploadedFileName = fileName;
            let fileBase64: string | null = null;

            if (file) {
                const result = await control.upload(file);
                uploadedFileName = result?.file?.name || file.name;
                //ohne prefix
                fileBase64 = (await fileToBase64(file)).split(",")[1];
            }

            const invoice: Invoice = {
                id: invoiceId ?? "",
                docNumber: docNumber,
                purpose: purpose,
                date: date ? format(date, "dd.MM.yyyy") : "",
                amount: amount ?? 0,
                isInvoice: isInvoice ?? false,
                fileName: uploadedFileName,
                fileBase64: fileBase64
            };

            let savedInvoice: Invoice;
            if (!invoiceId) {
                savedInvoice = await createInvoice(invoice);
            } else {
                savedInvoice = await updateInvoice(invoiceId, invoice);
            }

            onSave?.(savedInvoice);
            onOpenChange(false);
        } catch (err) {
            console.error("Fehler beim Speichern:", err);
        } finally {
            setSaving(false);
        }
    };

    // RENDER
    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="sm:max-w-[500px]">
                <DialogHeader>
                    <DialogTitle className="text-center w-full">
                        {invoiceId ? "Abrechnung bearbeiten" : "Neue Abrechnung anlegen"}
                        {saving && (
                            <div className="flex items-center justify-center gap-3 mt-3 p-3 rounded-lg
                                bg-blue-50 border border-blue-200 text-blue-700 text-sm">
                                <span className="font-medium">
                                    Daten werden gespeichert…
                                </span>
                            </div>
                        )}
                    </DialogTitle>
                </DialogHeader>

                {loading ? (
                    <div className="text-center py-10">Lade Daten...</div>
                ) : (
                    <form className="space-y-5 mt-4" onSubmit={handleSubmit}>

                        {/* Belegnummer*/}
                        <div className="flex items-center justify-between gap-4">
                            <Label className="font-bold">Belegnummer:</Label>
                            <Input value={docNumber} onChange={(e) => setDocNumber(e.target.value)} className="w-[350px]" />
                        </div>

                        {/* Beitrag */}
                        <div className="flex items-center justify-between gap-4">
                            <Label className="font-bold">Beitrag:</Label>
                            <Input
                                type="text"
                                value={amountInput}
                                onChange={(e) => {
                                    // Nur Zahlen, Punkt oder Komma erlauben
                                    const val = e.target.value.replace(/[^0-9.,]/g, "");

                                    // Maximal ein Komma oder Punkt erlauben
                                    const parts = val.split(/[.,]/);
                                    let formatted = parts[0];
                                    if (parts.length > 1) {
                                        formatted += "," + parts[1];
                                    }

                                    setAmountInput(formatted);
                                }}
                                onBlur={() => {
                                    // Beim Verlassen des Felds in number umwandeln
                                    if (amountInput) {
                                        const numeric = parseFloat(amountInput.replace(",", "."));
                                        setAmount(numeric);
                                    } else {
                                        setAmount(undefined);
                                    }
                                }}
                                className="w-[350px]"
                                placeholder="0,00"
                            />
                        </div>


                        {/* Datum */}
                        <div className="flex items-center justify-between gap-4">
                            <Label className="font-bold">Datum:</Label>
                            <Popover open={startPopoverOpen} onOpenChange={setStartPopoverOpen}>
                                <PopoverTrigger asChild>
                                    <Button variant="outline" className="w-[350px] justify-start">
                                        <CalendarIcon className="mr-2 h-4 w-4" />
                                        {date ? format(date, "dd.MM.yyyy") : "Datum wählen"}
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto p-0">
                                    <Calendar
                                        mode="single"
                                        selected={date}
                                        onSelect={(date) => {
                                            setDate(date);
                                            setStartPopoverOpen(false);
                                        }}
                                    />
                                </PopoverContent>
                            </Popover>
                        </div>

                        {/* Abrechnungsart */}
                        <div className="flex items-center justify-between gap-4">
                            <Label className="font-bold">Abrechnung:</Label>
                            <Popover open={invoiceTypeOpen} onOpenChange={setInvoiceTypeOpen}>
                                <PopoverTrigger asChild>
                                    <Button
                                        variant="outline"
                                        role="combobox"
                                        aria-expanded={invoiceTypeOpen}
                                        className="w-[350px] justify-between"
                                    >
                                        {invoiceType} {}
                                        <ChevronsUpDown className="opacity-50" />
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-[350px] p-0">
                                    <Command>
                                        <CommandList>
                                            <CommandEmpty>Keine Option gefunden.</CommandEmpty>
                                            <CommandGroup>
                                                {["Rechnung", "Zahlungsbeleg"].map((type) => (
                                                    <CommandItem
                                                        key={type}
                                                        value={type}
                                                        onSelect={(currentValue) => {
                                                            setInvoiceType(currentValue === invoiceType ? "" : currentValue);
                                                            setInvoiceTypeOpen(false);
                                                        }}
                                                    >
                                                        {type}
                                                        <Check
                                                            className={cn(
                                                                "ml-auto",
                                                                invoiceType === type ? "opacity-100" : "opacity-0"
                                                            )}
                                                        />
                                                    </CommandItem>
                                                ))}
                                            </CommandGroup>
                                        </CommandList>
                                    </Command>
                                </PopoverContent>
                            </Popover>
                        </div>

                        {/* Verwendungszweck */}
                        <div className="grid gap-3">
                            <Label className="font-bold">Verwendungszweck:</Label>
                            <Textarea value={purpose} onChange={(e) => setPurpose(e.target.value)} rows={2} />
                        </div>

                        {/* Upload + Download */}
                        <div className="grid gap-3">
                            <Label className="font-bold">Zahlungsbeleg/Rechnung anhängen:</Label>
                            <div className="flex gap-4">
                                {/* Upload */}
                                <Button
                                    asChild
                                    className="flex-1 justify-center"
                                    variant="outline"
                                >
                                    <label className="cursor-pointer flex items-center justify-center gap-2 w-full">
                                        Datei wählen
                                        <input
                                            type="file"
                                            accept=".pdf,.jpg,.png,.docx"
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
                                {/* Download */}
                                <Button
                                    className="flex-1 justify-center"
                                    type="button" // verhindert, dass das Formular abgeschickt wird
                                    variant="outline"
                                    disabled={!fileName}
                                    onClick={() => {
                                        handleDownload();
                                        onOpenChange(false);
                                    }}
                                >
                                    <Upload className="rotate-180 size-4" />
                                    Herunterladen
                                </Button>

                            </div>
                            {/* Dateinamen unter Buttons */}
                            <p className="text-sm text-gray-700">
                                {file ? `Ausgewählt: ${file.name}` :
                                    fileName ? `Aktuelle Datei: ${fileName}` : "Keine Datei vorhanden"}
                            </p>
                        </div>


                        {/* Buttons */}
                        <DialogFooter className="flex justify-between">
                            <div className="flex gap-2">
                                <Button variant="outline" onClick={() => onOpenChange(false)}>Abbrechen</Button>
                                <Button type="submit" variant="outline" disabled={saving}>
                                    {saving ? "Speichern..." : "Speichern"}
                                </Button>
                            </div>

                            {/* Löschen */}
                            {invoiceId && (
                                <AlertDialog>
                                    <AlertDialogTrigger asChild>
                                        <Button type="button" variant="outline" className="hover:bg-red-400">Löschen</Button>
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
                                                    if (!invoiceId) return;
                                                    setDeleting(true);
                                                    try {
                                                        await deleteInvoice(invoiceId);
                                                        onDelete?.(invoiceId);
                                                        onOpenChange(false);
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
