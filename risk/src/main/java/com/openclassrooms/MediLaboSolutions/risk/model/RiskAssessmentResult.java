package com.openclassrooms.MediLaboSolutions.risk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RiskAssessmentResult {

    private Long patId;
    private String patient;
    private String risk;
}