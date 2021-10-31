package com.clientui.IT;

import com.clientui.models.NotesClientUi;
import com.clientui.proxy.IMicroServiceHistoryPatientProxy;
import com.clientui.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class integraton tests for {@link com.clientui.models.NotesClientUi}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NoteClientUiTestIT {

    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcNoteClientUi;

    private NotesClientUi notesClientUiTest;

    @Autowired
    private IMicroServiceHistoryPatientProxy microServiceHistoryPatientProxy;


    @BeforeEach
    public void setupPerTest() {
        notesClientUiTest = new NotesClientUi("6169f7df2c0d9a754676809f", 1, "Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling terrific' Weight at or below recommended level", null);

    }

    @Test
    public void getListNotesByPatientTest_thenReturnAnIterableOfNotesByPatient() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.get("/patHistory/add/2"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("note-patient/addNote"))
                .andExpect(model().attributeExists("notesClientUi"))
                .andExpect(model().attributeExists("notesPatient"))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("patientId", is(2)))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("note", is(
                        "Patient: TestBorderline Practitioner's notes/recommendations: Patient states that they have had a Reaction to medication within last 3 months Patient also complains that their hearing continues to be problematic")))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("note", is(
                        "Patient: TestBorderline Practitioner's notes/recommendations: Patient states that they are feeling a great deal of stress at work Patient also complains that their hearing seems Abnormal as of late")))))
                .andDo(print());

        List<NotesClientUi> notesClientUIByPatientId = (List<NotesClientUi>) microServiceHistoryPatientProxy.getListNotesByPatient(2);
        assertEquals(2, notesClientUIByPatientId.size());
    }

    @Test
    public void updateNotePatientTest_whenNotePatientExitsInDbAndHasNoErrorInForm_thenReturnToViewAddNote() throws Exception {
        //GIVEN
        //WHEN
        microServiceHistoryPatientProxy.addNotePatient(notesClientUiTest);
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.post("/patHistory/update/6169f7df2c0d9a754676809f")
                        .content(Utils.asJsonString(notesClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("id", "6169f7df2c0d9a754676809f")
                        .param("patientId", String.valueOf(1))
                        .param("note", notesClientUiTest.getNote()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patHistory/add/1"))
                .andExpect(redirectedUrl("/patHistory/add/1"))
                .andDo(print());
    }

    @Test
    public void updateNotePatientTest_whenNotePatientHasErrorInForm_thenReturnToUpdateNote() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.post("/patHistory/update/6169f7df2c0d9a754676809f")
                        .content(Utils.asJsonString(notesClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("id", "")
                        .param("patientId", String.valueOf(0))
                        .param("note", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("notesClientUi", "note", "NotBlank"))
                .andDo(print());
    }

    @Test
    public void showFormUpdateNotePatientTest() throws Exception {
        //GIVEN
        //WHEN
        microServiceHistoryPatientProxy.addNotePatient(notesClientUiTest);
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.get("/patHistory/update/6169f7df2c0d9a754676809f"))
                .andExpect(status().isOk())
                .andExpect(view().name("note-patient/updateNote"))
                .andExpect(model().attribute("notesClientUi",hasProperty("id", is("6169f7df2c0d9a754676809f"))))
                .andExpect(model().attribute("notesClientUi",hasProperty("patientId", is(1))))
                .andExpect(model().attribute("notesClientUi",hasProperty("note", is(
                        "Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling terrific' Weight at or below recommended level"))))
                .andDo(print());

        NotesClientUi gettedNoteClientUiById = microServiceHistoryPatientProxy.getNotePatientById("6169f7df2c0d9a754676809f");
        assertNotNull(gettedNoteClientUiById);
        assertEquals("6169f7df2c0d9a754676809f", gettedNoteClientUiById.getId());
        assertEquals(1, gettedNoteClientUiById.getPatientId());
        assertEquals("Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling terrific' Weight at or below recommended level", gettedNoteClientUiById.getNote());

    }

}
