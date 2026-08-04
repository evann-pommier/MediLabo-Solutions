package com.openclassrooms.MediLaboSolutions.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.MediLaboSolutions.patient.exception.PatientNotFoundException;
import com.openclassrooms.MediLaboSolutions.patient.model.Patient;
import com.openclassrooms.MediLaboSolutions.patient.service.PatientService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @Test
    @WithMockUser
    void shouldReturnPatients() throws Exception {
        Patient patient = new Patient(1L, "Test", "TestNone",
                LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333");

        when(patientService.getAllPatients()).thenReturn(List.of(patient));

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("TestNone"));
    }

    @Test
    @WithMockUser
    void shouldReturnPatientById() throws Exception {
        Patient patient = new Patient(1L, "Test", "TestNone",
                LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333");

        when(patientService.getPatientById(1L)).thenReturn(patient);

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("TestNone"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenPatientNotFound() throws Exception {
        when(patientService.getPatientById(99L))
                .thenThrow(new PatientNotFoundException(99L));

        mockMvc.perform(get("/patients/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldCreatePatient() throws Exception {
        Patient requestPatient = new Patient(null, "Test", "TestNone",
                LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333");
        Patient savedPatient = new Patient(1L, "Test", "TestNone",
                LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333");

        when(patientService.createPatient(any(Patient.class))).thenReturn(savedPatient);

        mockMvc.perform(post("/patients")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPatient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void shouldRejectPatientWithoutFirstName() throws Exception {
        String invalidJson = """
                {
                  "lastName": "TestNone",
                  "dateOfBirth": "1966-12-31",
                  "gender": "F"
                }
                """;

        mockMvc.perform(post("/patients")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldUpdatePatient() throws Exception {
        Patient updatedPatient = new Patient(1L, "Test", "TestNone",
                LocalDate.of(1966, 12, 31), "F", "Nouvelle adresse", "100-222-3333");

        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(updatedPatient);

        mockMvc.perform(put("/patients/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Nouvelle adresse"));
    }
}