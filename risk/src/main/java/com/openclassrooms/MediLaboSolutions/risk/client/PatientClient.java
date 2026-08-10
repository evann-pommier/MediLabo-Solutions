package com.openclassrooms.MediLaboSolutions.risk.client;

import com.openclassrooms.MediLaboSolutions.risk.model.PatientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PatientClient {

    private final RestTemplate restTemplate;

    @Value("${patient-service.base-url}")
    private String patientServiceBaseUrl;

    public PatientClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PatientDto getPatientById(Long patId) {
        return restTemplate.getForObject(patientServiceBaseUrl + "/patients/" + patId, PatientDto.class);
    }
}