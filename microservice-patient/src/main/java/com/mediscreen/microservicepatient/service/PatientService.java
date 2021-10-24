package com.mediscreen.microservicepatient.service;

import com.mediscreen.microservicepatient.exception.PatientNotFoundException;
import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.repository.IPatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class PatientService implements IPatientService {

    private IPatientRepository patientRepository;

    @Autowired
    public PatientService(IPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient addPatient(Patient patient) {
        log.info("Service - Patient saved");
        return patientRepository.save(patient);
    }

    @Override
    public Iterable<Patient> getPatients() {
        log.info("Service - list of patients found");
        return patientRepository.findAll();
    }

    @Override
    public Patient findPatientById(int id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (!patient.isPresent()) {
            throw new PatientNotFoundException("Patient not found");
        }
        log.debug("Service - Patient found with ID: " + id);
        return patient.get();
    }

    @Override
    public List<Patient> findPatientsByLastName(String lastName) {
        List<Patient> patientByLastName = patientRepository.findByLastName(lastName);
        if (patientByLastName.isEmpty()) {
            throw new PatientNotFoundException("Patient not found");
        }
        log.debug("Service - Patient found with family name: " + lastName);
        return patientByLastName;
    }

    @Override
    public Patient updatePatient(Patient patient) {
        Optional<Patient> patientToUpdateOptional = patientRepository.findById(patient.getId());
        if (!patientToUpdateOptional.isPresent()) {
            throw new PatientNotFoundException("Patient to update not found");
        }
        Patient patientToUpdate = patientToUpdateOptional.get();

        patientToUpdate.setFirstName(patient.getFirstName());
        patientToUpdate.setLastName(patient.getLastName());
        patientToUpdate.setBirthDate(patient.getBirthDate());
        patientToUpdate.setGender(patient.getGender());
        patientToUpdate.setAddress(patient.getAddress());
        patientToUpdate.setPhone(patient.getPhone());

        log.debug("Service - Patient updated with ID: " + patientToUpdate.getId());
        return patientRepository.save(patientToUpdate);
    }

}
