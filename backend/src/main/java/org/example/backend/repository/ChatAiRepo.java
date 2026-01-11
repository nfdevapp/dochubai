package org.example.backend.repository;

import org.example.backend.model.entities.PromptsAi;
import org.example.backend.utils.enums.PromptType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatAiRepo extends MongoRepository<PromptsAi,String> {
    Optional<PromptsAi> findByKey(PromptType key);
}