package com.openclassrooms.MediLaboSolutions.risk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Représente le résultat de l'évaluation du risque, tel que renvoyé par
 * l'endpoint GET /assess/{patId}. C'est le format de réponse consommé par
 * le front (via la gateway).
 */
@Getter
@AllArgsConstructor
public class RiskAssessmentResult {

    private Long patId;
    private String patient;

    /** Un des 4 libellés attendus par le sujet : "None", "Borderline", "InDanger" ou "EarlyOnset". Voir {@link RiskLevel#getLabel()}. */
    private String risk;
}