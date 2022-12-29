package org.example.entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "rooms")
@AttributeOverride(name = "id", column = @Column(name = "room_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Room extends LongEntity {

    @Column(name = "name")
    @NotEmpty
    private String name;

    @ManyToOne
    @JoinColumn(name = "building_id")
    @NotNull
    private Building building;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomType type;

    @OneToMany(mappedBy = "room")
    private List<Lesson> lessons;

    @Column(name = "description")
    private String description;
}
