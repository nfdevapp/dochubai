package org.example.backend.repository;

import org.example.backend.model.entities.InvoiceAi;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceAiRepo extends MongoRepository<InvoiceAi,String> {}

