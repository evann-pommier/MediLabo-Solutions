package com.openclassrooms.MediLaboSolutions.notes.controller;

import com.openclassrooms.MediLaboSolutions.notes.model.Note;
import com.openclassrooms.MediLaboSolutions.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/patient/{patId}")
    public List<Note> getNotesByPatient(@PathVariable Long patId) {
        return noteService.getNotesByPatient(patId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note addNote(@Valid @RequestBody Note note) {
        return noteService.addNote(note);
    }
}