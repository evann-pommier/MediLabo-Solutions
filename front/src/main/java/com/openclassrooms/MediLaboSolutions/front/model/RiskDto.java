package com.openclassrooms.MediLaboSolutions.front.model;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilisé côté front pour désérialiser le résultat de l'évaluation du
 * risque de diabète renvoyé par risk-service (via la gateway).
 * <p>
 * Structure alignée sur la réponse de l'endpoint GET /assess/{patId} :
 * {@code risk} contient l'un des 4 libellés attendus par le sujet
 * ("None", "Borderline", "InDanger", "EarlyOnset"), calculés par
 * risk-service à partir de l'âge, du sexe et des notes du patient.
 */
@Getter
@Setter
public class RiskDto {

    /** Identifiant du patient évalué (id SQL côté patient-service). */
    private Long patId;

    /** Nom complet du patient, fourni par risk-service pour lisibilité. */
    private String patient;

    /** Niveau de risque calculé : "None", "Borderline", "InDanger" ou "EarlyOnset". */
    private String risk;
}