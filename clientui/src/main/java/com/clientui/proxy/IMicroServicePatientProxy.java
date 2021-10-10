package com.clientui.proxy;

import com.clientui.models.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Validated
@FeignClient(name = "microservice-patient", url = "localhost:8081")
public interface IMicroServicePatientProxy {

    @GetMapping(value = "/patients")
    List<PatientBean> listPatients();

    @GetMapping(value = "/patient/{id}")
    PatientBean getPatientById(@PathVariable("id") int id);


    @PostMapping(value = "/patient/add")
    ResponseEntity<PatientBean> addPatient(@Valid @RequestBody PatientBean patientBean);


    @PostMapping(value = "/patient/update/{id}")
    ResponseEntity<PatientBean> updatePatient(@PathVariable("id") Integer id, @RequestBody PatientBean patientBean);

    @GetMapping(value = "/patient/delete/{id}")
    public String deletePatientById(@PathVariable("id") Integer id);

}
