package com.openclassrooms.MediLaboSolutions.front.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO utilisé côté front pour désérialiser les informations personnelles
 * d'un patient renvoyées par patient-service (via la gateway).
 * <p>
 * Les champs reflètent exactement ceux exposés par patient-service ;
 * ce DTO reste volontairement séparé de l'entité JPA du back pour ne pas
 * coupler le front à la couche de persistance d'un autre microservice.
 */
@Getter
@Setter
public class PatientDto {

    /** Identifiant du patient (id SQL côté patient-service). */
    private Long id;

    private String firstName;
    private String lastName;

    /** Utilisée pour le calcul de l'âge dans risk-service. */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    /** "M" ou "F" — utilisé avec l'âge pour déterminer le niveau de risque. */
    private String gender;

    /** Optionnelle côté patient-service (peut être vide ou nulle). */
    private String address;

    /** Optionnel côté patient-service (peut être vide ou nul). */
    private String phone;
}