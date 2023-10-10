package coherentsolutions.airapp.service;

import coherentsolutions.airapp.config.JwtService;
import coherentsolutions.airapp.model.dto.LoginAuthDTO;
import coherentsolutions.airapp.model.dto.AuthenticationResponseTokenDTO;
import coherentsolutions.airapp.model.dto.RegisterDTO;
import coherentsolutions.airapp.model.dto.UserDTO;
import coherentsolutions.airapp.model.entity.UserEntity;

import coherentsolutions.airapp.model.entity.UserRoleEntity;
import coherentsolutions.airapp.model.enums.UserRoleEnum;
import coherentsolutions.airapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRoleService userRoleService;

    public AuthenticationResponseTokenDTO registerUser(RegisterDTO request) {
        UserRoleEntity roleUser = userRoleService.getByUserRole(UserRoleEnum.ROLE_USER);
        var user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .userRole(roleUser)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseTokenDTO.builder().token(jwtToken).build();
    }

    public AuthenticationResponseTokenDTO registerAdmin(RegisterDTO request) {
        UserRoleEntity roleAdmin = userRoleService.getByUserRole(UserRoleEnum.ROLE_ADMIN);
        var user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .userRole(roleAdmin)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseTokenDTO.builder().token(jwtToken).build();
    }

    public AuthenticationResponseTokenDTO login(LoginAuthDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseTokenDTO.builder()
                .token(jwtToken)
                .build();
    }

    public Optional<UserEntity> getById(Long id) {
        return userRepository.findById(id);
    }

        public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserEntity> getAll() {
        return (List<UserEntity>) userRepository.findAll();
    }

    public void edit(Long id, UserDTO userDTO) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        userOpt.ifPresent(user -> {
            Optional<UserRoleEntity> roleById = userRoleService.getById(userDTO.getUserRoleId());
            roleById.ifPresent(role -> {
                user.setId(id);
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setPhone(userDTO.getPhone());
                user.setUserRole(role);
                userRepository.save(user);
            });
        });
    }

    public UserEntity save(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    private AuthenticationResponseTokenDTO getAuthenticationResponseDTO(RegisterDTO request, UserRoleEntity roleUser) {
        var user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .userRole(roleUser)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseTokenDTO.builder()
                .token(jwtToken)
                .build();
    }

    public Optional<UserEntity> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
