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
        log.info("Service - Patient found with ID: " + id);
        return patient.get();
    }

    @Override
    public Patient updatePatient(Patient patient){
        Optional<Patient> patientToUpdate = patientRepository.findById(patient.getId());
        if(!patientToUpdate.isPresent()){
            throw  new PatientNotFoundException("Patient to update not found");
        }
        patientToUpdate.get().setFirstName(patient.getFirstName());
        patientToUpdate.get().setLastName(patient.getLastName());
        patientToUpdate.get().setBirthDate(patient.getBirthDate());
        patientToUpdate.get().setGender(patient.getGender());
        patientToUpdate.get().setAddress(patient.getAddress());
        patientToUpdate.get().setPhone(patient.getPhone());

        return patientRepository.save(patientToUpdate.get());
    }
}
