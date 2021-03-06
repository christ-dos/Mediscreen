package com.clientui.controller;

import com.clientui.exception.PatientNotFoundException;
import com.clientui.models.DiabetesAssessmentClientUi;
import com.clientui.proxy.IMicroServicePatientProxy;
import com.clientui.proxy.IMicroServiceReportDiabetesProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class that test {@link ReportDiabetesControllerClientUi}
 *
 * @author Christine Duarte
 */
@WebMvcTest(ReportDiabetesControllerClientUi.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ReportDiabetesControllerClientUiTest {

    @Autowired
    private MockMvc mockMvcReportDiabetesClientUi;

    @MockBean
    IMicroServiceReportDiabetesProxy reportDiabetesProxy;

    @MockBean
    IMicroServicePatientProxy patientProxyMock;

    private DiabetesAssessmentClientUi diabetesAssessmentClientUiTest;

    @BeforeEach
    public void setupPerTest() {
        diabetesAssessmentClientUiTest = new DiabetesAssessmentClientUi(1, "Bob", "Sinclar", 28, "InDanger");
    }


    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenPatientExistInDb_thenReturnDiabetesAssessmentFound() throws Exception {
        //GIVEN
        when(reportDiabetesProxy.getDiabetesAssessmentByPatientId(anyInt())).thenReturn(diabetesAssessmentClientUiTest);
        //WHEN
        //THEN
        mockMvcReportDiabetesClientUi.perform(MockMvcRequestBuilders.get("/assess/1")
                        .param("patientId", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(view().name("diabetes-report/assessmentId"))
                .andExpect(model().attribute("diabetesAssessmentClientUi", hasProperty("patientId", is(1))))
                .andExpect(model().attribute("diabetesAssessmentClientUi", hasProperty("firstName", is("Bob"))))
                .andExpect(model().attribute("diabetesAssessmentClientUi", hasProperty("lastName", is("Sinclar"))))
                .andExpect(model().attribute("diabetesAssessmentClientUi", hasProperty("age", is(28))))
                .andExpect(model().attribute("diabetesAssessmentClientUi", hasProperty("result", is("InDanger"))))
                .andDo(print());
    }

    @Test
    public void submitPatientIdToGetDiabetesAssessmentByPatientId_whenPatientIdExist_thenReturnDiadetesAssessmentForPatient() throws Exception {
        //GIVEN
        when(reportDiabetesProxy.getDiabetesAssessmentByPatientId(anyInt())).thenReturn(diabetesAssessmentClientUiTest);
        //WHEN
        //THEN
        mockMvcReportDiabetesClientUi.perform(MockMvcRequestBuilders.post("/assess")
                        .param("patientId", String.valueOf(1)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/assess/1"))
                .andExpect(redirectedUrl("/assess/1"))
                .andDo(print());
    }

    @Test
    public void submitPatientIdToGetDiabetesAssessmentByPatientId_whenDiabetesPatientIdNotExist_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        when(reportDiabetesProxy.getDiabetesAssessmentByPatientId(anyInt())).thenThrow(new PatientNotFoundException("patient Not Found"));
        //WHEN
        //THEN
        mockMvcReportDiabetesClientUi.perform(MockMvcRequestBuilders.post("/assess")
                        .param("patientId", String.valueOf(0)))
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("diabetesAssessmentClientUi",1))
                .andExpect(model().attributeHasFieldErrorCode("diabetesAssessmentClientUi","patientId","NotFound"))
                .andDo(print());
    }

}
