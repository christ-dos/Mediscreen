package com.mediscreen.microsevicehistorypatient.controller;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import com.mediscreen.microsevicehistorypatient.service.NotePatientService;
import com.mediscreen.microsevicehistorypatient.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class that test NotePatientController
 *
 * @author Christine Duarte
 */
@WebMvcTest(NotePatientController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class NotePatientControllerTest {
    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcPatient;

    @MockBean
    private NotePatientService notePatientServiceMock;

    private NotePatient notePatientTest;

    @BeforeEach
    public void setupPerTest() {
        notePatientTest = new NotePatient(1, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 1", null);
    }

    @Test
    public void addNotePatientTest_thenReturnResponseEntityCreated() throws Exception {
        //GIVEN
        when(notePatientServiceMock.saveNotePatient(any(NotePatient.class))).thenReturn(notePatientTest);
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/patHistory/add")
                        .content(Utils.asJsonString(notePatientTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "http://localhost:8082/patHistory/add/"))
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.note", is("Patient:Mr Durant Recommendation: une recommendation test pour le patient 1")))
                .andDo(print());
    }


    @Test
    public void getListNotesByPatientTest_thenReturnAnIterableOfNotesByPatient() throws Exception {
        //GIVEN
        List<NotePatient> listNotesPatient = new ArrayList<>(Arrays.asList(
                new NotePatient(1, "Patient Durand Recommendation:rien de nouveau sous le soleil", LocalDateTime.now()),
                new NotePatient(1, "Patient Durand Recommendation:une recommendation test pour le patient 1", LocalDateTime.now()),
                new NotePatient(1, "Patient Martin Recommendation:une recommendation test pour le patient 2", LocalDateTime.now())
        ));
        when(notePatientServiceMock.findNotesByPatientId(anyInt())).thenReturn(listNotesPatient);
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/notesPatient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].patientId", is(1)))
                .andExpect(jsonPath("$.[1].note", is("Patient Durand Recommendation:une recommendation test pour le patient 1")))
                .andDo(print());
    }

}
