package org.example.entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity(name = "students")
@AttributeOverride(name = "id", column = @Column(name = "student_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Student extends Person {

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

}
