package com.mediscreen.microsevicehistorypatient.controller;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import com.mediscreen.microsevicehistorypatient.service.NotePatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@Slf4j
public class NotePatientController {

    @Autowired
    private NotePatientService notePatientService;

    @PostMapping(value = "/patHistory/add")
    public ResponseEntity<NotePatient> addNotePatient(@Valid @RequestBody NotePatient notePatient) {
        NotePatient notePatientSaved= notePatientService.saveNotePatient(notePatient);
        if (notePatientSaved == null) {
            ResponseEntity.noContent().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "NotePatientController");
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .port(8082)
                .path("/{id}")
                .buildAndExpand(notePatient.getPatientId())
                .toUri();
        log.info("Controller - note patient created");
        return ResponseEntity.created(location).headers(headers).body(notePatientSaved);
    }

    @GetMapping(value = "/notesPatient/{id}")
    public Iterable<NotePatient> getListNotesByPatient(@PathVariable("id") int patientId) {
        log.info("Controller - List notes of Patient: " + patientId +" displayed");
        return notePatientService.findNotesByPatientId(patientId);
    }

    @GetMapping(value = "/notes")
    public Iterable<NotePatient> getListNotesAllPatients() {
        log.info("Controller - List notes of all patients displayed");
        return notePatientService.getAllNotesForAllPatients();
    }

}
