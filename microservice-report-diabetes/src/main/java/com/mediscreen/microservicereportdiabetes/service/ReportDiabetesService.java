package com.mediscreen.microservicereportdiabetes.service;

import com.mediscreen.microservicereportdiabetes.exception.PatientNotFoundException;
import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;
import com.mediscreen.microservicereportdiabetes.model.Gender;
import com.mediscreen.microservicereportdiabetes.model.NotesPatientReport;
import com.mediscreen.microservicereportdiabetes.model.PatientReport;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServiceHistoryPatientReportProxy;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServicePatientReportProxy;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportDiabetesService implements IReportDiabetesService {

    private IMicroServiceHistoryPatientReportProxy microServiceHistoryPatientReportProxy;

    private IMicroServicePatientReportProxy microServicePatientReportProxy;

    @Autowired
    public ReportDiabetesService(IMicroServiceHistoryPatientReportProxy microServiceHistoryPatientReportProxy, IMicroServicePatientReportProxy microServicePatientReportProxy) {
        this.microServiceHistoryPatientReportProxy = microServiceHistoryPatientReportProxy;
        this.microServicePatientReportProxy = microServicePatientReportProxy;
    }

    @Override
    public DiabetesAssessment getDiabetesAssessmentByPatientId(int patientId) {
        PatientReport patientReport = microServicePatientReportProxy.getPatientById(patientId);
        if(patientReport == null){
            throw new PatientNotFoundException("Patient not Found with ID: " + patientId);
        }
        String resultDiabetesAssessment = getDiabetesAssessment(patientReport);
        return new DiabetesAssessment(patientId,patientReport.getFirstName(),
                patientReport.getLastName(), getAge(patientReport.getBirthDate()), resultDiabetesAssessment);

    }

    @Override
    public DiabetesAssessment getDiabetesAssessmentByFamilyName(String lastName) {
        PatientReport patientReport = microServicePatientReportProxy.getPatientByLastName(lastName);
        if(patientReport == null){
            throw new PatientNotFoundException("Patient not Found with lastName: " + lastName);
        }
        String resultDiabetesAssessment = getDiabetesAssessment(patientReport);
        return new DiabetesAssessment(patientReport.getFirstName(),
                patientReport.getLastName(), getAge(patientReport.getBirthDate()), resultDiabetesAssessment);
    }

    private String getDiabetesAssessment(PatientReport patientReport) {
        String diabetesAssessment;

        List<String> notesByPatient = getListNoteByPatientId(patientReport);
        int counterTrigger = getCounterWordsTriggerInNotes(notesByPatient);
        int agePatient = getAge(patientReport.getBirthDate());

        if (agePatient < 30) {
            if (patientReport.getGender().equals(Gender.M)) {
                //for patient male with less 30 years
                if (counterTrigger >= 3 & counterTrigger < 5) {
                    diabetesAssessment = "In Danger";
                } else if (counterTrigger >= 5) {
                    diabetesAssessment = "Early On Set";
                } else {
                    diabetesAssessment = "None";
                }
            } else {
                //for patient female with less 30 years
                if (counterTrigger >= 4 & counterTrigger < 7) {
                    diabetesAssessment = "In Danger";
                } else if (counterTrigger >= 7) {
                    diabetesAssessment = "Early On Set";
                } else {
                    diabetesAssessment = "None";
                }
            }
        } else {
            // patient male or female greater than 30 years
            if (counterTrigger >= 2 & counterTrigger < 6) {
                diabetesAssessment = "Borderline";
            } else if (counterTrigger >= 6 & counterTrigger < 8) {
                diabetesAssessment = "In Danger";
            } else if (counterTrigger >= 8) {
                diabetesAssessment = "Early On Set";
            } else {
                diabetesAssessment = "None";
            }
        }
        log.info("Service - Diabetes Assessment is: " + diabetesAssessment);
        return diabetesAssessment;
    }

    private List<String> getListNoteByPatientId(PatientReport patientReport) {
        List<String> notesPatientReport = ((List<NotesPatientReport>) microServiceHistoryPatientReportProxy.getListNotesByPatient(patientReport.getId()))
                .stream().map(notePatientReport -> notePatientReport.getNote()).collect(Collectors.toList());
        return notesPatientReport;
    }

    private int getCounterWordsTriggerInNotes(List<String> notesPatientReportList) {
        List<String> listWordsTrigger = Arrays.asList("Hemoglobin", "Microalbumin", "Height", "Weight", "Smoker",
                "Abnormal", "Cholesterol", "Dizziness", "Relapse", "Reaction", "Antibodies");

        int counter = 0;
        for (String note : notesPatientReportList) {
            String[] noteSplits = note.split(" |,|:|\\.|/|\\'");
            for (String word : listWordsTrigger) {
                for (String noteSplit : noteSplits) {
                    if (noteSplit.equalsIgnoreCase(word))
                        counter++;
                }
            }
        }
        log.info("TriggerCounter: " + counter);//Todo retirer le log
        return counter;
    }

    private int getAge(String birthDate) {
        if (birthDate == null) {
        }
        if (birthDate != null) {
            LocalDate birthDateParse = LocalDate.parse(birthDate);
            LocalDate currentDate = LocalDate.now();
            if (currentDate.isAfter(birthDateParse)) {
                Years age = Years.yearsBetween(birthDateParse, currentDate);
                log.debug("DateUtils - Age calculated for birthDate: " + birthDate);
                log.info("age du patient:" + age.getYears());//Todo retirer le log
                return age.getYears();
            }
        }
        log.error("DateUtils - The birthdate is not valid");
        return 0;
    }

}
