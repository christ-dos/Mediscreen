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

/**
 * Class that manage requests to the microservice-history-patient
 *
 * @author Christine Duarte
 */
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
            log.error("Controller - Has error in form add note: ");
            return "note-patient/addNote";
        }
        historyNotesPatientProxy.addNotePatient(notesClientUi);

        log.info("Controller - return list of notes patient after addition");
        return "redirect:/patHistory/add/" + notesClientUi.getPatientId();
    }

    @PostMapping(value = "/patHistory/update/{id}")
    public String updateNotePatient(@PathVariable("id") String id, @Valid NotesClientUi notesClientUi, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("notesPatient", getListNotesByPatientId(notesClientUi.getPatientId()));
            log.error("Controller - Has error in form update note");
            return "note-patient/updateNote";
        }
        historyNotesPatientProxy.updateNotePatient(notesClientUi, id);

        log.debug("Controller - Note updated with ID: " + id);
        return "redirect:/patHistory/add/" + notesClientUi.getPatientId();
    }

    @GetMapping(value = "/patHistory/update/{id}")
    public String showFormUpdateNotePatient(NotesClientUi notesClientUi, Model model) {
        NotesClientUi notePatientClientUiById = historyNotesPatientProxy.getNotePatientById(notesClientUi.getId());
        model.addAttribute("notesClientUi", notePatientClientUiById);
        model.addAttribute("notesPatient", getListNotesByPatientId(notePatientClientUiById.getPatientId()));

        log.info("Controller - Displaying form for updating a note of patient");
        return "note-patient/updateNote";
    }

    private Iterable<NotesClientUi> getListNotesByPatientId(int patientId) {
        return historyNotesPatientProxy.getListNotesByPatient(patientId);

    }

}
