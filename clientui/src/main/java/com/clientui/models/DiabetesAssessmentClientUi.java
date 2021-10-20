package com.clientui.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiabetesAssessmentClientUi {

    @JsonAlias("patId")
    int patientId ;

    @Size(min=3, max=20, message = "Last name must be between 3 and 20 characters")
    private String firstName;

    @Size(min=3, max=20, message = "Last name must be between 3 and 20 characters")
    private String lastName;

    int age;

    String result;

    @Override
    public String toString() {
        return "DiabetesAssessmentClientUi{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", result='" + result + '\'' +
                '}';
    }
}
