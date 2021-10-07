package com.mediscreen.microservicepatient.IT;

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

import static org.hamcrest.CoreMatchers.is;
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
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/addPatient")
                        .content(Utils.asJsonString(patientTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.birthDate", is("1964-09-23")))
                .andDo(print());


    }
}
