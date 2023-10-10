package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.dto.AirportDTO;
import coherentsolutions.airapp.model.entity.Airport;
import coherentsolutions.airapp.model.entity.UserEntity;
import coherentsolutions.airapp.repository.AirportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
class AirportServiceTest {
    private static final String CITY = "Plovdiv";
    private static final String CITY_DB = "Sofia";

    @Autowired
    AirportService airportService;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    UserService userService;

    @Test
    void getById() {
        Airport airportSofia = new Airport();
        airportSofia.setId(1L);
        airportSofia.setCityName("Sofia");
        Optional<UserEntity> userOpt1 = userService.getById(1L);
        userOpt1.ifPresent(airportSofia::setAirportAdministrator);

        Optional<Airport> airportOpt = airportService.getById(1L);
        airportOpt.ifPresent(airport -> {
            Assertions.assertEquals(airportSofia.getId(), airport.getId());
            Assertions.assertEquals(airportSofia.getCityName(), airport.getCityName());
            Assertions.assertEquals(airportSofia.getAirportAdministrator().getId(),
                    airport.getAirportAdministrator().getId());
        });
    }

    @Test
    void getAll() {
        List<Airport> airportAll = airportService.getAll();
        long count = airportRepository.count();
        Assertions.assertEquals(count, airportAll.size());
    }

    @Test
    void edit() {
        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setCityName("TestCity");
        AtomicReference<String> cityName = new AtomicReference<>("");
        Optional<Airport> airportOpt = airportService.getById(1L);
        airportOpt.ifPresent(airport -> {
            cityName.set(airport.getCityName());
        });

        airportService.edit(1L, airportDTO);
        Assertions.assertNotNull(airportService.getByCity("TestCity"));

        airportDTO.setCityName(cityName.toString());
        airportService.edit(1L, airportDTO);
    }

    @Test
    void save() {
        long count = airportRepository.count();
        Airport airport = getAirport();

        Airport savedAirport = airportService.save(airport);
        Assertions.assertEquals(count + 1, airportRepository.count());
        Assertions.assertEquals(airport.getCityName(), savedAirport.getCityName());
        Assertions.assertEquals(airport.getAirportAdministrator().getId(),
                savedAirport.getAirportAdministrator().getId());

        airportService.delete(savedAirport.getId());
    }

    @Test
    void getByCity() {
        Airport airportByCity = airportService.getByCity(CITY_DB);
        Assertions.assertEquals(airportByCity.getCityName(), CITY_DB);
    }

    @Test
    void deleteById() {
        long count = airportRepository.count();
        Airport airport = getAirport();

        Airport savedAirport = airportService.save(airport);
        Assertions.assertEquals(count + 1, airportRepository.count());

        airportService.delete(savedAirport.getId());
        Assertions.assertEquals(count, airportRepository.count());
    }

    private Airport getAirport() {
        Airport airport = new Airport();
        userService.getById(1L).ifPresent(airport::setAirportAdministrator);
        airport.setCityName(CITY);
        return airport;
    }

}