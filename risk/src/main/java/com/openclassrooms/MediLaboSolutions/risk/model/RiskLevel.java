package com.openclassrooms.MediLaboSolutions.risk.model;

import lombok.Getter;

@Getter
public enum RiskLevel {
    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("InDanger"),
    EARLY_ONSET("EarlyOnset");

    private final String label;

    RiskLevel(String label) {
        this.label = label;
    }
}