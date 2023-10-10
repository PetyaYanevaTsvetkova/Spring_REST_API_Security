package coherentsolutions.airapp.model.dto;

import coherentsolutions.airapp.model.entity.UserRoleEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotEmpty(message = "The first name should not be empty")
    private String firstName;

    @NotEmpty(message = "The last name should not be empty")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotNull
    private String email;

    private String phone;

    private Long userRoleId;

    private String password;
}
