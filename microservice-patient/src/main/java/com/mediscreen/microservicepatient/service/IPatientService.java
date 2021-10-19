package com.mediscreen.microservicepatient.service;

import com.mediscreen.microservicepatient.model.Patient;

import java.util.Optional;

public interface IPatientService {
    Patient addPatient(Patient patient);

    Iterable<Patient> getPatients();

    Patient findPatientById(int id);

    Patient updatePatient(Patient patient);

    Patient findPatientByLastName(String lastName);

}
