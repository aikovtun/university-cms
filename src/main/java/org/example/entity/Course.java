package org.example.entity;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity(name = "courses")
@AttributeOverride(name = "id", column = @Column(name = "course_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Course extends LongEntity {

    @Column(name = "name")
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "course")
    private List<Group> groups;

    @Column(name = "description")
    private String description;

}
