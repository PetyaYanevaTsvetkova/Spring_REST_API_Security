package coherentsolutions.airapp.repository;

import coherentsolutions.airapp.model.entity.Plane;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneRepository extends CrudRepository<Plane, Long> {

    Plane getPlaneByDestination(String destination);
}
