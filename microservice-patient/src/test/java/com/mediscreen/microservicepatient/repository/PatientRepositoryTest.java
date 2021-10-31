package com.mediscreen.microservicepatient.repository;

import com.mediscreen.microservicepatient.model.Gender;
import com.mediscreen.microservicepatient.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * Class That test {@link IPatientRepository}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(value = {"/mediscreenTest.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class PatientRepositoryTest {

    private Patient patientTest;

    @Autowired
    private IPatientRepository patientRepositoryTest;


    @BeforeEach
    public void setUpPerTest() {
        patientTest = new Patient("John", "Boyd", "1964-09-23", Gender.M);
    }

    @Test
    public void findAllTest_ThenReturnListWith5Patients() {
        // GIVEN
        // WHEN
        List<Patient> patients = (List<Patient>) patientRepositoryTest.findAll();
        // THEN
        assertEquals(5, patients.size());
    }

    @Test
    public void findPatientsByLastNameTest_whenLastNameIsBoyd_ThenReturnListWith2Elements() {
        // GIVEN
        // WHEN
        List<Patient> PatientsByLastName = patientRepositoryTest.findByLastName("Martin");
        // THEN
        assertEquals(3, PatientsByLastName.size());
    }

    @Test
    public void saveTest() {
        // GIVEN
        // WHEN
        Patient patientSaved = patientRepositoryTest.save(patientTest);
        List<Patient> patients = (List<Patient>) patientRepositoryTest.findAll();
        // THEN
        assertNotNull(patientSaved);
        assertEquals("John", patientSaved.getFirstName());
        assertEquals("1964-09-23", patientSaved.getBirthDate());
        assertEquals(6, patients.size());
    }

    @Test
    public void findPatientByIdTest_whenPatientId3_thenReturnMartinWiliam() {
        // GIVEN
        // WHEN
        Patient patientTestResult = patientRepositoryTest.findById(3).get();
        //THEN
        assertNotNull(patientTestResult);
        assertEquals(3, patientTestResult.getId());
        assertEquals("Wiliam", patientTestResult.getFirstName());
        assertEquals("Martin", patientTestResult.getLastName());
        assertEquals("2005-01-02", patientTestResult.getBirthDate());
        assertEquals(Gender.F, patientTestResult.getGender());
    }
}