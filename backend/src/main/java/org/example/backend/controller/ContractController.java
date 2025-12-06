package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.entities.Contract;
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
    public ResponseEntity<List<Contract>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable String id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PostMapping
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        return ResponseEntity.ok(contractService.createContract(contract));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable String id, @RequestBody Contract contract) {
        return ResponseEntity.ok(contractService.updateContract(id, contract));
    }


//    fetch("/api/contract/1", { method: "DELETE" })
//            .then(response => {
//        if (response.status === 204) {
//            console.log("Löschen erfolgreich!");
//        } else {
//            console.error("Etwas ist schiefgelaufen!");
//        }
//    });
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable String id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}
