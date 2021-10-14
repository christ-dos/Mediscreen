package com.mediscreen.microsevicehistorypatient.IT;

import com.mediscreen.microsevicehistorypatient.model.NotePatient;
import com.mediscreen.microsevicehistorypatient.service.NotePatientService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

/**
 * Class integration tests for {@link NotePatient}
 *
 * @author Christine Duarte
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NotePatientTestIT {

    /**
     * An instance of {@link MockMvc} that permit simulate a request HTTP
     */
    @Autowired
    private MockMvc mockMvcPatient;

    private NotePatient notePatientTest;

    @BeforeEach
    public void setupPerTest() {
        notePatientTest = new NotePatient(1,"Patient:Mr Durant Recommendation: une recommendation test pour le patient 1", LocalDateTime.now());
    }

}
