package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.dto.AirportDTO;

import coherentsolutions.airapp.model.entity.Airport;

import coherentsolutions.airapp.repository.AirportRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository airportRepository;

    public Optional<Airport> getById(Long id) {
        return airportRepository.findById(id);
    }

    public void delete(Long id) {
        airportRepository.deleteById(id);
    }

    public List<Airport> getAll() {
        return (List<Airport>) airportRepository.findAll();
    }

    public void edit(Long id, AirportDTO airportDTO) {
        Optional<Airport> airportOpt = airportRepository.findById(id);
        airportOpt.ifPresent(airport -> {
            airport.setCityName(airportDTO.getCityName());

            airportRepository.save(airport);
        });
    }

    public Airport save(Airport airport) {
        return airportRepository.save(airport);
    }

    public Airport getByCity(String city) {
        return airportRepository.findByCityName(city);
    }

}

