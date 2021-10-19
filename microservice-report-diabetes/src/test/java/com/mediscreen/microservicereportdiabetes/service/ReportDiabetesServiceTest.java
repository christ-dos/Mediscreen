package com.mediscreen.microservicereportdiabetes.service;

import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;
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
import static org.mockito.ArgumentMatchers.anyString;
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
    private IMicroServiceHistoryPatientReportProxy microServiceHistoryPatientReportProxyMock;

    private PatientReport patientReportLessThanThirtyMaleTest;
    private PatientReport patientReportLessThanThirtyFemaleTest;

    private PatientReport patientReportGreaterThanThirtyMaleTest;
    private PatientReport patientReportGreaterThanThirtyFemaleTest;

    @BeforeEach
    public void setupPerTest() {
        reportDiabetesServiceTest = new ReportDiabetesService(microServiceHistoryPatientReportProxyMock, microServicePatientReportProxyMock);

        patientReportLessThanThirtyMaleTest = new PatientReport(1, "John", "Boyd", "2000-04-23", Gender.M, null, null);
        patientReportLessThanThirtyFemaleTest = new PatientReport(2, "laura", "Martin", "2003-05-27", Gender.F, null, null);

        patientReportGreaterThanThirtyFemaleTest = new PatientReport(3, "Johnna", "Leman", "1968-09-25", Gender.F, null, null);
        patientReportGreaterThanThirtyMaleTest = new PatientReport(4, "John", "Lefevre", "1964-09-23", Gender.M, null, null);
    }

/*---------------------------------------------------------------------------------------------------------------------------
                                Test get Diabetes assessment by patient id
------------------------------------------------------------------------------------------------------------------------------ */

    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenPatientFemaleAgeGreaterThanThirtyAndTriggersTwo_ThenReturnBorderline() {
        //GIVEN
        int id = 3;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, "une Chaîne avec MicroalBumin,", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, " pour vérifier le mot reaction/", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportGreaterThanThirtyFemaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("Borderline", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenMaleAgeGreaterThanThirtyTriggersSix_ThenReturnInDanger() {
        //GIVEN
        int id = 4;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " MicroalBumin, antibodies,dizziness", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " reaction/smoker height", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportGreaterThanThirtyMaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("In Danger", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenFemaleAgeGreaterThanThirtyTriggerEight_ThenReturnEarlyOnSet() {
        //GIVEN
        int id = 3;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, " MicroalBumin, antibodies,dizziness, relapse", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, " reaction/smoker height/weight ", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportGreaterThanThirtyFemaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("Early On Set", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenMaleAgeGreaterThanThirtyTriggersZero_ThenReturnNone() {
        //GIVEN
        int id = 4;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " une recommendation,", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " sans aucun trigger", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportGreaterThanThirtyMaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("None", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenMaleAgeLessThanThirtyTriggersTwo_ThenReturnNone() {
        //GIVEN
        int id = 1;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " une recommendation avec microalbumin,", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " et smoker comme triggers", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyMaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("None", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentTest_whenMaleAgeLessThanThirtyTriggersNine_ThenReturnEarlyOnSet() {
        //GIVEN
        int id = 1;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " une recommendation avec microalbumin, weight, height,dizziness", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " et smoker relapse/antibodies reaction /abnormal comme triggers", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyMaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("Early On Set", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenMaleAgeLessThanThirtyTriggersOne_ThenReturnNone() {
        //GIVEN
        int id = 1;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " une recommendation avec dizziness", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " le seul trigger dans les notes ", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyMaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("None", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenFemaleAgeLessThanThirtyTriggersThree_ThenReturnNone() {
        //GIVEN
        int id = 2;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " une recommendation avec dizziness, smoker et weight", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " les trois triggers dans les notes ", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyFemaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("None", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenFemaleAgeLessThanThirtyTriggersFive_ThenReturnInDanger() {
        //GIVEN
        int id = 2;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " une recommendation avec dizziness, smoker et weight reaction", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " les cinq triggers dans les notes,relapse ", null));
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyFemaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("In Danger", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientIdTest_whenFemaleAgeLessThanThirtyTriggersSeven_ThenReturnEarlyOnSet() {
        //GIVEN
        int id = 2;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " une recommendation avec dizziness, smoker et weight reaction", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " les sept triggers dans les notes,relapse/antibodies cholesterol ", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientById(anyInt())).thenReturn(patientReportLessThanThirtyFemaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByPatientId(id);
        //THEN
        assertEquals("Early On Set", diabetesAssessmentResult.getResult());
    }

    /*---------------------------------------------------------------------------------------------------------------------------
                                    Test get Diabetes assessment by family name
    ------------------------------------------------------------------------------------------------------------------------------ */
    @Test
    public void getDiabetesAssessmentByFamilyNameTest_whenPatientFemaleAgeGreaterThanThirtyAndTriggersTwo_ThenReturnBorderline() {
        //GIVEN
        int id = 3;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, "une Chaîne avec MicroalBumin,", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 3, " pour vérifier le mot reaction/", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientByLastName(anyString())).thenReturn(patientReportGreaterThanThirtyFemaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByFamilyName("Boyd");
        //THEN
        assertEquals("Borderline", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientFamilyNameTest_whenMaleAgeGreaterThanThirtyTriggersSix_ThenReturnInDanger() {
        //GIVEN
        int id = 4;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " MicroalBumin, antibodies,dizziness", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 4, " reaction/smoker height", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientByLastName(anyString())).thenReturn(patientReportGreaterThanThirtyMaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByFamilyName("Lefevre");
        //THEN
        assertEquals("In Danger", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientFamilyName_whenMaleAgeLessThanThirtyTriggersNine_ThenReturnEarlyOnSet() {
        //GIVEN
        int id = 1;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " une recommendation avec microalbumin, weight, height,dizziness", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 1, " et smoker relapse/antibodies reaction /abnormal comme triggers", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientByLastName(anyString())).thenReturn(patientReportLessThanThirtyMaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByFamilyName("Boyd");
        //THEN
        assertEquals("Early On Set", diabetesAssessmentResult.getResult());
    }

    @Test
    public void getDiabetesAssessmentByPatientFamilyNameTest_whenFemaleAgeLessThanThirtyTriggersThree_ThenReturnNone() {
        //GIVEN
        int id = 2;
        List<NotesPatientReport> notesPatientReportList = Arrays.asList(
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " une recommendation avec dizziness, smoker et weight", null),
                new NotesPatientReport("6169f8db6b9ef56480237003", 2, " les trois triggers dans les notes ", null)
        );
        when(microServiceHistoryPatientReportProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesPatientReportList);
        when(microServicePatientReportProxyMock.getPatientByLastName(anyString())).thenReturn(patientReportLessThanThirtyFemaleTest);
        //WHEN
        DiabetesAssessment diabetesAssessmentResult = reportDiabetesServiceTest.getDiabetesAssessmentByFamilyName("Martin");
        //THEN
        assertEquals("None", diabetesAssessmentResult.getResult());
    }

}
