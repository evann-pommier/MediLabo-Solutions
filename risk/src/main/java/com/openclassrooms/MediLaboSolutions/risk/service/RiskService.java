package com.openclassrooms.MediLaboSolutions.risk.service;

import com.openclassrooms.MediLaboSolutions.risk.client.NoteClient;
import com.openclassrooms.MediLaboSolutions.risk.client.PatientClient;
import com.openclassrooms.MediLaboSolutions.risk.model.NoteDto;
import com.openclassrooms.MediLaboSolutions.risk.model.PatientDto;
import com.openclassrooms.MediLaboSolutions.risk.model.RiskAssessmentResult;
import com.openclassrooms.MediLaboSolutions.risk.model.RiskLevel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Orchestre l'évaluation du risque d'un patient : récupère ses données
 * personnelles et ses notes auprès des deux autres microservices, puis
 * délègue le calcul proprement dit à {@link RiskAssessmentService}.
 * <p>
 * C'est la seule classe de risk-service qui dépend des clients HTTP —
 * {@code RiskAssessmentService} reste ainsi totalement indépendant du
 * réseau et testable de façon unitaire, sans mock.
 */
@Service
public class RiskService {

    private final PatientClient patientClient;
    private final NoteClient noteClient;
    private final RiskAssessmentService riskAssessmentService;

    public RiskService(PatientClient patientClient, NoteClient noteClient,
                       RiskAssessmentService riskAssessmentService) {
        this.patientClient = patientClient;
        this.noteClient = noteClient;
        this.riskAssessmentService = riskAssessmentService;
    }

    /**
     * Évalue le niveau de risque de diabète d'un patient.
     *
     * @param patId identifiant du patient (id SQL côté patient-service)
     * @return le résultat de l'évaluation, prêt à être renvoyé par l'API
     */
    public RiskAssessmentResult assessPatient(Long patId) {
        PatientDto patient = patientClient.getPatientById(patId);
        List<NoteDto> notes = noteClient.getNotesByPatient(patId);

        RiskLevel riskLevel = riskAssessmentService.assessRisk(patient, notes);

        String fullName = patient.getFirstName() + " " + patient.getLastName();
        return new RiskAssessmentResult(patId, fullName, riskLevel.getLabel());
    }
}