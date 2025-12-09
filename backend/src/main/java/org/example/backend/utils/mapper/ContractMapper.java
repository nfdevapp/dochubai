package org.example.backend.utils.mapper;

import org.example.backend.model.entities.Contract;
import org.example.backend.model.dto.ContractDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ContractMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // Contract => ContractDto
    public static ContractDto toDto(Contract contract) {
        //JSON unterstützt fileBase64 und mongo db verlangt byte[]
        //byte[] to Base64
        String fileBase = null;
        if (contract.file() != null) {
            fileBase = Base64.getEncoder().encodeToString(contract.file());
        }

        return ContractDto.builder()
                .title(contract.title())
                .description(contract.description())
                .startDate(contract.startDate() != null ? contract.startDate().format(FORMATTER) : null)
                .endDate(contract.endDate() != null ? contract.endDate().format(FORMATTER) : null)
                .aiLevel(contract.aiLevel())
                .aiAnalysisText(contract.aiAnalysisText())
                .fileName(contract.fileName())
                .fileBase64(fileBase)
                .build();
    }

    // ContractDto => Contract
    public static Contract fromDto(ContractDto dto) {
        byte[] fileBytes = null;

        // Base64-String in byte[] umwandeln für Mongo DB
        // Base64 to byte[]
        if (dto.fileBase64() != null) {
            fileBytes = Base64.getDecoder().decode(dto.fileBase64());
        }

        return Contract.builder()
                .id(dto.id())
                .title(dto.title())
                .description(dto.description())
                .startDate(dto.startDate() != null ? LocalDate.parse(dto.startDate(), FORMATTER) : null)
                .endDate(dto.endDate() != null ? LocalDate.parse(dto.endDate(), FORMATTER) : null)
                .aiLevel(dto.aiLevel())
                .aiAnalysisText(dto.aiAnalysisText())
                .fileName(dto.fileName())
                .file(fileBytes)
                .build();
    }

    // getAllContracts => without file and fileName
    public static ContractDto toDtoForAllContracts(Contract contract) {
        return ContractDto.builder()
                .id(contract.id())
                .title(contract.title())
                .description(contract.description())
                .startDate(contract.startDate() != null ? contract.startDate().format(FORMATTER) : null)
                .endDate(contract.endDate() != null ? contract.endDate().format(FORMATTER) : null)
                .aiLevel(contract.aiLevel())
                .build();
    }

}
