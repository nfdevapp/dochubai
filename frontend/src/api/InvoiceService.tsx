import axios from "axios";

import type { Invoice } from "@/model/Invoice";
import type {ApiError} from "@/api/ApiError";

const API_URL = "/api/invoice";

//Hilfsfunktion: fangt ApiError vom Backend ab und wirft die echte Fehlermeldung nach außen.
const handleAxiosError = (error: unknown) => {
    //prüfen ob Axios fehler ist
    if (axios.isAxiosError<ApiError>(error)) {
        if (error.response?.data) {
            // Backend-Fehlerobjekt weitergeben (ApiError)
            // Backend liefert z. B.
            // { message: "...", status: 404 }
            throw error.response.data;
        }
    }
    // falls es kein AxiosError ist
    throw new Error("Unexpected error");
};

//Promise repräsentiert einen zukünftigen Wert oder Fehler. Zukunft: einen Wert liefere oder einen Fehler melde. Vermeidet .then()
export const getAllInvoices = async (): Promise<Invoice[]> => {
    try {
        const response = await axios.get<Invoice[]>(API_URL);
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

export const getInvoiceById = async (id: string): Promise<Invoice> => {
    try {
        const response = await axios.get<Invoice>(`${API_URL}/${id}`);
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

export const createInvoice = async (invoice: Invoice): Promise<Invoice> => {
    try {
        const response = await axios.post<Invoice>(API_URL, invoice);
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

export const updateInvoice = async (id: string, invoice: Invoice): Promise<Invoice> => {
    try {
        const response = await axios.put<Invoice>(`${API_URL}/${id}`, invoice);
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

export const deleteInvoice = async (id: string): Promise<void> => {
    try {
        await axios.delete(`${API_URL}/${id}`);
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

export const getInvoiceChart = async (): Promise<Invoice[]> => {
    try {
        const response = await axios.get<Invoice[]>(`${API_URL}/chart`);
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

// z.B. src/api/InvoiceService.ts
export const getInvoiceAiAnalysis = async (): Promise<{ aiAnalysisText: string }> => {
    try {
        const response = await axios.get<{ aiAnalysisText: string }>(`${API_URL}/aianalysis`)
        return response.data
    } catch (err: unknown) {
        return handleAxiosError(err)
    }
}

