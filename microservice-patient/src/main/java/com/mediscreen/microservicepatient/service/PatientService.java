package com.mediscreen.microservicepatient.service;

import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.repository.IPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PatientService {

    private IPatientRepository patientRepository;

    @Autowired
    public PatientService(IPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient addPatient(Patient patient){
        return patientRepository.save(patient);
    }
}
