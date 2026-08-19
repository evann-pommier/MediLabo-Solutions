package com.openclassrooms.MediLaboSolutions.patient.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Gestionnaire d'exceptions centralisé pour patient-service.
 * <p>
 * Traduit les exceptions métier en réponses HTTP appropriées, plutôt que de
 * laisser Spring renvoyer une erreur 500 générique — ici, un patient
 * introuvable devient un 404 explicite avec un message clair pour le
 * consommateur de l'API (front, risk-service).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Intercepte {@link PatientNotFoundException} levée par le service
     * lorsqu'un patient demandé n'existe pas, et la convertit en réponse
     * 404 avec le message de l'exception comme corps.
     */
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<String> handlePatientNotFound(
            PatientNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}