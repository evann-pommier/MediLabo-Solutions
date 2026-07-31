package com.openclassrooms.MediLaboSolutions.notes.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Setter
@Getter
@AllArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    @NotNull(message = "L'identifiant du patient est obligatoire")
    private Long patId;

    @NotBlank(message = "Le nom du patient est obligatoire")
    private String patient;

    @NotBlank(message = "Le contenu de la note est obligatoire")
    private String note;
}