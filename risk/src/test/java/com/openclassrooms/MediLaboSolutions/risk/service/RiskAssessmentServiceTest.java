package com.openclassrooms.MediLaboSolutions.risk.service;

import com.openclassrooms.MediLaboSolutions.risk.model.NoteDto;
import com.openclassrooms.MediLaboSolutions.risk.model.PatientDto;
import com.openclassrooms.MediLaboSolutions.risk.model.RiskLevel;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RiskAssessmentServiceTest {

    private final RiskAssessmentService service = new RiskAssessmentService();

    private PatientDto patient(LocalDate dateOfBirth, String gender) {
        PatientDto patient = new PatientDto();
        patient.setDateOfBirth(dateOfBirth);
        patient.setGender(gender);
        return patient;
    }

    private NoteDto note(String text) {
        NoteDto note = new NoteDto();
        note.setNote(text);
        return note;
    }

    @Test
    void testNone_shouldReturnNone() {
        PatientDto testNone = patient(LocalDate.of(1966, 12, 31), "F");
        List<NoteDto> notes = List.of(
                note("Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé")
        );

        RiskLevel result = service.assessRisk(testNone, notes);

        assertThat(result).isEqualTo(RiskLevel.NONE);
    }

    @Test
    void testBorderline_shouldReturnBorderline() {
        PatientDto testBorderline = patient(LocalDate.of(1945, 6, 24), "M");
        List<NoteDto> notes = List.of(
                note("Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement"),
                note("Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale")
        );

        RiskLevel result = service.assessRisk(testBorderline, notes);

        assertThat(result).isEqualTo(RiskLevel.BORDERLINE);
    }

    @Test
    void testInDanger_shouldReturnInDanger() {
        PatientDto testInDanger = patient(LocalDate.of(2004, 6, 18), "M");
        List<NoteDto> notes = List.of(
                note("Le patient déclare qu'il fume depuis peu"),
                note("Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d'apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé")
        );

        RiskLevel result = service.assessRisk(testInDanger, notes);

        assertThat(result).isEqualTo(RiskLevel.IN_DANGER);
    }

    @Test
    void testEarlyOnset_shouldReturnEarlyOnset() {
        PatientDto testEarlyOnset = patient(LocalDate.of(2002, 6, 28), "F");
        List<NoteDto> notes = List.of(
                note("Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d'être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments"),
                note("Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps"),
                note("Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé"),
                note("Taille, Poids, Cholestérol, Vertige et Réaction")
        );

        RiskLevel result = service.assessRisk(testEarlyOnset, notes);

        assertThat(result).isEqualTo(RiskLevel.EARLY_ONSET);
    }

    @Test
    void shouldCountEachTriggerOnlyOnce() {
        List<NoteDto> notes = List.of(
                note("Poids anormal, poids ANORMAL, POIDS anormal")
        );

        int count = service.countTriggers(notes);

        assertThat(count).isEqualTo(2);
    }

    @Test
    void shouldCountFumeurAndFumeuseAsSingleTrigger() {
        List<NoteDto> notes = List.of(
                note("Le patient est fumeur"),
                note("La patiente est fumeuse")
        );

        int count = service.countTriggers(notes);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void shouldCalculateAgeCorrectly() {
        LocalDate dateOfBirth = LocalDate.of(2000, 1, 1);

        int expectedAge = Period.between(dateOfBirth, LocalDate.now()).getYears();

        int result = service.calculateAge(dateOfBirth);

        assertThat(result).isEqualTo(expectedAge);
    }
}