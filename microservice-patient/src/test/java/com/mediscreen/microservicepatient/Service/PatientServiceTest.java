package com.mediscreen.microservicepatient.Service;

import com.mediscreen.microservicepatient.exception.PatientNotFoundException;
import com.mediscreen.microservicepatient.model.Gender;
import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.repository.IPatientRepository;
import com.mediscreen.microservicepatient.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * Class That test PatientService
 *
 * @author Christine Duarte
 */
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    private PatientService patientServiceTest;

    @Mock
    private IPatientRepository patientRepositoryMock;

    private Patient patientTest;

    @BeforeEach
    public void setupPerTest() {
        patientServiceTest = new PatientService(patientRepositoryMock);
        patientTest = new Patient(1, "John", "Boyd", "1964-09-23", Gender.M, null, null);
    }

    @Test
    public void addPatientTest_whenPatientIsJohnBoyd_thenReturnPatientAdded() {
        //GIVEN
        when(patientRepositoryMock.save(isA(Patient.class))).thenReturn(patientTest);
        //WHEN
        Patient patientAdded = patientServiceTest.addPatient(patientTest);
        //THEN
        assertNotNull(patientAdded);
        assertEquals("John", patientAdded.getFirstName());
        assertEquals("1964-09-23", patientAdded.getBirthDate());
        verify(patientRepositoryMock, times(1)).save(any(Patient.class));
    }

    @Test
    public void getPatientsTest_thenReturnAnIterableWithAllPatients() {
        //GIVEN
        List<Patient> patientsTest = Arrays.asList(
                new Patient(
                        "John", "Boyd", "1964-09-23", Gender.M),
                new Patient(
                        "Jacob", "Boyd", "1968-07-15", Gender.M),
                new Patient(
                        "Johanna", "Lefevre", "1970-09-08", Gender.F)
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
        Patient patient = new Patient(1, "Johanna", "Lefevre", "1970-09-08", Gender.F, null, null);
        when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.of(patient));
        //WHEN
        Patient patientTestResult = patientServiceTest.findPatientById(patient.getId());
        //THEN
        assertNotNull(patientTestResult);
        assertEquals(1, patientTestResult.getId());
        assertEquals("Johanna", patientTestResult.getFirstName());
        assertEquals("Lefevre", patientTestResult.getLastName());
        assertEquals("1970-09-08", patientTestResult.getBirthDate());
        assertEquals(Gender.F, patientTestResult.getGender());
        verify(patientRepositoryMock, times(1)).findById(anyInt());
    }

    @Test
    public void findPatientByIdTest_whenPatientNotFound_thenThrowPatientNotFoundException() {
        //GIVEN
        when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
        //WHEN
        //THEN
        assertThrows(PatientNotFoundException.class, () -> patientServiceTest.findPatientById(150));
        verify(patientRepositoryMock, times(1)).findById(anyInt());
    }

    @Test
    public void findPatientsByLastNameTest_whenPatientIsPresentInDB_thenReturnListPatientFound() {
        //GIVEN
        List<Patient> patientsResultBoyd = Arrays.asList(
                new Patient(
                        "John", "Boyd", "1964-09-23", Gender.M),
                new Patient(
                        "Jacob", "Boyd", "1968-07-15", Gender.M),
                new Patient(
                        "Johanna", "Boyd", "1970-09-08", Gender.F)
        );
        when(patientRepositoryMock.findByLastName(anyString())).thenReturn(patientsResultBoyd);
        //WHEN
        List<Patient> patientTestResult = patientServiceTest.findPatientsByLastName("Boyd");
        //THEN
        assertTrue(patientTestResult.size() > 0);
        assertNotEquals(4, patientTestResult.size());
        assertEquals("John", patientTestResult.get(0).getFirstName());
        assertEquals("Jacob", patientTestResult.get(1).getFirstName());
        assertEquals("Johanna", patientTestResult.get(2).getFirstName());
        verify(patientRepositoryMock, times(1)).findByLastName(anyString());
    }

    @Test
    public void findPatientByLastNameTest_whenPatientNotFound_thenThrowPatientNotFoundException() {
        //GIVEN
        when(patientRepositoryMock.findByLastName(anyString())).thenReturn(Collections.emptyList());
        //WHEN
        //THEN
        assertThrows(PatientNotFoundException.class, () -> patientServiceTest.findPatientsByLastName("familyName"));
        verify(patientRepositoryMock, times(1)).findByLastName(anyString());
    }

    @Test
    public void updatePatientTest_whenPatientRecordedInDb_thenReturnPatientUpdated() {
        //GIVEN
        Patient patientToUpdate = new Patient(
                1, "Johnne", "Boyd", "1964-09-23", Gender.M, "1509 Culver St ,Culver 97451", "123-456-789");
        when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.ofNullable(patientTest));
        when(patientRepositoryMock.save(isA(Patient.class))).thenReturn(patientToUpdate);
        //WHEN
        Patient patientUpdatedResult = patientServiceTest.updatePatient(patientTest);
        //THEN
        assertEquals(1, patientUpdatedResult.getId());
        assertEquals("Johnne", patientUpdatedResult.getFirstName());
        assertEquals(patientToUpdate.getAddress(), patientUpdatedResult.getAddress());
        assertEquals("123-456-789", patientUpdatedResult.getPhone());
        verify(patientRepositoryMock, times(1)).findById(anyInt());
        verify(patientRepositoryMock, times(1)).save(any(Patient.class));
    }

    @Test
    public void updatePatientTest_whenPatientNotFoundInDb_thenThrowPatientNotFoundException() {
        //GIVEN
        when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
        //WHEN
        //THEN
        assertThrows(PatientNotFoundException.class, () -> patientServiceTest.updatePatient(patientTest));
        verify(patientRepositoryMock, times(1)).findById(anyInt());
        verify(patientRepositoryMock, times(0)).save(any(Patient.class));
    }
}
