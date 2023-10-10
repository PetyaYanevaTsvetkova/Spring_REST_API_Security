package coherentsolutions.airapp.model.dto;

import coherentsolutions.airapp.model.enums.UserRoleEnum;
import lombok.Data;

@Data
public class RoleDTO {

    private Long id;

    private UserRoleEnum userRole;
}
