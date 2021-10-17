package com.mediscreen.microsevicehistorypatient.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document("note_patient")
@NoArgsConstructor
@AllArgsConstructor
public class NotePatient {
    @Id
    private String id;

    @JsonAlias("patid")
    private Integer patientId;

    @JsonAlias("e")
    private String note;

    private LocalDateTime date;

    public NotePatient(Integer patientId, String note, LocalDateTime date) {
        this.patientId = patientId;
        this.note = note;
        this.date = date;
    }

    @Override
    public String toString() {
        return "NotePatient{" +
                "id='" + id + '\'' +
                ", patientId=" + patientId +
                ", note='" + note + '\'' +
                ", date=" + date +
                '}';
    }
}

