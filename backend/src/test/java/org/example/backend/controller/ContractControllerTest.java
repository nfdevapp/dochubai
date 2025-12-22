//package org.example.backend.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.backend.model.dto.ContractDto;
//import org.example.backend.service.ContractService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ContractController.class)
//class ContractControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ContractService contractService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    /**
//     * Testet das Laden aller Contracts.
//     */
//    @Test
//    void testGetAllContracts() throws Exception {
//
//        // GIVEN: Zwei Contracts vom Service
//        ContractDto dto1 = ContractDto.builder().id("1").title("C1").build();
//        ContractDto dto2 = ContractDto.builder().id("2").title("C2").build();
//
//        when(contractService.getAllContracts()).thenReturn(List.of(dto1, dto2));
//
//        // WHEN + THEN
//        mockMvc.perform(get("/api/contract"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].id").value("1"))
//                .andExpect(jsonPath("$[1].title").value("C2"));
//
//        verify(contractService).getAllContracts();
//    }
//
//    /**
//     * Testet das Laden eines Contracts per ID.
//     */
//    @Test
//    void testGetContractById() throws Exception {
//
//        // GIVEN: ContractDto vom Service
//        ContractDto dto = ContractDto.builder()
//                .id("1")
//                .title("Mietvertrag")
//                .build();
//
//        when(contractService.getContractById("1")).thenReturn(dto);
//
//        // WHEN + THEN
//        mockMvc.perform(get("/api/contract/{id}", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1"))
//                .andExpect(jsonPath("$.title").value("Mietvertrag"));
//
//        verify(contractService).getContractById("1");
//    }
//
//    /**
//     * Testet das Erstellen eines neuen Contracts.
//     */
//    @Test
//    void testCreateContract() throws Exception {
//
//        // GIVEN: Eingabe-DTO + Rückgabe-DTO
//        ContractDto input = ContractDto.builder()
//                .title("Neuer Vertrag")
//                .build();
//
//        ContractDto saved = ContractDto.builder()
//                .id("123")
//                .title("Neuer Vertrag")
//                .build();
//
//        when(contractService.createContract(any(ContractDto.class))).thenReturn(saved);
//
//        // WHEN + THEN
//        mockMvc.perform(post("/api/contract")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(input)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("123"))
//                .andExpect(jsonPath("$.title").value("Neuer Vertrag"));
//
//        verify(contractService).createContract(any(ContractDto.class));
//    }
//
//    /**
//     * Testet das Aktualisieren eines Contracts.
//     */
//    @Test
//    void testUpdateContract() throws Exception {
//
//        // GIVEN
//        ContractDto input = ContractDto.builder()
//                .title("Aktualisiert")
//                .build();
//
//        ContractDto updated = ContractDto.builder()
//                .id("1")
//                .title("Aktualisiert")
//                .build();
//
//        when(contractService.updateContract(eq("1"), any(ContractDto.class)))
//                .thenReturn(updated);
//
//        // WHEN + THEN
//        mockMvc.perform(put("/api/contract/{id}", "1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(input)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1"))
//                .andExpect(jsonPath("$.title").value("Aktualisiert"));
//
//        verify(contractService).updateContract(eq("1"), any(ContractDto.class));
//    }
//
//    /**
//     * Testet das Löschen eines Contracts.
//     */
//    @Test
//    void testDeleteContract() throws Exception {
//
//        // GIVEN: nichts weiter notwendig
//
//        // WHEN + THEN
//        mockMvc.perform(delete("/api/contract/{id}", "1"))
//                .andExpect(status().isNoContent());
//
//        verify(contractService).deleteContract("1");
//    }
//}
