package com.clientui.controller;

import com.clientui.models.NotesClientUi;
import com.clientui.proxy.IMicroServiceHistoryPatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class NoteClientUiController {

    @Autowired
    IMicroServiceHistoryPatientProxy historyNotesPatientProxy;

    @GetMapping(value = "/patHistory/add/{patientId}")
    public String showFormAddNewNotePatient(@ModelAttribute("notesClientUi") NotesClientUi notesClientUi, @PathVariable("patientId") int patientId, Model model) {
        model.addAttribute("notesPatient", getListNotesByPatientId(patientId));
        log.info("Controller - Displaying list of notes by patient");
        return "note-patient/addNote";
    }

    @PostMapping(value = "/patHistory/add")
    public String addNoteToPatient(@Valid NotesClientUi notesClientUi, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("notesPatient", getListNotesByPatientId(notesClientUi.getPatientId()));
            log.error("Controller - Has error in form add note: " + result.getFieldError());
            return "note-patient/addNote";
        }
        historyNotesPatientProxy.addNotePatient(notesClientUi);
        model.addAttribute("notesPatient", getListNotesByPatientId(notesClientUi.getPatientId()));
        log.info("Controller - return list of notes patient after addition");

        return "note-patient/addNote";
    }

    @PostMapping(value = "/patHistory/update/{id}")
    public String updateNotePatient(@PathVariable("id") String id, @Valid NotesClientUi notesClientUi, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("notesPatient", getListNotesByPatientId(notesClientUi.getPatientId()));
            log.error("Controller - Has error in form update note");
            return "note-patient/updateNote";
        }
        historyNotesPatientProxy.updateNotePatient(notesClientUi, id);
        model.addAttribute("notesPatient", getListNotesByPatientId(notesClientUi.getPatientId()));
        log.debug("Controller - Note updated with ID: " + id);
        return "note-patient/addNote";
    }

    @GetMapping(value = "/patHistory/update/{id}")
    public String showFormUpdateNotePatient(@ModelAttribute("notesClientUi") NotesClientUi notesClientUi, Model model) {
        NotesClientUi notePatientClientUiById = historyNotesPatientProxy.getNotePatientById(notesClientUi.getId());
        model.addAttribute("notesPatient", getListNotesByPatientId(notesClientUi.getPatientId()));
        model.addAttribute("notesClientUi", notePatientClientUiById);
        log.info("Controller - Displaying form for updating a note of patient");
        return "note-patient/updateNote";
    }

    private Iterable<NotesClientUi> getListNotesByPatientId(int patientId) {
        return historyNotesPatientProxy.getListNotesByPatient(patientId);

    }

}
