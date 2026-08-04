package com.openclassrooms.MediLaboSolutions.patient.controller;


import com.openclassrooms.MediLaboSolutions.patient.model.Patient;
import com.openclassrooms.MediLaboSolutions.patient.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/patients")
public class PatientController {


    private final PatientService patientService;


    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }



    @GetMapping
    public List<Patient> getPatients() {

        return patientService.getAllPatients();
    }



    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {

        return patientService.getPatientById(id);
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient createPatient(@Valid @RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }



    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patient) {

        return patientService.updatePatient(id, patient);
    }



    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {

        patientService.deletePatient(id);
    }

}