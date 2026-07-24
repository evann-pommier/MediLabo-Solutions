package com.openclassrooms.MediLaboSolutions.patient.config;

import com.openclassrooms.MediLaboSolutions.patient.model.Patient;
import com.openclassrooms.MediLaboSolutions.patient.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PatientRepository patientRepository;

    public DataSeeder(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) {
        if (patientRepository.count() > 0) {
            return;
        }

        patientRepository.save(new Patient(null, "Test", "TestNone",
                LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333"));
        patientRepository.save(new Patient(null, "Test", "TestBorderline",
                LocalDate.of(1945, 6, 24), "M", "2 High St", "200-333-4444"));
        patientRepository.save(new Patient(null, "Test", "TestInDanger",
                LocalDate.of(2004, 6, 18), "M", "3 Club Road", "300-444-5555"));
        patientRepository.save(new Patient(null, "Test", "TestEarlyOnset",
                LocalDate.of(2002, 6, 28), "F", "4 Valley Dr", "400-555-6666"));
    }
}