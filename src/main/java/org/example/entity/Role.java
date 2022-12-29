package org.example.entity;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "roles")
@AttributeOverride(name = "id", column = @Column(name = "role_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Role extends LongEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "comment")
    private String comment;

}
