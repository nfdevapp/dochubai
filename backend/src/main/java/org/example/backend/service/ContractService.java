package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.ContractDto;
import org.example.backend.model.entities.Contract;
import org.example.backend.repository.ContractRepo;
import org.example.backend.utils.mapper.ContractMapper;
import org.springframework.stereotype.Service;

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

    public Contract updateContract(String id, ContractDto contractDto) {
        Contract oldData = contractRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Contract not found: " + id));

        // Mapping
        Contract mapped = ContractMapper.fromDto(contractDto);

        Contract updated = oldData
                .withTitle(mapped.title())
                .withDescription(mapped.description())
                .withStartDate(mapped.startDate())
                .withEndDate(mapped.endDate())
                .withAiLevel(mapped.aiLevel())
                .withAiAnalysisText(mapped.aiAnalysisText())
                .withFileName(mapped.fileName())
                .withFile(mapped.file());

        return contractRepo.save(updated);
    }


    public void deleteContract(String id) {
        Contract contract = contractRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Contract not found: " + id));
        contractRepo.delete(contract);
    }

    public Contract createContract(ContractDto contractDto) {
        // Mapping
        Contract newContract = ContractMapper.fromDto(contractDto);
        return contractRepo.save(newContract);
    }


    public void deleteAllContracts() {
        contractRepo.deleteAll();
    }
}
