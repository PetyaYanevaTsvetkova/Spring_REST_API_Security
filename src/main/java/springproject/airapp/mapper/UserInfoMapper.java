package coherentsolutions.airapp.mapper;

import coherentsolutions.airapp.model.dto.UserInfoDto;
import coherentsolutions.airapp.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {

    UserInfoDto toUserInfoDTO(UserEntity user);

    UserEntity toUserEntity(UserInfoDto userDTO);
}


