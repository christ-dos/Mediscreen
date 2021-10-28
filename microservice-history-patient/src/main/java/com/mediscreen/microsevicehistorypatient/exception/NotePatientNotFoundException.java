package com.mediscreen.microsevicehistorypatient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class that handles exceptions when the note not exist in the database
 *
 * @author Christine Duarte
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotePatientNotFoundException extends RuntimeException {
    public NotePatientNotFoundException(String message) {
        super(message);
    }
}
