package com.mediscreen.microservicepatient.service;

import com.mediscreen.microservicepatient.model.Patient;

public interface IPatientService {
    Patient addPatient(Patient patient);

    Iterable<Patient> getPatients();

    Patient findPatientById(int id);

    Patient updatePatient(Patient patient);

    String deletePatientById(Integer id) ;
}
