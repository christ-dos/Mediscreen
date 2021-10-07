package com.mediscreen.microservicepatient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "The field cannot be blank")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "The field cannot be blank")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "The field cannot be blank")
    @Column(name = "birth_date")
    private String birthDate;

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
