package com.mediscreen.microsevicehistorypatient.service;

import com.mediscreen.microsevicehistorypatient.exception.NotePatientNotFoundException;
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
import java.util.Optional;

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

    private NotePatientService notePatientServiceTest;

    @Mock
    INotePatientRepository notePatientRepositoryMock;

    private NotePatient notePatientTest;

    @BeforeEach
    public void setupPerTest() {
        notePatientServiceTest = new NotePatientService(notePatientRepositoryMock);
        notePatientTest = new NotePatient("6169f7df2c0d9a754676809f",1, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 1", LocalDateTime.now());
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
                new NotePatient(1, "Patient Durand Recommendation:rien de nouveau sous le soleil", LocalDateTime.now()),
                new NotePatient(1, "Patient Durand Recommendation:une recommendation test pour le patient 1", LocalDateTime.now()),
                new NotePatient(1, "Patient Martin Recommendation:une recommendation test pour le patient 2", LocalDateTime.now())
        ));
        when(notePatientRepositoryMock.findAllByPatientIdOrderByDateDesc(anyInt())).thenReturn(listNotesPatient);
        //WHEN
        List<NotePatient> notesByPatientTest = (List<NotePatient>) notePatientServiceTest.findNotesByPatientId(1);
        //THEN
        assertEquals(3,notesByPatientTest.size());
        assertEquals(1, notesByPatientTest.get(0).getPatientId());
        assertEquals("Patient Durand Recommendation:rien de nouveau sous le soleil", notesByPatientTest.get(0).getNote());
        verify(notePatientRepositoryMock, times(1)).findAllByPatientIdOrderByDateDesc(anyInt());
    }

    @Test
    public void updateNotePatientTest_whenNoteExistInDB_thenReturnNoteOfPatientUpdated() {
        //GIVEN
        NotePatient notePatientToUpdate = new NotePatient("6169f7df2c0d9a754676809f",1, "Patient:Mr Durant Recommendation: une recommendation test updated", LocalDateTime.now());
        when(notePatientRepositoryMock.findById(anyString())).thenReturn(java.util.Optional.of(notePatientTest));
        when(notePatientRepositoryMock.save(isA(NotePatient.class))).thenReturn(notePatientToUpdate);
        //WHEN
        NotePatient notePatientUpdatedResult = notePatientServiceTest.updateNotePatient(notePatientTest);
        //THEN
        assertEquals("6169f7df2c0d9a754676809f", notePatientUpdatedResult.getId());
        assertEquals(1, notePatientUpdatedResult.getPatientId());
        assertEquals("Patient:Mr Durant Recommendation: une recommendation test updated",
                notePatientUpdatedResult.getNote());
        verify(notePatientRepositoryMock, times(1)).findById(anyString());
        verify(notePatientRepositoryMock, times(1)).save(any(NotePatient.class));
    }

    @Test
    public void updateNotePatientTest_whenNotePatientNotFoundInDb_thenThrowNotePatientNotFoundException() {
        //GIVEN
        when(notePatientRepositoryMock.findById(anyString())).thenReturn(Optional.empty());
        //WHEN
        //THEN
        assertThrows(NotePatientNotFoundException.class, () -> notePatientServiceTest.updateNotePatient(notePatientTest));
        verify(notePatientRepositoryMock, times(1)).findById(anyString());
        verify(notePatientRepositoryMock, times(0)).save(any(NotePatient.class));
    }

}
