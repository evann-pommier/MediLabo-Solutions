package com.openclassrooms.MediLaboSolutions.patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

/**
 * Entité JPA représentant le dossier patient, persistée dans une table
 * unique et normalisée (3NF) : chaque champ est atomique, sans redondance
 * ni groupe répétitif — inutile de la décomposer davantage, une seule
 * table plate suffit à respecter la 3NF pour ce jeu de données.
 */
@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    /** Utilisée pour le calcul de l'âge dans risk-service. */
    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateOfBirth;

    /** "M" ou "F" — utilisé avec l'âge pour déterminer le niveau de risque. */
    @NotBlank(message = "Le genre est obligatoire")
    private String gender;

    /** Optionnelle pour un dossier valide, conformément à la règle métier du client. */
    private String address;

    /** Optionnel pour un dossier valide, conformément à la règle métier du client. */
    private String phone;

}