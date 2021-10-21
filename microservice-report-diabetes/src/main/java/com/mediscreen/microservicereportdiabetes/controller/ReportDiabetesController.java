package com.mediscreen.microservicereportdiabetes.controller;

import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;
import com.mediscreen.microservicereportdiabetes.service.IReportDiabetesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Class taht expose endpoints for microservice-report-diabetes
 *
 * @author Christine Duarte
 */
@RestController
@Slf4j
public class ReportDiabetesController {
    @Autowired
    private IReportDiabetesService reportDiabetesService;

    @GetMapping(value = "/assess/{patientId}")
    public DiabetesAssessment getDiabetesAssessmentByPatientId(@Valid @PathVariable("patientId") int patientId) {
        log.debug("Controller - get Diabetes assessment with patient ID: " + patientId);
        return reportDiabetesService.getDiabetesAssessmentByPatientId(patientId);
    }

    @GetMapping(value = "/asses/familyName/{lastName}")
    public DiabetesAssessment getDiabetesAssessmentByFamilyName(@Valid @PathVariable("lastName") String lastName) {
        log.debug("Controller - get Diabetes assessment with family name: " + lastName);
        return reportDiabetesService.getDiabetesAssessmentByFamilyName(lastName);
    }

}
