package com.mediscreen.microsevicehistorypatient.service;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import com.mediscreen.microsevicehistorypatient.repository.INotePatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class NotePatientService {

    private INotePatientRepository notePatientRepository;

    @Autowired
    public NotePatientService(INotePatientRepository INotePatientRepository) {
        this.notePatientRepository = INotePatientRepository;
    }

    public Iterable<NotePatient> findNotesByPatientId(int patientId) {

        log.debug("Service - list notes found for patientID: " + patientId);
        return notePatientRepository.findAllByPatientId(patientId);
    }

    public NotePatient saveNotePatient(NotePatient notePatient) {
        notePatient.setDate(LocalDateTime.now());

        log.info("Service - Note of patient saved");
        return notePatientRepository.save(notePatient);
    }

}
