package com.clientui.proxy;

import com.clientui.models.PatientClientUi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "microservice-patient", url = "localhost:8081")
public interface IMicroServicePatientProxy {

    @GetMapping(value = "/patients")
    List<PatientClientUi> listPatients();

    @GetMapping(value = "/patient/{id}")
    PatientClientUi getPatientById(@PathVariable("id") int id);


    @PostMapping(value = "/patient/add")
    ResponseEntity<PatientClientUi> addPatient(@RequestBody PatientClientUi patientClientUi);


    @PostMapping(value = "/patient/update/{id}")
    ResponseEntity<PatientClientUi> updatePatient(@PathVariable("id") Integer id, @RequestBody PatientClientUi patientClientUi);

}
