package com.clientui.controller;

import com.clientui.models.GenderEnum;
import com.clientui.models.PatientBean;
import com.clientui.proxy.IMicroServicePatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class ClientUiController {

    @Autowired
    private IMicroServicePatientProxy patientProxy;

    @GetMapping(value = "/")
    public String showHomeView(Model model){
        List<PatientBean> patients = patientProxy.listPatients();
        model.addAttribute("patients", patients);
        log.info("Controller - Displaying list of patients");
        return "patient/home";
    }

    @GetMapping(value = "/patient/add")
    public String showFormAddNewPatient(PatientBean patientBean, Model model){
        model.addAttribute("genders", GenderEnum.values());
        log.info("Controller - Displaying form for adding new patient");
        return "patient/add";
    }

    @PostMapping("/patient/add")
    public String addNewPatient(@Valid PatientBean patientBean, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.error("Controller: Has error in form");
            return "redirect:patient/add";
        }
        patientProxy.addPatient(patientBean);
        model.addAttribute("patients", patientProxy.listPatients());
        log.info("Controller: redirection to patient list");
        return "redirect:/";
    }
}
