package com.openclassrooms.MediLaboSolutions.risk.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO utilisé côté risk-service pour désérialiser les informations
 * patient renvoyées par patient-service. Structure alignée sur
 * l'entité JPA {@code Patient} exposée en JSON — pas de logique ici,
 * uniquement le mapping de la réponse HTTP.
 */
@Getter
@Setter
public class PatientDto {

    private Long id;
    private String firstName;
    private String lastName;

    /** Utilisée par {@code RiskAssessmentService} pour calculer l'âge du patient. */
    private LocalDate dateOfBirth;

    /** "M" ou "F" — utilisé avec l'âge pour déterminer le niveau de risque. */
    private String gender;

    private String address;
    private String phone;
}