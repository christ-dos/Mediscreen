package com.clientui.controller;

import com.clientui.models.NotesClientUi;
import com.clientui.proxy.IMicroServiceHistoryPatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class NoteClientUiController {

    @Autowired
    IMicroServiceHistoryPatientProxy historyNotesPatientProxy;

    @GetMapping(value = "/notesPatient/{patientId}")
    public String showNotePatientHomeView(@PathVariable("patientId") Integer patientId, Model model) {
        Iterable<NotesClientUi> NotesPatient = historyNotesPatientProxy.getListNotesByPatient(patientId);
        model.addAttribute("NotesPatient", NotesPatient);
        log.info("Controller - Displaying list Notes of patient");
        return "patient/note-patient/listNote";
    }
}
