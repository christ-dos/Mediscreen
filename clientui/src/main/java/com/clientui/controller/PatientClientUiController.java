package com.clientui.controller;

import com.clientui.exception.PatientNotFoundException;
import com.clientui.models.DiabetesAssessmentClientUi;
import com.clientui.models.Gender;
import com.clientui.models.PatientClientUi;
import com.clientui.proxy.IMicroServicePatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class PatientClientUiController {

    @Autowired
    private IMicroServicePatientProxy patientProxy;


    @GetMapping(value = "/")
    public String showPatientHomeView(@ModelAttribute("patientClientUi") PatientClientUi patientClientUi, Model model) {

        List<PatientClientUi> patients = patientProxy.listPatients();
        model.addAttribute("patients", patients);
        log.info("Controller - Displaying list of patients");
        return "patient/home";
    }

    @GetMapping(value = "/patients/lastname")
    public String ShowDiabetesAssessmentView(@ModelAttribute("patientClientUi") PatientClientUi patientClientUi, @ModelAttribute("diabetesAssessmentClientUi") DiabetesAssessmentClientUi diabetesAssessmentClientUi) {

        log.info("Displaying View Diabetes Assessment by Id");
        return "diabetes-report/assessmentId";
    }

    @GetMapping(value = "/patients/lastname/{lastName}")
    public String getPatientsByLastName(@Valid @PathVariable("lastName") String lastName, DiabetesAssessmentClientUi diabetesAssessmentClientUi, Model model) {
        List<PatientClientUi> patientsByName = patientProxy.getPatientsByLastName(lastName);
        model.addAttribute("patientsByName", patientsByName);

        log.info("Displaying List of patients by name");
        return "diabetes-report/assessmentId";
    }

    @PostMapping(value = "/patients/lastname")
    public String submitFormToSearchPatientByLastName(@Valid String lastName,@Valid PatientClientUi patientClientUi, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.error("Controller - Has error in form");
            return "patient/home";
        }
        List<PatientClientUi> patientsByName = null;
        try {
            patientsByName = patientProxy.getPatientsByLastName(lastName);
        } catch (PatientNotFoundException ex) {
            model.addAttribute("patients", patientProxy.listPatients());
            log.error("Controller: Field lastName has error");
            result.rejectValue("lastName", "NotFound", ex.getMessage());
            return "patient/home";
        }
        model.addAttribute("patientsByName", patientsByName);
        log.info("submit lastName to get  List of patients by name");
        return "redirect:/patients/lastname/" + lastName;
    }

    @GetMapping(value = "/patient/add")
    public String showFormAddNewPatient(PatientClientUi patientClientUi,Model model) {
        model.addAttribute("genders", Gender.values());
        log.info("Controller - Displaying form for adding new patient");
        return "patient/add";
    }

    @PostMapping("/patient/add")
    public String addNewPatient(@Valid PatientClientUi patientClientUi, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("genders", Gender.values());
            log.error("Controller - Has error in form");
            return "/patient/add";
        }
        patientProxy.addPatient(patientClientUi);
        model.addAttribute("patients", patientProxy.listPatients());
        log.info("Controller - redirection to patient list after addition patient");
        return "redirect:/";
    }

    @GetMapping(value = "/patient/update/{id}")
    public String showFormUpdatePatient(PatientClientUi patientClientUi, Model model) {
        PatientClientUi patientClientUiToUpdate = patientProxy.getPatientById(patientClientUi.getId());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("patientClientUi", patientClientUiToUpdate);
        log.info("Controller - Displaying form for updating a patient");
        return "patient/update";
    }

    @PostMapping(value = "/patient/update/{id}")
    public String updatePatient( @PathVariable("id") Integer id,@Valid PatientClientUi patientClientUi, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("genders", Gender.values());
            log.error("Controller - Has error in form update patient");
            return "patient/update";
        }
        patientProxy.updatePatient(id, patientClientUi);
        model.addAttribute("patients", patientProxy.listPatients());
        log.info("Controller - Redirection to patient list after update");
        return "redirect:/";
    }
}
