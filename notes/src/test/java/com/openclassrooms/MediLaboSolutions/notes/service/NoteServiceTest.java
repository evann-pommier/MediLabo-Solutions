package com.openclassrooms.MediLaboSolutions.notes.service;

import com.openclassrooms.MediLaboSolutions.notes.model.Note;
import com.openclassrooms.MediLaboSolutions.notes.repository.NoteRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void shouldReturnNotesForGivenPatient() {
        Note note = new Note("1", 1L, "TestNone", "Observation");
        when(noteRepository.findByPatId(1L)).thenReturn(List.of(note));

        List<Note> result = noteService.getNotesByPatient(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPatient()).isEqualTo("TestNone");
    }

    @Test
    void shouldClearIdBeforeSavingNewNote() {
        Note incomingNote = new Note("should-be-ignored", 1L, "TestNone", "Nouvelle note");
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        noteService.addNote(incomingNote);

        assertThat(incomingNote.getId()).isNull();
        verify(noteRepository).save(incomingNote);
    }
}