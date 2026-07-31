package com.openclassrooms.MediLaboSolutions.notes.repository;

import com.openclassrooms.MediLaboSolutions.notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatId(Long patId);
}