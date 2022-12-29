package org.example.entity;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity(name = "buildings")
@AttributeOverride(name="id", column=@Column(name="building_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Building extends LongEntity {

    @Column(name = "name")
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "building")
    private List<Room> rooms;

    @Column(name = "description")
    private String description;

}
