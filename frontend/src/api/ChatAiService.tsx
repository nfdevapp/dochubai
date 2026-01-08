import axios from "axios";
import type { ApiError } from "@/api/ApiError";
import type { ChatAi } from "@/model/ChatAi";

const API_URL = "/api/chatai/question";

const handleAxiosError = (error: unknown) => {
    if (axios.isAxiosError<ApiError>(error)) {
        if (error.response?.data) {
            throw error.response.data;
        }
    }
    throw new Error("Unexpected error");
};

export const askAiQuestion = async (question: string): Promise<ChatAi> => {
    try {
        const response = await axios.post<ChatAi>(API_URL, {
            userQuestion: question,
        });
        return response.data;
    } catch (err: unknown) {
        return handleAxiosError(err);
    }
};
