export type Invoice = {
    id: string;
    docNumber: string; // Belegnummer
    date: string; // kommt als dd.MM.yyyy vom Backend
    amount: number; // Preis
    purpose: string; // Verwendungszweck
    isInvoice: boolean; // Rechnung oder Zahlungsbeleg
    fileType: string; // Dateityp
    fileName: string; // Dateiname
    fileBase64: string | null; // Base64 String vom Backend
};
