package com.mediscreen.microservicereportdiabetes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotesPatientReport {

    private String id;

    private int patientId;

    @NotBlank(message = "The field note cannot be blank")
    private String note;

    private LocalDate date;

    @Override
    public String toString() {
        return "NotesPatientReport{" +
                "id='" + id + '\'' +
                ", patientId=" + patientId +
                ", note='" + note + '\'' +
                ", date=" + date +
                '}';
    }
}
