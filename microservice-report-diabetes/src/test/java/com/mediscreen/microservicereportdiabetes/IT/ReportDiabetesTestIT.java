package com.mediscreen.microservicereportdiabetes.IT;

import com.mediscreen.microservicereportdiabetes.exception.PatientNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class integration tests for {@link com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
public class ReportDiabetesTestIT {

    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcReportDiabetes;

    @Test
    public void getDiabetesAssessmentByPatientId_whenPatientReportLessThanThirtyAndMale_thenReturnNone() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/assess/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("TestNone")))
                .andExpect(jsonPath("$.age", is(54)))
                .andExpect(jsonPath("$.result", is("None")))
                .andDo(print());
    }

    @Test
    public void getDiabetesAssessmentByPatientId_whenPatientReportLessThanThirtyAndMaleAndPatientNotFound_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/assess/0"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
                .andExpect(result -> assertEquals("Patient not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void getDiabetesAssessmentByFamilyName_whenPatientReportGreaterThanThirtyFemale_thenReturnBorderLIne() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/asses/familyName/Martin")
                        .param("lastName","Martin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("TestBorderline")))
                .andExpect(jsonPath("$.age", is(76)))
                .andExpect(jsonPath("$.result", is("Borderline")))
                .andDo(print());
    }

    @Test
    public void getDiabetesAssessmentByFamilyName_whenPatientReportNotFound_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/asses/familyName/Unknown"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
                .andExpect(result -> assertEquals("Patient not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

}
