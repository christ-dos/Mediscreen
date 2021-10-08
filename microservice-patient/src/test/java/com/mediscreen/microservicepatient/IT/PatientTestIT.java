package com.mediscreen.microservicepatient.IT;

import com.mediscreen.microservicepatient.exception.PatientNotFoundException;
import com.mediscreen.microservicepatient.model.GenderEnum;
import com.mediscreen.microservicepatient.model.Patient;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @BeforeEach
    public void setupPerTest() {
        patientTest = new Patient("John", "Boyd", "1964-09-23", GenderEnum.M);
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
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.birthDate", is("1964-09-23")))
                .andDo(print());
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
    }

    @Test
    public void getPatientByIdTest_whenPatientExistInDb_thenReturnPatientFound() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/patients/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Gérard")))
                .andExpect(jsonPath("$.lastName", is("Duhammel")))
                .andExpect(jsonPath("$.birthDate", is("2003-04-27")))
                .andDo(print());

    }

    @Test
    public void getPatientByIdTest_whenPatientNotFoundInDb_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
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
