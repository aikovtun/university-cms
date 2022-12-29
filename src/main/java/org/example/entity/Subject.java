package org.example.entity;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity(name = "subjects")
@AttributeOverride(name = "id", column = @Column(name = "subject_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Subject extends LongEntity {

    @Column(name = "name")
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "subject")
    private List<Lesson> lessons;

    @Column(name = "description")
    private String description;

}
