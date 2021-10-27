package com.mediscreen.microservicereportdiabetes.proxy;

import com.mediscreen.microservicereportdiabetes.model.NotesPatientReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-history-patient", url = "microservice-history-patient-app:8082")
public interface IMicroServiceHistoryPatientReportProxy {

    @GetMapping(value = "/notesPatient/{patientId}")
    Iterable<NotesPatientReport> getListNotesByPatient(@PathVariable("patientId") Integer patientId);


}
