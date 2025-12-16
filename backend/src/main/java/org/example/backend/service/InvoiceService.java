package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.InvoiceDto;
import org.example.backend.model.entities.Invoice;
import org.example.backend.repository.InvoiceRepo;
import org.example.backend.utils.mapper.InvoiceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepo invoiceRepo;
    private final InvoiceMapper invoiceMapper;

    public InvoiceDto getInvoiceById(String id) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Invoice not found: " + id));
        return invoiceMapper.toDto(invoice);
    }

    public List<InvoiceDto> getAllInvoices() {
        List<Invoice> invoices = invoiceRepo.findAll();
        List<InvoiceDto> invoiceDtos = invoices.stream()
                .map(invoiceMapper::toDtoWithoutFile)
                .collect(Collectors.toList());
        return invoiceDtos;
    }

    public InvoiceDto updateInvoice(String id, InvoiceDto invoiceDto) {
        Invoice oldData = invoiceRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Invoice not found: " + id));

        // Mapping
        Invoice mapped = invoiceMapper.fromDto(invoiceDto);

        Invoice updated = oldData
                .withDocNumber(mapped.docNumber())
                .withDate(mapped.date())
                .withAmount(mapped.amount())
                .withPurpose(mapped.purpose())
                .withInvoice(mapped.isInvoice())
                .withFileType(mapped.fileType())
                .withFileName(mapped.fileName())
                .withFile(mapped.file());

        invoiceRepo.save(updated);
        return invoiceMapper.toDtoWithoutFile(updated);
    }

    public void deleteInvoice(String id) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Invoice not found: " + id));
        invoiceRepo.delete(invoice);
    }

    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        // Mapping
        Invoice mapped = invoiceMapper.fromDto(invoiceDto);
        Invoice created = invoiceRepo.save(mapped);
        return invoiceMapper.toDtoWithoutFile(created);
    }


    public void deleteAllInvoices() {
        invoiceRepo.deleteAll();
    }
}
