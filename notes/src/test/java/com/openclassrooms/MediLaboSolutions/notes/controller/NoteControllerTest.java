package com.openclassrooms.MediLaboSolutions.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.MediLaboSolutions.notes.model.Note;
import com.openclassrooms.MediLaboSolutions.notes.service.NoteService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NoteService noteService;

    @Test
    @WithMockUser
    void shouldReturnNotesForPatient() throws Exception {
        Note note = new Note("1", 1L, "TestNone", "Le patient déclare qu'il se sent très bien");

        when(noteService.getNotesByPatient(1L)).thenReturn(List.of(note));

        mockMvc.perform(get("/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patient").value("TestNone"));
    }

    @Test
    @WithMockUser
    void shouldAddNote() throws Exception {
        Note requestNote = new Note(null, 1L, "TestNone", "Nouvelle observation du médecin");
        Note savedNote = new Note("generated-id", 1L, "TestNone", "Nouvelle observation du médecin");

        when(noteService.addNote(any(Note.class))).thenReturn(savedNote);

        mockMvc.perform(post("/notes")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNote)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("generated-id"))
                .andExpect(jsonPath("$.patient").value("TestNone"));
    }

    @Test
    @WithMockUser
    void shouldRejectNoteWithoutContent() throws Exception {
        Note invalidNote = new Note(null, 1L, "TestNone", "");

        mockMvc.perform(post("/notes")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNote)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldRejectNoteWithoutPatId() throws Exception {
        Note invalidNote = new Note(null, null, "TestNone", "Une observation valide");

        mockMvc.perform(post("/notes")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNote)))
                .andExpect(status().isBadRequest());
    }
}