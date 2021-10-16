package com.mediscreen.microsevicehistorypatient.service;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;

public interface INotePatientService {
    Iterable<NotePatient> findNotesByPatientId(int patientId);

    NotePatient saveNotePatient(NotePatient notePatient);

    NotePatient updateNotePatient(NotePatient notePatient);

}
