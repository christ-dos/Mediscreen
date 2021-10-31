package com.mediscreen.microsevicehistorypatient.repository;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class That test {@link INotePatientRepository}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@ActiveProfiles("test")
public class NotePatientRepositoryTest {

    @Autowired
    private INotePatientRepository notePatientRepositoryTest;

    private NotePatient notePatientTest;

    @BeforeEach
    public void setupPerTest() {
        notePatientTest = new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3", null);
        notePatientRepositoryTest.deleteAll();
    }

    @Test
    public void findAllByPatientIdOrderByDateDescTest_thenReturnListNotesWith5Elements() {
        // GIVEN
        List<NotePatient> listNotesPatientId3 = Arrays.asList(
                new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3", null),
                new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3A", null),
                new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3B", null),
                new NotePatient(5, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 5", null)
        );
        // WHEN
        notePatientRepositoryTest.saveAll(listNotesPatientId3);
        List<NotePatient> NotesByPatient = (List<NotePatient>) notePatientRepositoryTest.findAllByPatientIdOrderByDateDesc(3);
        // THEN
        assertEquals(3, NotesByPatient.size());
    }

    @Test
    public void saveNoteTest_thenReturnNoteSaved() {
        // GIVEN
        // WHEN
        NotePatient notePatientSaved = notePatientRepositoryTest.save(notePatientTest);
        List<NotePatient> notes = notePatientRepositoryTest.findAll();
        // THEN
        assertNotNull(notePatientSaved);
        assertEquals(3, notePatientSaved.getPatientId());
        assertEquals(notePatientTest.getNote(), notePatientSaved.getNote());
        assertEquals(1, notes.size());
    }


}