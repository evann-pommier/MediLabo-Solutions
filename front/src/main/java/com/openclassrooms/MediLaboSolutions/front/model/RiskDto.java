package com.openclassrooms.MediLaboSolutions.front.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiskDto {

    private Long patId;
    private String patient;
    private String risk;
}