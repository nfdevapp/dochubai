package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.ContractDto;
import org.example.backend.service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Vertrag. ResponseEntity => HTTP-Antworten steuern, nicht nur den Body. Kontrolle über den HTTP-Statuscode
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contract")
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public ResponseEntity<List<ContractDto>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ContractDto> getContractById(@PathVariable String id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PostMapping
    public ResponseEntity<ContractDto> createContract(@RequestBody ContractDto contractDto) {
        return ResponseEntity.ok(contractService.createContract(contractDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractDto> updateContract(@PathVariable String id, @RequestBody ContractDto contractDto) {
        return ResponseEntity.ok(contractService.updateContract(id, contractDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable String id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}
