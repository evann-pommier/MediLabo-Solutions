package com.openclassrooms.MediLaboSolutions.front.controller;

import com.openclassrooms.MediLaboSolutions.front.model.NoteDto;
import com.openclassrooms.MediLaboSolutions.front.model.PatientDto;
import com.openclassrooms.MediLaboSolutions.front.model.RiskDto;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PatientViewController.class)
class PatientViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    void shouldDisplayPatientList() throws Exception {
        PatientDto patient = new PatientDto();
        patient.setId(1L);
        patient.setLastName("TestNone");

        when(restTemplate.getForObject(eq("http://localhost:8080/patients"), eq(PatientDto[].class)))
                .thenReturn(new PatientDto[]{patient});

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients"))
                .andExpect(model().attributeExists("patients"));
    }

    @Test
    void shouldDisplayPatientDetailWithNotesAndRisk() throws Exception {
        PatientDto patient = new PatientDto();
        patient.setId(1L);
        patient.setLastName("TestNone");

        NoteDto note = new NoteDto();
        note.setNote("Observation du médecin");

        RiskDto risk = new RiskDto();
        risk.setPatId(1L);
        risk.setPatient("Test TestNone");
        risk.setRisk("None");

        when(restTemplate.getForObject(eq("http://localhost:8080/patients/1"), eq(PatientDto.class)))
                .thenReturn(patient);
        when(restTemplate.getForObject(eq("http://localhost:8080/notes/patient/1"), eq(NoteDto[].class)))
                .thenReturn(new NoteDto[]{note});
        when(restTemplate.getForObject(eq("http://localhost:8080/assess/1"), eq(RiskDto.class)))
                .thenReturn(risk);

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-detail"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("notes"))
                .andExpect(model().attributeExists("risk"));
    }

    @Test
    void shouldRedirectAfterAddingNote() throws Exception {
        mockMvc.perform(post("/patients/1/notes")
                        .param("patient", "TestNone")
                        .param("note", "Nouvelle observation"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients/1"));
    }
}