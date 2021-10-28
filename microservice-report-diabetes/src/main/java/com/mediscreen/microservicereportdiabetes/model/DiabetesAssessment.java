package com.mediscreen.microservicereportdiabetes.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 * Class that manage {@link DiabetesAssessment}
 *
 * @author Christine Duarte
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiabetesAssessment {
    @JsonAlias("patId")
    int patientId;

    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    private String firstName;

    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    private String lastName;

    int age;

    String result;

    public DiabetesAssessment(String firstName, String lastName, int age, String result) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.result = result;
    }

    @Override
    public String toString() {
        return "DiabetesAssessment{" +
                "patientId=" + patientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", result='" + result + '\'' +
                '}';
    }
}
