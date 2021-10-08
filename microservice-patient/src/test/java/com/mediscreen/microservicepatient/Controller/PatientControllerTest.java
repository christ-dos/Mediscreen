package com.mediscreen.microservicepatient.Controller;

import com.mediscreen.microservicepatient.controller.PatientController;
import com.mediscreen.microservicepatient.exception.PatientNotFoundException;
import com.mediscreen.microservicepatient.model.GenderEnum;
import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.repository.IPatientRepository;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @MockBean
    private IPatientRepository patientRepositoryMock;

    private Patient patientTest;

    @BeforeEach
    public void setupPerTest() {
        patientTest = new Patient(2, "Jacob", "Boyd", "1968-07-15", GenderEnum.M, null, null);

    }

    @Test
    public void addPatientTest_whenPatientNotExitsInDb_thenReturnPatientAdded() throws Exception {
        //GIVEN
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity responseEntity = new ResponseEntity(Utils.asJsonString(patientTest), header, 201);

        patientTest.setId(5);
        when(patientServiceMock.addPatient(any(Patient.class))).thenReturn(patientTest);
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/patient/add")
                        .content(Utils.asJsonString(patientTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
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
                        1, "John", "Boyd", "1964-09-23", GenderEnum.M, null, null),
                new Patient(
                        2, "Jacob", "Boyd", "1968-07-15", GenderEnum.M, null, null),
                new Patient(
                        3, "Johanna", "Lefevre", "1970-09-08", GenderEnum.F, null, null)
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
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patients/2"))
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
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patients/50")).
                andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
                .andExpect(result -> assertEquals("Patient not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());

    }
}
