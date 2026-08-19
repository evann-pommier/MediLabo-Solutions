package com.openclassrooms.MediLaboSolutions.risk.client;

import com.openclassrooms.MediLaboSolutions.risk.model.PatientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Client HTTP utilisé par risk-service pour récupérer les informations
 * personnelles d'un patient auprès de patient-service.
 * <p>
 * Le client utilise {@link RestTemplate} pour effectuer l'appel REST.
 * L'URL de base de patient-service est externalisée dans la configuration
 * via la propriété {@code patient-service.base-url}, afin de pouvoir
 * adapter l'adresse du service selon l'environnement d'exécution.
 */
@Component
public class PatientClient {

    /** Client HTTP utilisé pour communiquer avec patient-service. */
    private final RestTemplate restTemplate;

    /** URL de base de patient-service, injectée depuis la configuration. */
    @Value("${patient-service.base-url}")
    private String patientServiceBaseUrl;

    /**
     * Construit le client avec le {@link RestTemplate} fourni par Spring.
     *
     * @param restTemplate client HTTP utilisé pour appeler patient-service
     */
    public PatientClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Récupère les informations d'un patient depuis patient-service.
     * <p>
     * L'identifiant du patient est ajouté à l'URL de l'endpoint
     * {@code /patients/{id}}. La réponse JSON est automatiquement
     * désérialisée par Spring dans un {@link PatientDto}.
     *
     * @param patId identifiant du patient recherché
     * @return les informations du patient sous forme de {@link PatientDto}
     */
    public PatientDto getPatientById(Long patId) {
        return restTemplate.getForObject(patientServiceBaseUrl + "/patients/" + patId, PatientDto.class);
    }
}