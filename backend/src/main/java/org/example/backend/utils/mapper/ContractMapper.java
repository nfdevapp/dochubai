package org.example.backend.utils.mapper;

import org.example.backend.model.entities.Contract;
import org.example.backend.model.dto.ContractDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ContractMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // Contract => ContractDto
    public static ContractDto toDto(Contract contract) {
        List<Integer> fileList = null;
        if (contract.file() != null) {
            fileList = new ArrayList<>(contract.file().length);
            for (byte b : contract.file()) {
                // & 0xFF sorgt dafür, dass Bytes korrekt als positive Integer dargestellt werden
                fileList.add((int) b & 0xFF);
            }
        }

        return ContractDto.builder()
                .title(contract.title())
                .description(contract.description())
                .startDate(contract.startDate() != null ? contract.startDate().format(FORMATTER) : null)
                .endDate(contract.endDate() != null ? contract.endDate().format(FORMATTER) : null)
                .aiLevel(contract.aiLevel())
                .aiAnalysisText(contract.aiAnalysisText())
                .fileName(contract.fileName())
                .file(fileList)
                .build();
    }

    // ContractDto => Contract
    public static Contract fromDto(ContractDto dto) {
        byte[] fileBytes = null;
        if (dto.file() != null) {
            fileBytes = new byte[dto.file().size()];
            for (int i = 0; i < dto.file().size(); i++) {
                fileBytes[i] = dto.file().get(i).byteValue();
            }
        }

        return Contract.builder()
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
}
