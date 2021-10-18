package com.mediscreen.microservicereportdiabetes.service;

import com.mediscreen.microservicereportdiabetes.model.NotesPatientReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ReportDiabetesService {

    public void searchDeclancheur(List<NotesPatientReport> notesPatientReportList) {
        String chaine = "Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling terrific' Weight at or below recommended level";
        String[] list = chaine.split(" ");

        List<String> listString = Arrays.asList("weight", "height", "patient", "below", "level");
        int i = 0;
        for (String str : list) {
            for (String mot : listString) {
                if (str.equalsIgnoreCase(mot)) {
                    i++;
                }
            }
        }
        log.info("resultat:" + i);

    }

}
