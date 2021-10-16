package com.clientui.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class PatientClientUi {

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

    public PatientClientUi(int id, String firstName, String lastName, String birthDate, Gender gender, String address, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PatientClientUi{" +
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
