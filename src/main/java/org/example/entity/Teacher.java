package org.example.entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "teachers")
@AttributeOverride(name = "id", column = @Column(name = "teacher_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Teacher extends Person {

    @OneToMany(mappedBy = "teacher")
    private List<Lesson> lessons;

}
