package com.openclassrooms.MediLaboSolutions.notes.repository;

import com.openclassrooms.MediLaboSolutions.notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Accès aux notes stockées dans MongoDB.
 * <p>
 * {@code findByPatId} est une méthode dérivée : Spring Data MongoDB génère
 * automatiquement la requête à partir du nom de la méthode (pas de code
 * de requête à écrire) — c'est ce qui permet de retrouver toutes les notes
 * d'un patient donné, condition explicitement demandée dans le sujet.
 */
public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatId(Long patId);
}