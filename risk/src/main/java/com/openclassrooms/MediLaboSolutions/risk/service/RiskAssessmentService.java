package com.openclassrooms.MediLaboSolutions.risk.service;

import com.openclassrooms.MediLaboSolutions.risk.model.NoteDto;
import com.openclassrooms.MediLaboSolutions.risk.model.PatientDto;
import com.openclassrooms.MediLaboSolutions.risk.model.RiskLevel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;

/**
 * Implémente l'algorithme de détermination du niveau de risque de diabète,
 * basé sur l'âge, le sexe du patient et le nombre de termes déclencheurs
 * distincts trouvés dans ses notes médicales (règles fournies par le
 * client).
 */
@Service
public class RiskAssessmentService {

    /**
     * Les 11 catégories de termes déclencheurs à rechercher dans les notes.
     * <p>
     * Chaque catégorie est un tableau de synonymes : si l'un d'eux est
     * présent, la catégorie compte pour 1 déclencheur (pas plus, même si
     * plusieurs synonymes ou occurrences apparaissent). "Fumeur" et
     * "Fumeuse" sont regroupés car ce sont deux formes du même concept
     * médical, pas deux déclencheurs distincts.
     * <p>
     * Les termes utilisent des racines courtes ("anormal", "vertige")
     * plutôt que les formes exactes du sujet ("anormale", "Vertiges") : la
     * recherche par sous-chaîne ({@code contains}) permet ainsi de capter
     * aussi bien le singulier que le pluriel, ou les accords au féminin,
     * sans avoir à lister toutes les variantes possibles.
     */
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

    /**
     * Calcule l'âge actuel du patient à partir de sa date de naissance.
     */
    public int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Compte le nombre de catégories de déclencheurs distinctes présentes
     * dans l'ensemble des notes d'un patient (pas le nombre d'occurrences
     * totales).
     * <p>
     * Toutes les notes sont concaténées puis mises en minuscules
     * (locale française, pour un traitement correct des accents) avant
     * la recherche — la casse des notes ne doit pas influencer le
     * résultat, point de vigilance explicite du sujet.
     */
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

    /**
     * Détermine le niveau de risque d'un patient selon les règles du
     * sujet, croisant âge, sexe et nombre de déclencheurs.
     * <p>
     * Les conditions sont évaluées de la plus sévère à la moins sévère
     * (Early Onset avant In Danger) : un patient qui dépasse le seuil
     * "In Danger" dépasse forcément aussi son seuil "Early Onset" si les
     * deux sont proches, donc l'ordre de test évite un mauvais classement.
     * <p>
     * Le sujet ne définit "Borderline" que pour les patients de plus de
     * 30 ans : un patient de moins de 30 ans avec seulement 2 à 5
     * déclencheurs ne remplit aucune condition explicite et retombe donc
     * naturellement sur {@link RiskLevel#NONE} par défaut, sans traitement
     * particulier nécessaire.
     *
     * @param patient patient à évaluer (âge et sexe)
     * @param notes   ensemble des notes du médecin pour ce patient
     * @return le niveau de risque déterminé
     */
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