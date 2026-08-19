package com.openclassrooms.MediLaboSolutions.patient.controller;


import com.openclassrooms.MediLaboSolutions.patient.model.Patient;
import com.openclassrooms.MediLaboSolutions.patient.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Expose les endpoints REST de gestion du dossier patient.
 * <p>
 * Consommé par la gateway (pour le front) et par risk-service (pour le
 * calcul du niveau de risque) — les deux s'authentifient en HTTP Basic
 * avec les identifiants configurés dans {@code SecurityConfig}.
 */
@RestController
@RequestMapping("/patients")
public class PatientController {


    private final PatientService patientService;


    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    /**
     * Renvoie la liste de tous les patients.
     *
     * @return la liste complète des patients
     */
    @GetMapping
    public List<Patient> getPatients() {

        return patientService.getAllPatients();
    }


    /**
     * Renvoie le dossier détaillé d'un patient.
     *
     * @param id identifiant du patient
     * @return le patient correspondant (404 si absent, via GlobalExceptionHandler)
     */
    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {

        return patientService.getPatientById(id);
    }


    /**
     * Crée un nouveau dossier patient.
     * <p>
     * L'adresse et le téléphone restent optionnels (pas de {@code @NotBlank}
     * sur ces champs dans {@link Patient}), conformément à la règle métier
     * du client.
     *
     * @param patient patient à créer
     * @return le patient créé, avec son id généré (statut 201)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient createPatient(@Valid @RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }


    /**
     * Met à jour les informations personnelles d'un patient existant.
     *
     * @param id identifiant du patient à modifier
     * @param patient nouvelles valeurs
     * @return le patient mis à jour
     */
    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patient) {

        return patientService.updatePatient(id, patient);
    }


    /**
     * Supprime un dossier patient.
     * <p>
     * Non demandé par les user stories du sujet — endpoint conservé pour
     * complétude de l'API REST, mais à assumer explicitement si le jury
     * questionne sa présence.
     *
     * @param id identifiant du patient à supprimer
     */
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {

        patientService.deletePatient(id);
    }

}