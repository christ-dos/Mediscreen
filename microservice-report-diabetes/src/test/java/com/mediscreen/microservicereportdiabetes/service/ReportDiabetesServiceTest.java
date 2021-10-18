package com.mediscreen.microservicereportdiabetes.service;

import com.mediscreen.microservicereportdiabetes.model.Gender;
import com.mediscreen.microservicereportdiabetes.model.NotesPatientReport;
import com.mediscreen.microservicereportdiabetes.model.PatientReport;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServiceHistoryPatientReportProxy;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServicePatientReportProxy;
import javafx.beans.binding.When;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Class That test {@link ReportDiabetesService}
 *
 * @author Christine Duarte
 */
@ExtendWith(MockitoExtension.class)
public class ReportDiabetesServiceTest {

    private ReportDiabetesService reportDiabetesServiceTest;

    @Mock
    private IMicroServicePatientReportProxy microServicePatientReportProxyMock;

    @Mock
    private IMicroServiceHistoryPatientReportProxy microServiceHistoryPatientReportProxy;

    private PatientReport patientReportTest;

    @BeforeEach
    public void setupPerTest() {
        reportDiabetesServiceTest = new ReportDiabetesService(microServiceHistoryPatientReportProxy, microServicePatientReportProxyMock);
        patientReportTest = new PatientReport(1, "John", "Boyd", "1964-09-23", Gender.M, null, null);
    }

    @Test
    public void get(){
        //GIVEN
        int id = 1;
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportTest);
        //WHEN
        PatientReport patientReportResult = reportDiabetesServiceTest.getInfoPatientById(id);
        //THEN
        assertEquals(1,patientReportResult.getId());
        assertEquals("John",patientReportResult.getFirstName());
        assertEquals("Boyd",patientReportResult.getLastName());
        assertEquals("1964-09-23",patientReportResult.getBirthDate());
        assertEquals(Gender.M,patientReportResult.getGender());
        verify(microServicePatientReportProxyMock, times(1)).getPatientById(anyInt());

    }

    @Test
    public void searchTriggerWordInNotesPatientTest(){
        //GIVEN
        int id = 3;
        List<NotesPatientReport> notesPatientReportList= Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, "une Chaîne avec MicroalBumin,",null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 3," pour vérifier le mot reaction/",null),
                new NotesPatientReport("6169f8db6b9ef56480237003",3, "dans les notes avec Antibodies/Hemoglobin A1C.",null)
                );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        //WHEN
        int counterResult = reportDiabetesServiceTest.searchTriggerWordInNotesPatient(id);
        //THEN
        assertEquals(4,counterResult);
    }

}
