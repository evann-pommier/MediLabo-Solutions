package com.openclassrooms.MediLaboSolutions.notes.service;

import com.openclassrooms.MediLaboSolutions.notes.model.Note;
import com.openclassrooms.MediLaboSolutions.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Couche métier de gestion des notes du médecin.
 * <p>
 * Reste volontairement fine ici : la seule logique non triviale est la
 * protection contre l'injection d'un id à la création (voir
 * {@link #addNote}). Le reste délègue directement au repository.
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Récupère l'historique des notes d'un patient donné.
     *
     * @param patId identifiant du patient (id SQL côté patient-service)
     * @return la liste de ses notes
     */
    public List<Note> getNotesByPatient(Long patId) {
        return noteRepository.findByPatId(patId);
    }

    /**
     * Enregistre une nouvelle note.
     * <p>
     * {@code setId(null)} garantit qu'un client ne peut pas imposer un id
     * MongoDB arbitraire via le JSON envoyé (par exemple pour écraser une
     * note existante) : l'id est toujours généré par MongoDB à la création.
     *
     * @param note note à enregistrer (sans id, ou avec un id ignoré)
     * @return la note enregistrée, avec son id généré
     */
    public Note addNote(Note note) {
        note.setId(null);
        return noteRepository.save(note);
    }
    /**
     * Supprime une note.
     *
     * @param id identifiant MongoDB de la note à supprimer
     */
    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }
}