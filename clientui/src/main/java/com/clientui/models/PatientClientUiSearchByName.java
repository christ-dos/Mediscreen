package com.clientui.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 * Class that manage {@link PatientClientUiSearchByName}
 *
 * @author Christine Duarte
 */
@Getter
@Setter
public class PatientClientUiSearchByName {

    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    private String lastName;

    @Override
    public String toString() {
        return "PatientClientUiSearchByName{" +
                "lastName='" + lastName + '\'' +
                '}';
    }
}
