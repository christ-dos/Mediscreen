package com.clientui.proxy;

import com.clientui.models.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "microservice-patient", url = "localhost:8081")
public interface MicroServicePatientProxy {

    @GetMapping(value ="/patients")
    List<PatientBean> listPatients();

    @GetMapping(value = "/patient/{id}")
    PatientBean getPatientById(@PathVariable("id") int id);

    @PutMapping(value = "/patient/update")
    ResponseEntity<PatientBean> updatePatient(@RequestBody PatientBean patientBean);

    }
