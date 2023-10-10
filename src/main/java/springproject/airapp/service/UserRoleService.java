package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.entity.UserRoleEntity;
import coherentsolutions.airapp.model.enums.UserRoleEnum;
import coherentsolutions.airapp.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleEntity save(UserRoleEntity userRole) {
        return userRoleRepository.save(userRole);
    }

    public UserRoleEntity getByUserRole(UserRoleEnum roleEnum) {
        return userRoleRepository.findByUserRole(roleEnum);
    }

    public List<UserRoleEntity> findAll() {
        return (List<UserRoleEntity>) userRoleRepository.findAll();
    }

    public void deleteById(Long id) {
        userRoleRepository.deleteById(id);
    }

    public Optional<UserRoleEntity> getById(Long id){
       return userRoleRepository.findById(id);
    }

}
