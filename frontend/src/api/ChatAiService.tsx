import axios from "axios";
import type { ApiError } from "@/api/ApiError";
import type { ChatAi } from "@/model/ChatAi";
import type {ChatAiDto} from "@/model/ChatAiDto.ts";

const API_URL = "/api/chatai/question";

const handleAxiosError = (error: unknown) => {
    if (axios.isAxiosError<ApiError>(error)) {
        if (error.response?.data) {
            throw error.response.data;
        }
    }
    throw new Error("Unexpected error");
};

export const askWithContextWithHistory = async (question: string, history: ChatAiDto[]): Promise<ChatAi> => {
    try {
        const response = await axios.post<ChatAi>(API_URL, {
            userQuestion: question,
            history: history, // kompletter Chatverlauf wird mitgeschickt
        });
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};
