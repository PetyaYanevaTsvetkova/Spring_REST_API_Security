package coherentsolutions.airapp.mapper;

import coherentsolutions.airapp.model.dto.UserDTO;
import coherentsolutions.airapp.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserDTO toUserDTO(UserEntity user);

    UserEntity toUserEntity(UserDTO userDTO);
}
