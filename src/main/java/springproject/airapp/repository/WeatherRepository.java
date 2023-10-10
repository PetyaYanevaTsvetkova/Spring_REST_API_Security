package coherentsolutions.airapp.repository;

import coherentsolutions.airapp.model.entity.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {

    Weather findByName(String cityName);

}
