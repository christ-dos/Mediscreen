package com.mediscreen.microservicepatient.service;

import com.mediscreen.microservicepatient.exception.PatientNotFoundException;
import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.repository.IPatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class PatientService {

    private IPatientRepository patientRepository;

    @Autowired
    public PatientService(IPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient addPatient(Patient patient) {
        log.info("Service - Patient saved");
        return patientRepository.save(patient);
    }

    public Iterable<Patient> getPatients() {
        log.info("Service - list of patients found");
        return patientRepository.findAll();
    }

    public Patient findPatientById(int id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (!patient.isPresent()) {
            throw new PatientNotFoundException("Patient not found");
        }
        log.info("Service - Patient found with ID: " + id);
        return patient.get();
    }
}
