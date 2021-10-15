package com.clientui.controller;

import com.clientui.models.NotesClientUi;
import com.clientui.proxy.IMicroServiceHistoryPatientProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.anyInt;
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
    private MockMvc mockMvcClientUi;

    @MockBean
    private IMicroServiceHistoryPatientProxy microServiceHistoryPatientProxyMock;

    private NotesClientUi notesClientUi;

    @BeforeEach
    public void setupPerTest() {
        notesClientUi = new NotesClientUi(1,"Patient: Martin Recommendation: rien à signaler", null);
    }

    @Test
    public void getListNotesByPatientTest_thenReturnAnIterableOfNotesByPatient() throws Exception {
        //GIVEN
        List<NotesClientUi> notesByPatientsTest = Arrays.asList(
                new NotesClientUi(1,"Patient: Martin Recommendation: ras", null),
                new NotesClientUi(1,"Patient: Martin Recommendation: Le patient se sent fatigué", null),
                new NotesClientUi(1,"Patient: Martin Recommendation: consultation ce jour ras", null)
        );
        when(microServiceHistoryPatientProxyMock.getListNotesByPatient(anyInt())).thenReturn(notesByPatientsTest);
        //WHEN
        //THEN
        mockMvcClientUi.perform(MockMvcRequestBuilders.get("/notesPatient/1"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("patient/note-patient/listNote"))
                .andExpect(model().attributeExists("NotesPatient"))
                .andExpect(model().attribute("NotesPatient", hasItem(hasProperty("patientId", is(1)))))
                .andExpect(model().attribute("NotesPatient", hasItem(hasProperty("note", is("Patient: Martin Recommendation: ras")))))
                .andExpect(model().attribute("NotesPatient", hasItem(hasProperty("note", is("Patient: Martin Recommendation: Le patient se sent fatigué")))))
                .andDo(print());
    }


}
