package org.example.backend.ai;


import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class FileTextExtractor {

    private final Tika tika;

    public FileTextExtractor() {
        this.tika = new Tika();
    }

    /**
     * Extrahiert Text aus einer Datei, die als byte[] übergeben wird.
     * Unterstützt PDF, Word, Bilder usw.
     */
    public String extractText(byte[] fileBytes) {
        if (fileBytes == null) return null;

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            return tika.parseToString(inputStream);
        } catch (IOException | TikaException e) {
            e.printStackTrace();
            return null;
        }
    }
}
