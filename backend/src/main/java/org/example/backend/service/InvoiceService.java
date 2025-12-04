package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.repository.InvoiceRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepo invoiceRepo;
}
