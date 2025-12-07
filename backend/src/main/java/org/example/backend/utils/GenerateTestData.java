package org.example.backend.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.backend.model.entities.Contract;
import org.example.backend.service.ContractService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateTestData {

    private final ContractService contractService;

    @PostConstruct
    public void init() {
        createContracts();
    }

    private List<Contract> createContracts() {
        contractService.deleteAllContracts();
        List<Contract> contracts = List.of(
                Contract.builder().title("Mietvertrag")
                        .description("Mietvertrag für Büro- oder Wohnräume.")
                        .startDate(LocalDate.of(2024, 1, 15))
                        .endDate(LocalDate.of(2025, 1, 15))
                        .aiLevel(1)
                        .aiAnalysisText("Klare Struktur und umfassende Bedingungen, keine Auffälligkeiten.")
                        .build(),

                Contract.builder().title("Arbeitsvertrag")
                        .description("Unbefristeter Arbeitsvertrag mit Standardklauseln.")
                        .startDate(LocalDate.of(2023, 11, 3))
                        .endDate(LocalDate.of(2024, 11, 3))
                        .aiLevel(2)
                        .aiAnalysisText("Enthält die Grundregeln, aber einige Kündigungsfristen könnten präziser sein.")
                        .build(),

                Contract.builder().title("Dienstleistungsvertrag")
                        .description("Vertrag über externe Dienstleistungen.")
                        .startDate(LocalDate.of(2024, 2, 20))
                        .endDate(LocalDate.of(2025, 2, 20))
                        .aiLevel(1)
                        .aiAnalysisText("Alles Wesentliche geregelt, klare Leistungsbeschreibung vorhanden.")
                        .build(),

                Contract.builder().title("Kaufvertrag")
                        .description("Einmaliger Kaufvertrag für Waren oder Geräte.")
                        .startDate(LocalDate.of(2022, 8, 9))
                        .endDate(LocalDate.of(2023, 8, 9))
                        .aiLevel(3)
                        .aiAnalysisText("Lieferbedingungen und Gewährleistungsregelungen unklar, Risiko für Streitfälle.")
                        .build(),

                Contract.builder().title("Versicherungsvertrag")
                        .description("Standard-Versicherungspolice mit jährlicher Laufzeit.")
                        .startDate(LocalDate.of(2023, 12, 11))
                        .endDate(LocalDate.of(2024, 12, 11))
                        .aiLevel(1)
                        .aiAnalysisText("Alle Versicherungsbedingungen sind verständlich und korrekt formuliert.")
                        .build(),

                Contract.builder().title("Leasingvertrag")
                        .description("Leasingvertrag für technische Geräte oder Fahrzeuge.")
                        .startDate(LocalDate.of(2022, 6, 1))
                        .endDate(LocalDate.of(2025, 6, 1))
                        .aiLevel(2)
                        .aiAnalysisText("Vertrag deckt die Grundpunkte ab, aber Restwertregelung könnte klarer sein.")
                        .build(),

                Contract.builder().title("Wartungsvertrag")
                        .description("Regelmäßige Wartung von IT-Systemen.")
                        .startDate(LocalDate.of(2024, 3, 1))
                        .endDate(LocalDate.of(2025, 3, 1))
                        .aiLevel(1)
                        .aiAnalysisText("Klauseln verständlich, Leistungsumfang klar definiert.")
                        .build(),

                Contract.builder().title("Hostingvertrag")
                        .description("Webhosting oder Serverhosting-Leistungen.")
                        .startDate(LocalDate.of(2023, 9, 14))
                        .endDate(LocalDate.of(2024, 9, 14))
                        .aiLevel(1)
                        .aiAnalysisText("Hosting-Leistungen gut beschrieben, SLAs nachvollziehbar.")
                        .build(),

                Contract.builder().title("Supportvertrag")
                        .description("Technischer Support und Helpdesk.")
                        .startDate(LocalDate.of(2024, 1, 1))
                        .endDate(LocalDate.of(2025, 1, 1))
                        .aiLevel(1)
                        .aiAnalysisText("Supportzeiten und Verantwortlichkeiten klar definiert.")
                        .build(),

                Contract.builder().title("Kooperationsvertrag")
                        .description("Partnerschaft für gemeinsame Projekte.")
                        .startDate(LocalDate.of(2022, 11, 22))
                        .endDate(LocalDate.of(2023, 11, 22))
                        .aiLevel(2)
                        .aiAnalysisText("Grundlagen sind da, aber Verantwortlichkeiten könnten klarer sein.")
                        .build(),

                Contract.builder().title("Liefervertrag")
                        .description("Regelmäßige Lieferung von Waren.")
                        .startDate(LocalDate.of(2023, 1, 10))
                        .endDate(LocalDate.of(2024, 1, 10))
                        .aiLevel(1)
                        .aiAnalysisText("Lieferfristen und Mengenangaben klar geregelt.")
                        .build(),

                Contract.builder().title("Agenturvertrag")
                        .description("Marketing- oder Vermittlungsagenturvertrag.")
                        .startDate(LocalDate.of(2024, 2, 1))
                        .endDate(LocalDate.of(2025, 2, 1))
                        .aiLevel(1)
                        .aiAnalysisText("Aufgaben und Vergütung transparent beschrieben.")
                        .build(),

                Contract.builder().title("Lizenzvertrag")
                        .description("Lizenz zur Nutzung geistigen Eigentums.")
                        .startDate(LocalDate.of(2024, 1, 20))
                        .endDate(LocalDate.of(2025, 1, 20))
                        .aiLevel(1)
                        .aiAnalysisText("Nutzungsrechte klar definiert, keine offensichtlichen Lücken.")
                        .build(),

                Contract.builder().title("Bauvertrag")
                        .description("Vertrag über Bauleistungen.")
                        .startDate(LocalDate.of(2022, 5, 15))
                        .endDate(LocalDate.of(2024, 5, 15))
                        .aiLevel(3)
                        .aiAnalysisText("Unklare Abnahmebedingungen und Haftungsregelungen, hohes Risiko für Streitigkeiten.")
                        .build(),

                Contract.builder().title("Projektvertrag")
                        .description("Befristeter Projektvertrag mit definiertem Umfang.")
                        .startDate(LocalDate.of(2023, 3, 11))
                        .endDate(LocalDate.of(2024, 3, 11))
                        .aiLevel(1)
                        .aiAnalysisText("Projektumfang und Fristen klar definiert, keine Probleme erkennbar.")
                        .build(),

                Contract.builder().title("Beratungsvertrag")
                        .description("Externe Unternehmensberatung.")
                        .startDate(LocalDate.of(2023, 12, 2))
                        .endDate(LocalDate.of(2024, 12, 2))
                        .aiLevel(1)
                        .aiAnalysisText("Vertrag deckt Beratungsumfang und Vergütung korrekt ab.")
                        .build(),

                Contract.builder().title("Darlehensvertrag")
                        .description("Kreditvereinbarung mit langfristiger Laufzeit.")
                        .startDate(LocalDate.of(2021, 9, 30))
                        .endDate(LocalDate.of(2026, 9, 30))
                        .aiLevel(2)
                        .aiAnalysisText("Zinsregelungen sind vorhanden, aber Rückzahlungsmodalitäten teilweise unklar.")
                        .build(),

                Contract.builder().title("Schulungsvertrag")
                        .description("Vertrag über Weiterbildungs- oder Schulungsleistungen.")
                        .startDate(LocalDate.of(2024, 2, 22))
                        .endDate(LocalDate.of(2025, 2, 22))
                        .aiLevel(1)
                        .aiAnalysisText("Leistungsumfang und Termine klar definiert, alles in Ordnung.")
                        .build(),

                Contract.builder().title("Cloud-Service-Vertrag")
                        .description("Cloud-basierte IT-Services.")
                        .startDate(LocalDate.of(2023, 10, 5))
                        .endDate(LocalDate.of(2024, 10, 5))
                        .aiLevel(1)
                        .aiAnalysisText("Cloud-Dienste, SLAs und Supportbedingungen klar beschrieben.")
                        .build(),

                Contract.builder().title("IT-Rahmenvertrag")
                        .description("Übergeordneter Vertrag über IT-Dienstleistungen.")
                        .startDate(LocalDate.of(2024, 1, 3))
                        .endDate(LocalDate.of(2026, 1, 3))
                        .aiLevel(1)
                        .aiAnalysisText("Rahmenbedingungen für IT-Dienstleistungen umfassend und klar.")
                        .build(),

                Contract.builder().title("Werkvertrag")
                        .description("Herstellung eines bestimmten Werkergebnisses.")
                        .startDate(LocalDate.of(2022, 12, 12))
                        .endDate(LocalDate.of(2024, 12, 12))
                        .aiLevel(2)
                        .aiAnalysisText("Leistungsbeschreibung vorhanden, Qualitätsanforderungen könnten präziser sein.")
                        .build(),

                Contract.builder().title("ADV-Vertrag")
                        .description("Vertrag zur Auftragsdatenverarbeitung gemäß DSGVO.")
                        .startDate(LocalDate.of(2023, 11, 22))
                        .endDate(LocalDate.of(2024, 11, 22))
                        .aiLevel(1)
                        .aiAnalysisText("Datenschutz- und Verarbeitungspflichten klar geregelt, alles korrekt.")
                        .build(),

                Contract.builder().title("Telekommunikationsvertrag")
                        .description("Mobilfunk oder Internet-Vertrag.")
                        .startDate(LocalDate.of(2022, 3, 9))
                        .endDate(LocalDate.of(2023, 3, 9))
                        .aiLevel(3)
                        .aiAnalysisText("Vertragslaufzeiten und Kündigungsbedingungen unklar, potenzielles Risiko.")
                        .build(),

                Contract.builder().title("Providervertrag")
                        .description("Vertrag mit Internet- oder Service-Provider.")
                        .startDate(LocalDate.of(2023, 7, 12))
                        .endDate(LocalDate.of(2024, 7, 12))
                        .aiLevel(1)
                        .aiAnalysisText("Leistungsumfang und Gebühren klar beschrieben, keine Auffälligkeiten.")
                        .build(),

                Contract.builder().title("Abovertrag")
                        .description("Wiederkehrendes Abonnement für Dienstleistungen.")
                        .startDate(LocalDate.of(2024, 2, 14))
                        .endDate(LocalDate.of(2025, 2, 14))
                        .aiLevel(1)
                        .aiAnalysisText("Abo-Leistungen und Kündigungsfristen klar geregelt.")
                        .build(),

                Contract.builder().title("Überlassungsvertrag")
                        .description("Überlassung von Personal oder Gegenständen.")
                        .startDate(LocalDate.of(2021, 11, 1))
                        .endDate(LocalDate.of(2024, 11, 1))
                        .aiLevel(2)
                        .aiAnalysisText("Grundsätze der Überlassung vorhanden, Haftung könnte deutlicher sein.")
                        .build(),

                Contract.builder().title("Forschungsvertrag")
                        .description("Kooperative Forschungstätigkeiten.")
                        .startDate(LocalDate.of(2023, 5, 18))
                        .endDate(LocalDate.of(2024, 5, 18))
                        .aiLevel(1)
                        .aiAnalysisText("Forschungsziele und Verantwortlichkeiten klar definiert.")
                        .build(),

                Contract.builder().title("Kreditvertrag")
                        .description("Langfristiger Kreditvertrag mit festen Konditionen.")
                        .startDate(LocalDate.of(2022, 1, 28))
                        .endDate(LocalDate.of(2027, 1, 28))
                        .aiLevel(3)
                        .aiAnalysisText("Zinsberechnung und Sicherheiten teilweise unklar, hohes Risiko für Streitigkeiten.")
                        .build(),

                Contract.builder().title("Vertriebsvertrag")
                        .description("Vertrieb von Produkten oder Dienstleistungen.")
                        .startDate(LocalDate.of(2023, 8, 30))
                        .endDate(LocalDate.of(2024, 8, 30))
                        .aiLevel(1)
                        .aiAnalysisText("Vertriebsgebiete und Provisionen klar definiert.")
                        .build(),

                Contract.builder().title("Lizenzverlängerung")
                        .description("Verlängerung einer bestehenden Lizenz.")
                        .startDate(LocalDate.of(2024, 2, 25))
                        .endDate(LocalDate.of(2025, 2, 25))
                        .aiLevel(1)
                        .aiAnalysisText("Laufzeit und Nutzungsrechte der Lizenz korrekt geregelt.")
                        .build(),

                Contract.builder().title("SaaS-Vertrag")
                        .description("Software-as-a-Service-Nutzungslizenz.")
                        .startDate(LocalDate.of(2023, 9, 1))
                        .endDate(LocalDate.of(2024, 9, 1))
                        .aiLevel(1)
                        .aiAnalysisText("Nutzungsbedingungen und Support klar beschrieben.")
                        .build(),

                Contract.builder().title("Partnerschaftsvertrag")
                        .description("Langfristige Partnerschaft zwischen Unternehmen.")
                        .startDate(LocalDate.of(2024, 3, 12))
                        .endDate(LocalDate.of(2026, 3, 12))
                        .aiLevel(1)
                        .aiAnalysisText("Partnerschaftsziele und Verantwortlichkeiten klar definiert.")
                        .build(),

                Contract.builder().title("Wartungsvertrag Premium")
                        .description("Erweiterter Wartungs- und Servicevertrag.")
                        .startDate(LocalDate.of(2022, 10, 10))
                        .endDate(LocalDate.of(2024, 10, 10))
                        .aiLevel(2)
                        .aiAnalysisText("Leistungsumfang gut beschrieben, SLA-Klauseln könnten detaillierter sein.")
                        .build()
        );

        return contracts.stream()
                .map(contractService::createContract)
                .toList();
    }
}
