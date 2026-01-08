package org.example.backend.ai.chunk;

//import lombok.RequiredArgsConstructor;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.index.TextIndexDefinition;
//import org.springframework.stereotype.Component;

/**
 * Initialisiert automatisch einen Text-Index auf der MongoDB-Collection `contract_chunks`.
 *
 * Zweck:
 * - Erstellen eines Text-Indexes auf dem Feld `text`, damit spätere Textsuchen
 *   (z.B. mit $text Queries) effizient und performant durchgeführt werden können.
 *
 * Funktionsweise:
 * - Wird automatisch beim Start der Spring Boot Anwendung ausgeführt
 *   (durch @EventListener auf ContextRefreshedEvent).
 * - Verwendet MongoTemplate, um den Index auf der Collection `contract_chunks` zu erstellen.
 * - Mit dem Text-Index kann MongoDB schnell nach bestimmten Wörtern oder Phrasen im
 *   `text`-Feld suchen, was für die Suche nach relevanten Chunks notwendig ist.
 *
 *   !!!!AUF VECTOR SUCHE UMGESTIEGEN => MongoIndexInitializer wird nur bei exakte Suche (Textsuche) gebraucht
 *   daher auskommentiert!!!
 */

//@Component
//@RequiredArgsConstructor
public class MongoIndexInitializer {

//    private final MongoTemplate mongoTemplate;
//
//    @EventListener(ContextRefreshedEvent.class)
//    public void initIndexes() {
//        TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
//                .onField("text")
//                .build();
//
//        mongoTemplate.indexOps("contract_chunks").createIndex(textIndex);
//    }
}
