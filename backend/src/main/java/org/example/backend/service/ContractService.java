package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.repository.ContractRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepo contractRepo;
}
