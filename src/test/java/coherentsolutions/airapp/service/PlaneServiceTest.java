package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.dto.PlaneDTO;
import coherentsolutions.airapp.model.entity.Plane;
import coherentsolutions.airapp.repository.PlaneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
class PlaneServiceTest {

    private static final String AIRPORT_CITY = "Sofia";
    private static final String DESTINATION = "Varna";

    @Autowired
    PlaneService planeService;

    @Autowired
    PlaneRepository planeRepository;

    @Autowired
    AirportService airportService;

    @Test
    void getById() {
        Plane plane1 = new Plane(1L, airportService.getByCity("Sofia"), "Paris", new ArrayList<>());
        Optional<Plane> planeOpt = planeService.getById(1L);
        planeOpt.ifPresent(plane -> {
            Assertions.assertEquals(plane.getId(), plane1.getId());
            Assertions.assertEquals(plane.getAirport().getId(), plane1.getAirport().getId());
            Assertions.assertEquals(plane.getDestination(), plane1.getDestination());
        });
    }

    @Test
    void getAll() {
        long count = planeRepository.count();
        List<Plane> allPlanes = planeService.getAll();
        Assertions.assertEquals(allPlanes.size(), count);
    }

    @Test
    void edit() {
        Long idToEdit = 1L;
        Optional<Plane> planeOpt = planeService.getById(idToEdit);
        AtomicReference<String> cityName = new AtomicReference<>();
        AtomicReference<String> destination = new AtomicReference<>();
        planeOpt.ifPresent(plane -> {
            cityName.set(plane.getAirport().getCityName());
            destination.set(plane.getDestination());
        });
        PlaneDTO planeDTO = new PlaneDTO();
        planeDTO.setCity(AIRPORT_CITY);
        planeDTO.setDestination(DESTINATION);

        planeService.edit(idToEdit, planeDTO);
        Optional<Plane> planeOpt1 = planeService.getById(idToEdit);
        planeOpt1.ifPresent(plane -> {
            Assertions.assertEquals(planeDTO.getDestination(), plane.getDestination());
            Assertions.assertEquals(planeDTO.getCity(), plane.getAirport().getCityName());
        });

        planeDTO.setCity(cityName.toString());
        planeDTO.setDestination(destination.toString());
        planeService.edit(idToEdit, planeDTO);
        Optional<Plane> planeOpt2 = planeService.getById(idToEdit);
        planeOpt2.ifPresent(plane -> {
            Assertions.assertEquals(planeDTO.getDestination(), plane.getDestination());
            Assertions.assertEquals(planeDTO.getCity(), plane.getAirport().getCityName());
        });
    }

    @Test
    void save() {
        long count = planeRepository.count();
        Plane plane1 = getPlane();

        Plane savedPlane = planeService.save(plane1);
        Assertions.assertEquals(count + 1, planeRepository.count());
        Optional<Plane> planeOpt = planeService.getById(savedPlane.getId());
        planeOpt.ifPresent(plane -> {
            Assertions.assertEquals(plane1.getId(), savedPlane.getId());
            Assertions.assertEquals(plane1.getDestination(), savedPlane.getDestination());
            Assertions.assertEquals(plane1.getAirport().getId(), savedPlane.getAirport().getId());
        });

        planeService.delete(savedPlane.getId());
        Assertions.assertEquals(count, planeRepository.count());
    }

    @Test
    void deleteById() {
        long count = planeRepository.count();
        Plane plane1 = getPlane();
        Plane savedPlane = planeService.save(plane1);
        Assertions.assertEquals(count + 1, planeRepository.count());

        planeService.delete(savedPlane.getId());
        Assertions.assertEquals(count, planeRepository.count());
    }

    private Plane getPlane() {
        Plane plane1 = new Plane();
        plane1.setAirport(airportService.getByCity(AIRPORT_CITY));
        plane1.setDestination(DESTINATION);
        plane1.setTicket(new ArrayList<>());
        return plane1;
    }

}