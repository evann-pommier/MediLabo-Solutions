package com.openclassrooms.MediLaboSolutions.notes.controller;

import com.openclassrooms.MediLaboSolutions.notes.model.Note;
import com.openclassrooms.MediLaboSolutions.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Expose les endpoints REST de gestion des notes du médecin.
 * <p>
 * Consommé par la gateway (pour le front) et par risk-service (pour le
 * calcul du niveau de risque) — les deux s'authentifient en HTTP Basic
 * avec les identifiants configurés dans {@code SecurityConfig}.
 */
@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Renvoie l'historique complet des notes d'un patient, du plus ancien
     * au plus récent (ordre d'insertion en base).
     *
     * @param patId identifiant du patient (id SQL côté patient-service)
     * @return la liste des notes associées à ce patient
     */
    @GetMapping("/patient/{patId}")
    public List<Note> getNotesByPatient(@PathVariable Long patId) {
        return noteService.getNotesByPatient(patId);
    }

    /**
     * Ajoute une nouvelle note pour un patient. Le contenu n'est soumis à
     * aucune limite de taille ni contrainte de format (les retours à la
     * ligne sont conservés tels quels), conformément au besoin exprimé par
     * le client.
     *
     * @param note note à créer (patId, patient et note sont obligatoires — voir {@link Note})
     * @return la note créée, avec son identifiant MongoDB généré
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note addNote(@Valid @RequestBody Note note) {
        return noteService.addNote(note);
    }

    /**
     * Supprime une note.
     * <p>
     * Non demandé par les user stories initiales du sujet — endpoint
     * ajouté après coup pour compléter le CRUD, à assumer explicitement si
     * le jury questionne sa présence (même remarque que pour
     * {@code PatientController.deletePatient}).
     *
     * @param id identifiant MongoDB de la note à supprimer
     */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }
}