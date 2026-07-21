package com.openclassrooms.MediLaboSolutions.patient.repository;

import com.openclassrooms.MediLaboSolutions.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}