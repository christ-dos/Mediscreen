package com.mediscreen.microservicereportdiabetes.proxy;

import com.mediscreen.microservicereportdiabetes.model.PatientReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-patient", url = "localhost:8081")
public interface IMicroServicePatientReportProxy {

    @GetMapping(value = "/patient/{id}")
    PatientReport getPatientById(@PathVariable("id") int id);

    @GetMapping(value = "/patient/lastname/{lastName}")
    PatientReport getPatientByLastName(@PathVariable("lastName")String lastName);
}
