package com.clientui.controller;

import com.clientui.models.PatientBean;
import com.clientui.proxy.MicroServicePatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ClientUiController {

    @Autowired
    private MicroServicePatientProxy patientProxy;

    @GetMapping(value = "/")
    public String showHomeView(Model model){
        List<PatientBean> patients = patientProxy.listPatients();
        model.addAttribute("patients", patients);
        return "home";
    }

    @GetMapping(value = "/patient/add")
    public String addNewPatient(Model model){
        return "addPatient";
    }
}
