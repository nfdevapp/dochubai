package org.example.backend.utils.mapper;

import org.example.backend.ai.AiContractAnalysisResult;
import org.example.backend.ai.FileTextExtractor;
import org.example.backend.ai.TextAnalyzer;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.ContractDto;
import org.example.backend.model.entities.Contract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractMapperTest {

    private FileTextExtractor fileTextExtractor;
    private TextAnalyzer textAnalyzer;
    private ContractMapper contractMapper;

    @BeforeEach
    void setUp() {
        fileTextExtractor = mock(FileTextExtractor.class);
        textAnalyzer = mock(TextAnalyzer.class);
        contractMapper = new ContractMapper(fileTextExtractor, textAnalyzer);
    }

    // ---------- toDto ----------
    @Test
    void toDto_withFile_success() {
        byte[] file = "test".getBytes();
        Contract contract = Contract.builder()
                .id("1")
                .title("Title")
                .description("Desc")
                .startDate(LocalDate.of(2026, 1, 13))
                .endDate(LocalDate.of(2026, 2, 13))
                .aiLevel(1)
                .aiAnalysisText("Analysis")
                .fileName("file.pdf")
                .file(file)
                .build();

        ContractDto dto = contractMapper.toDto(contract);

        assertEquals("1", dto.id());
        assertEquals("Title", dto.title());
        assertEquals("Desc", dto.description());
        assertEquals("13.01.2026", dto.startDate());
        assertEquals("13.02.2026", dto.endDate());
        assertEquals(1, dto.aiLevel());
        assertEquals("Analysis", dto.aiAnalysisText());
        assertEquals("file.pdf", dto.fileName());
        assertEquals(Base64.getEncoder().encodeToString(file), dto.fileBase64());
    }

    @Test
    void toDto_withoutFile_success() {
        Contract contract = Contract.builder()
                .id("1")
                .title("Title")
                .description("Desc")
                .startDate(LocalDate.of(2026, 1, 13))
                .endDate(LocalDate.of(2026, 2, 13))
                .build();

        ContractDto dto = contractMapper.toDto(contract);

        assertNull(dto.fileBase64());
    }

    // ---------- fromDto ----------
    @Test
    void fromDto_withFileAndAI_success() {
        byte[] fileBytes = "data".getBytes();
        String base64 = Base64.getEncoder().encodeToString(fileBytes);

        ContractDto dto = ContractDto.builder()
                .title("Title")
                .description("Desc")
                .startDate("13.01.2026")
                .endDate("13.02.2026")
                .fileName("file.pdf")
                .fileBase64(base64)
                .build();

        // Mocks für TextExtractor & TextAnalyzer
        when(fileTextExtractor.extractText(fileBytes)).thenReturn("extracted text");
        when(textAnalyzer.analyzeContractText("extracted text"))
                .thenReturn(new AiContractAnalysisResult(2, "Analysis"));

        Contract contract = contractMapper.fromDto(dto);

        assertEquals("Title", contract.title());
        assertEquals("Desc", contract.description());
        assertEquals(LocalDate.of(2026, 1, 13), contract.startDate());
        assertEquals(LocalDate.of(2026, 2, 13), contract.endDate());
        assertEquals("extracted text", contract.extractedText());
        assertEquals(2, contract.aiLevel());
        assertEquals("Analysis", contract.aiAnalysisText());
        assertEquals("file.pdf", contract.fileName());
        assertArrayEquals(fileBytes, contract.file());
    }

    @Test
    void fromDto_withInvalidBase64_throwsException() {
        ContractDto dto = ContractDto.builder()
                .fileBase64("not-base64")
                .build();

        DocHubAiException ex = assertThrows(DocHubAiException.class,
                () -> contractMapper.fromDto(dto));
        assertTrue(ex.getMessage().contains("Error decoding file from Base64"));
    }

    @Test
    void fromDto_withInvalidDate_throwsException() {
        ContractDto dto = ContractDto.builder()
                .startDate("31-12-2026")
                .build();

        DocHubAiException ex = assertThrows(DocHubAiException.class,
                () -> contractMapper.fromDto(dto));
        assertTrue(ex.getMessage().contains("Error parsing date"));
    }

    @Test
    void fromDto_withImageFile_callsExtractTextFromImage() {
        byte[] fileBytes = "imgdata".getBytes();
        String base64 = Base64.getEncoder().encodeToString(fileBytes);

        ContractDto dto = ContractDto.builder()
                .fileName("image.jpg")
                .fileBase64(base64)
                .build();

        when(fileTextExtractor.extractTextFromImage(fileBytes)).thenReturn("extracted image text");
        when(textAnalyzer.analyzeContractText("extracted image text"))
                .thenReturn(new AiContractAnalysisResult(1, "Analysis"));

        Contract contract = contractMapper.fromDto(dto);

        assertEquals("extracted image text", contract.extractedText());
        assertEquals(1, contract.aiLevel());
        assertEquals("Analysis", contract.aiAnalysisText());
    }

    // ---------- toDtoWithoutFile ----------
    @Test
    void toDtoWithoutFile_success() {
        Contract contract = Contract.builder()
                .id("1")
                .title("Title")
                .description("Desc")
                .startDate(LocalDate.of(2026, 1, 13))
                .endDate(LocalDate.of(2026, 2, 13))
                .aiLevel(2)
                .fileName("file.pdf")
                .file("file".getBytes())
                .build();

        ContractDto dto = contractMapper.toDtoWithoutFile(contract);

        assertEquals("1", dto.id());
        assertEquals("Title", dto.title());
        assertEquals("Desc", dto.description());
        assertEquals("13.01.2026", dto.startDate());
        assertEquals("13.02.2026", dto.endDate());
        assertEquals(2, dto.aiLevel());
        assertNull(dto.fileName());
        assertNull(dto.fileBase64());
    }
}
