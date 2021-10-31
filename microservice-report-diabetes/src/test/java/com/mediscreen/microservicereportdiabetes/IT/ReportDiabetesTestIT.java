package com.mediscreen.microservicereportdiabetes.IT;

import com.mediscreen.microservicereportdiabetes.exception.PatientNotFoundException;
import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;
import com.mediscreen.microservicereportdiabetes.model.NotesPatientReport;
import com.mediscreen.microservicereportdiabetes.model.PatientReport;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServiceHistoryPatientReportProxy;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServicePatientReportProxy;
import com.mediscreen.microservicereportdiabetes.service.ReportDiabetesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
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
public class ReportDiabetesTestIT {

    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcReportDiabetes;

    @Autowired
    private IMicroServicePatientReportProxy microServicePatientReportProxy;

    @Autowired
    private IMicroServiceHistoryPatientReportProxy microServiceHistoryPatientReportProxy;


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

        PatientReport gettedPatientReport = microServicePatientReportProxy.getPatientById(1);
        assertNotNull(gettedPatientReport);
        assertEquals("Test", gettedPatientReport.getFirstName());
        assertEquals("TestNone", gettedPatientReport.getLastName());
        assertEquals("1966-12-31", gettedPatientReport.getBirthDate());

        List<NotesPatientReport> gettedNotesPatien = (List<NotesPatientReport>) microServiceHistoryPatientReportProxy.getListNotesByPatient(1);
        assertEquals(2,gettedNotesPatien.size());
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

}
