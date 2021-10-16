package com.mediscreen.microservicepatient.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @JsonAlias("given")
    @Size(min=3, max=20, message = "First name must be between 3 and 20 characters")
    @Column(name = "first_name")
    private String firstName;

    @JsonAlias("family")
    @Size(min=3, max=20, message = "Last name must be between 3 and 20 characters")
    @Column(name = "last_name")
    private String lastName;

    @JsonAlias("dob")
    @NotBlank(message = "The field cannot be blank")
    @Column(name = "birth_date")
    private String birthDate;

    @JsonAlias("sex")
    @NotNull(message = "The field cannot be blank")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String address;

    private String phone;

    public Patient(String firstName, String lastName, String birthDate, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Patient{" +
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
