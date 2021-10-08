package com.mediscreen.microservicepatient.Service;

import com.mediscreen.microservicepatient.exception.PatientNotFoundException;
import com.mediscreen.microservicepatient.model.GenderEnum;
import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.repository.IPatientRepository;
import com.mediscreen.microservicepatient.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
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
        Patient patientTest = new Patient("John", "Boyd", "1964-09-23", GenderEnum.M);
        when(patientRepositoryMock.save(isA(Patient.class))).thenReturn(patientTest);
        //WHEN
        Patient patientAdded = patientServiceTest.addPatient(patientTest);
        //THEN
        assertNotNull(patientAdded);
        assertEquals("John", patientAdded.getFirstName());
        assertEquals("1964-09-23", patientAdded.getBirthDate());
        verify(patientRepositoryMock,times(1)).save(patientTest);
    }

    @Test
    public void getPatientsTest_thenReturnAnIterableWithAllPatients() {
        //GIVEN
        List<Patient> patientsTest = Arrays.asList(
                new Patient(
                        "John", "Boyd", "1964-09-23", GenderEnum.M),
                new Patient(
                        "Jacob", "Boyd", "1968-07-15", GenderEnum.M),
                new Patient(
                        "Johanna", "Lefevre", "1970-09-08", GenderEnum.F)
        );
        when(patientRepositoryMock.findAll()).thenReturn(patientsTest);
        //WHEN
        List<Patient> listPatientsResult = (List<Patient>) patientServiceTest.getPatients();
        //THEN
        assertTrue(listPatientsResult.size() == 3);
        assertEquals("John", listPatientsResult.get(0).getFirstName());
        assertEquals("Boyd", listPatientsResult.get(0).getLastName());
        assertEquals("1964-09-23", listPatientsResult.get(0).getBirthDate());
        verify(patientRepositoryMock, times(1)).findAll();
    }

    @Test
    public void findPatientByIdTest_whenPatientIsPresentInDB_thenReturnPatientFound() {
        //GIVEN
        Patient patient = new Patient(1, "Johanna", "Lefevre", "1970-09-08", GenderEnum.F, null, null);
        when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.of(patient));
        //WHEN
        Patient patientTestResult = patientServiceTest.findPatientById(patient.getId());
        //THEN
        assertNotNull(patientTestResult);
        assertEquals(1, patientTestResult.getId());
        assertEquals("Johanna", patientTestResult.getFirstName());
        assertEquals("Lefevre", patientTestResult.getLastName());
        assertEquals("1970-09-08", patientTestResult.getBirthDate());
        assertEquals(GenderEnum.F, patientTestResult.getGender());
        verify(patientRepositoryMock, times(1)).findById(anyInt());
    }

    @Test
    public void findPatientByIdTest_whenPatientNotFound_thenThrowPatientNotFoundException() {
        //GIVEN
        when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
        //WHEN
        //THEN
        assertThrows(PatientNotFoundException.class, () -> patientServiceTest.findPatientById(150));
        verify(patientRepositoryMock,times(1)).findById(150);
    }
}
