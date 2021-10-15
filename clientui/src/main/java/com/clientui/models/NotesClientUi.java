package com.clientui.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotesClientUi {

//    private String id;

    @NotNull
    private int patientId;

    @NotBlank
    private String note;

    private LocalDate date;

    public NotesClientUi(Integer patientId, String note, LocalDate date) {
        this.patientId = patientId;
        this.note = note;
        this.date = date;
    }

//    @Override
//    public String toString() {
//        return "NotesClientUi{" +
//                "id='" + id + '\'' +
//                ", patientId=" + patientId +
//                ", note='" + note + '\'' +
//                ", date=" + date +
//                '}';
//    }
}
