package com.mediscreen.microservicepatient.service;

import com.mediscreen.microservicepatient.model.Patient;

import java.util.List;

public interface IPatientService {
    Patient addPatient(Patient patient);

    Iterable<Patient> getPatients();

    Patient findPatientById(int id);

    Patient updatePatient(Patient patient);

    List<Patient> findPatientsByLastName(String lastName);

}
