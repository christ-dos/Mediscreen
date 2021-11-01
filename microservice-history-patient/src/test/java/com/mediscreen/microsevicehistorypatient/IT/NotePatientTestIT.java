package com.mediscreen.microsevicehistorypatient.IT;

import com.mediscreen.microsevicehistorypatient.exception.NotePatientNotFoundException;
import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import com.mediscreen.microsevicehistorypatient.repository.INotePatientRepository;
import com.mediscreen.microsevicehistorypatient.utils.Utils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class integration tests for {@link NotePatient}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NotePatientTestIT {

    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcNotesPatient;

    @Autowired
    private INotePatientRepository notePatientRepository;

    private NotePatient notePatientTest;

    @BeforeEach
    public void setupPerTest() {
        notePatientTest = new NotePatient("616af930902b1e0e219a8113", 1,"Patient:Mr Durant Recommendation: une recommendation test pour le patient 1",null);
        notePatientRepository.deleteAll();
    }

    @AfterEach
    public void afterAll() {

    }



    @Test
    public void addNotePatientTest_thenReturnResponseEntityCreated() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcNotesPatient.perform(MockMvcRequestBuilders.post("/patHistory/add")
                        .content(Utils.asJsonString(notePatientTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.note", is("Patient:Mr Durant Recommendation: une recommendation test pour le patient 1")))
                .andDo(print());

        long count = notePatientRepository.count();
        assertEquals(1, count);
    }

    @Test
    public void getListNotesByPatientTest_thenReturnAnIterableOfNotesByPatient() throws Exception {
        List<NotePatient> listNotesPatientId3 = Arrays.asList(
                new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3", null),
                new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3A", null),
                new NotePatient(3, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 3B", null),
                new NotePatient(5, "Patient:Mr Durant Recommendation: une recommendation test pour le patient 5", null)
        );
        // WHEN
        notePatientRepository.saveAll(listNotesPatientId3);
        //THEN
        mockMvcNotesPatient.perform(MockMvcRequestBuilders.get("/notesPatient/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[2].patientId", is(3)))
                .andExpect(jsonPath("$.[2].note", is("Patient:Mr Durant Recommendation: une recommendation test pour le patient 3B")))
                .andDo(print());

        List<NotePatient> notesByPatientId = (List<NotePatient>) notePatientRepository.findAllByPatientIdOrderByDateDesc(3);
        assertEquals(3, notesByPatientId.size());
    }

    @Test
    public void getNotePatientByIdTest_whenNotePatientExistInDb_thenReturnNotePatientFound() throws Exception {
        //GIVEN
        //WHEN
        NotePatient notePatientAdded = notePatientRepository.save(notePatientTest);
        String noteIdAdded = "616af930902b1e0e219a8113";
        //THEN
        mockMvcNotesPatient.perform(MockMvcRequestBuilders.get("/note/616af930902b1e0e219a8113"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(noteIdAdded)))
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.note", is("Patient:Mr Durant Recommendation: une recommendation test pour le patient 1")))
                .andDo(print());

        Optional<NotePatient> gettedNoteById = notePatientRepository.findById("616af930902b1e0e219a8113");
        assertTrue(gettedNoteById.isPresent());
        NotePatient note = gettedNoteById.get();
        assertEquals("616af930902b1e0e219a8113", note.getId());
        assertEquals(1, note.getPatientId());
        assertEquals("Patient:Mr Durant Recommendation: une recommendation test pour le patient 1", note.getNote());

    }

    @Test
    public void getNotePatientByIdTest_whenNotePatientNotFoundInDb_thenThrowNotePatientNotFoundException() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcNotesPatient.perform(MockMvcRequestBuilders.get("/note/aaaaaaaaaaaa")
                        .content(Utils.asJsonString(notePatientTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotePatientNotFoundException))
                .andExpect(result -> assertEquals("Note of patient not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void updateNotePatientTest_whenNotePatientExist_thenReturnResponseEntityCreated() throws Exception {
        //GIVEN
        NotePatient notePatientToUpdate = new NotePatient(
                "616af930902b1e0e219a8113",1,
                "Patient Durand Recommendation:une nouvelle note mise à jour pour le patient 1",null);
        //WHEN
        NotePatient notePatientAdded = notePatientRepository.save(notePatientTest);
        String noteIdAdded = "616af930902b1e0e219a8113";
        //THEN
        mockMvcNotesPatient.perform(MockMvcRequestBuilders.post("/patHistory/update/616af930902b1e0e219a8113")
                        .content(Utils.asJsonString(notePatientToUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "http://localhost:8082/patHistory/update/616af930902b1e0e219a8113"))
                .andExpect(jsonPath("$.id", is("616af930902b1e0e219a8113")))
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.note", is("Patient Durand Recommendation:une nouvelle note mise à jour pour le patient 1")))
                .andDo(print());

        Optional<NotePatient> gettedNoteById = notePatientRepository.findById("616af930902b1e0e219a8113");
        assertTrue(gettedNoteById.isPresent());
        NotePatient note = gettedNoteById.get();
        assertEquals("616af930902b1e0e219a8113", note.getId());
        assertEquals(1, note.getPatientId());
        assertEquals("Patient Durand Recommendation:une nouvelle note mise à jour pour le patient 1", note.getNote());

    }

    @Test
    public void updateNotePatientTest_whenNotePatientNotRecodedInDb_thenThrowNotePatientNotFoundException() throws Exception {
        //GIVEN
        notePatientTest = new NotePatient("0000000000000000000",10,"Patient:Mr Durant Recommendation: une recommendation test pour le patient 1",null);
        //WHEN
        //THEN
        mockMvcNotesPatient.perform(MockMvcRequestBuilders.post("/patHistory/update/0000000000000000000")
                        .content(Utils.asJsonString(notePatientTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotePatientNotFoundException))
                .andExpect(result -> assertEquals("The Note of patient to update not found",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }
}
