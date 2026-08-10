package com.openclassrooms.MediLaboSolutions.risk.service;

import com.openclassrooms.MediLaboSolutions.risk.model.NoteDto;
import com.openclassrooms.MediLaboSolutions.risk.model.PatientDto;
import com.openclassrooms.MediLaboSolutions.risk.model.RiskLevel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;

@Service
public class RiskAssessmentService {

    private static final List<String[]> TRIGGER_GROUPS = List.of(
            new String[]{"hémoglobine a1c"},
            new String[]{"microalbumine"},
            new String[]{"taille"},
            new String[]{"poids"},
            new String[]{"fumeur", "fumeuse"},
            new String[]{"anormal"},
            new String[]{"cholestérol"},
            new String[]{"vertige"},
            new String[]{"rechute"},
            new String[]{"réaction"},
            new String[]{"anticorps"}
    );

    public int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public int countTriggers(List<NoteDto> notes) {
        String combinedText = notes.stream()
                .map(NoteDto::getNote)
                .reduce("", (a, b) -> a + " " + b)
                .toLowerCase(Locale.FRENCH);

        int count = 0;
        for (String[] synonyms : TRIGGER_GROUPS) {
            boolean found = false;
            for (String synonym : synonyms) {
                if (combinedText.contains(synonym)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                count++;
            }
        }
        return count;
    }

    public RiskLevel assessRisk(PatientDto patient, List<NoteDto> notes) {
        int age = calculateAge(patient.getDateOfBirth());
        int triggerCount = countTriggers(notes);
        boolean isMale = "M".equalsIgnoreCase(patient.getGender());

        if (age < 30) {
            if (isMale) {
                if (triggerCount >= 5) return RiskLevel.EARLY_ONSET;
                if (triggerCount >= 3) return RiskLevel.IN_DANGER;
            } else {
                if (triggerCount >= 7) return RiskLevel.EARLY_ONSET;
                if (triggerCount >= 4) return RiskLevel.IN_DANGER;
            }
        } else {
            if (triggerCount >= 8) return RiskLevel.EARLY_ONSET;
            if (triggerCount >= 6) return RiskLevel.IN_DANGER;
            if (triggerCount >= 2) return RiskLevel.BORDERLINE;
        }

        return RiskLevel.NONE;
    }
}