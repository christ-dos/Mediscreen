package com.clientui.IT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class integration tests for {@link ReportDiabetesUiIT}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ReportDiabetesUiIT {

    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcReportDiabetesClientUi;


    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenPatientExistInDb_thenReturnDiabetesAssessmentFound() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcReportDiabetesClientUi.perform(MockMvcRequestBuilders.get("/assess/1")
                        .param("patientId", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(view().name("diabetes-report/assessmentId"))
                .andExpect(model().attribute("diabetesAssessmentClientUi", hasProperty("firstName", is("Test"))))
                .andExpect(model().attribute("diabetesAssessmentClientUi", hasProperty("lastName", is("TestNone"))))
                .andExpect(model().attribute("diabetesAssessmentClientUi", hasProperty("age", is(54))))
                .andExpect(model().attribute("diabetesAssessmentClientUi", hasProperty("result", is("None"))))
                .andDo(print());
    }

    @Test
    public void submitPatientIdToGetDiabetesAssessmentByPatientId_whenPatientIdExist_thenReturnDiabetesAssessmentForPatient() throws Exception {
        //GIVEN
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
        //WHEN
        //THEN
        mockMvcReportDiabetesClientUi.perform(MockMvcRequestBuilders.post("/assess")
                        .param("patientId", String.valueOf(0)))
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("diabetesAssessmentClientUi", 1))
                .andExpect(model().attributeHasFieldErrorCode("diabetesAssessmentClientUi", "patientId", "NotFound"))
                .andDo(print());
    }


}
