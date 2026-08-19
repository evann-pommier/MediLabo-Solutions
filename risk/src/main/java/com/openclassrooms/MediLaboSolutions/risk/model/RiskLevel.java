package com.openclassrooms.MediLaboSolutions.risk.model;

import lombok.Getter;

/**
 * Les 4 niveaux de risque de diabète possibles, tels que définis par le
 * sujet, du moins au plus sévère : aucun risque, risque limité, danger,
 * apparition précoce.
 * <p>
 * Chaque valeur porte son libellé attendu par le format de sortie
 * ({@code label}), séparé du nom de la constante Java pour respecter les
 * conventions de nommage Java (SCREAMING_SNAKE_CASE) tout en produisant
 * exactement les chaînes attendues ("InDanger", "EarlyOnset", sans
 * underscore) dans les réponses de l'API.
 */
@Getter
public enum RiskLevel {
    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("InDanger"),
    EARLY_ONSET("EarlyOnset");

    private final String label;

    RiskLevel(String label) {
        this.label = label;
    }
}