package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.repository.StatementRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatementService {
    private final StatementRepo invoiceRepo;
}
