package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.entity.UserRoleEntity;
import coherentsolutions.airapp.model.enums.UserRoleEnum;
import coherentsolutions.airapp.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserRoleServiceTest {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Test
    void getByUserRole() {
        UserRoleEntity userRole = userRoleService.getByUserRole(UserRoleEnum.ROLE_USER);
        Assertions.assertEquals(userRole.getUserRole().toString(), UserRoleEnum.ROLE_USER.toString());
    }

    @Test
    void save() {
        long count = userRoleRepository.count();
        UserRoleEntity role = getUserRole();

        UserRoleEntity savedRole = userRoleService.save(role);
        Optional<UserRoleEntity> roleOpt = userRoleService.getById(savedRole.getId());
        roleOpt.ifPresent(role1 -> {
            Assertions.assertEquals(count + 1, userRoleRepository.count());
            Assertions.assertEquals(savedRole.getUserRole().toString(), role1.getUserRole().toString());
        });

        userRoleService.deleteById(savedRole.getId());
        Assertions.assertEquals(count, userRoleRepository.count());
    }

    @Test
    void getById() {
        long testId = 1L;
        UserRoleEntity roleUser = new UserRoleEntity();
        roleUser.setUserRole(UserRoleEnum.ROLE_USER);

        Optional<UserRoleEntity> roleOpt = userRoleService.getById(testId);
        roleOpt.ifPresent(role -> {
            Assertions.assertEquals(roleUser.getUserRole().toString(), role.getUserRole().toString());
        });
    }

    @Test
    void findAll() {
        List<UserRoleEntity> allRoles = userRoleService.findAll();
        long count = userRoleRepository.count();
        Assertions.assertEquals(count, allRoles.size());
    }

    @Test
    void deleteById() {
        long count = userRoleRepository.count();
        UserRoleEntity role = getUserRole();

        UserRoleEntity savedRole = userRoleService.save(role);
        Assertions.assertEquals(count + 1, userRoleRepository.count());

        userRoleService.deleteById(savedRole.getId());
        Assertions.assertEquals(count, userRoleRepository.count());
    }

    private static UserRoleEntity getUserRole() {
        UserRoleEntity role = new UserRoleEntity();
        role.setUserRole(UserRoleEnum.MODERATOR);
        return role;
    }

}