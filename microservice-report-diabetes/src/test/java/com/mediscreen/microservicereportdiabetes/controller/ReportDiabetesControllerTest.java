package com.mediscreen.microservicereportdiabetes.controller;


import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;
import com.mediscreen.microservicereportdiabetes.model.Gender;
import com.mediscreen.microservicereportdiabetes.model.PatientReport;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServiceHistoryPatientReportProxy;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServicePatientReportProxy;
import com.mediscreen.microservicereportdiabetes.service.ReportDiabetesService;
import com.mediscreen.microservicereportdiabetes.utils.Utils;
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
    public void getDiabetesAssessmentByPatientId_whenPatientReportLessThanThirtyAndMale_thenReturn() throws Exception {
        //GIVEN
        DiabetesAssessment diabetesAssessmentTest = new DiabetesAssessment("John", "Boyd",24,"BorderLine");
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyMaleTest);
        when(reportDiabetesServiceMock.getDiabetesAssessmentByPatientId(anyInt())).thenReturn(diabetesAssessmentTest);
        //WHEN
        //THEN
        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/assess/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Boyd")))
                .andExpect(jsonPath("$.age", is(24)))
                .andExpect(jsonPath("$.result", is("BorderLine")))
                .andDo(print());
    }

    @Test
    public void getDiabetesAssessmentByFamilyName_whenPatientReportGreaterThanThirtyFemale_thenReturn() throws Exception {
        //GIVEN
        DiabetesAssessment diabetesAssessmentTest = new DiabetesAssessment("Johnna", "Leman",44,"InDanger");
        when(microServicePatientReportProxyMock.getPatientByLastName(anyString())).thenReturn(patientReportGreaterThanThirtyFemaleTest);
        when(reportDiabetesServiceMock.getDiabetesAssessmentByFamilyName(anyString())).thenReturn(diabetesAssessmentTest);
        //WHEN
        //THEN
        mockMvcReportDiabetes.perform(MockMvcRequestBuilders.get("/assess/familyName/Leman"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Johnna")))
                .andExpect(jsonPath("$.lastName", is("Leman")))
                .andExpect(jsonPath("$.age", is(44)))
                .andExpect(jsonPath("$.result", is("InDanger")))
                .andDo(print());
    }

}
