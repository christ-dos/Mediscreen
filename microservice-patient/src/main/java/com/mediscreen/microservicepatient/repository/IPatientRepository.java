package com.mediscreen.microservicepatient.repository;

import com.mediscreen.microservicepatient.model.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Interface that handles database queries for {@link Patient}
 *
 * @author Christine Duarte
 */
public interface IPatientRepository extends CrudRepository<Patient, Integer> {
    List<Patient> findByLastName(String lastName);
}
