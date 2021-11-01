package com.clientui.proxy;

import com.clientui.models.NotesClientUi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * An Interface that manage requests send to microservice-history-patient
 *
 * @author Christine Duarte
 */
@FeignClient(name = "microservice-history-patient", url = "localhost:8082")
//@FeignClient(name = "microservice-history-patient", url = "microservice-history-patient-app:8082")
public interface IMicroServiceHistoryPatientProxy {

    @PostMapping(value = "/patHistory/add")
    ResponseEntity<NotesClientUi> addNotePatient(@Valid @RequestBody NotesClientUi notesClientUi);

    @GetMapping(value = "/notesPatient/{patientId}")
    Iterable<NotesClientUi> getListNotesByPatient(@PathVariable("patientId") Integer patientId);

    @PostMapping(value = "/patHistory/update/{id}")
    ResponseEntity<NotesClientUi> updateNotePatient(@Valid @RequestBody NotesClientUi notesClientUit, @PathVariable("id") String id);

    @GetMapping(value = "/note/{id}")
    NotesClientUi getNotePatientById(@PathVariable("id") String id);
}
