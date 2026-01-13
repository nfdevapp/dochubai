//package org.example.backend.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.backend.ai.embedding.EmbeddingService;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Service;
//import org.bson.Document;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Datei => Text => Chunking => Embedding => MongoDB (text + vector + Metadaten)
// *
// *
// * Ein Chunk sieht so aus:
// * {
// *   "entityType": "Contract",
// *   "entityId": "abc123",
// *   "chunkIndex": 4,
// *   "text": "Der Mietvertrag beträgt drei Monate...",
// *   "embedding": [0.0123, -0.8844, 0.3311, ...]
// * }
// *
// */
//@Service
//@RequiredArgsConstructor
//public class ChunkService {
//    private final MongoTemplate mongoTemplate;
//
//    private final EmbeddingService embeddingService;
//
//    private static final String COLLECTION = "contract_chunks";
//    // max Zeichen pro Chunk
//    private static final int CHUNK_SIZE = 500;
//    /**
//     * Wenn Sätze über Chunk-Grenzen gehen, gehen Infos evtl. verloren
//     * Deswegen 100 Zeichen Überlappung:
//     * Chunk 1: Zeichen 0–500, Chunk 2: Zeichen 400–900, Chunk 3: Zeichen 800–1300
//     */
//    private static final int OVERLAP = 100;
//
//    //Text to Chunks
//    public List<String> chunkText(String text) {
//        List<String> chunks = new ArrayList<>();
//        if (text == null || text.isBlank()) return chunks;
//
//        for (int i = 0; i < text.length(); i += (CHUNK_SIZE - OVERLAP)) {
//            chunks.add(text.substring(i, Math.min(text.length(), i + CHUNK_SIZE)));
//        }
//        return chunks;
//    }
//
//    //Chunks speichern
//    public void saveChunks(String contractId, List<String> chunks) {
//        if (chunks == null || chunks.isEmpty()) return;
//
//        List<Document> docs = new ArrayList<>();
//        for (int i = 0; i < chunks.size(); i++) {
//            String chunkText = chunks.get(i);
//            //Embedding erzeugen
//            List<Double> embedding = embeddingService.createEmbedding(chunkText);
//
//            docs.add(new Document()
//                    .append("entityType", "Contract")//Chunks von verschiedenen Entitätstypen unterscheiden
//                    .append("entityId", contractId)//Link zurück zum Originalvertrag
//                    .append("chunkIndex", i)//Reihenfolge der Chunks im Originaltext
//                    .append("text", chunkText)//Originaltext
//                    .append("embedding", embedding));  //Vektor
//
//        }
//        /**
//         * Speichert alle Chunks als Dokumente in der MongoDB-Collection `contract_chunks`.
//         * Jedes Dokument enthält den Text des Chunks, seine Reihenfolge (chunkIndex)
//         * und die zugehörige Contract-ID.
//         */
//        mongoTemplate.getCollection(COLLECTION).insertMany(docs);
//    }
//
//    //Chunks löschen (bei Update/Delete)
//    public void deleteChunks(String contractId) {
//        mongoTemplate.getCollection(COLLECTION)
//                .deleteMany(new Document("entityId", contractId));
//    }
//
//    //nach chunks suchen
//    public List<Document> searchRelevantChunks(
//            List<Double> queryEmbedding,
//            String contractId,
//            int limit
//    ) {
//        Document vectorSearchStage = new Document("$vectorSearch",
//                new Document("index", "default") // Name deines Atlas Index
//                        .append("path", "embedding")
//                        .append("queryVector", queryEmbedding)
//                        .append("numCandidates", 100)
//                        .append("limit", limit)
//                        .append("filter",
//                                new Document("entityId", contractId))
//        );
//
//        return mongoTemplate.getCollection(COLLECTION)
//                .aggregate(List.of(vectorSearchStage))
//                .into(new ArrayList<>());
//    }
//
//}
