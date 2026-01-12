package org.example.backend.service;

import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.ContractDto;
import org.example.backend.model.entities.Contract;
import org.example.backend.repository.ContractRepo;
import org.example.backend.utils.mapper.ContractMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private ContractRepo contractRepo;

    @Mock
    private ContractMapper contractMapper;

    @InjectMocks
    private ContractService contractService;

    // ---------- getContractById ----------

    @Test
    void getContractById_success() {
        // GIVEN
        Contract contract = mock(Contract.class);
        ContractDto dto = mock(ContractDto.class);

        when(contractRepo.findById("1")).thenReturn(Optional.of(contract));
        when(contractMapper.toDto(contract)).thenReturn(dto);

        // WHEN
        ContractDto result = contractService.getContractById("1");

        // THEN
        assertEquals(dto, result);
        verify(contractRepo).findById("1");
        verify(contractMapper).toDto(contract);
    }

    @Test
    void getContractById_notFound() {
        // GIVEN
        when(contractRepo.findById("1")).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(
                DocHubAiException.class,
                () -> contractService.getContractById("1")
        );
    }

    // ---------- getAllContracts ----------

    @Test
    void getAllContracts_success() {
        // GIVEN
        Contract contract1 = mock(Contract.class);
        Contract contract2 = mock(Contract.class);
        ContractDto dto1 = mock(ContractDto.class);
        ContractDto dto2 = mock(ContractDto.class);

        when(contractRepo.findAll()).thenReturn(List.of(contract1, contract2));
        when(contractMapper.toDtoWithoutFile(contract1)).thenReturn(dto1);
        when(contractMapper.toDtoWithoutFile(contract2)).thenReturn(dto2);

        // WHEN
        List<ContractDto> result = contractService.getAllContracts();

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    // ---------- createContract ----------

    @Test
    void createContract_success() {
        // GIVEN
        ContractDto inputDto = mock(ContractDto.class);
        Contract mapped = mock(Contract.class);
        Contract saved = mock(Contract.class);
        ContractDto outputDto = mock(ContractDto.class);

        when(contractMapper.fromDto(inputDto)).thenReturn(mapped);
        when(contractRepo.save(mapped)).thenReturn(saved);
        when(contractMapper.toDtoWithoutFile(saved)).thenReturn(outputDto);

        // WHEN
        ContractDto result = contractService.createContract(inputDto);

        // THEN
        assertEquals(outputDto, result);
        verify(contractRepo).save(mapped);
    }

    // ---------- updateContract ----------

    @Test
    void updateContract_success() {
        // GIVEN
        ContractDto dto = mock(ContractDto.class);

        Contract oldContract = mock(Contract.class);
        Contract mapped = mock(Contract.class);
        Contract updated = mock(Contract.class);

        when(contractRepo.findById("1")).thenReturn(Optional.of(oldContract));
        when(contractMapper.fromDto(dto)).thenReturn(mapped);

        when(oldContract.withTitle(any())).thenReturn(oldContract);
        when(oldContract.withDescription(any())).thenReturn(oldContract);
        when(oldContract.withStartDate(any())).thenReturn(oldContract);
        when(oldContract.withEndDate(any())).thenReturn(oldContract);
        when(oldContract.withAiLevel(anyInt())).thenReturn(oldContract);
        when(oldContract.withAiAnalysisText(any())).thenReturn(oldContract);
        when(oldContract.withFileName(any())).thenReturn(oldContract);
        when(oldContract.withFile(any())).thenReturn(updated);

        when(contractRepo.save(updated)).thenReturn(updated);
        when(contractMapper.toDtoWithoutFile(updated)).thenReturn(dto);

        // WHEN
        ContractDto result = contractService.updateContract("1", dto);

        // THEN
        assertEquals(dto, result);
        verify(contractRepo).save(updated);
    }

    @Test
    void updateContract_notFound() {
        // GIVEN
        when(contractRepo.findById("1")).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(
                DocHubAiException.class,
                () -> contractService.updateContract("1", mock(ContractDto.class))
        );
    }

    // ---------- deleteContract ----------

    @Test
    void deleteContract_success() {
        // GIVEN
        Contract contract = mock(Contract.class);
        when(contractRepo.findById("1")).thenReturn(Optional.of(contract));

        // WHEN
        contractService.deleteContract("1");

        // THEN
        verify(contractRepo).delete(contract);
    }

    @Test
    void deleteContract_notFound() {
        // GIVEN
        when(contractRepo.findById("1")).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(
                DocHubAiException.class,
                () -> contractService.deleteContract("1")
        );
    }

    // ---------- deleteAllContracts ----------

    @Test
    void deleteAllContracts_success() {
        // WHEN
        contractService.deleteAllContracts();

        // THEN
        verify(contractRepo).deleteAll();
    }

    // ---------- createTestDataContract ----------

    @Test
    void createTestDataContract_success() {
        // GIVEN
        Contract contract = mock(Contract.class);

        // WHEN
        contractService.createTestDataContract(contract);

        // THEN
        verify(contractRepo).save(contract);
    }
}