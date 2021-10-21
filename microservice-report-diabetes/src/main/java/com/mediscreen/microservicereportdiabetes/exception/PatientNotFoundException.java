package com.mediscreen.microservicereportdiabetes.exception;

import com.mediscreen.microservicereportdiabetes.model.DiabetesAssessment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException{
    public PatientNotFoundException(String message) {
        super(message);
    }
}
