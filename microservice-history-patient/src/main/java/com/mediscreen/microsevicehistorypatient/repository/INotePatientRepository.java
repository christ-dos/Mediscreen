package com.mediscreen.microsevicehistorypatient.repository;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface that handles database queries for {@link NotePatient}
 *
 * @author Christine Duarte
 */
@Repository
public interface INotePatientRepository extends MongoRepository<NotePatient, String> {

    Iterable<NotePatient> findAllByPatientIdOrderByDateDesc(Integer patientId);
}
