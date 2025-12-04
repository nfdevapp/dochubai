"use client";

import type {JSX} from "react";

interface HeaderProps {
    title: string;
}

export default function Header({ title }: HeaderProps): JSX.Element {
    return (
        <header className="flex items-center justify-center p-4 bg-card">
            <div className="border-b border-border pb-2 -translate-x-20">
                <h1 className="text-2xl font-bold text-center">{title}</h1>
            </div>
        </header>
    );
}
