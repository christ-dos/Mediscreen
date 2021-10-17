package com.clientui.IT;

import com.clientui.models.Gender;
import com.clientui.models.PatientClientUi;
import com.clientui.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class integration tests for {@link PatientClientUi}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PatientClientUiTestIT {
    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcPatientClientUi;

    private PatientClientUi patientClientUiTest;

    @BeforeEach
    public void setupPerTest() {
        patientClientUiTest = new PatientClientUi(2,"John", "Boyd", "1964-09-23", Gender.M,null,null);
    }

    @Test
    public void addPatientTest_whenPatientNotExitsInDbAndHasNoErrorInForm_thenReturnRedirectHome() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatientClientUi.perform(MockMvcRequestBuilders.post("/patient/add")
                        .content(Utils.asJsonString(patientClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("firstName","Jacob")
                        .param("lastName","Boyd")
                        .param("birthDate","1968-07-15")
                        .param("gender", String.valueOf(Gender.M)))
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
                .andExpect(model().attribute("patients", hasItem(hasProperty("firstName", is("Christ")))))
                .andExpect(model().attribute("patients", hasItem(hasProperty("lastName", is("Dos Santos")))))
                .andExpect(model().attribute("patients", hasItem(hasProperty("birthDate", is("1974-09-17")))))
                .andExpect(model().attribute("patients", hasItem(hasProperty("gender", is(Gender.F)))))
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
                        .param("firstName","Jacob")
                        .param("lastName","Boyd")
                        .param("birthDate","1968-07-15")
                        .param("gender", String.valueOf(Gender.M)))
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
