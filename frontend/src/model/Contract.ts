export type Contract = {
    id: string;
    title: string;
    description: string;
    startDate: string;     // kommt als dd.MM.yyyy vom Backend
    endDate: string;       // kommt als dd.MM.yyyy vom Backend
    aiLevel: number;
    aiAnalysisText: string;
    fileName: string;
    file: number[] | null;   // Backend: byte[]  → Frontend: Uint8Array
};
