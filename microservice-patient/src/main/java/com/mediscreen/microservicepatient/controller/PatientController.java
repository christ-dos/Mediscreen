package com.mediscreen.microservicepatient.controller;

import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.service.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;


@RestController
@Slf4j
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @PostMapping(value = "/patient/add")
    public ResponseEntity<Patient> addPatient(@Valid @RequestBody Patient patient) {
        Patient patientSaved = patientService.addPatient(patient);
        if (patientSaved == null) {
            ResponseEntity.noContent().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "PatientController");
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .port(8081)
                .path("/{id}")
                .buildAndExpand(patientSaved.getId())
                .toUri();

        log.info("Controller - Patient created");
        return ResponseEntity.created(location).headers(headers).body(patientSaved);
    }

    @GetMapping(value = "/patients")
    public Iterable<Patient> getListPatients() {
        log.info("Controller - List Patients displayed");
        return patientService.getPatients();
    }

    @GetMapping(value = "/patient/{id}")
    public Patient getPatientById(@PathVariable("id") int id) {
        log.info("Controller - Find Patient with ID: " + id);
        return patientService.findPatientById(id);
    }

    @GetMapping(value = "/patient/lastname/{lastName}")
    public Patient getPatientByLastName(@PathVariable("lastName") String lastName) {
        log.info("Controller - Find Patient with family name: " + lastName);
        return patientService.findPatientByLastName(lastName);
    }

    @PostMapping(value = "/patient/update/{id}")
    public ResponseEntity<Patient> updatePatient(@Valid @RequestBody Patient patient) {
        Patient patientUpdated = patientService.updatePatient(patient);
        if (patientUpdated == null) {
            ResponseEntity.noContent().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "PatientController");
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .port(8081).build().toUri();

        log.info("Controller - Patient to update with ID: " + patient.getId());
        return ResponseEntity.created(location).headers(headers).body(patientUpdated);
    }

}
