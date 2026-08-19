package com.openclassrooms.MediLaboSolutions.patient.repository;

import com.openclassrooms.MediLaboSolutions.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Accès aux patients stockés en base PostgreSQL.
 * <p>
 * Aucune méthode dérivée nécessaire ici : les opérations CRUD standard
 * fournies par {@link JpaRepository} (findAll, findById, save, deleteById)
 * suffisent aux besoins de patient-service.
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {

}