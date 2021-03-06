package com.clientui.controller;

import com.clientui.exception.PatientNotFoundException;
import com.clientui.models.DiabetesAssessmentClientUi;
import com.clientui.models.PatientClientUi;
import com.clientui.proxy.IMicroServicePatientProxy;
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
import java.util.List;

/**
 * Class that manage requests to the microservice-report-diabetes
 *
 * @author Christine Duarte
 */
@Controller
@Slf4j
public class ReportDiabetesControllerClientUi {

    @Autowired
    IMicroServiceReportDiabetesProxy reportDiabetesProxy;

    @Autowired
    IMicroServicePatientProxy patientProxy;


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
    public String getDiabetesAssessmentByPatientId(@Valid @PathVariable("patientId") int patientId,@ModelAttribute("diabetesAssessmentClientUi") DiabetesAssessmentClientUi diabetesAssessmentClientUi, Model model) {
        DiabetesAssessmentClientUi diabetesAssessmentClientUi1 = reportDiabetesProxy.getDiabetesAssessmentByPatientId(diabetesAssessmentClientUi.getPatientId());
        model.addAttribute("diabetesAssessmentClientUi", diabetesAssessmentClientUi1);
        List<PatientClientUi> patientsByName = patientProxy.getPatientsByLastName(diabetesAssessmentClientUi1.getLastName());
        model.addAttribute("patientsByName", patientsByName);
        log.debug("Controller - Get assessment by id:" + patientId);
        return "diabetes-report/assessmentId";
    }
}


