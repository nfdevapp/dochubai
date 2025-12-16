"use client"

import * as React from "react"
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

export const description = "An interactive area chart"

// Funktion zum Parsen von dd.mm.yyyy-Daten
const parseDate = (dateStr: string): Date | undefined => {
    if (!dateStr) return undefined
    const [day, month, year] = dateStr.split(".").map(Number)
    return new Date(year, month - 1, day)
}

const chartData = [
    { date: "01.04.2024", desktop: 222 },
    { date: "02.04.2024", desktop: 97 },
    { date: "03.04.2024", desktop: 167 },
    { date: "04.04.2024", desktop: 242 },
    { date: "05.04.2024", desktop: 373 },
    { date: "06.04.2024", desktop: 301 },
    { date: "07.04.2024", desktop: 245 },
    { date: "08.04.2024", desktop: 409 },
    { date: "09.04.2024", desktop: 59 },
    { date: "10.04.2024", desktop: 261 },
    { date: "11.04.2024", desktop: 327 },
    { date: "12.04.2024", desktop: 292 },
    { date: "13.04.2024", desktop: 342 },
    { date: "14.04.2024", desktop: 137 },
    { date: "15.04.2024", desktop: 120 },
    { date: "16.04.2024", desktop: 138 },
    { date: "17.04.2024", desktop: 446 },
    { date: "18.04.2024", desktop: 364 },
    { date: "19.04.2024", desktop: 243 },
    { date: "20.04.2024", desktop: 89 },
    { date: "21.04.2024", desktop: 137 },
    { date: "22.04.2024", desktop: 224 },
    { date: "23.04.2024", desktop: 138 },
    { date: "24.04.2024", desktop: 387 },
    { date: "25.04.2024", desktop: 215 },
    { date: "26.04.2024", desktop: 75 },
    { date: "27.04.2024", desktop: 383 },
    { date: "28.04.2024", desktop: 122 },
    { date: "29.04.2024", desktop: 315 },
    { date: "30.04.2024", desktop: 454 },
    { date: "01.05.2024", desktop: 165 },
    { date: "02.05.2024", desktop: 293 },
    { date: "03.05.2024", desktop: 247 },
    { date: "04.05.2024", desktop: 385 },
    { date: "05.05.2024", desktop: 481 },
    { date: "06.05.2024", desktop: 498 },
    { date: "07.05.2024", desktop: 388 },
    { date: "08.05.2024", desktop: 149 },
    { date: "09.05.2024", desktop: 227 },
    { date: "10.05.2024", desktop: 293 },
    { date: "11.05.2024", desktop: 335 },
    { date: "12.05.2024", desktop: 197 },
    { date: "13.05.2024", desktop: 197 },
    { date: "14.05.2024", desktop: 448 },
    { date: "15.05.2024", desktop: 473 },
    { date: "16.05.2024", desktop: 338 },
    { date: "17.05.2024", desktop: 499 },
    { date: "18.05.2024", desktop: 315 },
    { date: "19.05.2024", desktop: 235 },
    { date: "20.05.2024", desktop: 177 },
    { date: "21.05.2024", desktop: 82 },
    { date: "22.05.2024", desktop: 81 },
    { date: "23.05.2024", desktop: 252 },
    { date: "24.05.2024", desktop: 294 },
    { date: "25.05.2024", desktop: 201 },
    { date: "26.05.2024", desktop: 213 },
    { date: "27.05.2024", desktop: 420 },
    { date: "28.05.2024", desktop: 233 },
    { date: "29.05.2024", desktop: 78 },
    { date: "30.05.2024", desktop: 340 },
    { date: "31.05.2024", desktop: 178 },
    { date: "01.06.2024", desktop: 178 },
    { date: "02.06.2024", desktop: 470 },
    { date: "03.06.2024", desktop: 103 },
    { date: "04.06.2024", desktop: 439 },
    { date: "05.06.2024", desktop: 88 },
    { date: "06.06.2024", desktop: 294 },
    { date: "07.06.2024", desktop: 323 },
    { date: "08.06.2024", desktop: 385 },
    { date: "09.06.2024", desktop: 438 },
    { date: "10.06.2024", desktop: 155 },
    { date: "11.06.2024", desktop: 92 },
    { date: "12.06.2024", desktop: 492 },
    { date: "13.06.2024", desktop: 81 },
    { date: "14.06.2024", desktop: 426 },
    { date: "15.06.2024", desktop: 307 },
    { date: "16.06.2024", desktop: 371 },
    { date: "17.06.2024", desktop: 475 },
    { date: "18.06.2024", desktop: 107 },
    { date: "19.06.2024", desktop: 341 },
    { date: "20.06.2024", desktop: 408 },
    { date: "21.06.2024", desktop: 169 },
    { date: "22.06.2024", desktop: 317 },
    { date: "23.06.2024", desktop: 480 },
    { date: "24.06.2024", desktop: 132 },
    { date: "25.06.2024", desktop: 141 },
    { date: "26.06.2024", desktop: 434 },
    { date: "27.06.2024", desktop: 448 },
    { date: "28.06.2024", desktop: 149 },
    { date: "29.06.2024", desktop: 103 },
    { date: "30.06.2024", desktop: 446 },
]

const chartConfig = {
    visitors: {
        label: "Visitors",
    },
    desktop: {
        label: "Zahlungen",
        color: "var(--chart-1)",
    },
} satisfies ChartConfig

export function InvoiceChart() {
    const [timeRange, setTimeRange] = React.useState("90d")
    const [loading, setLoading] = React.useState(true)
    const [data, setData] = React.useState<typeof chartData>([])

    // Daten simuliert laden
    React.useEffect(() => {
        const timeout = setTimeout(() => {
            setData(chartData)
            setLoading(false)
        }, 1000) // z.B. 1 Sekunde Ladezeit
        return () => clearTimeout(timeout)
    }, [])

    const filteredData = data.filter((item) => {
        const date = parseDate(item.date)
        if (!date) return false

        const referenceDate = new Date(2024, 5, 30) // 30.06.2024
        let daysToSubtract = 90
        if (timeRange === "30d") daysToSubtract = 30
        else if (timeRange === "7d") daysToSubtract = 7

        const startDate = new Date(referenceDate)
        startDate.setDate(startDate.getDate() - daysToSubtract)
        return date >= startDate
    })

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
                <Select value={timeRange} onValueChange={setTimeRange}>
                    <SelectTrigger
                        className="hidden w-[160px] rounded-lg sm:ml-auto sm:flex"
                        aria-label="Wählen Sie einen Wert aus"
                    >
                        <SelectValue placeholder="Letzte 3 Monate" />
                    </SelectTrigger>
                    <SelectContent className="rounded-xl">
                        <SelectItem value="90d" className="rounded-lg">
                            Letzte 3 Monate
                        </SelectItem>
                        <SelectItem value="30d" className="rounded-lg">
                            Letzte 30 Tage
                        </SelectItem>
                        <SelectItem value="7d" className="rounded-lg">
                            Letzte 7 Tage
                        </SelectItem>
                    </SelectContent>
                </Select>
            </CardHeader>
            <CardContent className="px-2 pt-4 sm:px-6 sm:pt-6">
                <ChartContainer
                    config={chartConfig}
                    className="aspect-auto h-[250px] w-full"
                >
                    <AreaChart data={filteredData}>
                        <defs>
                            <linearGradient id="fillDesktop" x1="0" y1="0" x2="0" y2="1">
                                <stop
                                    offset="5%"
                                    stopColor="var(--color-desktop)"
                                    stopOpacity={0.8}
                                />
                                <stop
                                    offset="95%"
                                    stopColor="var(--color-desktop)"
                                    stopOpacity={0.1}
                                />
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
                                return date.toLocaleDateString("de-DE", {
                                    month: "short",
                                    day: "numeric",
                                })
                            }}
                        />
                        <ChartTooltip
                            cursor={false}
                            content={
                                <ChartTooltipContent
                                    labelFormatter={(value) => {
                                        const date = parseDate(value)
                                        if (!date) return ""
                                        return date.toLocaleDateString("de-DE", {
                                            month: "short",
                                            day: "numeric",
                                        })
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
            </CardContent>
        </Card>
    )
}
