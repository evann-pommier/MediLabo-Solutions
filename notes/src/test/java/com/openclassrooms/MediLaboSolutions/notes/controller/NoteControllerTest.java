package com.openclassrooms.MediLaboSolutions.notes.controller;

import com.openclassrooms.MediLaboSolutions.notes.model.Note;
import com.openclassrooms.MediLaboSolutions.notes.service.NoteService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}