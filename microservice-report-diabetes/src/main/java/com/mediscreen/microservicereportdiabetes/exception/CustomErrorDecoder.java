package com.mediscreen.microservicereportdiabetes.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * Class that configure a custom decoder for handler exceptions
 *
 * @author Christine Duarte
 */
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String invoqueur, Response response) {
        if (response.status() == 404) {
            return new PatientNotFoundException("Patient not found");
        }
        return defaultErrorDecoder.decode(invoqueur, response);
    }
}
