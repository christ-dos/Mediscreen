package com.mediscreen.microservicepatient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min=3, max=20, message = "First name must be between 3 and 20 characters")
    @Column(name = "first_name")
    private String firstName;

    @Size(min=3, max=20, message = "Last name must be between 3 and 20 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "The field cannot be blank")
    @Column(name = "birth_date")
    private String birthDate;

//    @NotBlank(message = "The field cannot be blank")
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    private String address;

    private String phone;

    public Patient(String firstName, String lastName, String birthDate, GenderEnum gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }


}
