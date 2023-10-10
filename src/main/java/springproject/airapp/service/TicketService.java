package coherentsolutions.airapp.service;

import coherentsolutions.airapp.mapper.TicketMapper;
import coherentsolutions.airapp.model.dto.TicketDTO;
import coherentsolutions.airapp.model.entity.Order;
import coherentsolutions.airapp.model.entity.Plane;
import coherentsolutions.airapp.model.entity.Ticket;
import coherentsolutions.airapp.model.entity.UserEntity;
import coherentsolutions.airapp.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final WeatherService weatherService;
    private final OrderService orderService;
    private final UserService userService;
    private final PlaneService planeService;
    private final TicketMapper ticketMapper;

    public Integer getActualTicketPrice(Long ticketId) {
        AtomicInteger price1 = new AtomicInteger(0);
        Optional<Ticket> ticketOpt = this.getById(ticketId);

        ticketOpt.ifPresent(ticket -> {
            String city = ticket.getPlane().getDestination();
           Double temp = weatherService.temperatureDouble(city);

            if (temp > 270.00 && temp < 280) {
                Integer price = ticket.getInitialPrice();
                ticket.setInitialPrice(price + 50);
            } else if (temp > 280 && temp < 290) {
                Integer price = ticket.getInitialPrice();
                ticket.setInitialPrice(price + 100);
            } else if (temp > 290) {
                Integer price = ticket.getInitialPrice();
                ticket.setInitialPrice(price + 150);
            }
            price1.set(ticket.getInitialPrice());
            this.edit(ticketId, ticketMapper.toTicketDTO(ticket));
        });

        return Integer.parseInt(String.valueOf(price1));
    }

    public Optional<Ticket> getById(Long id) {
        return ticketRepository.findById(id);
    }

    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }

       public List<Ticket> getAll() {
        return (List<Ticket>) ticketRepository.findAll();
    }


    public void edit(Long id, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        ticketOpt.ifPresent(ticket -> {
            Optional<Plane> planeOpt = planeService.getById(ticketDTO.getPlaneId());
            Optional<UserEntity> userOpt = userService.getById(ticketDTO.getUserId());
            Optional<Order> orderOpt = orderService.getById(ticketDTO.getOrderId());
            ticket.setId(id);
            ticket.setOrder(orderOpt.orElse(ticket.getOrder()));
            ticket.setUser(userOpt.orElse(ticket.getUser()));
            ticket.setPlane(planeOpt.orElse(ticket.getPlane()));
            ticket.setInitialPrice(ticketDTO.getInitialPrice());
            ticketRepository.save(ticket);
        });
    }

     public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Long mostActiveUserId() {
        return ticketRepository.mostActiveUserId();
    }

    public Long mostBoughtTicketsPlaneId() {
        return ticketRepository.mostBoughtTicketsPlaneId();
    }

}
