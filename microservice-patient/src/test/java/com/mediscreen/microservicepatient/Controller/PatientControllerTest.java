package com.mediscreen.microservicepatient.Controller;

import com.mediscreen.microservicepatient.controller.PatientController;
import com.mediscreen.microservicepatient.exception.PatientNotFoundException;
import com.mediscreen.microservicepatient.model.Gender;
import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.service.PatientService;
import com.mediscreen.microservicepatient.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class that test PatientController
 *
 * @author Christine Duarte
 */
@WebMvcTest(PatientController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class PatientControllerTest {
    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcPatient;

    @MockBean
    private PatientService patientServiceMock;


    private Patient patientTest;

    @BeforeEach
    public void setupPerTest() {
        patientTest = new Patient(2, "Jacob", "Boyd", "1968-07-15", Gender.M, null, null);
    }

    @Test
    public void addPatientTest_whenPatientNotExitsInDb_thenReturnResponseEntityCreated() throws Exception {
        //GIVEN
        patientTest.setId(5);
        when(patientServiceMock.addPatient(any(Patient.class))).thenReturn(patientTest);
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/patient/add")
                        .content(Utils.asJsonString(patientTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "http://localhost:8081/patient/add/5"))
                .andExpect(jsonPath("$.firstName", is("Jacob")))
                .andExpect(jsonPath("$.lastName", is("Boyd")))
                .andExpect(jsonPath("$.birthDate", is("1968-07-15")))
                .andDo(print());
    }

    @Test
    public void getListPatientsTest_thenReturnAnIterableOfPatient() throws Exception {
        //GIVEN
        List<Patient> patientsTests = Arrays.asList(
                new Patient(
                        1, "John", "Boyd", "1964-09-23", Gender.M, null, null),
                new Patient(
                        2, "Jacob", "Boyd", "1968-07-15", Gender.M, null, null),
                new Patient(
                        3, "Johanna", "Lefevre", "1970-09-08", Gender.F, null, null)
        );
        when(patientServiceMock.getPatients()).thenReturn(patientsTests);
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].firstName", is("Jacob")))
                .andExpect(jsonPath("$.[1].lastName", is("Boyd")))
                .andExpect(jsonPath("$.[1].birthDate", is("1968-07-15")))
                .andDo(print());

    }

    @Test
    public void getPatientByIdTest_whenPatientExistInDb_thenReturnPatientFound() throws Exception {
        //GIVEN
        when(patientServiceMock.findPatientById(anyInt())).thenReturn(patientTest);
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patient/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jacob")))
                .andExpect(jsonPath("$.lastName", is("Boyd")))
                .andExpect(jsonPath("$.birthDate", is("1968-07-15")))
                .andDo(print());
    }

    @Test
    public void getPatientByIdTest_whenPatientNotFoundInDb_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        when(patientServiceMock.findPatientById(anyInt())).thenThrow(new PatientNotFoundException("Patient not found"));
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patient/50"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
                .andExpect(result -> assertEquals("Patient not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void getPatientByLastNameTest_whenPatientExistInDb_thenReturnPatientFound() throws Exception {
        //GIVEN
        when(patientServiceMock.findPatientByLastName(anyString())).thenReturn(patientTest);
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patient/lastname/Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jacob")))
                .andExpect(jsonPath("$.lastName", is("Boyd")))
                .andExpect(jsonPath("$.birthDate", is("1968-07-15")))
                .andDo(print());
    }
  @Test
    public void getPatientByLastNameTest_whenPatientNotFoundInDb_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        when(patientServiceMock.findPatientByLastName(anyString())).thenThrow(new PatientNotFoundException("Patient not found"));
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patient/lastname/familyName"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
                .andExpect(result -> assertEquals("Patient not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void updatePatientTest_whenPatientRecodedInDb_thenReturnResponseEntityCreated() throws Exception {
        //GIVEN
        Patient patientToUpdate = new Patient(
                1, "Johnne", "Boyd", "1964-09-23", Gender.M, "1509 Culver St ,Culver 97451", "123-456-789");
        when(patientServiceMock.updatePatient(any(Patient.class))).thenReturn(patientToUpdate);
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/patient/update/1")
                        .content(Utils.asJsonString(patientToUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "http://localhost:8081/patient/update/1"))
                .andExpect(jsonPath("$.firstName", is("Johnne")))
                .andExpect(jsonPath("$.address", is("1509 Culver St ,Culver 97451")))
                .andExpect(jsonPath("$.phone", is("123-456-789")))
                .andDo(print());

    }

    @Test
    public void updatePatientTest_whenPatientNotRecodedInDb_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        when(patientServiceMock.updatePatient(any(Patient.class))).thenThrow(new PatientNotFoundException("Patient not found"));
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/patient/update/1")
                .content(Utils.asJsonString(patientTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
                .andExpect(result -> assertEquals("Patient not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }
}
