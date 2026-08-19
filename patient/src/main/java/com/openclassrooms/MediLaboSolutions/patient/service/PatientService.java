package com.openclassrooms.MediLaboSolutions.patient.service;


import com.openclassrooms.MediLaboSolutions.patient.exception.PatientNotFoundException;
import com.openclassrooms.MediLaboSolutions.patient.model.Patient;
import com.openclassrooms.MediLaboSolutions.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Couche métier de gestion des dossiers patients.
 */
@Service
public class PatientService {


    private final PatientRepository patientRepository;


    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    /**
     * Renvoie tous les patients enregistrés.
     */
    public List<Patient> getAllPatients() {

        return patientRepository.findAll();
    }


    /**
     * Récupère un patient par son id.
     *
     * @param id identifiant du patient
     * @return le patient correspondant
     * @throws PatientNotFoundException si aucun patient ne correspond à cet id
     */
    public Patient getPatientById(Long id) {

        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(id));
    }


    /**
     * Crée un nouveau patient.
     */
    public Patient createPatient(Patient patient) {

        return patientRepository.save(patient);
    }


    /**
     * Met à jour un patient existant en copiant champ par champ les
     * nouvelles valeurs sur l'entité déjà persistée, plutôt que de
     * remplacer l'entité entière — évite d'écraser accidentellement l'id
     * ou d'autres métadonnées gérées par JPA.
     *
     * @param id identifiant du patient à modifier
     * @param patient nouvelles valeurs à appliquer
     * @return le patient mis à jour
     * @throws PatientNotFoundException si aucun patient ne correspond à cet id
     */
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


    /**
     * Supprime un patient. Non demandé par les user stories du sujet — voir
     * la remarque équivalente sur {@code PatientController.deletePatient}.
     */
    public void deletePatient(Long id) {

        patientRepository.deleteById(id);
    }

}