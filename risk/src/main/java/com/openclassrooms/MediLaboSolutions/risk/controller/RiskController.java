package com.openclassrooms.MediLaboSolutions.risk.controller;

import com.openclassrooms.MediLaboSolutions.risk.model.RiskAssessmentResult;
import com.openclassrooms.MediLaboSolutions.risk.service.RiskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST permettant d'évaluer le niveau de risque d'un patient.
 * <p>
 * Ce contrôleur reçoit les requêtes HTTP et délègue le calcul du risque
 * au {@link RiskService}.
 */
@RestController
public class RiskController {

    /** Service utilisé pour effectuer l'évaluation du risque du patient. */
    private final RiskService riskService;

    /**
     * Construit le contrôleur avec le service d'évaluation des risques.
     *
     * @param riskService service utilisé pour calculer le niveau de risque
     */
    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    /**
     * Évalue le niveau de risque d'un patient à partir de son identifiant.
     * <p>
     * L'identifiant est récupéré depuis l'URL puis transmis au service
     * qui effectue l'ensemble du traitement nécessaire à l'évaluation.
     *
     * @param patId identifiant du patient à évaluer
     * @return résultat de l'évaluation du risque du patient
     */
    @GetMapping("/assess/{patId}")
    public RiskAssessmentResult assessPatient(@PathVariable Long patId) {
        return riskService.assessPatient(patId);
    }
}