package org.example.backend.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.ContractDto;
import org.example.backend.service.ContractService;
import org.example.backend.service.InvoiceService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateTestData {

    private final ContractService contractService;
    private final InvoiceService invoiceService;

    @PostConstruct
    public void init() {
        createContracts();
    }

    private void createContracts() {
        // Vorhandene Contracts löschen
        contractService.deleteAllContracts();
        // Vorhandene Invoices löschen
        invoiceService.deleteAllInvoices();

        //Testdaten als ContractDto erstellen
        List<ContractDto> contractDtos = createContractDtos();
        contractDtos.forEach(contractService::createContract);

        //Testdaten als InvoiceDto erstellen
        //TODO
    }

    private List<ContractDto> createContractDtos() {
        return List.of(
                createContractDto("Mietvertrag", "Mietvertrag für Büro- oder Wohnräume.", "15.01.2024", "15.01.2025", 1,
                        "Klare Struktur und umfassende Bedingungen, keine Auffälligkeiten.", "Mietvertrag.pdf", null),

                createContractDto("Arbeitsvertrag", "Unbefristeter Arbeitsvertrag mit Standardklauseln.", "03.11.2023", "03.11.2024", 2,
                        "Enthält die Grundregeln, aber einige Kündigungsfristen könnten präziser sein.", "Arbeitsvertrag.docx", null),

                createContractDto("Dienstleistungsvertrag", "Vertrag über externe Dienstleistungen.", "20.02.2024", "20.02.2025", 1,
                        "Alles Wesentliche geregelt, klare Leistungsbeschreibung vorhanden.", "Dienstleistungsvertrag.pdf", null),

                createContractDto("Kaufvertrag", "Einmaliger Kaufvertrag für Waren oder Geräte.", "09.08.2022", "09.08.2023", 3,
                        "Lieferbedingungen und Gewährleistungsregelungen unklar, Risiko für Streitfälle.", "Kaufvertrag.docx", null),

                createContractDto("Versicherungsvertrag", "Standard-Versicherungspolice mit jährlicher Laufzeit.", "11.12.2023", "11.12.2024", 1,
                        "Alle Versicherungsbedingungen sind verständlich und korrekt formuliert.", "Versicherungsvertrag.pdf", null),

                createContractDto("Leasingvertrag", "Leasingvertrag für technische Geräte oder Fahrzeuge.", "01.06.2022", "01.06.2025", 2,
                        "Vertrag deckt die Grundpunkte ab, aber Restwertregelung könnte klarer sein.", "Leasingvertrag.pdf", null),

                createContractDto("Wartungsvertrag", "Regelmäßige Wartung von IT-Systemen.", "01.03.2024", "01.03.2025", 1,
                        "Klauseln verständlich, Leistungsumfang klar definiert.", "Wartungsvertrag.docx", null),

                createContractDto("Hostingvertrag", "Webhosting oder Serverhosting-Leistungen.", "14.09.2023", "14.09.2024", 1,
                        "Hosting-Leistungen gut beschrieben, SLAs nachvollziehbar.", "Hostingvertrag.pdf", null),

                createContractDto("Supportvertrag", "Technischer Support und Helpdesk.", "01.01.2024", "01.01.2025", 1,
                        "Supportzeiten und Verantwortlichkeiten klar definiert.", "Supportvertrag.docx", null),

                createContractDto("Kooperationsvertrag", "Partnerschaft für gemeinsame Projekte.", "22.11.2022", "22.11.2023", 2,
                        "Grundlagen sind da, aber Verantwortlichkeiten könnten klarer sein.", "Kooperationsvertrag.pdf", null),

                createContractDto("Liefervertrag", "Regelmäßige Lieferung von Waren.", "10.01.2023", "10.01.2024", 1,
                        "Lieferfristen und Mengenangaben klar geregelt.", "Liefervertrag.docx", null),

                createContractDto("Agenturvertrag", "Marketing- oder Vermittlungsagenturvertrag.", "01.02.2024", "01.02.2025", 1,
                        "Aufgaben und Vergütung transparent beschrieben.", "Agenturvertrag.pdf", null),

                createContractDto("Lizenzvertrag", "Lizenz zur Nutzung geistigen Eigentums.", "20.01.2024", "20.01.2025", 1,
                        "Nutzungsrechte klar definiert, keine offensichtlichen Lücken.", "Lizenzvertrag.docx", null),

                createContractDto("Bauvertrag", "Vertrag über Bauleistungen.", "15.05.2022", "15.05.2024", 3,
                        "Unklare Abnahmebedingungen und Haftungsregelungen, hohes Risiko für Streitigkeiten.", "Bauvertrag.pdf", null),

                createContractDto("Projektvertrag", "Befristeter Projektvertrag mit definiertem Umfang.", "11.03.2023", "11.03.2024", 1,
                        "Projektumfang und Fristen klar definiert, keine Probleme erkennbar.", "Projektvertrag.docx", null),

                createContractDto("Beratungsvertrag", "Externe Unternehmensberatung.", "02.12.2023", "02.12.2024", 1,
                        "Vertrag deckt Beratungsumfang und Vergütung korrekt ab.", "Beratungsvertrag.pdf", null),

                createContractDto("Darlehensvertrag", "Kreditvereinbarung mit langfristiger Laufzeit.", "30.09.2021", "30.09.2026", 2,
                        "Zinsregelungen sind vorhanden, aber Rückzahlungsmodalitäten teilweise unklar.", "Darlehensvertrag.pdf", null),

                createContractDto("Schulungsvertrag", "Vertrag über Weiterbildungs- oder Schulungsleistungen.", "22.02.2024", "22.02.2025", 1,
                        "Leistungsumfang und Termine klar definiert, alles in Ordnung.", "Schulungsvertrag.docx", null),

                createContractDto("Cloud-Service-Vertrag", "Cloud-basierte IT-Services.", "05.10.2023", "05.10.2024", 1,
                        "Cloud-Dienste, SLAs und Supportbedingungen klar beschrieben.", "Cloud-Service-Vertrag.pdf", null),

                createContractDto("IT-Rahmenvertrag", "Übergeordneter Vertrag über IT-Dienstleistungen.", "03.01.2024", "03.01.2026", 1,
                        "Rahmenbedingungen für IT-Dienstleistungen umfassend und klar.", "IT-Rahmenvertrag.docx", null),

                createContractDto("Werkvertrag", "Herstellung eines bestimmten Werkergebnisses.", "12.12.2022", "12.12.2024", 2,
                        "Leistungsbeschreibung vorhanden, Qualitätsanforderungen könnten präziser sein.", "Werkvertrag.pdf", null),

                createContractDto("ADV-Vertrag", "Vertrag zur Auftragsdatenverarbeitung gemäß DSGVO.", "22.11.2023", "22.11.2024", 1,
                        "Datenschutz- und Verarbeitungspflichten klar geregelt, alles korrekt.", "ADV-Vertrag.docx", null),

                createContractDto("Telekommunikationsvertrag", "Mobilfunk oder Internet-Vertrag.", "09.03.2022", "09.03.2023", 3,
                        "Vertragslaufzeiten und Kündigungsbedingungen unklar, potenzielles Risiko.", "Telekommunikationsvertrag.pdf", null),

                createContractDto("Providervertrag", "Vertrag mit Internet- oder Service-Provider.", "12.07.2023", "12.07.2024", 1,
                        "Leistungsumfang und Gebühren klar beschrieben, keine Auffälligkeiten.", "Providervertrag.docx", null),

                createContractDto("Abovertrag", "Wiederkehrendes Abonnement für Dienstleistungen.", "14.02.2024", "14.02.2025", 1,
                        "Abo-Leistungen und Kündigungsfristen klar geregelt.", "Abovertrag.pdf", null),

                createContractDto("Überlassungsvertrag", "Überlassung von Personal oder Gegenständen.", "01.11.2021", "01.11.2024", 2,
                        "Grundsätze der Überlassung vorhanden, Haftung könnte deutlicher sein.", "Überlassungsvertrag.docx", null),

                createContractDto("Forschungsvertrag", "Kooperative Forschungstätigkeiten.", "18.05.2023", "18.05.2024", 1,
                        "Forschungsziele und Verantwortlichkeiten klar definiert.", "Forschungsvertrag.pdf", null),

                createContractDto("Kreditvertrag", "Langfristiger Kreditvertrag mit festen Konditionen.", "28.01.2022", "28.01.2027", 3,
                        "Zinsberechnung und Sicherheiten teilweise unklar, hohes Risiko für Streitigkeiten.", "Kreditvertrag.docx", null),

                createContractDto("Vertriebsvertrag", "Vertrieb von Produkten oder Dienstleistungen.", "30.08.2023", "30.08.2024", 1,
                        "Vertriebsgebiete und Provisionen klar definiert.", "Vertriebsvertrag.pdf", null),

                createContractDto("Lizenzverlängerung", "Verlängerung einer bestehenden Lizenz.", "25.02.2024", "25.02.2025", 1,
                        "Laufzeit und Nutzungsrechte der Lizenz korrekt geregelt.", "Lizenzverlängerung.docx", null),

                createContractDto("SaaS-Vertrag", "Software-as-a-Service-Nutzungslizenz.", "01.09.2023", "01.09.2024", 1,
                        "Nutzungsbedingungen und Support klar beschrieben.", "SaaS-Vertrag.pdf", null),

                createContractDto("Partnerschaftsvertrag", "Langfristige Partnerschaft zwischen Unternehmen.", "12.03.2024", "12.03.2026", 1,
                        "Partnerschaftsziele und Verantwortlichkeiten klar definiert.", "Partnerschaftsvertrag.docx", null),

                createContractDto("Wartungsvertrag Premium", "Erweiterter Wartungs- und Servicevertrag.", "10.10.2022", "10.10.2024", 2,
                        "Leistungsumfang gut beschrieben, SLA-Klauseln könnten detaillierter sein.", "Wartungsvertrag-Premium.pdf", null)
        );
    }

    private ContractDto createContractDto(String title, String description, String startDate, String endDate, int aiLevel,
                                  String aiAnalysisText, String fileName, String file) {
        return ContractDto.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .aiLevel(aiLevel)
                .aiAnalysisText(aiAnalysisText)
                .fileName(fileName)
                .fileBase64(file)
                .build();
    }
}
