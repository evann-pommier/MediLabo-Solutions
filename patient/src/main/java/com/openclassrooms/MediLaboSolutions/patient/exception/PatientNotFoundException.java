package com.openclassrooms.MediLaboSolutions.patient.exception;


/**
 * Exception levée lorsqu'un patient demandé (par id) n'existe pas en base.
 * Interceptée par {@link GlobalExceptionHandler} pour être convertie en
 * réponse HTTP 404.
 */
public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(Long id) {

        super("Patient non trouvé avec l'id : " + id);
    }
}