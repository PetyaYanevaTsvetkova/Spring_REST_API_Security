package coherentsolutions.airapp.mapper;

import coherentsolutions.airapp.model.dto.RoleDTO;
import coherentsolutions.airapp.model.entity.UserRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    RoleDTO toRoleDTO(UserRoleEntity userRoleEntity);

    UserRoleEntity toUserRoleEntity(RoleDTO roleDTO);
}
