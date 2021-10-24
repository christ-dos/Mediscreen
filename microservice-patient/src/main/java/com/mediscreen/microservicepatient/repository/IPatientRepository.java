package com.mediscreen.microservicepatient.repository;

import com.mediscreen.microservicepatient.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;


public interface IPatientRepository extends CrudRepository<Patient,Integer> {
    List<Patient> findByLastName(String lastName);
}
