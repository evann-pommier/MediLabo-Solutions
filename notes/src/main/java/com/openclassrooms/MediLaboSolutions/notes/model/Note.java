package com.openclassrooms.MediLaboSolutions.notes.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Document MongoDB représentant une note du médecin.
 * <p>
 * Contrairement à {@code Patient} (entité SQL), {@code id} est un
 * {@link String} : c'est le format natif de l'ObjectId généré par MongoDB,
 * pas un entier auto-incrémenté — différence structurelle propre au NoSQL.
 * <p>
 * {@code patId} fait le lien avec le patient concerné (id SQL côté
 * patient-service). Il ne s'agit pas d'une clé étrangère au sens SQL : il
 * n'existe aucune contrainte d'intégrité référentielle entre les deux
 * bases, chaque microservice restant maître de ses propres données —
 * principe fondamental d'une architecture microservices.
 * <p>
 * Le champ {@code patient} (nom du patient) est volontairement dupliqué
 * pour lisibilité : une redondance normalement proscrite en base SQL
 * (3NF), mais couramment acceptée en NoSQL au profit des performances de
 * lecture.
 */
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

    /** Contenu libre saisi par le médecin, sans limite de taille ni contrainte de format. */
    @NotBlank(message = "Le contenu de la note est obligatoire")
    private String note;
}