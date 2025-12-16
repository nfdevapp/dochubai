"use client";

import InvoiceTable from "@/components/invoice/InvoiceTable";
import {InvoiceChart} from "@/components/invoice/InvoiceChart.tsx";

export default function InvoicePage() {
    return (
        <div className="space-y-6">
            <InvoiceChart />
            <InvoiceTable />
        </div>
    );
}