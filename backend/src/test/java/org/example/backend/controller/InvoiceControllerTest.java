package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.model.dto.InvoiceAiDto;
import org.example.backend.model.dto.InvoiceDto;
import org.example.backend.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- GET /api/invoice ----------

    @Test
    void getAllInvoices_success() throws Exception {
        InvoiceDto dto = InvoiceDto.builder()
                .id("1")
                .docNumber("DOC-1")
                .amount(100)
                .build();

        when(invoiceService.getAllInvoices()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/invoice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].docNumber").value("DOC-1"));

        verify(invoiceService).getAllInvoices();
    }

    // ---------- GET /api/invoice/{id} ----------

    @Test
    void getInvoiceById_success() throws Exception {
        InvoiceDto dto = InvoiceDto.builder()
                .id("1")
                .docNumber("DOC-1")
                .build();

        when(invoiceService.getInvoiceById("1")).thenReturn(dto);

        mockMvc.perform(get("/api/invoice/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        verify(invoiceService).getInvoiceById("1");
    }

    // ---------- POST /api/invoice ----------

    @Test
    void createInvoice_success() throws Exception {
        InvoiceDto dto = InvoiceDto.builder()
                .docNumber("DOC-NEW")
                .amount(200)
                .build();

        when(invoiceService.createInvoice(any())).thenReturn(dto);

        mockMvc.perform(post("/api/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(invoiceService).createInvoice(any());
    }

    // ---------- PUT /api/invoice/{id} ----------

    @Test
    void updateInvoice_success() throws Exception {
        InvoiceDto dto = InvoiceDto.builder()
                .docNumber("UPDATED")
                .build();

        when(invoiceService.updateInvoice(eq("1"), any())).thenReturn(dto);

        mockMvc.perform(put("/api/invoice/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(invoiceService).updateInvoice(eq("1"), any());
    }

    // ---------- DELETE /api/invoice/{id} ----------

    @Test
    void deleteInvoice_success() throws Exception {
        doNothing().when(invoiceService).deleteInvoice("1");

        mockMvc.perform(delete("/api/invoice/1"))
                .andExpect(status().isNoContent());

        verify(invoiceService).deleteInvoice("1");
    }

    // ---------- GET /api/invoice/chart ----------

    @Test
    void getInvoiceChart_success() throws Exception {
        InvoiceDto dto = InvoiceDto.builder()
                .id("1")
                .amount(150)
                .build();

        when(invoiceService.getInvoiceChart()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/invoice/chart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(150));

        verify(invoiceService).getInvoiceChart();
    }

    // ---------- GET /api/invoice/aianalysis ----------

    @Test
    void getLastInvoiceAiAnalysis_success() throws Exception {
        InvoiceAiDto aiDto = InvoiceAiDto.builder()
                .aiAnalysisText("AI RESULT")
                .build();

        when(invoiceService.getLastInvoiceAiAnalysis()).thenReturn(aiDto);

        mockMvc.perform(get("/api/invoice/aianalysis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aiAnalysisText").value("AI RESULT"));

        verify(invoiceService).getLastInvoiceAiAnalysis();
    }

    // ---------- POST /api/invoice/aianalysis ----------

    @Test
    void analyzeInvoices_success() throws Exception {
        InvoiceAiDto aiDto = InvoiceAiDto.builder()
                .aiAnalysisText("ANALYZED")
                .build();

        when(invoiceService.runInvoiceAiAnalysis()).thenReturn(aiDto);

        mockMvc.perform(post("/api/invoice/aianalysis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aiAnalysisText").value("ANALYZED"));

        verify(invoiceService).runInvoiceAiAnalysis();
    }
}