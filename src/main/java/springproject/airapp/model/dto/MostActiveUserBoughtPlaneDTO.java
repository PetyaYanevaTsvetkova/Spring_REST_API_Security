package coherentsolutions.airapp.model.dto;

import coherentsolutions.airapp.mapper.UserInfoMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MostActiveUserBoughtPlaneDTO {

    private UserInfoDto userDTO;

    private PlaneDTO planeDTO;
}
