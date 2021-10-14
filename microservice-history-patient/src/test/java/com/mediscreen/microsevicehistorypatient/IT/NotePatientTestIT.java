package com.mediscreen.microsevicehistorypatient.IT;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import com.mediscreen.microsevicehistorypatient.service.NotePatientService;
import com.mediscreen.microsevicehistorypatient.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    private MockMvc mockMvcPatient;

    private NotePatient notePatientTest;

    @BeforeEach
    public void setupPerTest() {
        notePatientTest = new NotePatient(1,"Patient:Mr Durant Recommendation: une recommendation test pour le patient 1",null);
    }

    @Test
    public void addNotePatientTest_thenReturnResponseEntityCreated() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.post("/patHistory/add")
                        .content(Utils.asJsonString(notePatientTest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.note", is("Patient:Mr Durant Recommendation: une recommendation test pour le patient 1")))
                .andDo(print());
    }

    @Test
    public void getListNotesByPatientTest_thenReturnAnIterableOfNotesByPatient() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcPatient.perform(MockMvcRequestBuilders.get("/notesPatient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].patientId", is(1)))
                .andExpect(jsonPath("$.[1].note", is("Patient:Mr Durant Recommendation: une recommendation test pour le patient 1")))
                .andDo(print());
    }

}
