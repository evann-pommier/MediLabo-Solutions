package com.openclassrooms.MediLaboSolutions.risk.client;

import com.openclassrooms.MediLaboSolutions.risk.model.NoteDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class NoteClient {

    private final RestTemplate restTemplate;

    @Value("${notes-service.base-url}")
    private String notesServiceBaseUrl;

    public NoteClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<NoteDto> getNotesByPatient(Long patId) {
        NoteDto[] notes = restTemplate.getForObject(
                notesServiceBaseUrl + "/notes/patient/" + patId, NoteDto[].class);
        return notes != null ? List.of(notes) : List.of();
    }
}