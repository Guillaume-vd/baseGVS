package fr.gvs.base.controller.user.model;

import fr.gvs.base.model.User;
import lombok.Data;

@Data
public class UserResponse
{
    public UserResponse(User user) {
        id = user.getId();
        email = user.getEmail();
        firstname = user.getFirstname();
        lastname = user.getLastname();
        enabled = user.getEnabled();
        role = user.getRole().name();
    }

    Long id;
    String email;
    String firstname;
    String lastname;
    Boolean enabled;
    String role;
}
