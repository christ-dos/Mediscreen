package com.mediscreen.microsevicehistorypatient.controller;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import com.mediscreen.microsevicehistorypatient.service.INotePatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Slf4j
public class NotePatientController {

    @Autowired
    private INotePatientService notePatientService;

    @PostMapping(value = "/patHistory/add")
    public ResponseEntity<NotePatient> addNotePatient(@Valid @RequestBody NotePatient notePatient) {
        NotePatient notePatientSaved = notePatientService.saveNotePatient(notePatient);
        if (notePatientSaved == null) {
            ResponseEntity.noContent().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "NotePatientController");
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .port(8082)
                .path("/{id}")
                .buildAndExpand(notePatient.getId())
                .toUri();
        log.info("Controller - note patient created");
        return ResponseEntity.created(location).headers(headers).body(notePatientSaved);
    }

    @GetMapping(value = "/notesPatient/{patientId}")
    public Iterable<NotePatient> getListNotesByPatient(@PathVariable("patientId") int patientId) {
        log.debug("Controller - List notes of Patient n°: " + patientId + " displayed");
        return notePatientService.findNotesByPatientId(patientId);
    }

    @PostMapping(value = "/patHistory/update/{id}")
    public ResponseEntity<NotePatient> updateNotePatient(@Valid @RequestBody NotePatient notePatient) {
        NotePatient notePatientUpdated = notePatientService.updateNotePatient(notePatient);
        if (notePatientUpdated == null) {
            ResponseEntity.noContent().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "PatientController");
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .port(8082).build().toUri();

        log.debug("Controller - note patient to update with ID: " + notePatient.getId());
        return ResponseEntity.created(location).headers(headers).body(notePatientUpdated);
    }

    @GetMapping(value = "/note/{id}")
    public NotePatient getNotePatientById(@PathVariable("id") String id) {
        log.debug("Controller - Find Note of patient with ID: " + id);
        return notePatientService.getNotePatientById(id);
    }
}
