package com.mediscreen.microservicereportdiabetes.controller;


import com.mediscreen.microservicereportdiabetes.exception.PatientNotFoundException;
import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;
import com.mediscreen.microservicereportdiabetes.model.Gender;
import com.mediscreen.microservicereportdiabetes.model.PatientReport;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServiceHistoryPatientReportProxy;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServicePatientReportProxy;
import com.mediscreen.microservicereportdiabetes.service.ReportDiabetesService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class that test {@link ReportDiabetesController}
 *
 * @author Christine Duarte
 */
@WebMvcTest(ReportDiabetesController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ReportDiabetesControllerTest {

    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcReportDiabetes;

    @MockBean
    private ReportDiabetesService reportDiabetesServiceMock;

    @MockBean
    private IMicroServicePatientReportProxy microServicePatientReportProxyMock;

    @MockBean
    private IMicroServiceHistoryPatientReportProxy microServiceHistoryPatientReportProxyMock;


    private PatientReport patientReportLessThanThirtyMaleTest;

    private PatientReport patientReportGreaterThanThirtyFemaleTest;

    @BeforeEach
    public void setupPerTest() {
        patientReportLessThanThirtyMaleTest = new PatientReport(1, "John", "Boyd", "2000-04-23", Gender.M, null, null);
        patientReportGreaterThanThirtyFemaleTest = new PatientReport(3, "Johnna", "Leman", "1968-09-25", Gender.F, null, null);
    }

    @Test
    public void getDiabetesAssessmentByPatientId_whenPatientReportGreaterThanThirtyFemaleTest_thenReturnNone() throws Exception {
        //GIVEN
        DiabetesAssessment diabetesAssessmentTest = new DiabetesAssessment(3,"Johnna", "Leman", 53, "None");
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportGreaterThanThirtyFemaleTest);
        when(reportDiabetesServiceMock.getDiabetesAssessmentByPatientId(anyInt())).thenReturn(diabetesAssessmentTest);
        //WHEN
        //THEN
        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/assess/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Johnna")))
                .andExpect(jsonPath("$.lastName", is("Leman")))
                .andExpect(jsonPath("$.age", is(53)))
                .andExpect(jsonPath("$.result", is("None")))
                .andDo(print());
    }

    @Test
    public void getDiabetesAssessmentByPatientId_whenPatientReportLessThanThirtyAndMaleAndPatientNotFound_thenThrowPatientNotFoundException() throws Exception {
        //GIVEN
        when(reportDiabetesServiceMock.getDiabetesAssessmentByPatientId(anyInt())).thenThrow(new PatientNotFoundException(("Patient not found")));
        //WHEN
        //THEN
        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/assess/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
                .andExpect(result -> assertEquals("Patient not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

//    @Test
//    public void getDiabetesAssessmentByFamilyName_whenPatientReportLessThanThirtyAndMaleAndPatientNotFound_thenReturnBorderline() throws Exception {
//        //GIVEN
//        DiabetesAssessment diabetesAssessmentTest = new DiabetesAssessment("John", "Boyd", 24, "BorderLine");
//        when(microServicePatientReportProxyMock.getPatientByLastName(anyString())).thenReturn(patientReportLessThanThirtyMaleTest);
//        when(reportDiabetesServiceMock.getDiabetesAssessmentByFamilyName(anyString())).thenReturn(diabetesAssessmentTest);
//        //WHEN
//        //THEN
//        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/asses/familyName/Boyd"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName", is("John")))
//                .andExpect(jsonPath("$.lastName", is("Boyd")))
//                .andExpect(jsonPath("$.age", is(24)))
//                .andExpect(jsonPath("$.result", is("BorderLine")))
//                .andDo(print());
//    }
//
//    @Test
//    public void getDiabetesAssessmentByFamilyName_whenPatientReportNotFound_thenThrowPatientNotFoundException() throws Exception {
//        //GIVEN
//        when(reportDiabetesServiceMock.getDiabetesAssessmentByFamilyName(anyString())).thenThrow(new PatientNotFoundException(("Patient not found")));
//
//        //WHEN
//        //THEN
//        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/asses/familyName/Unknown"))
//                .andExpect(status().isNotFound())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException))
//                .andExpect(result -> assertEquals("Patient not found",
//                        result.getResolvedException().getMessage()))
//                .andDo(print());
//    }
    //todo clean code
}
