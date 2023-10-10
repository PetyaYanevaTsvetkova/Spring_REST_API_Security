package coherentsolutions.airapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class AllUsersDTO {

    private final List<UserDTO> users;
}
