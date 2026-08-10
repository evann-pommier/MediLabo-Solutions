package com.openclassrooms.MediLaboSolutions.risk.controller;

import com.openclassrooms.MediLaboSolutions.risk.model.RiskAssessmentResult;
import com.openclassrooms.MediLaboSolutions.risk.service.RiskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/assess/{patId}")
    public RiskAssessmentResult assessPatient(@PathVariable Long patId) {
        return riskService.assessPatient(patId);
    }
}