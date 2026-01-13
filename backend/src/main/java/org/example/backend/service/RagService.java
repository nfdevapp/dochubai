//package org.example.backend.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.backend.ai.embedding.EmbeddingService;
//import org.example.backend.exeptions.DocHubAiException;
//import org.springframework.stereotype.Service;
//import org.bson.Document;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class RagService {
//
//    private final ChunkService chunkService;
//    private final EmbeddingService embeddingService;
//    private final ChatGPTService chatGPTService;
//
//    /**
//     * Stellt eine Frage an die gesamte Wissensbasis
//     */
//    public String ask(String question) {
//        try {
//            List<Double> questionEmbedding = embeddingService.createEmbedding(question);
//
//            List<Document> chunks = chunkService.searchRelevantChunks(
//                    questionEmbedding,
//                    null, // kein spezifischer Contract, globale Suche
//                    5
//            );
//
//            if (chunks.isEmpty()) {
//                return "Keine relevanten Informationen gefunden.";
//            }
//
//            StringBuilder context = new StringBuilder();
//            for (Document doc : chunks) {
//                context.append("Quelle (")
//                        .append(doc.getString("entityType"))
//                        .append(", ID=")
//                        .append(doc.getString("entityId"))
//                        .append("):\n")
//                        .append(doc.getString("text"))
//                        .append("\n\n");
//            }
//
//            return chatGPTService.askWithContextForRag(context.toString(), question);
//
//        } catch (Exception e) {
//            throw new DocHubAiException("Fehler bei der RAG-Abfrage", e);
//        }
//    }
//}
