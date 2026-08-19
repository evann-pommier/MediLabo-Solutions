package com.openclassrooms.MediLaboSolutions.front.model;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilisé côté front pour désérialiser les notes du médecin renvoyées
 * par notes-service (via la gateway), et pour sérialiser une nouvelle note
 * lors de son ajout. La structure reflète exactement le document MongoDB
 * exposé par notes-service : {@code patId} permet de relier la note à un
 * patient sans dupliquer ses données personnelles.
 */
@Getter
@Setter
public class NoteDto {

    /** Identifiant MongoDB de la note (généré côté notes-service, absent lors d'un ajout). */
    private String id;

    /** Identifiant du patient concerné (id SQL côté patient-service). */
    private Long patId;

    /** Nom du patient, dupliqué ici pour lisibilité côté vue sans appel supplémentaire. */
    private String patient;

    /** Contenu de la note, saisi librement par le médecin. */
    private String note;
}