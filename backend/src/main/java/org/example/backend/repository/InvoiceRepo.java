package org.example.backend.repository;

import org.example.backend.model.entities.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceRepo extends MongoRepository<Invoice,String> {}
