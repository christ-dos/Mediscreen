package com.mediscreen.microservicepatient.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.microservicepatient.controller.PatientController;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        patientTest = new Patient("John", "Boyd", "1964-09-23", GenderEnum.M);

    }

    @Test
    public void addPatientTest_whenPatientNotExitsInDb_thenReturnPatientAdded() throws Exception {
        //GIVEN
        when(patientServiceMock.addPatient(any(Patient.class))).thenReturn(patientTest);
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/addPatient")
                        .content(Utils.asJsonString(patientTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.birthDate", is("1964-09-23")))
                .andDo(print());
    }
}
