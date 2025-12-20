package org.example.backend.repository;

import org.example.backend.model.entities.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepo extends MongoRepository<Invoice,String> {
    // Liefert alle Rechnungen (isInvoice = true) ab einem bestimmten Datum
    @Query("{ 'isInvoice': true, 'date': { '$gte': ?0 } }")
    List<Invoice> findInvoicesFrom(LocalDate fromDate);
}
