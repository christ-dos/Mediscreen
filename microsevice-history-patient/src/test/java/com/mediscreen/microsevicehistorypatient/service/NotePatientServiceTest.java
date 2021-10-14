package com.mediscreen.microsevicehistorypatient.service;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import com.mediscreen.microsevicehistorypatient.repository.INotePatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * Class That test NotePatientService
 *
 * @author Christine Duarte
 */
@ExtendWith(MockitoExtension.class)
public class NotePatientServiceTest {

    private  NotePatientService notePatientServiceTest;

    @Mock
    INotePatientRepository notePatientRepositoryMock;

    private NotePatient notePatientTest;

    @BeforeEach
    public void setupPerTest() {
        notePatientServiceTest = new NotePatientService(notePatientRepositoryMock);
        notePatientTest = new NotePatient(1,"Patient:Mr Durant Recommendation: une recommendation test pour le patient 1", LocalDateTime.now());
    }

    @Test
    public void saveNotePatientTest_whenPatientIdIsOne_thenReturnNotePatientAdded() {
        //GIVEN
        when(notePatientRepositoryMock.save(isA(NotePatient.class))).thenReturn(notePatientTest);
        //WHEN
        NotePatient notePatientAdded = notePatientServiceTest.saveNotePatient(notePatientTest);
        //THEN
        assertNotNull(notePatientAdded);
        assertEquals(1, notePatientAdded.getPatientId());
        assertEquals("Patient:Mr Durant Recommendation: une recommendation test pour le patient 1"
                , notePatientAdded.getNote());
        verify(notePatientRepositoryMock, times(1)).save(any(NotePatient.class));
    }

    @Test
    public void findNotesByPatientIdTest_whenPatientIdIsOne_thenReturnAllNotesOfPatient() {
        //GIVEN
        List<NotePatient> listNotesPatient = new ArrayList<>(Arrays.asList(
                new NotePatient(1,"Patient Durand Recommendation:rien de nouveau sous le soleil", LocalDateTime.now()),
                new NotePatient(1,"Patient Durand Recommendation:une recommendation test pour le patient 1", LocalDateTime.now()),
                new NotePatient(1,"Patient Martin Recommendation:une recommendation test pour le patient 2", LocalDateTime.now())
        ));
        when(notePatientRepositoryMock.findAllByPatientId(anyInt())).thenReturn(listNotesPatient);
        //WHEN
        List<NotePatient> notesByPatientTest = (List<NotePatient>) notePatientServiceTest.findNotesByPatientId(1);
        //THEN
        assertTrue(notesByPatientTest.size() == 3);
        assertEquals(1, notesByPatientTest.get(0).getPatientId());
        assertEquals("Patient Durand Recommendation:rien de nouveau sous le soleil", notesByPatientTest.get(0).getNote());
        verify(notePatientRepositoryMock, times(1)).findAllByPatientId(anyInt());

    }

}
