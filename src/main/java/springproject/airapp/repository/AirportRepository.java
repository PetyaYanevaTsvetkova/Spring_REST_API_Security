package coherentsolutions.airapp.repository;

import coherentsolutions.airapp.model.entity.Airport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends CrudRepository<Airport, Long> {

    Airport findByCityName(String city);

}
