package com.clientui.controller;

import com.clientui.models.Gender;
import com.clientui.models.PatientClientUi;
import com.clientui.proxy.IMicroServicePatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class ClientUiController {

    @Autowired
    private IMicroServicePatientProxy patientProxy;

    @GetMapping(value = "/")
    public String showHomeView(Model model) {
        List<PatientClientUi> patients = patientProxy.listPatients();
        model.addAttribute("patients", patients);
        log.info("Controller - Displaying list of patients");
        return "patient/home";
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
            log.error("Controller - Has error in form");
            return "patient/update";
        }
        patientProxy.updatePatient(id, patientClientUi);
        model.addAttribute("patients", patientProxy.listPatients());
        log.info("Controller - Redirection to patient list after update");
        return "redirect:/";
    }
}
