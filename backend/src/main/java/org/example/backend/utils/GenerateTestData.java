package org.example.backend.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.backend.service.ChatGPTService;
import org.example.backend.model.entities.PromptsAi;
import org.example.backend.model.entities.Contract;
import org.example.backend.model.entities.Invoice;
import org.example.backend.service.ContractService;
import org.example.backend.service.InvoiceService;
import org.example.backend.utils.enums.PromptType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateTestData {

    private final ContractService contractService;
    private final InvoiceService invoiceService;
    private final ChatGPTService chatGPTService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @PostConstruct
    public void init() {
        createContracts();
        createInoices();
        createPrompts();
    }

    private void createInoices(){
        // Vorhandene Invoices löschen
        invoiceService.deleteAllInvoices();
        // Vorhandene Ai Analyse Invoices löschen
        invoiceService.deleteAiInvoices();
        //Testdaten als InvoiceDto erstellen
        List<Invoice> invoices = createTestDataInvoices();
        invoices.forEach(invoiceService::createTestDataInvoice);

    }

    private void createContracts() {
        // Vorhandene Contracts löschen
        contractService.deleteAllContracts();
        //Testdaten als ContractDto erstellen
        List<Contract> contracts = createTestDataContracts();
        contracts.forEach(contractService::createTestDataContract);

    }

    private List<Invoice> createTestDataInvoices() {
        LocalDate startDate = LocalDate.now().minusMonths(3);

        return List.of(
                createInvoice("RE-2024-001", startDate.plusDays(0), 1250.00, "Miete Büroräume Januar 2024", true, "Miete_Jan_2024.pdf", null),
                createInvoice("RE-2024-002", startDate.plusDays(1), 320.50, "IT-Wartung Februar", true, "IT_Wartung_Feb.pdf", null),
                createInvoice("RE-2024-003", startDate.plusDays(2), 89.99, "Software-Abonnement", true, "SaaS_Abo_Feb.pdf", null),
                createInvoice("ZA-2024-004", startDate.plusDays(3), 500.00, "Anzahlung Projekt Alpha", false, "Zahlungsbeleg_Alpha.pdf", null),
                createInvoice("RE-2024-005", startDate.plusDays(4), 2450.75, "Beratungsleistung Februar", true, "Beratung_Februar.docx", null),
                createInvoice("RE-2024-006", startDate.plusDays(5), 159.00, "Cloud-Hosting März", true, "Cloud_Hosting_Maerz.pdf", null),
                createInvoice("ZA-2024-007", startDate.plusDays(6), 1200.00, "Leasingrate Fahrzeug", false, "Leasingrate_Maerz.pdf", null),
                createInvoice("RE-2024-008", startDate.plusDays(7), 980.40, "Marketing Agenturleistung", true, "Agentur_Marketing.pdf", null),
                createInvoice("RE-2024-009", startDate.plusDays(8), 75.00, "Domain- und Hostinggebühren", true, "Domain_Hosting.pdf", null),
                createInvoice("RE-2024-010", startDate.plusDays(9), 4300.00, "Bauabschlagsrechnung", true, "Bau_Abschlag_03_2024.pdf", null),

                createInvoice("RE-2024-011", startDate.plusDays(10), 222.0, "Beratung Müller", true, "Beratung_01_04_2024.pdf", null),
                createInvoice("RE-2024-012", startDate.plusDays(11), 97.0, "IT-Service Schulz", true, "ITService_02_04_2024.jpeg", null),
                createInvoice("RE-2024-013", startDate.plusDays(12), 167.0, "Marketing Schmidt", true, "Marketing_03_04_2024.pdf", null),
                createInvoice("RE-2024-014", startDate.plusDays(13), 242.0, "Support Wagner", true, "Support_04_04_2024.jpeg", null),
                createInvoice("RE-2024-015", startDate.plusDays(14), 373.0, "Beratung Becker", true, "Beratung_05_04_2024.pdf", null),
                createInvoice("RE-2024-016", startDate.plusDays(15), 301.0, "IT-Service Hofmann", true, "ITService_06_04_2024.pdf", null),
                createInvoice("RE-2024-017", startDate.plusDays(16), 245.0, "Marketing Fischer", true, "Marketing_07_04_2024.jpeg", null),
                createInvoice("RE-2024-018", startDate.plusDays(17), 409.0, "Support Weber", true, "Support_08_04_2024.pdf", null),
                createInvoice("RE-2024-019", startDate.plusDays(18), 59.0, "Beratung Schäfer", true, "Beratung_09_04_2024.jpeg", null),
                createInvoice("RE-2024-020", startDate.plusDays(19), 261.0, "IT-Service Richter", true, "ITService_10_04_2024.pdf", null),

                createInvoice("RE-2024-021", startDate.plusDays(20), 327.0, "Marketing Klein", true, "Marketing_11_04_2024.jpeg", null),
                createInvoice("RE-2024-022", startDate.plusDays(21), 292.0, "Support Wolf", true, "Support_12_04_2024.pdf", null),
                createInvoice("RE-2024-023", startDate.plusDays(22), 342.0, "Beratung Zimmermann", true, "Beratung_13_04_2024.jpeg", null),
                createInvoice("RE-2024-024", startDate.plusDays(23), 137.0, "IT-Service Braun", true, "ITService_14_04_2024.pdf", null),
                createInvoice("RE-2024-025", startDate.plusDays(24), 120.0, "Marketing Hartmann", true, "Marketing_15_04_2024.jpeg", null),
                createInvoice("RE-2024-026", startDate.plusDays(25), 138.0, "Support Lange", true, "Support_16_04_2024.pdf", null),
                createInvoice("RE-2024-027", startDate.plusDays(26), 446.0, "Beratung Schmitt", true, "Beratung_17_04_2024.jpeg", null),
                createInvoice("RE-2024-028", startDate.plusDays(27), 364.0, "IT-Service Werner", true, "ITService_18_04_2024.pdf", null),
                createInvoice("RE-2024-029", startDate.plusDays(28), 243.0, "Marketing Neumann", true, "Marketing_19_04_2024.jpeg", null),
                createInvoice("RE-2024-030", startDate.plusDays(29), 89.0, "Support König", true, "Support_20_04_2024.pdf", null),

                createInvoice("RE-2024-031", startDate.plusDays(30), 137.0, "Beratung Frank", true, "Beratung_21_04_2024.jpeg", null),
                createInvoice("RE-2024-032", startDate.plusDays(31), 224.0, "IT-Service Maier", true, "ITService_22_04_2024.pdf", null),
                createInvoice("RE-2024-033", startDate.plusDays(32), 138.0, "Marketing Bauer", true, "Marketing_23_04_2024.jpeg", null),
                createInvoice("RE-2024-034", startDate.plusDays(33), 387.0, "Support Schulze", true, "Support_24_04_2024.pdf", null),
                createInvoice("RE-2024-035", startDate.plusDays(34), 215.0, "Beratung Fuchs", true, "Beratung_25_04_2024.jpeg", null),
                createInvoice("RE-2024-036", startDate.plusDays(35), 75.0, "IT-Service Busch", true, "ITService_26_04_2024.pdf", null),
                createInvoice("RE-2024-037", startDate.plusDays(36), 383.0, "Marketing Horn", true, "Marketing_27_04_2024.jpeg", null),
                createInvoice("RE-2024-038", startDate.plusDays(37), 122.0, "Support Krüger", true, "Support_28_04_2024.pdf", null),
                createInvoice("RE-2024-039", startDate.plusDays(38), 315.0, "Beratung Graf", true, "Beratung_29_04_2024.jpeg", null),
                createInvoice("RE-2024-040", startDate.plusDays(39), 454.0, "IT-Service Pohl", true, "ITService_30_04_2024.pdf", null),

                createInvoice("RE-2024-041", startDate.plusDays(40), 165.0, "Marketing Lorenz", false, "Marketing_01_05_2024.jpeg", null),
                createInvoice("RE-2024-042", startDate.plusDays(41), 293.0, "Support Voigt", true, "Support_02_05_2024.pdf", null),
                createInvoice("RE-2024-043", startDate.plusDays(42), 247.0, "Beratung Kühn", true, "Beratung_03_05_2024.jpeg", null),
                createInvoice("RE-2024-044", startDate.plusDays(43), 385.0, "IT-Service Stark", false, "ITService_04_05_2024.pdf", null),
                createInvoice("RE-2024-045", startDate.plusDays(44), 481.0, "Marketing Engel", true, "Marketing_05_05_2024.jpeg", null),
                createInvoice("RE-2024-046", startDate.plusDays(45), 498.0, "Support Dietrich", true, "Support_06_05_2024.pdf", null),
                createInvoice("RE-2024-047", startDate.plusDays(46), 388.0, "Beratung Arnold", true, "Beratung_07_05_2024.jpeg", null),
                createInvoice("RE-2024-048", startDate.plusDays(47), 149.0, "IT-Service Albrecht", true, "ITService_08_05_2024.pdf", null),
                createInvoice("RE-2024-049", startDate.plusDays(48), 227.0, "Marketing Seidel", false, "Marketing_09_05_2024.jpeg", null),
                createInvoice("RE-2024-050", startDate.plusDays(49), 293.0, "Support Barth", true, "Support_10_05_2024.pdf", null),

                createInvoice("RE-2024-051", startDate.plusDays(50), 335.0, "Beratung Huber", true, "Beratung_11_05_2024.jpeg", null),
                createInvoice("RE-2024-052", startDate.plusDays(51), 197.0, "IT-Service König", false, "ITService_12_05_2024.pdf", null),
                createInvoice("RE-2024-053", startDate.plusDays(52), 197.0, "Marketing Bauer", true, "Marketing_13_05_2024.jpeg", null),
                createInvoice("RE-2024-054", startDate.plusDays(53), 448.0, "Support Wolf", true, "Support_14_05_2024.pdf", null),
                createInvoice("RE-2024-055", startDate.plusDays(54), 473.0, "Beratung Klein", true, "Beratung_15_05_2024.jpeg", null),
                createInvoice("RE-2024-056", startDate.plusDays(55), 338.0, "IT-Service Hartmann", true, "ITService_16_05_2024.pdf", null),
                createInvoice("RE-2024-057", startDate.plusDays(56), 499.0, "Marketing Lehmann", false, "Marketing_17_05_2024.jpeg", null),
                createInvoice("RE-2024-058", startDate.plusDays(57), 315.0, "Support Frank", true, "Support_18_05_2024.pdf", null),
                createInvoice("RE-2024-059", startDate.plusDays(58), 235.0, "Beratung Graf", true, "Beratung_19_05_2024.jpeg", null),
                createInvoice("RE-2024-060", startDate.plusDays(59), 177.0, "IT-Service Voigt", true, "ITService_20_05_2024.pdf", null),

                createInvoice("RE-2024-061", startDate.plusDays(60), 82.0, "Marketing Pohl", true, "Marketing_21_05_2024.jpeg", null),
                createInvoice("RE-2024-062", startDate.plusDays(61), 81.0, "Support Lorenz", true, "Support_22_05_2024.pdf", null),
                createInvoice("RE-2024-063", startDate.plusDays(62), 252.0, "Beratung Müller", true, "Beratung_23_05_2024.jpeg", null),
                createInvoice("RE-2024-064", startDate.plusDays(63), 294.0, "IT-Service Schulz", false, "ITService_24_05_2024.pdf", null),
                createInvoice("RE-2024-065", startDate.plusDays(64), 201.0, "Marketing Schmidt", true, "Marketing_25_05_2024.jpeg", null),
                createInvoice("RE-2024-066", startDate.plusDays(65), 213.0, "Support Wagner", true, "Support_26_05_2024.pdf", null),
                createInvoice("RE-2024-067", startDate.plusDays(66), 420.0, "Beratung Becker", true, "Beratung_27_05_2024.jpeg", null),
                createInvoice("RE-2024-068", startDate.plusDays(67), 233.0, "IT-Service Hofmann", true, "ITService_28_05_2024.pdf", null),
                createInvoice("RE-2024-069", startDate.plusDays(68), 78.0, "Marketing Fischer", true, "Marketing_29_05_2024.jpeg", null),
                createInvoice("RE-2024-070", startDate.plusDays(69), 340.0, "Support Weber", true, "Support_30_05_2024.pdf", null),

                createInvoice("RE-2024-071", startDate.plusDays(70), 178.0, "Beratung Schäfer", true, "Beratung_31_05_2024.jpeg", null),
                createInvoice("RE-2024-072", startDate.plusDays(71), 178.0, "IT-Service Richter", true, "ITService_01_06_2024.pdf", null),
                createInvoice("RE-2024-073", startDate.plusDays(72), 470.0, "Marketing Klein", false, "Marketing_02_06_2024.jpeg", null),
                createInvoice("RE-2024-074", startDate.plusDays(73), 103.0, "Support Wolf", true, "Support_03_06_2024.pdf", null),
                createInvoice("RE-2024-075", startDate.plusDays(74), 439.0, "Beratung Graf", true, "Beratung_04_06_2024.jpeg", null),
                createInvoice("RE-2024-076", startDate.plusDays(75), 88.0, "IT-Service Voigt", true, "ITService_05_06_2024.pdf", null),
                createInvoice("RE-2024-077", startDate.plusDays(76), 294.0, "Marketing Pohl", true, "Marketing_06_06_2024.jpeg", null),
                createInvoice("RE-2024-078", startDate.plusDays(77), 323.0, "Support Lorenz", true, "Support_07_06_2024.pdf", null),
                createInvoice("RE-2024-079", startDate.plusDays(78), 385.0, "Beratung Müller", true, "Beratung_08_06_2024.jpeg", null),
                createInvoice("RE-2024-080", startDate.plusDays(79), 438.0, "IT-Service Schulz", true, "ITService_09_06_2024.pdf", null),

                createInvoice("RE-2024-081", startDate.plusDays(80), 155.0, "Marketing Schmidt", true, "Marketing_10_06_2024.jpeg", null),
                createInvoice("RE-2024-082", startDate.plusDays(81), 92.0, "Support Wagner", true, "Support_11_06_2024.pdf", null),
                createInvoice("RE-2024-083", startDate.plusDays(82), 492.0, "Beratung Becker", false, "Beratung_12_06_2024.jpeg", null),
                createInvoice("RE-2024-084", startDate.plusDays(83), 81.0, "IT-Service Hofmann", true, "ITService_13_06_2024.pdf", null),
                createInvoice("RE-2024-085", startDate.plusDays(84), 426.0, "Marketing Fischer", true, "Marketing_14_06_2024.jpeg", null),
                createInvoice("RE-2024-086", startDate.plusDays(85), 307.0, "Support Weber", true, "Support_15_06_2024.pdf", null),
                createInvoice("RE-2024-087", startDate.plusDays(86), 371.0, "Beratung Schäfer", true, "Beratung_16_06_2024.jpeg", null),
                createInvoice("RE-2024-088", startDate.plusDays(87), 475.0, "IT-Service Richter", true, "ITService_17_06_2024.pdf", null),
                createInvoice("RE-2024-089", startDate.plusDays(88), 107.0, "Marketing Klein", false, "Marketing_18_06_2024.jpeg", null),
                createInvoice("RE-2024-090", startDate.plusDays(89), 341.0, "Support Wolf", true, "Support_19_06_2024.pdf", null),

                createInvoice("RE-2024-091", startDate.plusDays(90), 408.0, "Beratung Graf", true, "Beratung_20_06_2024.jpeg", null),
                createInvoice("RE-2024-092", startDate.plusDays(91), 169.0, "IT-Service Voigt", true, "ITService_21_06_2024.pdf", null),
                createInvoice("RE-2024-093", startDate.plusDays(92), 317.0, "Marketing Pohl", true, "Marketing_22_06_2024.jpeg", null),
                createInvoice("RE-2024-094", startDate.plusDays(93), 480.0, "Support Lorenz", false, "Support_23_06_2024.pdf", null),
                createInvoice("RE-2024-095", startDate.plusDays(94), 132.0, "Beratung Müller", true, "Beratung_24_06_2024.jpeg", null),
                createInvoice("RE-2024-096", startDate.plusDays(95), 141.0, "IT-Service Schulz", true, "ITService_25_06_2024.pdf", null),
                createInvoice("RE-2024-097", startDate.plusDays(96), 434.0, "Marketing Schmidt", true, "Marketing_26_06_2024.jpeg", null),
                createInvoice("RE-2024-098", startDate.plusDays(97), 448.0, "Support Wagner", true, "Support_27_06_2024.pdf", null),
                createInvoice("RE-2024-099", startDate.plusDays(98), 149.0, "Beratung Becker", true, "Beratung_28_06_2024.jpeg", null),
                createInvoice("RE-2024-100", startDate.plusDays(99), 103.0, "IT-Service Hofmann", true, "ITService_29_06_2024.pdf", null),
                createInvoice("RE-2024-101", startDate.plusDays(100), 446.0, "Marketing Fischer", true, "Marketing_30_06_2024.jpeg", null)
        );

    }

    private Invoice createInvoice(
            String docNumber,
            LocalDate date,
            double amount,
            String purpose,
            boolean isInvoice,
            String fileName,
            byte[] file
    ) {
        return Invoice.builder()
                .docNumber(docNumber)
                .date(date)
                .amount(amount)
                .purpose(purpose)
                .isInvoice(isInvoice)
                .fileName(fileName)
                .file(null) // keine Datei für Testdaten
                .build();
    }


    private List<Contract> createTestDataContracts() {
        return List.of(
                createContract(
                        "Mietvertrag",
                        "Mietvertrag für Büro- oder Wohnräume.",
                        "15.01.2024",
                        "15.01.2025",
                        "Dieser Mietvertrag regelt die Überlassung von Wohn- oder Büroräumen. Mietdauer, Miethöhe, Nebenkosten sowie Kündigungsfristen sind festgelegt.",
                        1,
                        "Klare Struktur und umfassende Bedingungen, keine Auffälligkeiten.",
                        "Mietvertrag.pdf",
                        null
                ),

                createContract(
                        "Arbeitsvertrag",
                        "Unbefristeter Arbeitsvertrag mit Standardklauseln.",
                        "03.11.2023",
                        "03.11.2024",
                        "Der Arbeitsvertrag regelt das Arbeitsverhältnis zwischen Arbeitgeber und Arbeitnehmer. Arbeitszeit, Vergütung, Urlaub und Kündigungsfristen sind definiert.",
                        2,
                        "Enthält die Grundregeln, aber einige Kündigungsfristen könnten präziser sein.",
                        "Arbeitsvertrag.docx",
                        null
                ),

                createContract(
                        "Dienstleistungsvertrag",
                        "Vertrag über externe Dienstleistungen.",
                        "20.02.2024",
                        "20.02.2025",
                        "Dieser Dienstleistungsvertrag beschreibt Art und Umfang externer Leistungen. Vergütung, Haftung und Laufzeit sind geregelt.",
                        1,
                        "Alles Wesentliche geregelt, klare Leistungsbeschreibung vorhanden.",
                        "Dienstleistungsvertrag.pdf",
                        null
                ),

                createContract(
                        "Kaufvertrag",
                        "Einmaliger Kaufvertrag für Waren oder Geräte.",
                        "09.08.2022",
                        "09.08.2023",
                        "Der Kaufvertrag regelt den Verkauf von Waren. Kaufpreis, Lieferbedingungen sowie Gewährleistung und Haftung sind Bestandteil des Vertrages.",
                        3,
                        "Lieferbedingungen und Gewährleistungsregelungen unklar, Risiko für Streitfälle.",
                        "Kaufvertrag.docx",
                        null
                ),

                createContract(
                        "Versicherungsvertrag",
                        "Standard-Versicherungspolice mit jährlicher Laufzeit.",
                        "11.12.2023",
                        "11.12.2024",
                        "Dieser Versicherungsvertrag definiert den Versicherungsschutz, versicherte Risiken, Prämien und Pflichten im Schadensfall.",
                        1,
                        "Alle Versicherungsbedingungen sind verständlich und korrekt formuliert.",
                        "Versicherungsvertrag.pdf",
                        null
                ),

                createContract(
                        "Leasingvertrag",
                        "Leasingvertrag für technische Geräte oder Fahrzeuge.",
                        "01.06.2022",
                        "01.06.2025",
                        "Der Leasingvertrag regelt die Nutzung eines Leasingobjekts. Leasingdauer, Raten, Wartung und Rückgabe sind festgelegt.",
                        2,
                        "Vertrag deckt die Grundpunkte ab, aber Restwertregelung könnte klarer sein.",
                        "Leasingvertrag.pdf",
                        null
                ),

                createContract(
                        "Wartungsvertrag",
                        "Regelmäßige Wartung von IT-Systemen.",
                        "01.03.2024",
                        "01.03.2025",
                        "Der Wartungsvertrag beschreibt regelmäßige Wartungsleistungen, Reaktionszeiten und den Leistungsumfang.",
                        1,
                        "Klauseln verständlich, Leistungsumfang klar definiert.",
                        "Wartungsvertrag.docx",
                        null
                ),

                createContract(
                        "Hostingvertrag",
                        "Webhosting oder Serverhosting-Leistungen.",
                        "14.09.2023",
                        "14.09.2024",
                        "Dieser Hostingvertrag regelt die Bereitstellung von Server- und Hosting-Leistungen inklusive Verfügbarkeit und SLAs.",
                        1,
                        "Hosting-Leistungen gut beschrieben, SLAs nachvollziehbar.",
                        "Hostingvertrag.pdf",
                        null
                ),

                createContract(
                        "Supportvertrag",
                        "Technischer Support und Helpdesk.",
                        "01.01.2024",
                        "01.01.2025",
                        "Der Supportvertrag umfasst technischen Support, Supportzeiten sowie Reaktions- und Eskalationsstufen.",
                        1,
                        "Supportzeiten und Verantwortlichkeiten klar definiert.",
                        "Supportvertrag.docx",
                        null
                ),

                createContract(
                        "Kooperationsvertrag",
                        "Partnerschaft für gemeinsame Projekte.",
                        "22.11.2022",
                        "22.11.2023",
                        "Dieser Kooperationsvertrag regelt die Zusammenarbeit der Parteien, Ziele, Aufgabenverteilung und Laufzeit.",
                        2,
                        "Grundlagen sind da, aber Verantwortlichkeiten könnten klarer sein.",
                        "Kooperationsvertrag.pdf",
                        null
                ),

                createContract(
                        "Liefervertrag",
                        "Regelmäßige Lieferung von Waren.",
                        "10.01.2023",
                        "10.01.2024",
                        "Der Liefervertrag regelt Liefermengen, Lieferfristen sowie Zahlungsbedingungen für regelmäßig gelieferte Waren.",
                        1,
                        "Lieferfristen und Mengenangaben klar geregelt.",
                        "Liefervertrag.docx",
                        null
                ),

                createContract(
                        "Agenturvertrag",
                        "Marketing- oder Vermittlungsagenturvertrag.",
                        "01.02.2024",
                        "01.02.2025",
                        "Dieser Agenturvertrag definiert Marketing- und Vermittlungsleistungen sowie Vergütung und Kündigungsmodalitäten.",
                        1,
                        "Aufgaben und Vergütung transparent beschrieben.",
                        "Agenturvertrag.pdf",
                        null
                ),

                createContract(
                        "Lizenzvertrag",
                        "Lizenz zur Nutzung geistigen Eigentums.",
                        "20.01.2024",
                        "20.01.2025",
                        "Der Lizenzvertrag regelt die Nutzung geistigen Eigentums. Umfang der Nutzungsrechte, Laufzeit, Vergütung sowie Einschränkungen sind festgelegt.",
                        1,
                        "Nutzungsrechte klar definiert, keine offensichtlichen Lücken.",
                        "Lizenzvertrag.docx",
                        null
                ),

                createContract(
                        "Bauvertrag",
                        "Vertrag über Bauleistungen.",
                        "15.05.2022",
                        "15.05.2024",
                        "Dieser Bauvertrag regelt die Erbringung von Bauleistungen. Leistungsumfang, Bauzeiten, Abnahmebedingungen und Haftungsregelungen sind Bestandteil des Vertrages.",
                        3,
                        "Unklare Abnahmebedingungen und Haftungsregelungen, hohes Risiko für Streitigkeiten.",
                        "Bauvertrag.pdf",
                        null
                ),

                createContract(
                        "Projektvertrag",
                        "Befristeter Projektvertrag mit definiertem Umfang.",
                        "11.03.2023",
                        "11.03.2024",
                        "Der Projektvertrag definiert einen zeitlich befristeten Projektumfang. Meilensteine, Fristen und Verantwortlichkeiten sind geregelt.",
                        1,
                        "Projektumfang und Fristen klar definiert, keine Probleme erkennbar.",
                        "Projektvertrag.docx",
                        null
                ),

                createContract(
                        "Beratungsvertrag",
                        "Externe Unternehmensberatung.",
                        "02.12.2023",
                        "02.12.2024",
                        "Dieser Beratungsvertrag regelt externe Beratungsleistungen. Beratungsumfang, Vergütung und Vertraulichkeit sind festgelegt.",
                        1,
                        "Vertrag deckt Beratungsumfang und Vergütung korrekt ab.",
                        "Beratungsvertrag.pdf",
                        null
                ),

                createContract(
                        "Darlehensvertrag",
                        "Kreditvereinbarung mit langfristiger Laufzeit.",
                        "30.09.2021",
                        "30.09.2026",
                        "Der Darlehensvertrag regelt die Gewährung eines Kredits. Darlehenssumme, Zinssätze, Laufzeit und Rückzahlungsmodalitäten sind beschrieben.",
                        2,
                        "Zinsregelungen sind vorhanden, aber Rückzahlungsmodalitäten teilweise unklar.",
                        "Darlehensvertrag.pdf",
                        null
                ),

                createContract(
                        "Schulungsvertrag",
                        "Vertrag über Weiterbildungs- oder Schulungsleistungen.",
                        "22.02.2024",
                        "22.02.2025",
                        "Dieser Schulungsvertrag regelt Weiterbildungsmaßnahmen. Inhalte, Termine, Teilnahmebedingungen und Vergütung sind festgelegt.",
                        1,
                        "Leistungsumfang und Termine klar definiert, alles in Ordnung.",
                        "Schulungsvertrag.docx",
                        null
                ),

                createContract(
                        "Cloud-Service-Vertrag",
                        "Cloud-basierte IT-Services.",
                        "05.10.2023",
                        "05.10.2024",
                        "Der Cloud-Service-Vertrag regelt die Nutzung cloudbasierter IT-Dienste. Serviceumfang, Verfügbarkeit, SLAs und Datenschutz sind definiert.",
                        1,
                        "Cloud-Dienste, SLAs und Supportbedingungen klar beschrieben.",
                        "Cloud-Service-Vertrag.pdf",
                        null
                ),

                createContract(
                        "IT-Rahmenvertrag",
                        "Übergeordneter Vertrag über IT-Dienstleistungen.",
                        "03.01.2024",
                        "03.01.2026",
                        "Dieser IT-Rahmenvertrag definiert allgemeine Bedingungen für IT-Dienstleistungen. Einzelbeauftragungen erfolgen auf Basis dieses Vertrages.",
                        1,
                        "Rahmenbedingungen für IT-Dienstleistungen umfassend und klar.",
                        "IT-Rahmenvertrag.docx",
                        null
                ),

                createContract(
                        "Werkvertrag",
                        "Herstellung eines bestimmten Werkergebnisses.",
                        "12.12.2022",
                        "12.12.2024",
                        "Der Werkvertrag regelt die Herstellung eines konkreten Werkergebnisses. Leistungsbeschreibung, Qualitätsanforderungen und Abnahme sind festgelegt.",
                        2,
                        "Leistungsbeschreibung vorhanden, Qualitätsanforderungen könnten präziser sein.",
                        "Werkvertrag.pdf",
                        null
                ),

                createContract(
                        "ADV-Vertrag",
                        "Vertrag zur Auftragsdatenverarbeitung gemäß DSGVO.",
                        "22.11.2023",
                        "22.11.2024",
                        "Dieser ADV-Vertrag regelt die Verarbeitung personenbezogener Daten. Datenschutzpflichten, technische Maßnahmen und Verantwortlichkeiten sind definiert.",
                        1,
                        "Datenschutz- und Verarbeitungspflichten klar geregelt, alles korrekt.",
                        "ADV-Vertrag.docx",
                        null
                ),

                createContract(
                        "Telekommunikationsvertrag",
                        "Mobilfunk oder Internet-Vertrag.",
                        "09.03.2022",
                        "09.03.2023",
                        "Der Telekommunikationsvertrag regelt Mobilfunk- oder Internetleistungen. Laufzeit, Tarife, Kündigungsbedingungen und Leistungsumfang sind festgelegt.",
                        3,
                        "Vertragslaufzeiten und Kündigungsbedingungen unklar, potenzielles Risiko.",
                        "Telekommunikationsvertrag.pdf",
                        null
                ),

                createContract(
                        "Providervertrag",
                        "Vertrag mit Internet- oder Service-Provider.",
                        "12.07.2023",
                        "12.07.2024",
                        "Dieser Providervertrag regelt die Bereitstellung von IT- oder Internetdiensten. Leistungsumfang, Gebühren und Laufzeit sind vereinbart.",
                        1,
                        "Leistungsumfang und Gebühren klar beschrieben, keine Auffälligkeiten.",
                        "Providervertrag.docx",
                        null
                ),


                createContract(
                        "Abovertrag",
                        "Wiederkehrendes Abonnement für Dienstleistungen.",
                        "14.02.2024",
                        "14.02.2025",
                        "Der Abovertrag regelt wiederkehrende Dienstleistungen. Leistungsumfang, Abrechnungsintervalle, Laufzeit und Kündigungsfristen sind festgelegt.",
                        1,
                        "Abo-Leistungen und Kündigungsfristen klar geregelt.",
                        "Abovertrag.pdf",
                        null
                ),

                createContract(
                        "Überlassungsvertrag",
                        "Überlassung von Personal oder Gegenständen.",
                        "01.11.2021",
                        "01.11.2024",
                        "Dieser Überlassungsvertrag regelt die zeitlich begrenzte Überlassung von Personal oder Gegenständen. Haftung, Dauer und Rückgaberegelungen sind definiert.",
                        2,
                        "Grundsätze der Überlassung vorhanden, Haftung könnte deutlicher sein.",
                        "Überlassungsvertrag.docx",
                        null
                ),

                createContract(
                        "Forschungsvertrag",
                        "Kooperative Forschungstätigkeiten.",
                        "18.05.2023",
                        "18.05.2024",
                        "Der Forschungsvertrag regelt gemeinsame Forschungstätigkeiten. Forschungsziele, Aufgabenverteilung und Rechte an Ergebnissen sind festgelegt.",
                        1,
                        "Forschungsziele und Verantwortlichkeiten klar definiert.",
                        "Forschungsvertrag.pdf",
                        null
                ),

                createContract(
                        "Kreditvertrag",
                        "Langfristiger Kreditvertrag mit festen Konditionen.",
                        "28.01.2022",
                        "28.01.2027",
                        "Der Kreditvertrag regelt die Bereitstellung eines langfristigen Kredits. Kreditbetrag, Zinsen, Sicherheiten und Rückzahlung sind Bestandteil der Vereinbarung.",
                        3,
                        "Zinsberechnung und Sicherheiten teilweise unklar, hohes Risiko für Streitigkeiten.",
                        "Kreditvertrag.docx",
                        null
                ),

                createContract(
                        "Vertriebsvertrag",
                        "Vertrieb von Produkten oder Dienstleistungen.",
                        "30.08.2023",
                        "30.08.2024",
                        "Dieser Vertriebsvertrag regelt den Vertrieb von Produkten oder Dienstleistungen. Vertriebsgebiete, Provisionen und Pflichten sind definiert.",
                        1,
                        "Vertriebsgebiete und Provisionen klar definiert.",
                        "Vertriebsvertrag.pdf",
                        null
                ),

                createContract(
                        "Lizenzverlängerung",
                        "Verlängerung einer bestehenden Lizenz.",
                        "25.02.2024",
                        "25.02.2025",
                        "Die Lizenzverlängerung regelt die Fortführung bestehender Nutzungsrechte. Neue Laufzeit, Konditionen und Einschränkungen sind festgelegt.",
                        1,
                        "Laufzeit und Nutzungsrechte der Lizenz korrekt geregelt.",
                        "Lizenzverlängerung.docx",
                        null
                ),

                createContract(
                        "SaaS-Vertrag",
                        "Software-as-a-Service-Nutzungslizenz.",
                        "01.09.2023",
                        "01.09.2024",
                        "Der SaaS-Vertrag regelt die Nutzung einer cloudbasierten Software. Nutzungsbedingungen, Support, Verfügbarkeit und Laufzeit sind definiert.",
                        1,
                        "Nutzungsbedingungen und Support klar beschrieben.",
                        "SaaS-Vertrag.pdf",
                        null
                ),

                createContract(
                        "Partnerschaftsvertrag",
                        "Langfristige Partnerschaft zwischen Unternehmen.",
                        "12.03.2024",
                        "12.03.2026",
                        "Dieser Partnerschaftsvertrag regelt eine langfristige Zusammenarbeit. Ziele, Verantwortlichkeiten und Kooperationsmodelle sind festgelegt.",
                        1,
                        "Partnerschaftsziele und Verantwortlichkeiten klar definiert.",
                        "Partnerschaftsvertrag.docx",
                        null
                ),

                createContract(
                        "Wartungsvertrag Premium",
                        "Erweiterter Wartungs- und Servicevertrag.",
                        "10.10.2022",
                        "10.10.2024",
                        "Der Premium-Wartungsvertrag umfasst erweiterte Service- und Wartungsleistungen. Garantierte Reaktionszeiten, SLAs und Zusatzservices sind geregelt.",
                        2,
                        "Leistungsumfang gut beschrieben, SLA-Klauseln könnten detaillierter sein.",
                        "Wartungsvertrag-Premium.pdf",
                        null
                )
        );
    }

    private Contract createContract(String title, String description, String startDate, String endDate, String extractedText, int aiLevel,
                                  String aiAnalysisText, String fileName, byte[] file) {
        return Contract.builder()
                .title(title)
                .description(description)
                .startDate(LocalDate.parse(startDate, formatter))
                .endDate(LocalDate.parse(endDate, formatter))
                .extractedText(extractedText)
                .aiLevel(aiLevel)
                .aiAnalysisText(aiAnalysisText)
                .fileName(fileName)
                .file(file)
                .build();
    }

    private void createPrompts() {
        //Vorhandene Prompts löschen
        chatGPTService.deleteAllPrompts();

        //Vertragsprompt
        PromptsAi contractPrompt = PromptsAi.builder()
                .key(PromptType.CONTRACT)
                .prompt("Analysiere den folgenden Vertrag. " +
                        "Bewerte ihn nach diesem Schema:\n" +
                        "1 = ist einwandfrei\n" +
                        "2 = sollte überprüft werden\n" +
                        "3 = weist kritische Abweichungen auf\n\n" +
                        "Antworte AUSSCHLIESSLICH im JSON-Format:\n" +
                        "{\n" +
                        "  \"aiLevel\": 1,\n" +
                        "  \"aiAnalysisText\": \"Text mit maximal 500 Zeichen\"\n" +
                        "}\n\n" +
                        "Vertragstext:\n")
                .build();

        //Rechnungs-Prompt
        PromptsAi invoicePrompt = PromptsAi.builder()
                .key(PromptType.INVOICE)
                .prompt("Analysiere die folgenden Rechnungen. " +
                        "Ermittle, in welchen Ausgabenkategorien (basierend auf dem Verwendungszweck) " +
                        "die höchsten Kosten entstanden sind und welche Kategorien am häufigsten vorkommen. " +
                        "Gib eine kurze Zusammenfassung der Ausgabenstruktur.\n\n" +
                        "Antworte AUSSCHLIESSLICH im JSON-Format:\n" +
                        "{\n" +
                        "  \"aiAnalysisText\": \"Text mit maximal 500 Zeichen\"\n" +
                        "}\n\n" +
                        "Rechnungen:\n")
                .build();

        //Chat-Prompt
        PromptsAi chatPrompt = PromptsAi.builder()
                .key(PromptType.CHAT)
                .prompt("Du bist ein Assistenzsystem für Dokumentenfragen.\n" +
                        "Beantworte die Frage ausschließlich anhand des bereitgestellten Kontexts.\n" +
                        "Wenn die Antwort nicht im Kontext enthalten ist, sage:\n" +
                        "\"Die Information ist im Dokument nicht enthalten.\"\n" +
                        "Antworte sachlich, präzise und ohne Spekulationen.")
                .build();

        chatGPTService.savePrompt(contractPrompt);
        chatGPTService.savePrompt(invoicePrompt);
        chatGPTService.savePrompt(chatPrompt);
    }
}
