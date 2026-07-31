package com.openclassrooms.MediLaboSolutions.notes.service;

import com.openclassrooms.MediLaboSolutions.notes.model.Note;
import com.openclassrooms.MediLaboSolutions.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getNotesByPatient(Long patId) {
        return noteRepository.findByPatId(patId);
    }

    public Note addNote(Note note) {
        note.setId(null);
        return noteRepository.save(note);
    }
}