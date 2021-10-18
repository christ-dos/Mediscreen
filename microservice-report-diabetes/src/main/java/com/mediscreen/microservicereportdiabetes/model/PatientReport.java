package com.mediscreen.microservicereportdiabetes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientReport {

    private int id;

    @Size(min=3, max=20, message = "First name must be between 3 and 20 characters")
    private String firstName;

    @Size(min=3, max=20, message = "Last name must be between 3 and 20 characters")
    private String lastName;

    @NotBlank(message = "The field cannot be blank")
    private String birthDate;

    @NotNull(message = "The field cannot be blank")
    private Gender gender;

    private String address;

    private String phone;

    @Override
    public String toString() {
        return "PatientReport{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
