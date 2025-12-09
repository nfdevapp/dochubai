package org.example.backend.repository;

import org.example.backend.model.entities.Statement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatementRepo extends MongoRepository<Statement,String> {}
