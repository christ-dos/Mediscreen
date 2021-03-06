package com.clientui.IT;

import com.clientui.exception.PatientNotFoundException;
import com.clientui.models.Gender;
import com.clientui.models.PatientClientUi;
import com.clientui.proxy.IMicroServicePatientProxy;
import com.clientui.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class integration tests for {@link PatientClientUi}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PatientClientUiTestIT {
    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcPatientClientUi;

    private PatientClientUi patientClientUiTest;

    @Autowired
    private IMicroServicePatientProxy microServicePatientProxy;

    @BeforeEach
    public void setupPerTest() {
        patientClientUiTest = new PatientClientUi(1,"Test", "TestNone", "1966-12-31", Gender.F,null,null);
    }

    @Test
    public void addPatientTest_whenPatientNotExitsInDbAndHasNoErrorInForm_thenReturnRedirectHome() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.post("/patient/add")
                        .content(Utils.asJsonString(patientClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("firstName","Test")
                        .param("lastName","TestNone")
                        .param("birthDate","1966-12-31")
                        .param("gender", String.valueOf(Gender.F)))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/"))
                .andDo(print());

    }

    @Test
    public void addPatientTest_whenHasErrorInFieldsForm_thenRedirectViewAdd() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.post("/patient/add")
                        .content(Utils.asJsonString(patientClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("firstName","")
                        .param("lastName","")
                        .param("birthDate","")
                        .param("gender", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(4))
                .andExpect(model().attributeHasFieldErrorCode("patientClientUi", "firstName", "Size"))
                .andExpect(model().attributeHasFieldErrorCode("patientClientUi", "lastName", "Size"))
                .andExpect(model().attributeHasFieldErrorCode("patientClientUi", "birthDate", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("patientClientUi", "gender", "NotNull"))
                .andDo(print());
    }

    @Test
    public void showHomeViewToGetListPatientsTest_thenReturnAnIterableOfPatient() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("patient/home"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", hasItem(hasProperty("firstName", is("Test")))))
                .andExpect(model().attribute("patients", hasItem(hasProperty("lastName", is("TestNone")))))
                .andExpect(model().attribute("patients", hasItem(hasProperty("birthDate", is("1966-12-31")))))
                .andExpect(model().attribute("patients", hasItem(hasProperty("gender", is(Gender.F)))))
                .andDo(print());

    }

    @Test
    public void showDiabetesAssessmentViewTest_thenReturnViewAssessmentId() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.get("/patients/lastname"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("diabetes-report/assessmentId"))
                .andExpect(model().attributeExists("patientClientUi"))
                .andExpect(model().attributeExists("diabetesAssessmentClientUi"))
                .andDo(print());
    }

    @Test
    public void getPatientsByLastNameTest_whenLastnameIsTestNoneAndExist_thenReturnPatientTestNone() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.get("/patients/lastname/TestNone"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("diabetes-report/assessmentId"))
                .andExpect(model().attributeExists("diabetesAssessmentClientUi"))
                .andExpect(model().attribute("patientsByName", hasItem(hasProperty("firstName", is("Test")))))
                .andExpect(model().attribute("patientsByName", hasItem(hasProperty("lastName", is("TestNone")))))
                .andExpect(model().attribute("patientsByName", hasItem(hasProperty("firstName", is("Test")))))
                .andExpect(model().attribute("patientsByName", hasItem(hasProperty("birthDate", is("1966-12-31")))))
                .andExpect(model().attribute("patientsByName", hasItem(hasProperty("address", is("1 Brookside St")))))
                .andDo(print());
    }

    @Test
    public void submitFormToSearchPatientByLastNameTest_whenPatientExits_thenReturnListPatientFound() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.post("/patients/lastname")
                        .param("lastName", "TestNone"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patients/lastname/TestNone"))
                .andExpect(header().stringValues("Location", "/patients/lastname/TestNone"))
                .andDo(print());
    }

    @Test
    public void submitFormToSearchPatientByLastNameTest_whenPatientNotExits_thenReturnPatientNotFoundException() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.post("/patients/lastname")
                        .param("lastName", "Casimir"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/home"))
                .andExpect(model().attributeExists("patientClientUi"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("patientClientUi", "lastName", "NotFound"))
                .andDo(print());
    }

    @Test
    public void updatePatientTest_whenPatientExitsInDbAndHasNoErrorInForm_thenRedirectHome() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.post("/patient/update/2")
                        .content(Utils.asJsonString(patientClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("firstName","Test")
                        .param("lastName","TestBorderlineUpdate")
                        .param("birthDate","1966-12-31")
                        .param("gender", String.valueOf(Gender.F)))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/"))
                .andDo(print());
    }

    @Test
    public void updatePatientTest_whenHasErrorInFieldsForm_thenRedirectViewUpdate() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.post("/patient/update/2")
                        .content(Utils.asJsonString(patientClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("firstName","")
                        .param("lastName","")
                        .param("birthDate","")
                        .param("gender", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(4))
                .andExpect(model().attributeHasFieldErrorCode("patientClientUi", "firstName", "Size"))
                .andExpect(model().attributeHasFieldErrorCode("patientClientUi", "lastName", "Size"))
                .andExpect(model().attributeHasFieldErrorCode("patientClientUi", "birthDate", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("patientClientUi", "gender", "NotNull"))
                .andDo(print());

    }
}
