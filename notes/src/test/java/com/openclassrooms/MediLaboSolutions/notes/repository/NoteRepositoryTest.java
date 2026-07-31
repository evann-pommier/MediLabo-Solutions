package com.openclassrooms.MediLaboSolutions.notes.repository;

import com.openclassrooms.MediLaboSolutions.notes.model.Note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    void cleanUp() {
        noteRepository.deleteAll();
    }

    @Test
    void shouldFindNotesByPatId() {
        noteRepository.save(new Note(null, 1L, "TestNone", "Note de test"));
        noteRepository.save(new Note(null, 2L, "TestBorderline", "Autre note"));

        List<Note> result = noteRepository.findByPatId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getPatient()).isEqualTo("TestNone");
    }
}