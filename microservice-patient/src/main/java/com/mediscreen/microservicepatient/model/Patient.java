package com.mediscreen.microservicepatient.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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

}
