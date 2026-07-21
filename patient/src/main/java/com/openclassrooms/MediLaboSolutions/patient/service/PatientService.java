package com.openclassrooms.MediLaboSolutions.patient.service;


import com.openclassrooms.MediLaboSolutions.patient.exception.PatientNotFoundException;
import com.openclassrooms.MediLaboSolutions.patient.model.Patient;
import com.openclassrooms.MediLaboSolutions.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PatientService {


    private final PatientRepository patientRepository;


    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }



    public List<Patient> getAllPatients() {

        return patientRepository.findAll();
    }



    public Patient getPatientById(Long id) {

        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(id));
    }



    public Patient createPatient(Patient patient) {

        return patientRepository.save(patient);
    }



    public Patient updatePatient(Long id, Patient patient) {

        Patient existingPatient = getPatientById(id);

        existingPatient.setFirstName(patient.getFirstName());
        existingPatient.setLastName(patient.getLastName());
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        existingPatient.setGender(patient.getGender());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setPhone(patient.getPhone());

        return patientRepository.save(existingPatient);
    }



    public void deletePatient(Long id) {

        patientRepository.deleteById(id);
    }

}