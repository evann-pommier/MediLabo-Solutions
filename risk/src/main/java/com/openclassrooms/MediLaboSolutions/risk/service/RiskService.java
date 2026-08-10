package com.openclassrooms.MediLaboSolutions.risk.service;

import com.openclassrooms.MediLaboSolutions.risk.client.NoteClient;
import com.openclassrooms.MediLaboSolutions.risk.client.PatientClient;
import com.openclassrooms.MediLaboSolutions.risk.model.NoteDto;
import com.openclassrooms.MediLaboSolutions.risk.model.PatientDto;
import com.openclassrooms.MediLaboSolutions.risk.model.RiskAssessmentResult;
import com.openclassrooms.MediLaboSolutions.risk.model.RiskLevel;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public RiskAssessmentResult assessPatient(Long patId) {
        PatientDto patient = patientClient.getPatientById(patId);
        List<NoteDto> notes = noteClient.getNotesByPatient(patId);

        RiskLevel riskLevel = riskAssessmentService.assessRisk(patient, notes);

        String fullName = patient.getFirstName() + " " + patient.getLastName();
        return new RiskAssessmentResult(patId, fullName, riskLevel.getLabel());
    }
}