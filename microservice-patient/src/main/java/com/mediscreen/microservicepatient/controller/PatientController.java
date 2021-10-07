package com.mediscreen.microservicepatient.controller;

import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;




@RestController
@Slf4j
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping(value = "/addPatient")
    public Patient addPatient(@Valid @RequestBody Patient patient, BindingResult result, Model model) {
        log.info("Controller: patient added: ");

        return patientService.addPatient(patient) ;
    }
}
