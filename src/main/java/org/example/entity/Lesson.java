package org.example.entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity(name = "lessons")
@AttributeOverride(name = "id", column = @Column(name = "lesson_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Lesson extends LongEntity {

    @ManyToOne
    @JoinColumn(name = "room_id")
    @NotNull
    private Room room;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @NotNull
    private Group group;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @NotNull
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @NotNull
    private Teacher teacher;

    @Column(name = "lesson_date")
    @NotNull
    private LocalDate date;

    @Column(name = "lesson_number")
    @Min(1)
    @Max(10)
    private int number;

}
