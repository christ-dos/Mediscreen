package com.clientui.IT;

import com.clientui.models.NotesClientUi;
import com.clientui.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
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
public class NoteClientUiTestIT {

    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcNoteClientUi;

    private NotesClientUi notesClientUiTest;

    @BeforeEach
    public void setupPerTest() {
        notesClientUiTest = new NotesClientUi("6169f7df2c0d9a754676809f", 1, "Patient: Martin Recommendation: rien à signaler", null);

    }

    @Test
    public void getListNotesByPatientTest_thenReturnAnIterableOfNotesByPatient() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.get("/patHistory/add/1"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("note-patient/addNote"))
                .andExpect(model().attributeExists("notesClientUi"))
                .andExpect(model().attributeExists("notesPatient"))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("id", is("616b45951524b440b8203c99")))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("patientId", is(1)))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("note", is("ajouter une note...")))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("note", is("modifier la note ")))))
                .andDo(print());
    }

    @Test
    public void updateNotePatientTest_whenNotePatientExitsInDbAndHasNoErrorInForm_thenReturnToViewAddNote() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.post("/patHistory/update/6169f7df2c0d9a754676809f")
                        .content(Utils.asJsonString(notesClientUiTest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("id", "6169f7df2c0d9a754676809f")
                        .param("patientId", String.valueOf(1))
                        .param("note", notesClientUiTest.getNote()))
                .andExpect(status().isOk())
                .andExpect(view().name("note-patient/addNote"))
                .andExpect(model().attribute("notesClientUi", hasProperty("id", is("6169f7df2c0d9a754676809f"))))
                .andExpect(model().attribute("notesClientUi", hasProperty("patientId", is(1))))
                .andExpect(model().attribute("notesClientUi", hasProperty("note", is("Patient: Martin Recommendation: rien à signaler"))))
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
                .andExpect(model().errorCount(2))
                .andExpect(model().attributeHasFieldErrorCode("notesClientUi", "patientId", "Min"))
                .andExpect(model().attributeHasFieldErrorCode("notesClientUi", "note", "NotBlank"))
                .andDo(print());

    }

    @Test
    public void showFormUpdateNotePatientTest() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        mockMvcNoteClientUi.perform(MockMvcRequestBuilders.get("/patHistory/update/6169f7df2c0d9a754676809f"))
                .andExpect(status().isOk())
                .andExpect(view().name("note-patient/updateNote"))
                .andExpect(model().attribute("notesClientUi",hasProperty("id", is("6169f7df2c0d9a754676809f"))))
                .andExpect(model().attribute("notesClientUi",hasProperty("patientId", is(3))))
                .andExpect(model().attribute("notesClientUi",hasProperty("note", is("Patient: Martin Recommendation: rien à signaler"))))
                .andDo(print());
    }

}
