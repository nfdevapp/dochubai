"use client";

import { useEffect, useRef, useState } from "react";
import axios from "axios";

import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";

import {askWithContextWithHistory} from "@/api/ChatAiService";
import type { ApiError } from "@/api/ApiError";
import type { ChatAiDto } from "@/model/ChatAiDto";

interface Message {
    id: number;
    text: string;
    isUser: boolean;
}

const CHAT_STORAGE_KEY = "dochub-ai-chat";

export default function ChatAi() {
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);
    const [hydrated, setHydrated] = useState(false);

    const scrollRef = useRef<HTMLDivElement>(null);

    // Chatverlauf aus localStorage wiederherstellen
    useEffect(() => {
        try {
            const stored = localStorage.getItem(CHAT_STORAGE_KEY);
            if (stored) setMessages(JSON.parse(stored));
        } catch (e) {
            console.error("Chat wiederherstellen fehlgeschlagen", e);
        } finally {
            setHydrated(true);
        }
    }, []);

    // Chatverlauf nach Änderungen speichern (nur nach Hydration)
    useEffect(() => {
        if (!hydrated) return;
        localStorage.setItem(CHAT_STORAGE_KEY, JSON.stringify(messages));
    }, [messages, hydrated]);

    // Automatisch zum letzten Chatbeitrag scrollen
    useEffect(() => {
        if (!scrollRef.current) return;
        const lastMessage = scrollRef.current.querySelector(
            ".chat-last-message"
        ) as HTMLDivElement | null;
        lastMessage?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    // Hilfsfunktion: konvertiert Message[] zu ChatAiDto[]
    const convertToDto = (messages: Message[]): ChatAiDto[] => {
        const dtos: ChatAiDto[] = [];
        let tempDto: ChatAiDto = { userQuestion: "", aiAnswer: "" };

        messages.forEach((msg) => {
            if (msg.isUser) {
                // Neue User-Nachricht → neuen DTO starten
                if (tempDto.userQuestion || tempDto.aiAnswer) dtos.push(tempDto);
                tempDto = { userQuestion: msg.text, aiAnswer: "" };
            } else {
                tempDto.aiAnswer = msg.text;
                dtos.push(tempDto);
                tempDto = { userQuestion: "", aiAnswer: "" };
            }
        });

        // Restlichen DTO hinzufügen, falls noch nicht
        if (tempDto.userQuestion || tempDto.aiAnswer) dtos.push(tempDto);

        return dtos;
    };

    // Nachricht senden
    const sendMessage = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!input.trim() || loading) return;

        // Benutzer-Nachricht erstellen
        const userMessage: Message = {
            id: Date.now(),
            text: input,
            isUser: true,
        };

        // Benutzer-Nachricht sofort anzeigen
        setMessages((prev) => [...prev, userMessage]);
        setInput("");
        setLoading(true);

        try {
            // Chatverlauf in DTO umwandeln
            const history = convertToDto([...messages, userMessage]);

            // AI-Antwort abrufen
            const response = await askWithContextWithHistory(userMessage.text, history);

            // AI-Antwort zum Chat hinzufügen
            setMessages((prev) => [
                ...prev,
                {
                    id: Date.now() + 1,
                    text: response.aiAnswer,
                    isUser: false,
                },
            ]);
        } catch (err: unknown) {
            let errorMessage = "Fehler bei der AI-Anfrage";

            if (axios.isAxiosError<ApiError>(err)) {
                errorMessage = err.response?.data?.message ?? errorMessage;
            } else if (err instanceof Error) {
                errorMessage = err.message;
            }

            // Fehlermeldung zum Chat hinzufügen
            setMessages((prev) => [
                ...prev,
                {
                    id: Date.now() + 1,
                    text: errorMessage,
                    isUser: false,
                },
            ]);
        } finally {
            setLoading(false);
        }
    };

    // Funktion zum Löschen des Chatverlaufs
    const clearChat = () => {
        setMessages([]);
        localStorage.removeItem(CHAT_STORAGE_KEY);
    };

    return (
        <div className="h-screen flex flex-col">
            {/* Chatbereich */}
            <div className="flex-1 overflow-y-auto p-4" ref={scrollRef}>
                <div className="space-y-4">
                    {messages.map((msg, index) => (
                        <div
                            key={msg.id}
                            className={`flex ${msg.isUser ? "justify-end" : "justify-start"}`}
                        >
                            <div
                                className={`max-w-[70%] px-4 py-2 rounded-2xl text-sm ${
                                    msg.isUser
                                        ? "bg-neutral-800 text-white rounded-br-md"
                                        : "bg-neutral-200 text-black rounded-bl-md"
                                } ${index === messages.length - 1 ? "chat-last-message" : ""}`}
                            >
                                {msg.text}
                            </div>
                        </div>
                    ))}

                    {loading && (
                        <div className="flex justify-start">
                            <div className="bg-neutral-200 px-4 py-2 rounded-2xl text-sm chat-last-message">
                                AI denkt nach…
                            </div>
                        </div>
                    )}
                </div>
            </div>

            {/* Eingabebereich + Buttons in einer Zeile */}
            <form
                onSubmit={sendMessage}
                className="sticky bottom-0 border-t p-4 flex gap-2 bg-background"
            >
                {/* Button zum Chat löschen links */}
                <Button
                    type="button"
                    onClick={clearChat}
                    className="bg-red-500 text-white rounded-full px-4 py-1 cursor-pointer flex-shrink-0"
                >
                    Chat löschen
                </Button>

                {/* Eingabefeld für neue Nachricht */}
                <Textarea
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    placeholder="Stell deine Frage…"
                    disabled={loading}
                    className="resize-none flex-1"
                />

                {/* Button zum Senden der Nachricht */}
                <Button
                    type="submit"
                    disabled={loading}
                    className="bg-blue-500 text-white rounded-full px-4 py-1 cursor-pointer flex-shrink-0"
                >
                    Senden
                </Button>
            </form>
        </div>
    );
}
