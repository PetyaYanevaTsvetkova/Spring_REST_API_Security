package coherentsolutions.airapp.repository;

import coherentsolutions.airapp.model.entity.MethodInvocations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodInvocationsRepository extends CrudRepository<MethodInvocations, Long> {
}
