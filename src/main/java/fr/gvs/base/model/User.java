package fr.gvs.base.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import fr.gvs.base.security.Role;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class User {

    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    private String email;
    @NotNull
    private String password;
    private String firstname;
    private String lastname;
    @NotNull
    private Boolean enabled;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
}
