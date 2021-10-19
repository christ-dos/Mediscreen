package com.mediscreen.microservicereportdiabetes.service;

import com.mediscreen.microservicereportdiabetes.model.Gender;
import com.mediscreen.microservicereportdiabetes.model.NotesPatientReport;
import com.mediscreen.microservicereportdiabetes.model.PatientReport;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServiceHistoryPatientReportProxy;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServicePatientReportProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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

    private PatientReport patientReportLessThanThirtyMaleTest;
    private PatientReport patientReportLessThanThirtyFemaleTest;

    private PatientReport patientReportGreaterThanThirtyMaleTest;
    private PatientReport patientReportGreaterThanThirtyFemaleTest;

    @BeforeEach
    public void setupPerTest() {
        reportDiabetesServiceTest = new ReportDiabetesService(microServiceHistoryPatientReportProxy, microServicePatientReportProxyMock);

        patientReportLessThanThirtyMaleTest = new PatientReport(1, "John", "Boyd", "2000-04-23", Gender.M, null, null);
        patientReportLessThanThirtyFemaleTest = new PatientReport(2, "laura", "Boyd", "2003-05-27", Gender.F, null, null);

        patientReportGreaterThanThirtyFemaleTest = new PatientReport(3, "Johnna", "Boyd", "1968-09-25", Gender.F, null, null);
        patientReportGreaterThanThirtyMaleTest = new PatientReport(4, "John", "Boyd", "1964-09-23", Gender.M, null, null);
    }


    @Test
    public void getDiabetesAssessmentTest_whenPatientFemaleAgeGreaterThanThirtyAndTriggersTwo_ThenReturnBorderline() {
        //GIVEN
        int id = 3;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, "une Chaîne avec MicroalBumin,", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, " pour vérifier le mot reaction/", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportGreaterThanThirtyFemaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("Borderline", diabetesAssessmentResult);
    }

    @Test
    public void getDiabetesAssessmentTest_whenMaleAgeGreaterThanThirtyTriggersSix_ThenReturnInDanger() {
        //GIVEN
        int id = 4;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " MicroalBumin, antibodies,dizziness", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " reaction/smoker height", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportGreaterThanThirtyMaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("In Danger", diabetesAssessmentResult);
    }

    @Test
    public void getDiabetesAssessmentTest_whenFemaleAgeGreaterThanThirtyTriggerEight_ThenReturnEarlyOnSet() {
        //GIVEN
        int id = 3;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, " MicroalBumin, antibodies,dizziness, relapse", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, " reaction/smoker height/weight ", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportGreaterThanThirtyFemaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("Early On Set", diabetesAssessmentResult);
    }

    @Test
    public void getDiabetesAssessmentTest_whenMaleAgeGreaterThanThirtyTriggersZero_ThenReturnNone() {
        //GIVEN
        int id = 4;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " une recommendation,", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " sans aucun trigger", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportGreaterThanThirtyMaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("None", diabetesAssessmentResult);
    }

    @Test
    public void getDiabetesAssessmentTest_whenMaleAgeLessThanThirtyTriggersTwo_ThenReturnNone() {
        //GIVEN
        int id = 1;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " une recommendation avec microalbumin,", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " et smoker comme triggers", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyMaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("None", diabetesAssessmentResult);
    }

    @Test
    public void getDiabetesAssessmentTest_whenMaleAgeLessThanThirtyTriggersNine_ThenReturnEarlyOnSet() {
        //GIVEN
        int id = 1;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " une recommendation avec microalbumin, weight, height,dizziness", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " et smoker relapse/antibodies reaction /abnormal comme triggers", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyMaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("Early On Set", diabetesAssessmentResult);
    }

 @Test
    public void getDiabetesAssessmentTest_whenMaleAgeLessThanThirtyTriggersOne_ThenReturnNone() {
        //GIVEN
        int id = 1;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " une recommendation avec dizziness", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " le seul trigger dans les notes ", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyMaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("None", diabetesAssessmentResult);
    }

@Test
    public void getDiabetesAssessmentTest_whenFemaleAgeLessThanThirtyTriggersThree_ThenReturnNone() {
        //GIVEN
        int id = 2;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " une recommendation avec dizziness, smoker et weight", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " les trois triggers dans les notes ", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyFemaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("None", diabetesAssessmentResult);
    }

@Test
    public void getDiabetesAssessmentTest_whenFemaleAgeLessThanThirtyTriggersFive_ThenReturnInDanger() {
        //GIVEN
        int id = 2;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " une recommendation avec dizziness, smoker et weight reaction", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " les cinq triggers dans les notes,relapse ", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyFemaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("In Danger", diabetesAssessmentResult);
    }

@Test
    public void getDiabetesAssessmentTest_whenFemaleAgeLessThanThirtyTriggersSeven_ThenReturnEarlyOnSet() {
        //GIVEN
        int id = 2;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " une recommendation avec dizziness, smoker et weight reaction", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " les sept triggers dans les notes,relapse/antibodies cholesterol ", null)
        );
        when(microServiceHistoryPatientReportProxy.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyFemaleTest);
        //WHEN
        String diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessment(id);
        //THEN
        assertEquals("Early On Set", diabetesAssessmentResult);
    }


}
