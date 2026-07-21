package com.openclassrooms.MediLaboSolutions.patient.exception;


public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(Long id) {

        super("Patient non trouvé avec l'id : " + id);
    }
}