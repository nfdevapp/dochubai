package org.example.backend.ai;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class FileTextExtractor {

    private final Tika tika;
    private final Tesseract tesseract;

    public FileTextExtractor() {
        this.tika = new Tika();
        this.tesseract = new Tesseract();
        this.tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
    }

    /**
     * Extrahiert Text aus einer Datei (PDF, DOCX, TXT etc.).
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

    /**
     * Extrahiert Text aus einem Bild (JPG, PNG) mittels OCR.
     */
    public String extractTextFromImage(byte[] imageBytes) {
        if (imageBytes == null) return null;

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                return null;
            }
            return tesseract.doOCR(image);
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }
}
