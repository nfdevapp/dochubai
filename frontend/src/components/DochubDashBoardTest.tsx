"use client";

import type { JSX } from "react";
import { FileText, Receipt, FileSpreadsheet } from "lucide-react";

type Stat = {
    id: string;
    label: string;
    value: string;
    change?: string;
};

const stats: Stat[] = [
    { id: "1", label: "Active Users", value: "1,254", change: "+4.6%" },
    { id: "2", label: "New Signups", value: "312", change: "+1.2%" },
    { id: "3", label: "Revenue", value: "€12,430", change: "-0.8%" },
    { id: "4", label: "Tasks", value: "24", change: "+10%" },
];

const tableData = [
    { id: 1, name: "Anna Schmidt", email: "anna@example.com", status: "active" },
    { id: 2, name: "Jonas Meyer", email: "jonas@example.com", status: "pending" },
    { id: 3, name: "Lena Fischer", email: "lena@example.com", status: "active" },
    { id: 4, name: "Markus Weber", email: "markus@example.com", status: "disabled" },
];

function Sidebar() {
    return (
        <aside className="w-64 hidden md:flex flex-col gap-4 p-4 border-r border-sidebar-border bg-sidebar-background text-sidebar-foreground">
            <div className="text-lg font-semibold">DochubDashBoard</div>

            <nav className="flex flex-col gap-1 mt-4">
                <a
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
                    href="#vertraege"
                >
                    <FileText size={18} />
                    Verträge
                </a>

                <a
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
                    href="#zahlungsbelege"
                >
                    <Receipt size={18} />
                    Zahlungsbelege
                </a>

                <a
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
                    href="#rechnungen"
                >
                    <FileSpreadsheet size={18} />
                    Rechnungen
                </a>
                <a
                    className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
                    href="#einstellungen"
                >
                    <FileSpreadsheet size={18} />
                    Einstellungen
                </a>
            </nav>

            <div className="mt-auto text-sm text-muted-foreground">
                © {new Date().getFullYear()} DochubDashBoard
            </div>
        </aside>
    );
}

function Header() {
    return (
        <header className="flex items-center justify-between gap-4 p-4 border-b border-border bg-card">
            <div className="flex items-center gap-4">
                <button className="md:hidden p-2 rounded-md border border-input">☰</button>
                <h1 className="text-2xl font-bold">Dashboard</h1>
                <span className="text-sm text-muted-foreground">Überblick & Statistiken</span>
            </div>
            <div className="flex items-center gap-3">
                <input placeholder="Search..." className="px-3 py-2 rounded-md border border-input bg-transparent" />
                <button className="px-3 py-2 rounded-md border border-input">Profile</button>
            </div>
        </header>
    );
}

function StatCard({ stat }: { stat: Stat }) {
    return (
        <div className="p-4 rounded-lg border border-border bg-card">
            <div className="text-sm text-muted-foreground">{stat.label}</div>
            <div className="mt-2 flex items-baseline gap-2">
                <div className="text-2xl font-semibold">{stat.value}</div>
                {stat.change && (
                    <div
                        className={`text-sm font-medium ${
                            stat.change.startsWith("-")
                                ? "text-destructive-foreground bg-destructive/10"
                                : "text-primary-foreground bg-primary/10"
                        } px-2 py-0.5 rounded-md`}
                    >
                        {stat.change}
                    </div>
                )}
            </div>
        </div>
    );
}

function SimpleAreaChart() {
    return (
        <div className="p-4 rounded-lg border border-border bg-card">
            <div className="flex items-center justify-between mb-2">
                <div className="text-sm text-muted-foreground">Traffic (last 7 days)</div>
                <div className="text-xs text-muted-foreground">Updated now</div>
            </div>
            <svg viewBox="0 0 200 60" className="w-full h-24">
                <defs>
                    <linearGradient id="g" x1="0" x2="0" y1="0" y2="1">
                        <stop offset="0%" stopOpacity="0.6" stopColor="rgb(59 130 246)" />
                        <stop offset="100%" stopOpacity="0" stopColor="rgb(59 130 246)" />
                    </linearGradient>
                </defs>
                <path
                    d="M0,40 L20,30 L40,20 L60,25 L80,15 L100,20 L120,10 L140,18 L160,8 L180,12 L200,6"
                    fill="url(#g)"
                    stroke="rgb(59 130 246)"
                    strokeWidth="2"
                />
            </svg>
        </div>
    );
}

function DataTable() {
    return (
        <div className="p-4 rounded-lg border border-border bg-card">
            <div className="text-sm text-muted-foreground mb-2">Recent users</div>
            <div className="overflow-x-auto">
                <table className="w-full text-left">
                    <thead>
                    <tr className="text-sm text-muted-foreground">
                        <th className="px-3 py-2">Name</th>
                        <th className="px-3 py-2">Email</th>
                        <th className="px-3 py-2">Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    {tableData.map((row) => (
                        <tr key={row.id} className="border-t border-border">
                            <td className="px-3 py-3">{row.name}</td>
                            <td className="px-3 py-3 text-sm text-muted-foreground">{row.email}</td>
                            <td className="px-3 py-3">
                                    <span
                                        className={`inline-flex items-center px-2 py-1 text-xs rounded-full ${
                                            row.status === "active"
                                                ? "bg-primary/10 text-primary-foreground"
                                                : row.status === "pending"
                                                    ? "bg-accent/10 text-accent-foreground"
                                                    : "bg-muted/20 text-muted-foreground"
                                        }`}
                                    >
                                        {row.status}
                                    </span>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default function DochubDashBoard(): JSX.Element {
    return (
        <div className="min-h-screen flex bg-background text-foreground">
            <Sidebar />
            <div className="flex-1 flex flex-col">
                <Header />
                <main className="p-6 space-y-6">
                    <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                        {stats.map((s) => (
                            <StatCard key={s.id} stat={s} />
                        ))}
                    </section>

                    <section className="grid grid-cols-1 lg:grid-cols-3 gap-4">
                        <div className="lg:col-span-2">
                            <SimpleAreaChart />
                        </div>
                        <div className="lg:col-span-1">
                            <DataTable />
                        </div>
                    </section>

                    <section className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div className="md:col-span-2 p-4 rounded-lg border border-border bg-card">
                            <h2 className="text-lg font-semibold mb-2">Latest documents</h2>
                            <p className="text-sm text-muted-foreground">
                                Hier ist ein kurzer Platzhalter für Dokumente, Links und Aktionen.
                            </p>
                        </div>
                        <div className="p-4 rounded-lg border border-border bg-card">
                            <h3 className="text-sm font-medium mb-2">Quick actions</h3>
                            <div className="flex flex-col gap-2">
                                <button className="px-3 py-2 rounded-md border border-input">Neues Dokument</button>
                                <button className="px-3 py-2 rounded-md border border-input">Upload</button>
                                <button className="px-3 py-2 rounded-md border border-input">Team einladen</button>
                            </div>
                        </div>
                    </section>
                </main>
            </div>
        </div>
    );
}
