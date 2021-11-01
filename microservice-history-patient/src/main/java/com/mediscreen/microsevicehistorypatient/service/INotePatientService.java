package com.mediscreen.microsevicehistorypatient.service;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;

/**
 * An Interface that exposes methods for {@link NotePatientService}
 *
 * @author Christine Duarte
 */
public interface INotePatientService {
    Iterable<NotePatient> findNotesByPatientId(int patientId);

    NotePatient saveNotePatient(NotePatient notePatient);

    NotePatient updateNotePatient(NotePatient notePatient);

    NotePatient getNotePatientById(String id);
}
