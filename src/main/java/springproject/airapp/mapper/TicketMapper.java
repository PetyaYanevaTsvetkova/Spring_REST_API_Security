package coherentsolutions.airapp.mapper;

import coherentsolutions.airapp.model.dto.TicketDTO;

import coherentsolutions.airapp.model.entity.Order;
import coherentsolutions.airapp.model.entity.Plane;
import coherentsolutions.airapp.model.entity.Ticket;

import coherentsolutions.airapp.model.entity.UserEntity;
import coherentsolutions.airapp.service.OrderService;
import coherentsolutions.airapp.service.PlaneService;
import coherentsolutions.airapp.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class TicketMapper {

    @Autowired
    OrderService orderService;

    @Autowired
    PlaneService planeService;

    @Autowired
    UserService userService;

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "plane.id", target = "planeId")
    public abstract TicketDTO toTicketDTO(Ticket ticket);

    public Ticket toTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        Optional<Order> orderOpt = orderService.getById(ticketDTO.getOrderId());
        Optional<UserEntity> userOpt = userService.getById(ticketDTO.getUserId());
        Optional<Plane> planeOpt = planeService.getById(ticketDTO.getPlaneId());

        orderOpt.ifPresent(ticket::setOrder);
        userOpt.ifPresent(ticket::setUser);
        planeOpt.ifPresent(ticket::setPlane);
        ticket.setInitialPrice(ticketDTO.getInitialPrice());
        return ticket;
    }
}
