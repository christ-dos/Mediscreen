package com.mediscreen.microservicepatient.Service;

import com.mediscreen.microservicepatient.model.GenderEnum;
import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.repository.IPatientRepository;
import com.mediscreen.microservicepatient.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.isA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class That test Patient Service
 *
 * @author Christine Duarte
 */
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    private PatientService patientServiceTest;

    @Mock
    private IPatientRepository patientRepositoryMock;

    @BeforeEach
    public void setupPerTest() {
        patientServiceTest = new PatientService(patientRepositoryMock);
    }

    @Test
    public void addPatientTest_whenPatientIsJohnBoyd_thenReturnPatientAdded() {
        //GIVEN
        Patient patientTest =  new Patient("John","Boyd","1964-09-23", GenderEnum.M);
        when(patientRepositoryMock.save(isA(Patient.class))).thenReturn(patientTest);
        //WHEN
        Patient patientAdded = patientServiceTest.addPatient(patientTest);
        //THEN
        assertNotNull(patientAdded);
        assertEquals("John",patientAdded.getFirstName());
        assertEquals("1964-09-23",patientAdded.getBirthDate());

    }
}
