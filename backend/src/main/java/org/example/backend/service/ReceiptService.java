package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.exeptions.DochubAppException;
import org.example.backend.model.entities.Receipt;
import org.example.backend.repository.ReceiptRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepo receiptRepo;

    public Receipt getReceiptById(String id) {
        return receiptRepo.findById(id)
                .orElseThrow(() -> new DochubAppException("Product not found: " + id));
    }

    public List<Receipt> getAllReceipts() {
        return receiptRepo.findAll();
    }

    public Receipt updateReceipt(String id, Receipt receipt) {
        Receipt oldData = receiptRepo.findById(id)
                .orElseThrow(() -> new DochubAppException("Receipt not found: " + id));
        receiptRepo.save(
                oldData.
                        withName(receipt.name()));
        return  receipt;
    }

    public void deleteReceipt(String id) {
        Receipt receipt = receiptRepo.findById(id)
                .orElseThrow(() -> new DochubAppException("Receipt not found: " + id));
        receiptRepo.delete(receipt);
    }

    public Receipt createReceipt(Receipt receipt) {
        //TODO OPEN AI
//        Receipt newReceipt = Receipt.builder()
//                .name(receipt.getName())
//                .build();
//        receiptRepo.save(newReceipt);
//        return newReceipt;
        return receiptRepo.save(receipt);
    }

}
