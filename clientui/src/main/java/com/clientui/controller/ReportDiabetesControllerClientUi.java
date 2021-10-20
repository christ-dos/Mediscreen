package com.clientui.controller;

import com.clientui.models.DiabetesAssessmentClientUi;
import com.clientui.proxy.IMicroServiceReportDiabetesProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class ReportDiabetesControllerClientUi {

    @Autowired
    IMicroServiceReportDiabetesProxy reportDiabetesProxy;


    @GetMapping(value = "/assess")
    public String ShowDiabetesAssessmentView(DiabetesAssessmentClientUi diabetesAssessmentClientUi, Model model) {
        return "diabetes-report/assessmentId";
    }

    @GetMapping(value = "/assess/{patientId}")
    public String getDiabetesAssessmentByPatientId(@PathVariable("patientId") int patientId,Model model) {
        DiabetesAssessmentClientUi diabetesAssessmentClientUi = reportDiabetesProxy.getDiabetesAssessmentByPatientId(patientId);
        model.addAttribute("diabetesAssessmentClientUi", diabetesAssessmentClientUi);

//        String result = "Patient: " + diabetesAssessmentClientUi.getFirstName() + " "
//                + diabetesAssessmentClientUi.getLastName() + "(age " + diabetesAssessmentClientUi.getAge() +
//                ") diabetes assessment is: " + diabetesAssessmentClientUi.getResult();
        log.debug("Controller - Get assessment by id:" + patientId);
        return "diabetes-report/assessmentId";
    }

    @PostMapping(value = "/assess")
    public String submitIdToGetDiabetesAssessmentByPatientId(DiabetesAssessmentClientUi diabetesAssessmentClientUi, Model model) {
        DiabetesAssessmentClientUi diabetesAssessmentClientUi1 = reportDiabetesProxy.getDiabetesAssessmentByPatientId(diabetesAssessmentClientUi.getPatientId());
        model.addAttribute("diabetesAssessmentClientUi", diabetesAssessmentClientUi1);
        log.debug("Controller - submit for obtain diabetes report with ID :" + diabetesAssessmentClientUi.getPatientId());
        return "redirect:/assess/" + diabetesAssessmentClientUi1.getPatientId();
    }


    @GetMapping(value = "/assess/familyName/{lastName}")
    public String getDiabetesAssessmentByFamilyName(@PathVariable("lastName") String lastName, Model model) {

        log.debug("Controller - Get assessment by family name:" + lastName);
        return "assessmentId";
    }


}


