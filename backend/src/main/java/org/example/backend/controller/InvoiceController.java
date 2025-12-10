package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.InvoiceDto;
import org.example.backend.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable String id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        return ResponseEntity.ok(invoiceService.createInvoice(invoiceDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDto> updateInvoice(@PathVariable String id, @RequestBody InvoiceDto invoiceDto) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, invoiceDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
