package com.mediscreen.microsevicehistorypatient.repository;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void findAllByPatientIdOrderByDateDescTest_thenReturnListNotesWithThreeElements() {
        // GIVEN
        List<NotePatient> listNotesPatientId3 = Arrays.asList(
                new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3", LocalDateTime.now().minusMinutes(15)),
                new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3A", LocalDateTime.now().minusMinutes(10)),
                new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3B", LocalDateTime.now().minusMinutes(5)),
                new NotePatient(5, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 5", LocalDateTime.now())
        );
        // WHEN
        notePatientRepositoryTest.saveAll(listNotesPatientId3);
        List<NotePatient> listAllNotes = notePatientRepositoryTest.findAll();
        List<NotePatient> notesByPatient = (List<NotePatient>) notePatientRepositoryTest.findAllByPatientIdOrderByDateDesc(3);
        // THEN
        assertTrue(notesByPatient.get(0).getNote().contains("3B"));
        assertEquals(4,listAllNotes.size());
        assertEquals(3, notesByPatient.size());
    }


    @Test
    public void saveNoteTest_thenReturnNoteSaved() {
        // GIVEN
        // WHEN
        List<NotePatient> notes = notePatientRepositoryTest.findAll();
        assertTrue(notes.isEmpty());

        NotePatient notePatientSaved = notePatientRepositoryTest.save(notePatientTest);
        List<NotePatient> notesAfterSaved = notePatientRepositoryTest.findAll();
        // THEN
        assertNotNull(notePatientSaved);
        assertEquals(3, notePatientSaved.getPatientId());
        assertEquals(notePatientTest.getNote(), notePatientSaved.getNote());
        assertEquals(1, notesAfterSaved.size());
    }
}
