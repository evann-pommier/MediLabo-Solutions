package com.openclassrooms.MediLaboSolutions.risk.model;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilisé côté risk-service pour désérialiser les notes renvoyées par
 * notes-service. Structure identique au document MongoDB {@code Note}
 * (id String, patId, patient, note) — pas de logique ici, uniquement le
 * mapping JSON de la réponse HTTP.
 */
@Getter
@Setter
public class NoteDto {

    private String id;
    private Long patId;
    private String patient;

    /** Contenu textuel de la note, analysé par {@code RiskAssessmentService} à la recherche des termes déclencheurs. */
    private String note;
}