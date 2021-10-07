package com.mediscreen.microservicepatient.repository;

import com.mediscreen.microservicepatient.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPatientRepository extends CrudRepository<Patient,Integer> {
}
