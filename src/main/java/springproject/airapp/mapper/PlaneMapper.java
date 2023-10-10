package coherentsolutions.airapp.mapper;

import coherentsolutions.airapp.model.dto.PlaneDTO;
import coherentsolutions.airapp.model.entity.*;
import coherentsolutions.airapp.service.AirportService;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class PlaneMapper {
    @Autowired
    AirportService airportService;

    @Mapping(source = "airport.cityName", target = "city")
    public abstract PlaneDTO toPlaneDTO(Plane plane);


    public Plane toPlane(PlaneDTO planeDTO) {
        Plane plane = new Plane();
        String city = planeDTO.getCity();
        String destination = planeDTO.getDestination();
        plane.setAirport(airportService.getByCity(city));
        plane.setDestination(destination);
        return plane;
    }
}
