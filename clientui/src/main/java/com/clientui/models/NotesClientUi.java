package com.clientui.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;


/**
 * Class that manage {@link NotesClientUi}
 *
 * @author Christine Duarte
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotesClientUi {
    private String id;

    private int patientId;

    @NotBlank(message = "The field note cannot be blank")
    private String note;

    private LocalDate date;

    public NotesClientUi(Integer patientId, String note, LocalDate date) {
        this.patientId = patientId;
        this.note = note;
        this.date = date;
    }

    @Override
    public String toString() {
        return "NotesClientUi{" +
                "id='" + id + '\'' +
                ", patientId=" + patientId +
                ", note='" + note + '\'' +
                ", date=" + date +
                '}';
    }
}
