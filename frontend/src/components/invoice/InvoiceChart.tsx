"use client"

import * as React from "react"
import { Sparkles } from "lucide-react"
import { Area, AreaChart, CartesianGrid, XAxis } from "recharts"

import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import {
    ChartContainer,
    ChartLegend,
    ChartLegendContent,
    ChartTooltip,
    ChartTooltipContent,
    type ChartConfig,
} from "@/components/ui/chart"
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import { Skeleton } from "@/components/ui/skeleton"
import {getInvoiceAiAnalysis, getInvoiceChart, runInvoiceAiAnalysis} from "@/api/InvoiceService"
import type { InvoiceChartItem } from "@/model/InvoiceChartItem";



export const description = "An interactive area chart"

// Funktion zum Parsen von dd.mm.yyyy-Daten
const parseDate = (dateStr: string): Date | undefined => {
    if (!dateStr) return undefined
    const [day, month, year] = dateStr.split(".").map(Number)
    return new Date(year, month - 1, day)
}

const chartConfig = {
    visitors: { label: "Visitors" },
    desktop: { label: "Zahlungen", color: "var(--chart-1)" },
} satisfies ChartConfig

export function InvoiceChart() {
    const [timeRange, setTimeRange] = React.useState<"7d" | "30d" | "90d">("90d")
    const [loading, setLoading] = React.useState(true)
    const [data, setData] = React.useState<InvoiceChartItem[]>([])
    const [analyzing, setAnalyzing] = React.useState(false)
    const [analysisResult, setAnalysisResult] = React.useState<string | null>(null)

    React.useEffect(() => {
        const fetchData = async () => {
            try {
                const invoices = await getInvoiceChart()
                setData(
                    invoices.map((inv) => ({
                        date: inv.date,
                        desktop: inv.amount,
                    }))
                )

                // Analyse-Ergebnis aus DB laden (KEINE neue Analyse!)
                const analysis = await getInvoiceAiAnalysis()
                setAnalysisResult(analysis.aiAnalysisText)

            } catch (error) {
                console.error("Fehler beim Laden:", error)
            } finally {
                setLoading(false)
            }
        }

        fetchData()
    }, [])


    const filteredData = data.filter((item: InvoiceChartItem) => {
        const date = parseDate(item.date)
        if (!date) return false

        const today = new Date()
        let daysToSubtract = 90
        if (timeRange === "30d") daysToSubtract = 30
        else if (timeRange === "7d") daysToSubtract = 7

        const startDate = new Date(today)
        startDate.setDate(today.getDate() - daysToSubtract)
        return date >= startDate
    })

    const filteredDataSorted = [...filteredData].sort((a, b) => {
        const dateA = parseDate(a.date)?.getTime() ?? 0
        const dateB = parseDate(b.date)?.getTime() ?? 0
        return dateA - dateB
    })

    const handleAnalyze = async () => {
        setAnalyzing(true)

        try {
            const result = await runInvoiceAiAnalysis()
            setAnalysisResult(result.aiAnalysisText)
        } catch (error) {
            console.error("Analyse fehlgeschlagen", error)
            setAnalysisResult("Fehler bei der KI-Analyse")
        } finally {
            setAnalyzing(false)
        }
    }

    if (loading) {
        return (
            <Card className="pt-0">
                <CardHeader className="flex items-center gap-2 space-y-0 border-b py-5 sm:flex-row">
                    <div className="grid flex-1 gap-1">
                        <CardTitle>Ausgaben</CardTitle>
                        <CardDescription>Lade Daten...</CardDescription>
                    </div>
                    <Skeleton className="h-8 w-40 sm:flex hidden" />
                </CardHeader>
                <CardContent className="px-2 pt-4 sm:px-6 sm:pt-6">
                    <Skeleton className="h-[250px] w-full" />
                </CardContent>
            </Card>
        )
    }

    return (
        <Card className="pt-0">
            <CardHeader className="flex items-center gap-2 space-y-0 border-b py-5 sm:flex-row">
                <div className="grid flex-1 gap-1">
                    <CardTitle>Ausgaben</CardTitle>
                    <CardDescription>
                        Zeigt die gesamten Ausgaben für die letzten 3 Monate, 30 Tage oder 7 Tage an
                    </CardDescription>
                </div>
                <div className="flex items-center gap-2">
                    <Select value={timeRange} onValueChange={(value) => setTimeRange(value as "7d" | "30d" | "90d")}>
                        <SelectTrigger className="hidden w-[160px] rounded-lg sm:ml-auto sm:flex">
                            <SelectValue
                                placeholder={
                                    timeRange === "90d"
                                        ? "Letzte 3 Monate"
                                        : timeRange === "30d"
                                            ? "Letzte 30 Tage"
                                            : "Letzte 7 Tage"
                                }
                            />
                        </SelectTrigger>
                        <SelectContent className="rounded-xl">
                            <SelectItem value="90d" className="rounded-lg">Letzte 3 Monate</SelectItem>
                            <SelectItem value="30d" className="rounded-lg">Letzte 30 Tage</SelectItem>
                            <SelectItem value="7d" className="rounded-lg">Letzte 7 Tage</SelectItem>
                        </SelectContent>
                    </Select>
                    <button
                        className="ml-4 rounded-lg flex items-center gap-2 border border-blue-600 bg-white px-4 py-2 hover:bg-blue-50 disabled:opacity-50"
                        onClick={handleAnalyze}
                        disabled={analyzing}
                    >
                        <Sparkles className="w-4 h-4" />
                        {analyzing ? "Analysiere..." : "KI Analyse"}
                    </button>

                </div>
            </CardHeader>
            <CardContent className="px-2 pt-4 sm:px-6 sm:pt-6">
                <ChartContainer config={chartConfig} className="aspect-auto h-[250px] w-full">
                    <AreaChart data={filteredDataSorted}>
                        <defs>
                            <linearGradient id="fillDesktop" x1="0" y1="0" x2="0" y2="1">
                                <stop offset="5%" stopColor="var(--color-desktop)" stopOpacity={0.8} />
                                <stop offset="95%" stopColor="var(--color-desktop)" stopOpacity={0.1} />
                            </linearGradient>
                        </defs>
                        <CartesianGrid vertical={false} />
                        <XAxis
                            dataKey="date"
                            tickLine={false}
                            axisLine={false}
                            tickMargin={8}
                            minTickGap={32}
                            tickFormatter={(value) => {
                                const date = parseDate(value)
                                if (!date) return ""
                                return date.toLocaleDateString("de-DE", { month: "short", day: "numeric" })
                            }}
                        />
                        <ChartTooltip
                            cursor={false}
                            content={
                                <ChartTooltipContent
                                    labelFormatter={(value) => {
                                        const date = parseDate(value)
                                        if (!date) return ""
                                        return date.toLocaleDateString("de-DE", { month: "short", day: "numeric" })
                                    }}
                                    indicator="dot"
                                />
                            }
                        />
                        <Area
                            dataKey="desktop"
                            type="natural"
                            fill="url(#fillDesktop)"
                            stroke="var(--color-desktop)"
                            stackId="a"
                        />
                        <ChartLegend content={<ChartLegendContent />} />
                    </AreaChart>
                </ChartContainer>

                {analysisResult && analysisResult.trim() !== "" && (
                    <div className="mt-4 p-4 bg-gray-100 rounded-lg">
                        <h3 className="font-bold mb-1">Analyse-Ergebnis:</h3>
                        <p>{analysisResult}</p>
                    </div>
                )}
            </CardContent>
        </Card>
    )
}
