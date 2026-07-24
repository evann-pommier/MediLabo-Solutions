package com.openclassrooms.MediLaboSolutions.patient.controller;

import com.openclassrooms.MediLaboSolutions.patient.model.Patient;
import com.openclassrooms.MediLaboSolutions.patient.service.PatientService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}