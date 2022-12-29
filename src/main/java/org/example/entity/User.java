package org.example.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@AttributeOverride(name = "id", column = @Column(name="user_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class User extends LongEntity {

    @Column(name = "email")
    @NotEmpty
    @Email
    private String email;

    @Column(name = "password")
    @NotEmpty
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(name = "comment")
    private String comment;

    public void addRole(Role role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        if (roles.contains(role)) {
            return;
        }
        roles.add(role);
    }

    public void removeRole(Role role) {
        if (roles == null) {
            return;
        }
        roles.remove(role);
    }

}
