"use client";

interface HeaderProps {
    title: string;
}

export default function Header({ title }: HeaderProps) {
    return (
        <header className="flex items-center justify-center p-4 bg-card">
            <div className="border-b border-border pb-2 -translate-x-10">
                <h1 className="text-2xl font-bold text-center">{title}</h1>
            </div>
        </header>
    );
}
