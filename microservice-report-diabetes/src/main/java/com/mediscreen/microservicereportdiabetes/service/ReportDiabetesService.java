package com.mediscreen.microservicereportdiabetes.service;

import com.mediscreen.microservicereportdiabetes.model.NotesPatientReport;
import com.mediscreen.microservicereportdiabetes.model.PatientReport;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServiceHistoryPatientReportProxy;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServicePatientReportProxy;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportDiabetesService {

    private IMicroServiceHistoryPatientReportProxy microServiceHistoryPatientReportProxy;

    private IMicroServicePatientReportProxy microServicePatientReportProxy;

    @Autowired
    public ReportDiabetesService(IMicroServiceHistoryPatientReportProxy microServiceHistoryPatientReportProxy, IMicroServicePatientReportProxy microServicePatientReportProxy) {
        this.microServiceHistoryPatientReportProxy = microServiceHistoryPatientReportProxy;
        this.microServicePatientReportProxy = microServicePatientReportProxy;
    }


    public PatientReport getInfoPatientById(int id) {

        return null;
    }

    public int searchTriggerWordInNotesPatient(int id) {
        PatientReport patientReport = microServicePatientReportProxy.getPatientById(id);
        List<String> notesPatientReport = ((List<NotesPatientReport>) microServiceHistoryPatientReportProxy.getListNotesByPatient(id))
                .stream().map(notesPatientReport1 -> notesPatientReport1.getNote()).collect(Collectors.toList());

        int counterTrigger = counterWordsTriggerInNotes(notesPatientReport);
        int agePatient = getAge(patientReport.getBirthDate());




        return counterTrigger;
    }

    private int counterWordsTriggerInNotes(List<String> notesPatientReportList) {
        List<String> listWordsTrigger = Arrays.asList("Hemoglobin", "Microalbumin", "Height", "Weight", "Smoker",
                "Abnormal", "Cholesterol", "Dizziness", "Relapse", "Reaction", "Antibodies");

        int counter = 0;
        for (String note : notesPatientReportList) {
            String[] noteSplits = note.split(" |,|:|\\.|/");
            for (String word : listWordsTrigger) {
                for (String noteSplit : noteSplits) {
                    if (noteSplit.equalsIgnoreCase(word))
                        counter++;
                }
            }
        }
        return counter;
    }

    private int getAge(String birthDate) {
        if (birthDate != null) {
            LocalDate birthDateParse = LocalDate.parse(birthDate);
            LocalDate currentDate = LocalDate.now();
            if (currentDate.isAfter(birthDateParse)) {
                Years age = Years.yearsBetween(birthDateParse,currentDate);
                log.debug("DateUtils - Age calculated for birthDate: " + birthDate);
                return age.getYears();
            }
        }
        log.error("DateUtils - The birthdate is not valid");
        return 0;
    }

}
