package org.example.backend.repository;

import org.example.backend.model.entities.ChatAi;
import org.example.backend.utils.enums.PromptType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatAiRepo extends MongoRepository<ChatAi,String> {
    Optional<ChatAi> findByKey(PromptType key);
}