package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.model.dto.ContractDto;
import org.example.backend.service.ContractService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContractController.class)
class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- GET /api/contract ----------
    @Test
    void getAllContracts_success() throws Exception {
        ContractDto dto = ContractDto.builder()
                .id("1")
                .title("Contract 1")
                .description("Desc 1")
                .build();

        when(contractService.getAllContracts()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/contract"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("Contract 1"))
                .andExpect(jsonPath("$[0].description").value("Desc 1"));

        verify(contractService).getAllContracts();
    }

    // ---------- GET /api/contract/{id} ----------
    @Test
    void getContractById_success() throws Exception {
        ContractDto dto = ContractDto.builder()
                .id("1")
                .title("Contract 1")
                .description("Desc 1")
                .build();

        when(contractService.getContractById("1")).thenReturn(dto);

        mockMvc.perform(get("/api/contract/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Contract 1"))
                .andExpect(jsonPath("$.description").value("Desc 1"));

        verify(contractService).getContractById("1");
    }

    // ---------- POST /api/contract ----------
    @Test
    void createContract_success() throws Exception {
        ContractDto dto = ContractDto.builder()
                .title("New Contract")
                .description("New Desc")
                .build();

        when(contractService.createContract(any())).thenReturn(dto);

        mockMvc.perform(post("/api/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Contract"))
                .andExpect(jsonPath("$.description").value("New Desc"));

        verify(contractService).createContract(any());
    }

    // ---------- PUT /api/contract/{id} ----------
    @Test
    void updateContract_success() throws Exception {
        ContractDto dto = ContractDto.builder()
                .title("Updated Contract")
                .description("Updated Desc")
                .build();

        when(contractService.updateContract(eq("1"), any())).thenReturn(dto);

        mockMvc.perform(put("/api/contract/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Contract"))
                .andExpect(jsonPath("$.description").value("Updated Desc"));

        verify(contractService).updateContract(eq("1"), any());
    }

    // ---------- DELETE /api/contract/{id} ----------
    @Test
    void deleteContract_success() throws Exception {
        doNothing().when(contractService).deleteContract("1");

        mockMvc.perform(delete("/api/contract/1"))
                .andExpect(status().isNoContent());

        verify(contractService).deleteContract("1");
    }
}
