package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.ContractDto;
import org.example.backend.model.entities.Contract;
import org.example.backend.repository.ContractRepo;
import org.example.backend.utils.mapper.ContractMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepo contractRepo;

    public ContractDto getContractById(String id) {
        Contract contract = contractRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Contract not found: " + id));
        return ContractMapper.toDto(contract);
    }

    public List<ContractDto> getAllContracts() {
        List<Contract> contracts = contractRepo.findAll();
        List<ContractDto> contractDtos = contracts.stream()
                .map(ContractMapper::toDtoWithoutFile)
                .collect(Collectors.toList());
        return contractDtos;
    }

    public ContractDto updateContract(String id, ContractDto contractDto) {
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

        contractRepo.save(updated);
        return ContractMapper.toDtoWithoutFile(updated);
    }


    public void deleteContract(String id) {
        Contract contract = contractRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Contract not found: " + id));
        contractRepo.delete(contract);
    }

    public ContractDto createContract(ContractDto contractDto) {
        // Mapping
        Contract mapped = ContractMapper.fromDto(contractDto);
        Contract created = contractRepo.save(mapped);
        return ContractMapper.toDtoWithoutFile(created);
    }


    public void deleteAllContracts() {
        contractRepo.deleteAll();
    }
}
