package org.example.backend.utils.mapper;

import org.example.backend.ai.AiContractAnalysisResult;
import org.example.backend.ai.FileTextExtractor;
import org.example.backend.ai.TextAnalyzer;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.entities.Contract;
import org.example.backend.model.dto.ContractDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;

@Component
public class ContractMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final FileTextExtractor fileTextExtractor;
    private final TextAnalyzer textAnalyzer;

    public ContractMapper(FileTextExtractor fileTextExtractor, TextAnalyzer textAnalyzer) {
        this.fileTextExtractor = fileTextExtractor;
        this.textAnalyzer = textAnalyzer;
    }

    // Contract => ContractDto
    public ContractDto toDto(Contract contract) {
        try {
            String fileBase = null;
            if (contract.file() != null) {
                fileBase = Base64.getEncoder().encodeToString(contract.file());
            }

            return ContractDto.builder()
                    .id(contract.id())
                    .title(contract.title())
                    .description(contract.description())
                    .startDate(contract.startDate() != null ? contract.startDate().format(FORMATTER) : null)
                    .endDate(contract.endDate() != null ? contract.endDate().format(FORMATTER) : null)
                    .aiLevel(contract.aiLevel())
                    .aiAnalysisText(contract.aiAnalysisText())
                    .fileName(contract.fileName())
                    .fileBase64(fileBase)
                    .build();

        } catch (IllegalArgumentException e) {
            throw new DocHubAiException("Error encoding file to Base64: " + e.getMessage());
        } catch (Exception e) {
            throw new DocHubAiException("Unexpected error while mapping Contract to DTO: " + e.getMessage());
        }
    }

    // ContractDto => Contract
    public Contract fromDto(ContractDto dto) {
        try {
            byte[] fileBytes = null;

            if (dto.fileBase64() != null) {
                try {
                    fileBytes = Base64.getDecoder().decode(dto.fileBase64());
                } catch (IllegalArgumentException e) {
                    throw new DocHubAiException("Error decoding file from Base64: " + e.getMessage());
                }
            }
            // Text extraction
            String extractedText;
            try {
                if (dto.fileName() != null &&
                        (dto.fileName().toLowerCase().endsWith(".jpg")
                                || dto.fileName().toLowerCase().endsWith(".jpeg")
                                || dto.fileName().toLowerCase().endsWith(".png"))) {

                    extractedText = fileTextExtractor.extractTextFromImage(fileBytes);

                } else {
                    extractedText = fileTextExtractor.extractText(fileBytes);
                }
            } catch (Exception e) {
                throw new DocHubAiException("Error extracting text from file: " + e.getMessage());
            }

            // AI analysis
            AiContractAnalysisResult analysisResult = null;
            try {
                if (extractedText != null && !extractedText.isBlank()) {
                    analysisResult = textAnalyzer.analyzeContractText(extractedText);
                }
            } catch (Exception e) {
                throw new DocHubAiException("Error during AI analysis: " + e.getMessage());
            }

            LocalDate start;
            LocalDate end;
            try {
                start = dto.startDate() != null ? LocalDate.parse(dto.startDate(), FORMATTER) : null;
                end = dto.endDate() != null ? LocalDate.parse(dto.endDate(), FORMATTER) : null;
            } catch (DateTimeParseException e) {
                throw new DocHubAiException("Error parsing date: " + e.getMessage());
            }

            return Contract.builder()
                    .title(dto.title())
                    .description(dto.description())
                    .startDate(start)
                    .endDate(end)
                    .extractedText(extractedText)
                    .aiLevel(analysisResult != null ? analysisResult.aiLevel() : null)
                    .aiAnalysisText(analysisResult != null ? analysisResult.aiAnalysisText() : null)
                    .fileName(dto.fileName())
                    .file(fileBytes)
                    .build();

        } catch (DocHubAiException e) {
            throw e;
        } catch (Exception e) {
            throw new DocHubAiException("Unexpected error while mapping DTO to Contract: " + e.getMessage());
        }
    }

    // getAllContracts: without file and fileName
    public ContractDto toDtoWithoutFile(Contract contract) {
        try {
            return ContractDto.builder()
                    .id(contract.id())
                    .title(contract.title())
                    .description(contract.description())
                    .startDate(contract.startDate() != null ? contract.startDate().format(FORMATTER) : null)
                    .endDate(contract.endDate() != null ? contract.endDate().format(FORMATTER) : null)
                    .aiLevel(contract.aiLevel())
                    .build();
        } catch (Exception e) {
            throw new DocHubAiException("Error mapping Contract to DTO without file: " + e.getMessage());
        }
    }
}
