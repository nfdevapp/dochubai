package org.example.backend.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.entities.Contract;
import org.example.backend.repository.ContractRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepo contractRepo;

    public Contract getContractById(String id) {
        return contractRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Contract not found: " + id));
    }

    public List<Contract> getAllContracts() {
        return contractRepo.findAll();
    }

    public Contract updateContract(String id, Contract contract) {
        Contract oldData = contractRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Contract not found: " + id));

        Contract updated = oldData
                .withTitle(contract.title())
                .withDescription(contract.description())
                .withStartDate(contract.startDate())
                .withEndDate(contract.endDate())
                .withAiLevel(contract.aiLevel())
                .withAiAnalysisText(contract.aiAnalysisText())
                .withFileName(contract.fileName())
                .withFile(contract.file());
        return contractRepo.save(updated);
    }

    public void deleteContract(String id) {
        Contract Contract = contractRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Contract not found: " + id));
        contractRepo.delete(Contract);
    }

    public Contract createContract(Contract contract) {
        return contractRepo.save(contract);
    }

    public void deleteAllContracts() {
        contractRepo.deleteAll();
    }
}
