package com.openclassrooms.MediLaboSolutions.front.controller;

import com.openclassrooms.MediLaboSolutions.front.model.NoteDto;
import com.openclassrooms.MediLaboSolutions.front.model.PatientDto;
import com.openclassrooms.MediLaboSolutions.front.model.RiskDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Contrôleur MVC responsable de l'affichage des pages patient côté front.
 * <p>
 * Ce contrôleur ne contient aucune logique métier : il se contente d'agréger
 * les réponses des microservices back (patient, notes, risque) pour les
 * exposer via des vues Thymeleaf. Tous les appels passent exclusivement par
 * la Spring Cloud Gateway ({@code gatewayBaseUrl}) plutôt que d'interroger
 * directement les microservices back — le front ne doit connaître qu'un seul
 * point d'entrée, conformément à l'architecture définie pour ce projet.
 */
@Controller
public class PatientViewController {

    private final RestTemplate restTemplate;

    @Value("${gateway.base-url}")
    private String gatewayBaseUrl;

    public PatientViewController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Affiche la liste de tous les patients.
     *
     * @param model modèle Thymeleaf, alimenté avec la liste des patients
     * @return le nom de la vue "patients" (patients.html)
     */
    @GetMapping("/patients")
    public String listPatients(Model model) {
        PatientDto[] patients = restTemplate.getForObject(gatewayBaseUrl + "/patients", PatientDto[].class);
        model.addAttribute("patients", patients);
        return "patients";
    }

    /**
     * Affiche la fiche détaillée d'un patient : informations personnelles,
     * historique des notes du médecin et niveau de risque de diabète.
     * <p>
     * Trois appels distincts sont nécessaires ici car chaque donnée provient
     * d'un microservice différent (patient-service, notes-service,
     * risk-service) — le front est responsable d'agréger ces trois sources
     * sur une seule page, comme demandé par le client.
     *
     * @param id identifiant du patient (id SQL côté patient-service)
     * @param model modèle Thymeleaf, alimenté avec le patient, ses notes et son risque
     * @return le nom de la vue "patient-detail" (patient-detail.html)
     */
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

    /**
     * Traite la soumission du formulaire d'ajout de note depuis la page
     * détail patient, puis redirige vers cette même page.
     * <p>
     * La redirection (plutôt qu'un simple retour de vue) évite la
     * re-soumission accidentelle du formulaire si l'utilisateur rafraîchit
     * la page (pattern Post/Redirect/Get), et permet de recharger les notes
     * à jour — y compris celle qui vient d'être ajoutée.
     *
     * @param id identifiant du patient concerné par la note
     * @param patient nom du patient (pré-rempli côté vue, non ressaisi par l'utilisateur)
     * @param note contenu de la note saisie par le médecin
     * @param redirectAttributes non utilisé actuellement, réservé pour un futur message de confirmation
     * @return une redirection vers la page détail du patient
     */
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

    @GetMapping("/patients/new")
    public String newPatientForm(Model model) {
        model.addAttribute("patient", new PatientDto());
        return "patient-form";
    }

    @PostMapping("/patients/new")
    public String createPatient(@ModelAttribute PatientDto patient, RedirectAttributes redirectAttributes) {
        PatientDto created = restTemplate.postForObject(gatewayBaseUrl + "/patients", patient, PatientDto.class);
        assert created != null;
        return "redirect:/patients/" + created.getId();
    }

    @GetMapping("/patients/{id}/edit")
    public String editPatientForm(@PathVariable Long id, Model model) {
        PatientDto patient = restTemplate.getForObject(gatewayBaseUrl + "/patients/" + id, PatientDto.class);
        model.addAttribute("patient", patient);
        return "patient-form";
    }

    @PostMapping("/patients/{id}/edit")
    public String updatePatient(@PathVariable Long id, @ModelAttribute PatientDto patient) {
        restTemplate.put(gatewayBaseUrl + "/patients/" + id, patient);
        return "redirect:/patients/" + id;
    }
}