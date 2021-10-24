package com.mediscreen.microservicereportdiabetes.service;

import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;

public interface IReportDiabetesService {

    DiabetesAssessment getDiabetesAssessmentByPatientId(int patientId);

//    DiabetesAssessment getDiabetesAssessmentByFamilyName(String lastName);
    //todo clean code
}