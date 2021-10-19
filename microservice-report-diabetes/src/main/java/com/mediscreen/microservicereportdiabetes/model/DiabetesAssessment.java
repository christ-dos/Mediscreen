package com.mediscreen.microservicereportdiabetes.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class DiabetesAssessment {

//    int id;

    @Size(min=3, max=20, message = "Last name must be between 3 and 20 characters")
    private String firstName;

    @Size(min=3, max=20, message = "Last name must be between 3 and 20 characters")
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
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", result='" + result + '\'' +
                '}';
    }
}
