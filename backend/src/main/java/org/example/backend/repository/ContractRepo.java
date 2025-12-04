package org.example.backend.repository;

import org.example.backend.model.entities.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContractRepo extends MongoRepository<Contract,String> {}
