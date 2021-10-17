package com.clientui.controller;

import com.clientui.models.NotesClientUi;
import com.clientui.proxy.IMicroServiceHistoryPatientProxy;
import com.clientui.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class that test ClientUiController
 *
 * @author Christine Duarte
 */
@WebMvcTest(NoteClientUiController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class NoteClientUiControllerTest {
    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcNoteClientUi;

    @MockBean
    private IMicroServiceHistoryPatientProxy microServiceHistoryPatientProxyMock;

    private NotesClientUi noteClientUi;

    @BeforeEach
    public void setupPerTest() {
        noteClientUi = new NotesClientUi("6169f7df2c0d9a754676809f", 1, "Patient: Martin Recommendation: rien à signaler", null);
    }

    @Test
    public void getListNotesByPatientTest_thenReturnAnIterableOfNotesByPatient() throws Exception {
        //GIVEN
        List<NotesClientUi> notesByPatientsTest = Arrays.asList(
                new NotesClientUi(1, "Patient: Martin Recommendation: ras", null),
                new NotesClientUi(1, "Patient: Martin Recommendation: Le patient se sent fatigué", null),
                new NotesClientUi(1, "Patient: Martin Recommendation: consultation ce jour ras", null)
        );
        when(microServiceHistoryPatientProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesByPatientsTest);
        //WHEN
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.get("/patHistory/add/1"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("note-patient/addNote"))
                .andExpect(model().attributeExists("notesPatient"))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("patientId", is(1)))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("note", is("Patient: Martin Recommendation: ras")))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("note", is("Patient: Martin Recommendation: Le patient se sent fatigué")))))
                .andDo(print());
    }

    @Test
    public void updateNotePatientTest_whenNotePatientExitsInDbAndHasNoErrorInForm_thenReturnToViewAddNote() throws Exception {
        //GIVEN
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                Utils.asJsonString(noteClientUi),
                header,
                HttpStatus.CREATED
        );
        when(microServiceHistoryPatientProxyMock.
                updateNotePatient(any(NotesClientUi.class), anyString())).thenReturn((ResponseEntity<NotesClientUi>) responseEntity);
        //WHEN
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.post("/patHistory/update/6169f7df2c0d9a754676809f")
                        .content(Utils.asJsonString(noteClientUi))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("id", "6169f7df2c0d9a754676809f")
                        .param("patientId", String.valueOf(1))
                        .param("note", noteClientUi.getNote()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patHistory/add/1"))
                .andExpect(redirectedUrl("/patHistory/add/1"))
                .andDo(print());
    }

    @Test
    public void updateNotePatientTest_whenNotePatientHasErrorInForm_thenReturnToUpdateNote() throws Exception {
        //GIVEN
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                Utils.asJsonString(noteClientUi),
                header,
                HttpStatus.CREATED
        );
        when(microServiceHistoryPatientProxyMock.
                updateNotePatient(any(NotesClientUi.class), anyString())).thenReturn((ResponseEntity<NotesClientUi>) responseEntity);
        //WHEN
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.post("/patHistory/update/6169f7df2c0d9a754676809f")
                        .content(Utils.asJsonString(noteClientUi))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("id", "")
                        .param("patientId", String.valueOf(0))
                        .param("note", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2))
                .andExpect(model().attributeHasFieldErrorCode("notesClientUi", "patientId", "Min"))
                .andExpect(model().attributeHasFieldErrorCode("notesClientUi", "note", "NotBlank"))
                .andDo(print());

    }

    @Test
    public void showFormUpdateNotePatientTest() throws Exception {
        //GIVEN
        when(microServiceHistoryPatientProxyMock.getNotePatientById(anyString())).thenReturn(noteClientUi);
        //WHEN
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.get("/patHistory/update/6169f7df2c0d9a754676809f"))
                .andExpect(status().isOk())
                .andExpect(view().name("note-patient/updateNote"))
                .andExpect(model().attribute("notesClientUi",hasProperty("id", is("6169f7df2c0d9a754676809f"))))
                .andExpect(model().attribute("notesClientUi",hasProperty("patientId", is(1))))
                .andExpect(model().attribute("notesClientUi",hasProperty("note", is("Patient: Martin Recommendation: rien à signaler"))))
                .andDo(print());
    }

}
