package com.openclassrooms.MediLaboSolutions.front.controller;

import com.openclassrooms.MediLaboSolutions.front.model.PatientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

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
        model.addAttribute("patient", patient);
        return "patient-detail";
    }
}