package com.mediscreen.microservicereportdiabetes.controller;

import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;
import com.mediscreen.microservicereportdiabetes.service.IReportDiabetesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ReportDiabetesController {
    @Autowired
    private IReportDiabetesService reportDiabetesService;

    @GetMapping(value = "/assess/{id}")
    public DiabetesAssessment getDiabetesAssessmentByPatientId(@PathVariable("id") int id) {
        log.debug("Controller - get Diabetes assessment with  patient ID: " + id);
        return reportDiabetesService.getDiabetesAssessmentByPatientId(id);
    }

 @GetMapping(value = "/assess/familyName/{lastName}")
    public DiabetesAssessment getDiabetesAssessmentByFamilyName(@PathVariable("lastName") String lastName) {
        log.debug("Controller - get Diabetes assessment with family name: " + lastName);
        return reportDiabetesService.getDiabetesAssessmentByFamilyName(lastName);
    }

}
