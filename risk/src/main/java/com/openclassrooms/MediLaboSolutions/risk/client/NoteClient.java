package com.openclassrooms.MediLaboSolutions.risk.client;

import com.openclassrooms.MediLaboSolutions.risk.model.NoteDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Client HTTP vers notes-service. risk-service appelle ce service
 * directement (sans repasser par la gateway) : les appels service-à-service
 * internes n'ont pas besoin de transiter par le point d'entrée destiné au
 * trafic externe (front), ce qui évite un double saut réseau inutile.
 * L'authentification passe par le même {@code RestTemplate} configuré avec
 * un {@code BasicAuthenticationInterceptor} (voir {@code RiskApplication}).
 */
@Component
public class NoteClient {

    private final RestTemplate restTemplate;

    @Value("${notes-service.base-url}")
    private String notesServiceBaseUrl;

    public NoteClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Récupère les notes d'un patient auprès de notes-service.
     * <p>
     * Protège contre une réponse {@code null} (par exemple si notes-service
     * renvoie un corps vide plutôt qu'un tableau JSON vide) — sans cette
     * protection, un patient sans note ferait planter le calcul du risque
     * avec un {@link NullPointerException}.
     *
     * @param patId identifiant du patient
     * @return la liste de ses notes, jamais {@code null}
     */
    public List<NoteDto> getNotesByPatient(Long patId) {
        NoteDto[] notes = restTemplate.getForObject(
                notesServiceBaseUrl + "/notes/patient/" + patId, NoteDto[].class);
        return notes != null ? List.of(notes) : List.of();
    }
}