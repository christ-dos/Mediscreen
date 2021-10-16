package com.clientui.controller;

import com.clientui.models.Gender;
import com.clientui.models.PatientClientUi;
import com.clientui.proxy.IMicroServicePatientProxy;
import com.clientui.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class that test ClientUiController
 *
 * @author Christine Duarte
 */
@WebMvcTest(PatientClientUiController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class PatientClientUiControllerTest {
    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcClientUi;

    @MockBean
    private IMicroServicePatientProxy microServicePatientProxyMock;

    private PatientClientUi patientClientUiTest;

    @BeforeEach
    public void setupPerTest() {
        patientClientUiTest = new PatientClientUi(2, "Jacob", "Boyd", "1968-07-15", Gender.M, null, null);
    }

    @Test
    public void addPatientTest_whenPatientNotExitsInDbAndHasNoErrorInForm_thenReturnRedirectHome() throws Exception {
        //GIVEN
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                Utils.asJsonString(patientClientUiTest),
                header,
                HttpStatus.CREATED
        );
        when(microServicePatientProxyMock.addPatient(any(PatientClientUi.class))).thenReturn((ResponseEntity<PatientClientUi>) responseEntity);
        //WHEN
        //THEN
        mockMvcClientUi.perform(MockMvcRequestBuilders.post("/patient/add")
                        .content(Utils.asJsonString(patientClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("firstName", "Jacob")
                        .param("lastName", "Boyd")
                        .param("birthDate", "1968-07-15")
                        .param("gender", String.valueOf(Gender.M)))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/"))
                .andDo(print());
    }

    @Test
    public void addPatientTest_whenHasErrorInFieldsForm_thenRedirectViewAdd() throws Exception {
        //GIVEN
        patientClientUiTest.setId(2);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                Utils.asJsonString(patientClientUiTest),
                header,
                HttpStatus.CREATED
        );
        when(microServicePatientProxyMock.addPatient(any(PatientClientUi.class))).thenReturn((ResponseEntity<PatientClientUi>) responseEntity);
        //WHEN
        //THEN
        mockMvcClientUi.perform(MockMvcRequestBuilders.post("/patient/add")
                        .content(Utils.asJsonString(patientClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("birthDate", "")
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
        List<PatientClientUi> patientsTest = Arrays.asList(
                new PatientClientUi(
                        1, "John", "Boyd", "1964-09-23", Gender.M, null, null),
                new PatientClientUi(
                        2, "Jacob", "Boyd", "1968-07-15", Gender.M, null, null),
                new PatientClientUi(
                        3, "Johanna", "Lefevre", "1970-09-08", Gender.F, null, null)
        );
        when(microServicePatientProxyMock.listPatients()).thenReturn(patientsTest);
        //WHEN
        //THEN
        mockMvcClientUi.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("patient/home"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", hasItem(hasProperty("firstName", is("John")))))
                .andExpect(model().attribute("patients", hasItem(hasProperty("lastName", is("Boyd")))))
                .andExpect(model().attribute("patients", hasItem(hasProperty("birthDate", is("1964-09-23")))))
                .andExpect(model().attribute("patients", hasItem(hasProperty("gender", is(Gender.M)))))
                .andDo(print());

    }

    @Test
    public void updatePatientTest_whenPatientExitsInDbAndHasNoErrorInForm_thenRedirectHome() throws Exception {
        //GIVEN
        patientClientUiTest.setId(2);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                Utils.asJsonString(patientClientUiTest),
                header,
                HttpStatus.CREATED
        );
        when(microServicePatientProxyMock.updatePatient(anyInt(), any(PatientClientUi.class))).thenReturn((ResponseEntity<PatientClientUi>) responseEntity);
        //WHEN
        //THEN
        mockMvcClientUi.perform(MockMvcRequestBuilders.post("/patient/update/2")
                        .content(Utils.asJsonString(patientClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("firstName", "Jacob")
                        .param("lastName", "Boyd")
                        .param("birthDate", "1968-07-15")
                        .param("gender", String.valueOf(Gender.M)))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/"))
                .andDo(print());
    }

    @Test
    public void updatePatientTest_whenHasErrorInFieldsForm_thenRedirectViewUpdate() throws Exception {
        //GIVEN
        patientClientUiTest.setId(2);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                Utils.asJsonString(patientClientUiTest),
                header,
                HttpStatus.CREATED
        );
        when(microServicePatientProxyMock.updatePatient(anyInt(), any(PatientClientUi.class))).thenReturn((ResponseEntity<PatientClientUi>) responseEntity);
        //WHEN
        //THEN
        mockMvcClientUi.perform(MockMvcRequestBuilders.post("/patient/update/2")
                        .content(Utils.asJsonString(patientClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("birthDate", "")
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

