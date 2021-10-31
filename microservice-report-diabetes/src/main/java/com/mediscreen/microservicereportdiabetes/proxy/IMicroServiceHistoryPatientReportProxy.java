package com.mediscreen.microservicereportdiabetes.proxy;

import com.mediscreen.microservicereportdiabetes.model.NotesPatientReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * An Interface that manage requests send to microservice-history-patient
 *
 * @author Christine Duarte
 */
@FeignClient(name = "microservice-history-patient", url = "localhost:8082")
//@FeignClient(name = "microservice-history-patient", url = "microservice-history-patient-app:8082")
public interface IMicroServiceHistoryPatientReportProxy {

    @GetMapping(value = "/notesPatient/{patientId}")
    Iterable<NotesPatientReport> getListNotesByPatient(@PathVariable("patientId") Integer patientId);


}
