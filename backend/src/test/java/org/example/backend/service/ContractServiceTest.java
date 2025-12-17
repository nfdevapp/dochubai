package org.example.backend.service;

import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.ContractDto;
import org.example.backend.model.entities.Contract;
import org.example.backend.repository.ContractRepo;
import org.example.backend.utils.mapper.ContractMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractServiceTest {

    private ContractRepo contractRepo;
    private ContractMapper contractMapper;
    private ContractService contractService;

    /**
     * Wird vor jedem Test ausgeführt.
     * Erstellt frische Mocks und den Service.
     */
    @BeforeEach
    void setup() {
        contractRepo = mock(ContractRepo.class);
        contractMapper = mock(ContractMapper.class);
        contractService = new ContractService(contractRepo, contractMapper);
    }

    /**
     * Testet, dass ein Contract korrekt per ID gefunden wird.
     */
    @Test
    void testGetContractById_found() {

        // GIVEN: Ein bestehender Contract im Repository
        Contract contract = Contract.builder()
                .id("1")
                .title("Mietvertrag")
                .description("Beschreibung")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusYears(1))
                .aiLevel(1)
                .aiAnalysisText("text")
                .fileName("filename")
                .file(null)
                .build();

        ContractDto dto = ContractDto.builder()
                .id(contract.id())
                .title(contract.title())
                .description(contract.description())
                .startDate(contract.startDate().toString())
                .endDate(contract.endDate().toString())
                .aiLevel(contract.aiLevel())
                .aiAnalysisText(contract.aiAnalysisText())
                .fileName(contract.fileName())
//                .fileBase64(contract.file())
                .build();

        when(contractRepo.findById("1")).thenReturn(Optional.of(contract));
        when(contractMapper.toDto(contract)).thenReturn(dto);

        // WHEN: getContractById aufgerufen wird
        ContractDto result = contractService.getContractById("1");

        // THEN: DTO wird zurückgegeben
        assertEquals("1", result.id());
        assertEquals("Mietvertrag", result.title());

        verify(contractRepo).findById("1");
        verify(contractMapper).toDto(contract);
    }

    /**
     * Testet, dass eine Exception geworfen wird,
     * wenn der Contract nicht existiert.
     */
    @Test
    void testGetContractById_notFound() {

        // GIVEN: Repository liefert keinen Contract
        when(contractRepo.findById("1")).thenReturn(Optional.empty());

        // WHEN + THEN: Exception wird geworfen
        assertThrows(DocHubAiException.class,
                () -> contractService.getContractById("1"));

        verify(contractRepo).findById("1");
        verifyNoInteractions(contractMapper);
    }

    /**
     * Testet das Laden aller Contracts (ohne Datei).
     */
    @Test
    void testGetAllContracts() {

        // GIVEN: Zwei Contracts im Repository
        Contract c1 = Contract.builder().id("1").title("C1").build();
        Contract c2 = Contract.builder().id("2").title("C2").build();

        ContractDto dto1 = ContractDto.builder().id("1").title("C1").build();
        ContractDto dto2 = ContractDto.builder().id("2").title("C2").build();

        when(contractRepo.findAll()).thenReturn(List.of(c1, c2));
        when(contractMapper.toDtoWithoutFile(c1)).thenReturn(dto1);
        when(contractMapper.toDtoWithoutFile(c2)).thenReturn(dto2);

        // WHEN
        List<ContractDto> result = contractService.getAllContracts();

        // THEN
        assertEquals(2, result.size());
        assertEquals("C1", result.get(0).title());
        assertEquals("C2", result.get(1).title());

        verify(contractRepo).findAll();
    }

    /**
     * Testet das Aktualisieren eines bestehenden Contracts.
     */
    @Test
    void testUpdateContract() {

        // GIVEN: Alter Contract im Repository
        Contract oldContract = Contract.builder()
                .id("1")
                .title("Alt")
                .description("Alt")
                .build();

        ContractDto inputDto = ContractDto.builder()
                .title("Neu")
                .description("Neu")
                .build();

        Contract mappedContract = Contract.builder()
                .title("Neu")
                .description("Neu")
                .build();

        ContractDto resultDto = ContractDto.builder()
                .id("1")
                .title("Neu")
                .build();

        when(contractRepo.findById("1")).thenReturn(Optional.of(oldContract));
        when(contractMapper.fromDto(inputDto)).thenReturn(mappedContract);
        when(contractMapper.toDtoWithoutFile(any(Contract.class))).thenReturn(resultDto);

        // WHEN
        ContractDto result = contractService.updateContract("1", inputDto);

        // THEN
        assertEquals("Neu", result.title());

        verify(contractRepo).save(any(Contract.class));
    }

    /**
     * Testet das Löschen eines Contracts.
     */
    @Test
    void testDeleteContract() {

        // GIVEN: Contract existiert
        Contract contract = Contract.builder().id("1").build();
        when(contractRepo.findById("1")).thenReturn(Optional.of(contract));

        // WHEN
        contractService.deleteContract("1");

        // THEN
        verify(contractRepo).delete(contract);
    }

    /**
     * Testet das Erstellen eines neuen Contracts.
     */
    @Test
    void testCreateContract() {

        // GIVEN: Eingabe DTO
        ContractDto inputDto = ContractDto.builder()
                .title("Neuer Vertrag")
                .build();

        Contract mapped = Contract.builder()
                .title("Neuer Vertrag")
                .build();

        Contract saved = Contract.builder()
                .id("123")
                .title("Neuer Vertrag")
                .build();

        ContractDto resultDto = ContractDto.builder()
                .id("123")
                .title("Neuer Vertrag")
                .build();

        when(contractMapper.fromDto(inputDto)).thenReturn(mapped);
        when(contractRepo.save(mapped)).thenReturn(saved);
        when(contractMapper.toDtoWithoutFile(saved)).thenReturn(resultDto);

        // WHEN
        ContractDto result = contractService.createContract(inputDto);

        // THEN
        assertNotNull(result);
        assertEquals("123", result.id());

        verify(contractRepo).save(mapped);
    }
}
