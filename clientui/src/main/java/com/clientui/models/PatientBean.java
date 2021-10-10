package com.clientui.models;


import com.clientui.models.GenderEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class PatientBean {
    private int id;

//    @NotBlank(message = "The field cannot be blank")
    private String firstName;

//    @NotBlank(message = "The field cannot be blank")
    private String lastName;

//    @NotBlank(message = "The field cannot be blank")
    private String birthDate;

//    @NotBlank(message = "The field cannot be blank")
    private GenderEnum gender;

    private String address;

    private String phone;


    @Override
    public String toString() {
        return "PatientBean{" +
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
