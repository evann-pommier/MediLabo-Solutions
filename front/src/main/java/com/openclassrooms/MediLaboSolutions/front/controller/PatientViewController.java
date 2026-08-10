package com.openclassrooms.MediLaboSolutions.front.controller;

import com.openclassrooms.MediLaboSolutions.front.model.NoteDto;
import com.openclassrooms.MediLaboSolutions.front.model.PatientDto;
import com.openclassrooms.MediLaboSolutions.front.model.RiskDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PatientViewController {

    private final RestTemplate restTemplate;

    @Value("${gateway.base-url}")
    private String gatewayBaseUrl;

    public PatientViewController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/patients")
    public String listPatients(Model model) {
        PatientDto[] patients = restTemplate.getForObject(gatewayBaseUrl + "/patients", PatientDto[].class);
        model.addAttribute("patients", patients);
        return "patients";
    }

    @GetMapping("/patients/{id}")
    public String patientDetail(@PathVariable Long id, Model model) {
        PatientDto patient = restTemplate.getForObject(gatewayBaseUrl + "/patients/" + id, PatientDto.class);
        NoteDto[] notes = restTemplate.getForObject(gatewayBaseUrl + "/notes/patient/" + id, NoteDto[].class);
        RiskDto risk = restTemplate.getForObject(gatewayBaseUrl + "/assess/" + id, RiskDto.class);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        model.addAttribute("risk", risk);
        return "patient-detail";
    }

    @PostMapping("/patients/{id}/notes")
    public String addNote(@PathVariable Long id, @RequestParam String patient,
                          @RequestParam String note, RedirectAttributes redirectAttributes) {
        NoteDto newNote = new NoteDto();
        newNote.setPatId(id);
        newNote.setPatient(patient);
        newNote.setNote(note);

        restTemplate.postForObject(gatewayBaseUrl + "/notes", newNote, NoteDto.class);

        return "redirect:/patients/" + id;
    }
}