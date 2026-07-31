package com.openclassrooms.MediLaboSolutions.front.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDto {

    private String id;
    private Long patId;
    private String patient;
    private String note;
}