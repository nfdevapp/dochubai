import axios from "axios";

import type { Contract } from "@/model/Contract.ts";
import type {ApiError} from "@/api/ApiError.tsx";

const API_URL = "/api/contract";

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
export const getAllContracts = async (): Promise<Contract[]> => {
    try {
        const response = await axios.get<Contract[]>(API_URL);
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

export const getContractById = async (id: string): Promise<Contract> => {
    try {
        const response = await axios.get<Contract>(`${API_URL}/${id}`);
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

export const createContract = async (contract: Contract): Promise<Contract> => {
    try {
        const response = await axios.post<Contract>(API_URL, contract);
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

export const updateContract = async (id: string, contract: Contract): Promise<Contract> => {
    try {
        const response = await axios.put<Contract>(`${API_URL}/${id}`, contract);
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};

export const deleteContract = async (id: string): Promise<void> => {
    try {
        await axios.delete(`${API_URL}/${id}`);
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};
