package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.dto.AuthenticationResponseTokenDTO;
import coherentsolutions.airapp.model.dto.LoginAuthDTO;
import coherentsolutions.airapp.model.dto.RegisterDTO;
import coherentsolutions.airapp.model.dto.UserDTO;
import coherentsolutions.airapp.model.entity.UserEntity;
import coherentsolutions.airapp.model.entity.UserRoleEntity;
import coherentsolutions.airapp.model.enums.UserRoleEnum;
import coherentsolutions.airapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    @Test
    void login() {
        LoginAuthDTO request = new LoginAuthDTO("nick@yahoo.com", "123");
        AuthenticationResponseTokenDTO login = userService.login(request);
        Assertions.assertNotNull(login);
    }

    @Test
    void getById() {
        long testId = 1L;
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(testId);
        userEntity1.setFirstName("Nick");
        userEntity1.setLastName("Doe");
        userEntity1.setEmail("nick@yahoo.com");
        userEntity1.setPhone("889754612");
        userEntity1.setPassword("123");
        userEntity1.setUserRole(userRoleService.getByUserRole(UserRoleEnum.ROLE_ADMIN));

        Optional<UserEntity> userOpt = userService.getById(testId);
        userOpt.ifPresent(user -> {
            Assertions.assertEquals(userEntity1.getId(), user.getId());
            Assertions.assertEquals(userEntity1.getFirstName(), user.getFirstName());
            Assertions.assertEquals(userEntity1.getLastName(), user.getLastName());
            Assertions.assertEquals(userEntity1.getPhone(), user.getPhone());
            Assertions.assertEquals(userEntity1.getEmail(), user.getEmail());
            Assertions.assertEquals("ADMIN", user.getUserRole().getUserRole().name());
        });
    }

    @Test
    void save() {
        long before = userRepository.count();
        UserEntity userEntity = getUser();

        UserEntity savedUser = userService.save(userEntity);
        Optional<UserEntity> userOpt = userService.getById(savedUser.getId());
        userOpt.ifPresent(user -> {
            Assertions.assertEquals(before + 1, userRepository.count());
            Assertions.assertEquals(savedUser.getId(), user.getId());
            Assertions.assertEquals(savedUser.getUserRole().getUserRole().toString(),
                    user.getUserRole().getUserRole().toString());
            Assertions.assertEquals(savedUser.getFirstName(), user.getFirstName());
            Assertions.assertEquals(savedUser.getLastName(), user.getLastName());
            Assertions.assertEquals(savedUser.getPhone(), user.getPhone());
            Assertions.assertEquals(savedUser.getEmail(), user.getEmail());
            Assertions.assertEquals(savedUser.getPassword(), user.getPassword());
        });

        userService.delete(savedUser.getId());
        Assertions.assertEquals(before, userRepository.count());
    }

    @Test
    void getAll() {
        long count = userRepository.count();
        List<UserEntity> all = userService.getAll();
        Assertions.assertEquals(count, all.size());
    }

    @Test
    void edit() {
        Long idToEdit = 1L;
        UserRoleEntity role = userRoleService.getByUserRole(UserRoleEnum.ROLE_USER);
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("TestFirstName");
        userDTO.setLastName("TestLastName");
        userDTO.setEmail("test@abv.bg");
        userDTO.setPhone("02578944");
        userDTO.setUserRole(role);
        AtomicReference<String> firstName = new AtomicReference<>();
        AtomicReference<String> lastName = new AtomicReference<>();
        AtomicReference<String> email = new AtomicReference<>();
        AtomicReference<String> phone = new AtomicReference<>();
        AtomicReference<UserRoleEntity> userRole = new AtomicReference<>();
        Optional<UserEntity> userToEditOpt = userService.getById(idToEdit);
        userToEditOpt.ifPresent(user -> {
            firstName.set(user.getFirstName());
            lastName.set(user.getLastName());
            email.set(user.getEmail());
            phone.set(user.getPhone());
            userRole.set(userRoleService.getByUserRole(user.getUserRole().getUserRole()));
        });

        userService.edit(idToEdit, userDTO);
        Optional<UserEntity> userOpt = userService.getById(idToEdit);
        userOpt.ifPresent(user -> {
            Assertions.assertEquals(userDTO.getFirstName(), user.getFirstName());
            Assertions.assertEquals(userDTO.getLastName(), user.getLastName());
            Assertions.assertEquals(userDTO.getEmail(), user.getEmail());
            Assertions.assertEquals(userDTO.getPhone(), user.getPhone());
            Assertions.assertEquals(userDTO.getUserRole().getUserRole().toString(),
                    user.getUserRole().getUserRole().toString());
        });

        userDTO.setFirstName(firstName.toString());
        userDTO.setLastName(lastName.toString());
        userDTO.setPhone(phone.toString());
        userDTO.setEmail(email.toString());
        userDTO.setUserRole(userRole.get());
        userService.edit(idToEdit, userDTO);
    }

    @Test
    void registerAdmin() {
        RegisterDTO request = new RegisterDTO();
        request.setFirstName("TestFirstName");
        request.setLastName("TestLastName");
        request.setEmail("admin3@abv.bg");
        request.setPhone("02578944");
        request.setPassword("123");
        userService.registerAdmin(request);

        Optional<UserEntity> userOpt = userService.getByEmail(request.getEmail());
        userOpt.ifPresent(user -> {
            Assertions.assertEquals("ADMIN", user.getUserRole().getUserRole().toString());
        });
    }

    @Test
    void registerUser() {
        RegisterDTO request = new RegisterDTO();
        request.setFirstName("TestFirstName");
        request.setLastName("TestLastName");
        request.setEmail("user1@abv.bg");
        request.setPhone("02578944");
        request.setPassword("123");
        userService.registerUser(request);

        Optional<UserEntity> userOpt = userService.getByEmail(request.getEmail());
        userOpt.ifPresent(user -> {
            Assertions.assertEquals("USER", user.getUserRole().getUserRole().toString());
        });
    }

      @Test
    void deleteById() {
        long count = userRepository.count();
        UserEntity user = getUser();
        UserEntity savedUser = userService.save(user);
        Assertions.assertEquals(count + 1, userRepository.count());

        userService.delete(savedUser.getId());
        Assertions.assertEquals(count, userRepository.count());
    }

    private UserEntity getUser() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setFirstName("Nick");
        userEntity1.setLastName("Doe");
        userEntity1.setEmail("nick11@yahoo.com");
        userEntity1.setPhone("00889754612");
        userEntity1.setPassword("123");
        userEntity1.setUserRole(userRoleService.getByUserRole(UserRoleEnum.ROLE_ADMIN));
        return userEntity1;
    }

}