"use client";

import { useEffect, useRef, useState } from "react";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";

interface Message {
    id: number;
    text: string;
    isUser: boolean;
}

export default function AiChat() {
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState("");
    const scrollRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        scrollRef.current?.scrollTo({
            top: scrollRef.current.scrollHeight,
            behavior: "smooth",
        });
    }, [messages]);

    const sendMessage = (e: React.FormEvent) => {
        e.preventDefault();
        if (!input.trim()) return;

        setMessages((prev) => [
            ...prev,
            { id: Date.now(), text: input, isUser: true },
        ]);

        setInput("");

        // Demo-Antwort
        setTimeout(() => {
            setMessages((prev) => [
                ...prev,
                {
                    id: Date.now() + 1,
                    text: "Das ist eine Beispiel-Antwort.",
                    isUser: false,
                },
            ]);
        }, 600);
    };

    return (
        <div className="h-screen flex flex-col">
            {/* Chat */}
            <div className="flex-1 overflow-hidden">
                <ScrollArea className="h-full p-4" ref={scrollRef}>
                    <div className="space-y-4">
                        {messages.map((msg) => (
                            <div
                                key={msg.id}
                                className={`flex ${msg.isUser ? "justify-end" : "justify-start"}`}
                            >
                                <div
                                    className={`max-w-[70%] px-4 py-2 rounded-2xl text-sm
                                    ${msg.isUser
                                        ? "bg-neutral-800 text-white rounded-br-md"
                                        : "bg-neutral-200 text-black rounded-bl-md"
                                    }`
                                }
                                >
                                    {msg.text}
                                </div>

                            </div>
                        ))}
                    </div>
                </ScrollArea>
            </div>

            {/* Input */}
            <form
                onSubmit={sendMessage}
                className="sticky bottom-0 border-t p-4 flex gap-2 bg-background"
            >
                <Textarea
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    placeholder="Stell deine Frage…"
                    className="resize-none"
                />
                <Button className="cursor-pointer bg-blue-500 text-white rounded-full px-4 py-1" type="submit">Senden</Button>
            </form>
        </div>
    );
}