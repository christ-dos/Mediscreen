package com.mediscreen.microsevicehistorypatient.service;

import com.mediscreen.microsevicehistorypatient.exception.NotePatientNotFoundException;
import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import com.mediscreen.microsevicehistorypatient.repository.INotePatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Class of service that manage {@link NotePatient}
 * and implements INotePatientService
 *
 * @author Christine Duarte
 */
@Service
@Slf4j
public class NotePatientService implements INotePatientService {

    private INotePatientRepository notePatientRepository;

    @Autowired
    public NotePatientService(INotePatientRepository INotePatientRepository) {
        this.notePatientRepository = INotePatientRepository;
    }

    @Override
    public Iterable<NotePatient> findNotesByPatientId(int patientId) {

        log.debug("Service - list notes found for patientID: " + patientId);
        return notePatientRepository.findAllByPatientIdOrderByDateDesc(patientId);
    }

    @Override
    public NotePatient saveNotePatient(NotePatient notePatient) {
        notePatient.setDate(LocalDateTime.now());

        log.info("Service - Note of patient saved");
        return notePatientRepository.save(notePatient);
    }

    @Override
    public NotePatient updateNotePatient(NotePatient notePatient) {
        Optional<NotePatient> notePatientToUpdateOptional = notePatientRepository.findById(notePatient.getId());
        if (!notePatientToUpdateOptional.isPresent()) {
            throw new NotePatientNotFoundException("The Note of patient to update not found");
        }
        NotePatient notePatientToUpdate = notePatientToUpdateOptional.get();

        notePatientToUpdate.setNote(notePatient.getNote());
        notePatientToUpdate.setDate(LocalDateTime.now());

        log.debug("Service - Note patient updated with patient Id: " + notePatient.getId());
        return notePatientRepository.save(notePatientToUpdate);
    }

    @Override
    public NotePatient getNotePatientById(String id) {
        Optional<NotePatient> notePatientOptional = notePatientRepository.findById(id);
        if (!notePatientOptional.isPresent()) {
            log.error("Note with ID: " + id + " not found");
            throw new NotePatientNotFoundException("Note of patient not found");
        }
        log.debug("Service - Note patient found with ID: " + id);
        return notePatientOptional.get();
    }
}
