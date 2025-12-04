"use client";

import { useState } from "react";
import Sidebar from "./Sidebar";
import Header from "./Header";

export default function Dashboard() {
    const [title, setTitle] = useState("Dashboard");

    return (
        <div className="min-h-screen flex bg-background text-foreground">
            <Sidebar onSelect={setTitle} />
            <div className="flex-1 flex flex-col">
                <Header title={title} />
            </div>
        </div>
    );
}
