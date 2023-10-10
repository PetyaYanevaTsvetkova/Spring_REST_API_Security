package coherentsolutions.airapp.repository;

import coherentsolutions.airapp.model.entity.UserRoleEntity;
import coherentsolutions.airapp.model.enums.UserRoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRoleEntity, Long> {

    UserRoleEntity findByUserRole(UserRoleEnum userRole);
}
