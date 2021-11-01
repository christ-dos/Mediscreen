package com.mediscreen.microservicereportdiabetes.service;

import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;

/**
 * An Interface that exposes methods for {@link ReportDiabetesService}
 *
 * @author Christine Duarte
 */
public interface IReportDiabetesService {

    DiabetesAssessment getDiabetesAssessmentByPatientId(int patientId);
}
