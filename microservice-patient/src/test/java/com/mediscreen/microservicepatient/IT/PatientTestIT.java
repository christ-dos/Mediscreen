package com.mediscreen.microservicepatient.IT;

import com.mediscreen.microservicepatient.exception.PatientNotFoundException;
import com.mediscreen.microservicepatient.model.Gender;
import com.mediscreen.microservicepatient.model.Patient;
import com.mediscreen.microservicepatient.repository.IPatientRepository;
import com.mediscreen.microservicepatient.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class integration tests for {@link Patient}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = {"/mediscreenTest.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class PatientTestIT {

    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcPatient;

    private Patient patientTest;

    @Autowired
    private IPatientRepository patientRepository;

    @BeforeEach
    public void setupPerTest() {
        patientTest = new Patient("John", "Boyd", "1964-09-23", Gender.M);
    }

    @Test
    public void addPatientTest_whenPatientNotExitsInDb_thenReturnPatientAdded() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/patient/add")
                        .content(Utils.asJsonString(patientTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.birthDate", is("1964-09-23")))
                .andDo(print());

        long count = patientRepository.count();
        assertEquals(6, count);
    }

    @Test
    public void getListPatientsTest_thenReturnAnIterableOfPatient() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].firstName", is("Christine")))
                .andExpect(jsonPath("$.[0].lastName", is("Deschamps")))
                .andExpect(jsonPath("$.[0].birthDate", is("1974-05-25")))
                .andDo(print());

        List patients = (List) patientRepository.findAll();
        assertEquals(5, patients.size());
    }

    @Test
    public void getPatientByIdTest_whenPatientExistInDb_thenReturnPatientFound() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patient/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Gérard")))
                .andExpect(jsonPath("$.lastName", is("Duhammel")))
                .andExpect(jsonPath("$.birthDate", is("2003-04-27")))
                .andDo(print());

        Optional<Patient> gettedPatient = patientRepository.findById(2);
        assertTrue(gettedPatient.isPresent());
        Patient patient = gettedPatient.get();
        assertEquals("Gérard", patient.getFirstName());
        assertEquals("Duhammel", patient.getLastName());
        assertEquals("2003-04-27", patient.getBirthDate());

    }

    @Test
    public void getPatientByIdTest_whenPatientNotFoundInDb_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patient/50")).
                andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
                .andExpect(result -> assertEquals("Patient not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());

    }

    @Test
    public void getPatientsByLastNameTest_whenPatientExistInDb_thenReturnListPatientFound() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patients/lastname/Martin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName", is("Wiliam")))
                .andExpect(jsonPath("$.[1].firstName", is("Stephan")))
                .andExpect(jsonPath("$.[2].firstName", is("Lili")))
                .andDo(print());

        long count = patientRepository.findByLastName("Martin").stream().count();
        assertEquals(3, count);
    }
    @Test
    public void getPatientsByLastNameTest_whenPatientNotFoundInDb_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patients/lastname/familyName"))
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
                1, "Leon", "Boyd", "1960-12-23", Gender.M, "1509 Culver St ,Culver 97451", "123-456-789");
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/patient/update/1")
                        .content(Utils.asJsonString(patientToUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "http://localhost:8081/patient/update/1"))
                .andExpect(jsonPath("$.firstName", is("Leon")))
                .andExpect(jsonPath("$.address", is("1509 Culver St ,Culver 97451")))
                .andExpect(jsonPath("$.phone", is("123-456-789")))
                .andDo(print());

        Optional<Patient> updatedPatient = patientRepository.findById(1);
        assertTrue(updatedPatient.isPresent());
        Patient patient = updatedPatient.get();
        assertEquals("Leon", patient.getFirstName());
        assertEquals("Boyd", patient.getLastName());
        assertEquals("1960-12-23", patient.getBirthDate());

    }

    @Test
    public void updatePatientTest_whenPatientNotRecodedInDb_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/patient/update/50")
                        .content(Utils.asJsonString(patientTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
                .andExpect(result -> assertEquals("Patient to update not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }
}
