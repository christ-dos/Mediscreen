package com.clientui.controller;

import com.clientui.exception.PatientNotFoundException;
import com.clientui.models.DiabetesAssessmentClientUi;
import com.clientui.proxy.IMicroServiceReportDiabetesProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class ReportDiabetesControllerClientUi {

    @Autowired
    IMicroServiceReportDiabetesProxy reportDiabetesProxy;

    @GetMapping(value = "/assess")
    public String ShowDiabetesAssessmentView(@ModelAttribute("diabetesAssessmentClientUi") DiabetesAssessmentClientUi diabetesAssessmentClientUi) {

        log.info("Displaying View Diabetes Assessment by Id");
        return "diabetes-report/assessmentId";
    }

    @PostMapping(value = "/assess")
    public String submitPatientIdToGetDiabetesAssessmentByPatientId(@Valid DiabetesAssessmentClientUi diabetesAssessmentClientUi, BindingResult result, Model model) {
        DiabetesAssessmentClientUi diabetesAssessmentClientUiByPatientId = null;
        try {
            diabetesAssessmentClientUiByPatientId = reportDiabetesProxy.getDiabetesAssessmentByPatientId(diabetesAssessmentClientUi.getPatientId());
            model.addAttribute("diabetesAssessmentClientUi", diabetesAssessmentClientUiByPatientId);
        } catch (PatientNotFoundException ex) {
            log.error("Controller: Field patientId has error");
            result.rejectValue("patientId", "NotFound", ex.getMessage());
            return "diabetes-report/assessmentId";
        }

        log.debug("Controller - submit for obtain diabetes report with ID :" + diabetesAssessmentClientUi.getPatientId());
        return "redirect:/assess/" + diabetesAssessmentClientUiByPatientId.getPatientId();
    }

    @GetMapping(value = "/assess/{patientId}")
    public String getDiabetesAssessmentByPatientId(@Valid @PathVariable("patientId") int patientId, Model model) {
        DiabetesAssessmentClientUi diabetesAssessmentClientUi = reportDiabetesProxy.getDiabetesAssessmentByPatientId(patientId);
        model.addAttribute("diabetesAssessmentClientUi", diabetesAssessmentClientUi);

        log.debug("Controller - Get assessment by id:" + patientId);
        return "diabetes-report/assessmentId";
    }

    @GetMapping(value = "/assess/familyName")
    public String ShowDiabetesAssessmentViewByName(@Valid DiabetesAssessmentClientUi diabetesAssessmentClientUi) {

        log.info("Displaying View Diabetes Assessment by family name");
        return "diabetes-report/assessmentName";
    }

    @GetMapping(value = "/assess/familyName/{lastName}")
    public String getDiabetesAssessmentByFamilyName(@PathVariable("lastName") String lastName, Model model) {
        DiabetesAssessmentClientUi diabetesAssessmentClientUiByName = reportDiabetesProxy.getDiabetesAssessmentByFamilyName(lastName);
        model.addAttribute("diabetesAssessmentClientUi", diabetesAssessmentClientUiByName);

        log.debug("Controller - Get assessment by family name:" + lastName);
        return "diabetes-report/assessmentName";
    }


    @PostMapping(value = "/assess/familyName")
    public String submitFamilyNameToGetDiabetesAssessmentByFamilyName(@Valid DiabetesAssessmentClientUi diabetesAssessmentClientUi, BindingResult result, Model model) {
        DiabetesAssessmentClientUi diabetesAssessmentClientUiByName = null;
        if(result.hasErrors()){
            return "diabetes-report/assessmentName";
        }
        try {
            diabetesAssessmentClientUiByName = reportDiabetesProxy.getDiabetesAssessmentByFamilyName(diabetesAssessmentClientUi.getLastName());
            model.addAttribute("diabetesAssessmentClientUi", diabetesAssessmentClientUiByName);
        } catch (PatientNotFoundException ex) {
            log.error("Controller: Field lastName has error");
            result.rejectValue("lastName", "NotFound", ex.getMessage());
            return "diabetes-report/assessmentName";
        }

        log.debug("Controller - submit for obtain diabetes report with family name :" + diabetesAssessmentClientUi.getLastName());
        return "redirect:/assess/familyName/" + diabetesAssessmentClientUiByName.getLastName();
    }

}


