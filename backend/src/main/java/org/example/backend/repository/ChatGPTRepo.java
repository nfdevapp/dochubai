package org.example.backend.repository;

import org.example.backend.model.entities.ChatGPT;
import org.example.backend.utils.enums.PromptType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatGPTRepo extends MongoRepository<ChatGPT,String> {
    Optional<ChatGPT> findByKey(PromptType key);
}
