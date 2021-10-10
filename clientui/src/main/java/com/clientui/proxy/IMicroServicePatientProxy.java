package com.clientui.proxy;

import com.clientui.models.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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


    @PutMapping(value = "/patient/update")
    ResponseEntity<PatientBean> updatePatient(@RequestBody PatientBean patientBean);

    @DeleteMapping(value = "/patient/delete/{id}")
    public String deletePatientById(@PathVariable("id") Integer id);

}
