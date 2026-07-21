package com.openclassrooms.MediLaboSolutions.patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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


    @NotBlank(message = "La date de naissance est obligatoire")
    private String dateOfBirth;


    @NotBlank(message = "Le genre est obligatoire")
    private String gender;


    private String address;


    private String phone;

}