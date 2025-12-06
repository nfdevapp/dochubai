import axios from "axios";

import type { Contract } from "@/model/Contract";

const API_URL = "/api/contract";

export const getAllContracts = async (): Promise<Contract[]> => {
    const response = await axios.get<Contract[]>(API_URL);
    return response.data;
};

export const getContractById = async (id: string): Promise<Contract> => {
    const response = await axios.get<Contract>(`${API_URL}/${id}`);
    return response.data;
};

export const createContract = async (contract: Contract): Promise<Contract> => {
    const response = await axios.post<Contract>(API_URL, contract);
    return response.data;
};

export const updateContract = async (id: string, contract: Contract): Promise<Contract> => {
    const response = await axios.put<Contract>(`${API_URL}/${id}`, contract);
    return response.data;
};

export const deleteContract = async (id: string): Promise<void> => {
    await axios.delete(`${API_URL}/${id}`);
};
