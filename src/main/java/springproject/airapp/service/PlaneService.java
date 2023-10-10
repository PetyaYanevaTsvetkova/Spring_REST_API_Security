package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.dto.PlaneDTO;
import coherentsolutions.airapp.model.entity.Plane;
import coherentsolutions.airapp.model.entity.Ticket;
import coherentsolutions.airapp.repository.PlaneRepository;
import coherentsolutions.airapp.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaneService {

    private final PlaneRepository planeRepository;
    private final TicketRepository ticketRepository;
    private final AirportService airportService;

    public Optional<Plane> getById(Long id) {
        return planeRepository.findById(id);
    }

     public void delete(Long id) {
        for (Ticket ticket : ticketRepository.findAll()) {
            if (ticket.getPlane().getId() == id) {
                ticketRepository.deleteById(ticket.getId());
            }
        }
        planeRepository.deleteById(id);
    }

    public List<Plane> getAll() {
        return (List<Plane>) planeRepository.findAll();
    }

    public void edit(Long id, PlaneDTO planeDTO) {
        Optional<Plane> planeOpt = planeRepository.findById(id);
        planeOpt.ifPresent(plane -> {
            plane.setId(id);
            plane.setAirport(airportService.getByCity(planeDTO.getCity()));
            plane.setDestination(planeDTO.getDestination());
            planeRepository.save(plane);
        });
    }

    public Plane save(Plane plane) {
        return planeRepository.save(plane);
    }

}
