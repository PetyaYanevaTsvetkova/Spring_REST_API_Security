package coherentsolutions.airapp.controller;

import coherentsolutions.airapp.mapper.UserInfoMapper;
import coherentsolutions.airapp.model.dto.LoginAuthDTO;
import coherentsolutions.airapp.model.dto.AuthenticationResponseTokenDTO;
import coherentsolutions.airapp.mapper.PlaneMapper;
import coherentsolutions.airapp.model.dto.*;
import coherentsolutions.airapp.model.entity.Plane;
import coherentsolutions.airapp.service.PlaneService;
import coherentsolutions.airapp.service.TicketService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import coherentsolutions.airapp.mapper.UserEntityMapper;
import coherentsolutions.airapp.model.entity.UserEntity;
import coherentsolutions.airapp.model.exception.ObjectNotFoundException;
import coherentsolutions.airapp.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PlaneService planeService;
    private final TicketService ticketService;
    private final UserEntityMapper userEntityMapper;
    private final UserInfoMapper userInfoMapper;
    private final PlaneMapper planeMapper;

    @PostMapping("/registerUser")
    public ResponseEntity<AuthenticationResponseTokenDTO> registerUser(@RequestBody RegisterDTO request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<AuthenticationResponseTokenDTO> registerAdmin(@RequestBody RegisterDTO request) {
        return ResponseEntity.ok(userService.registerAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseTokenDTO> login(@RequestBody LoginAuthDTO request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/userAccess")
    public String userAccess() {
        return "USER and ADMIN Access allowed";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/adminAccess")
    public String adminAccess() {
        return "Only ADMIN Access allowed";
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "Get user by id", response = UserDTO.class, notes = "User must exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "User not found", response = ObjectNotFoundException.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public UserInfoDto getById(@PathVariable("id") Long id) {
        return
                userInfoMapper.toUserInfoDTO(userService
                        .getById(id)
                        .orElseThrow(() -> new ObjectNotFoundException(id)));
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/mostActive")
    @ApiOperation(value = "Get most active user", response = MostActiveUserBoughtPlaneDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public MostActiveUserBoughtPlaneDTO getMostActive() {
        Optional<UserEntity> mostActiveUserOpt = userService.getById(ticketService.mostActiveUserId());
        Optional<Plane> mostBoughtTicketsForPlane = planeService.getById(ticketService.mostBoughtTicketsPlaneId());
        MostActiveUserBoughtPlaneDTO mostActiveUserBoughtPlaneDTO = new MostActiveUserBoughtPlaneDTO();
        mostActiveUserOpt.ifPresent(user -> {
            mostBoughtTicketsForPlane.ifPresent(plane -> {
                mostActiveUserBoughtPlaneDTO.setUserDTO(userInfoMapper.toUserInfoDTO(user));
                mostActiveUserBoughtPlaneDTO.setPlaneDTO(planeMapper.toPlaneDTO(plane));
            });
        });
        return mostActiveUserBoughtPlaneDTO;
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/all")
    @ApiOperation(value = "Get all users", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public List<UserInfoDto> getAll() {
        List<UserInfoDto> userDtos = userService.getAll()
                .stream()
                .map(userInfoMapper::toUserInfoDTO)
                .collect(Collectors.toList());
        return userDtos;
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/new")
    @ApiOperation(value = "Create user", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Invalid input supplied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        UserEntity user = userService.save(userEntityMapper.toUserEntity(userDTO));
        UserDTO userResponse = userEntityMapper.toUserDTO(user);
        return userResponse;
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Edit user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void editUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        userService.edit(id, userDTO);
    }

}
