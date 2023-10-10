package coherentsolutions.airapp.controller;

import coherentsolutions.airapp.mapper.UserRoleMapper;
import coherentsolutions.airapp.model.dto.AllRolesDTO;

import coherentsolutions.airapp.model.dto.RoleDTO;
import coherentsolutions.airapp.model.dto.UserDTO;

import coherentsolutions.airapp.service.UserRoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final UserRoleService userRoleService;
    private final UserRoleMapper userRoleMapper;

    @GetMapping("/all")
    @ApiOperation(value = "Get all tickets", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RolesAllowed("ROLE_ADMIN")
    public AllRolesDTO getAll() {
        List<RoleDTO> roleDtos = userRoleService.findAll()
                .stream()
                .map(userRoleMapper::toRoleDTO)
                .collect(Collectors.toList());
        AllRolesDTO allRolesDTO = new AllRolesDTO(roleDtos);
        return allRolesDTO;
    }

}
