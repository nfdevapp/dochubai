package org.example.backend.repository;

import org.example.backend.model.entities.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReceiptRepo extends MongoRepository<Receipt,String> {}

