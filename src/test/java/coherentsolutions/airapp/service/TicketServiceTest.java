package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.dto.TicketDTO;
import coherentsolutions.airapp.model.entity.Order;
import coherentsolutions.airapp.model.entity.Plane;
import coherentsolutions.airapp.model.entity.Ticket;
import coherentsolutions.airapp.model.entity.UserEntity;
import coherentsolutions.airapp.repository.TicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
class TicketServiceTest {
    private static final Long TICKET_ID = 1L;

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    PlaneService planeService;

    @Autowired
    UserService userService;

    @Autowired
    WeatherService weatherService;

    @Test
    void getActualTicketPrice() {
        ticketService.getById(TICKET_ID).ifPresent(ticket -> {
            String destination = ticket.getPlane().getDestination();
            Double temp = weatherService.temperatureDouble(destination);
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
            Integer actualTicketPrice = ticketService.getActualTicketPrice(1L);
            Assertions.assertEquals(ticket.getInitialPrice(), actualTicketPrice);
        });
    }

    @Test
    void getById() {
        Ticket ticket1 = new Ticket();
        Optional<Order> orderOpt1 = orderService.getById(1L);
        Optional<Plane> planeOpt1 = planeService.getById(1L);
        planeOpt1.ifPresent(plane -> {
            orderOpt1.ifPresent(order -> {
                Optional<UserEntity> userOpt = userService.getById(order.getUser().getId());
                userOpt.ifPresent(user -> {
                    ticket1.setId(1L);
                    ticket1.setOrder(order);
                    ticket1.setPlane(plane);
                    ticket1.setUser(user);
                    ticket1.setInitialPrice(155);
                });
            });
        });

        Optional<Ticket> ticketOpt = ticketService.getById(1L);
        ticketOpt.ifPresent(ticket -> {
            Assertions.assertEquals(ticket.getId(), ticket1.getId());
            Assertions.assertEquals(ticket.getPlane().getId(), ticket1.getPlane().getId());
            Assertions.assertEquals(ticket.getOrder().getId(), ticket1.getOrder().getId());
            Assertions.assertEquals(ticket.getUser().getId(), ticket1.getUser().getId());
            Assertions.assertEquals(ticket.getInitialPrice(), ticket1.getInitialPrice());
        });
    }

    @Test
    void getAll() {
        List<Ticket> allTickets = ticketService.getAll();
        Assertions.assertEquals(allTickets.size(), ticketRepository.count());
    }

    @Test
    void edit() {
        Long idToEdit = 1L;
        Optional<Ticket> ticketOpt = ticketService.getById(idToEdit);
        AtomicReference<Long> planeId = new AtomicReference<>();
        AtomicReference<Long> userId = new AtomicReference<>();
        AtomicReference<Long> orderId = new AtomicReference<>();
        AtomicReference<Long> initialPrice = new AtomicReference<>();
        ticketOpt.ifPresent(ticket -> {
            planeId.set(ticket.getPlane().getId());
            userId.set(ticket.getUser().getId());
            orderId.set(ticket.getOrder().getId());
            initialPrice.set(Long.valueOf(ticket.getInitialPrice()));
        });
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setPlaneId(2L);
        ticketDTO.setOrderId(2L);
        ticketDTO.setUserId(2L);
        ticketDTO.setInitialPrice(200);

        ticketService.edit(idToEdit, ticketDTO);
        Optional<Ticket> ticketOpt1 = ticketService.getById(idToEdit);
        ticketOpt1.ifPresent(ticket -> {
            Assertions.assertEquals(ticketDTO.getInitialPrice(), ticket.getInitialPrice());
            Assertions.assertEquals(ticketDTO.getPlaneId(), ticket.getPlane().getId());
            Assertions.assertEquals(ticketDTO.getOrderId(), ticket.getOrder().getId());
            Assertions.assertEquals(ticketDTO.getUserId(), ticket.getUser().getId());
        });

        ticketDTO.setPlaneId(planeId.get());
        ticketDTO.setOrderId(orderId.get());
        ticketDTO.setUserId(userId.get());
        ticketDTO.setInitialPrice(initialPrice.get().intValue());
        ticketService.edit(idToEdit, ticketDTO);
        Optional<Ticket> ticketOpt2 = ticketService.getById(idToEdit);
        ticketOpt2.ifPresent(ticket -> {
            Assertions.assertEquals(ticketDTO.getInitialPrice(), ticket.getInitialPrice());
            Assertions.assertEquals(ticketDTO.getPlaneId(), ticket.getPlane().getId());
            Assertions.assertEquals(ticketDTO.getOrderId(), ticket.getOrder().getId());
            Assertions.assertEquals(ticketDTO.getUserId(), ticket.getUser().getId());
        });
    }

    @Test
    void save() {
        long count = ticketRepository.count();
        Ticket ticket1 = getTicket();

        Ticket savedTicket = ticketService.save(ticket1);
        Optional<Ticket> ticketOpt = ticketService.getById(savedTicket.getId());
        ticketOpt.ifPresent(ticket -> {
            Assertions.assertEquals(count + 1, ticketRepository.count());
            Assertions.assertEquals(savedTicket.getId(), ticket.getId());
            Assertions.assertEquals(savedTicket.getUser().getId(), ticket.getUser().getId());
            Assertions.assertEquals(savedTicket.getPlane().getId(), ticket.getPlane().getId());
            Assertions.assertEquals(savedTicket.getOrder().getId(), ticket.getOrder().getId());
            Assertions.assertEquals(savedTicket.getInitialPrice(), ticket.getInitialPrice());
        });

        ticketService.delete(savedTicket.getId());
        Assertions.assertEquals(count, ticketRepository.count());
    }

    @Test
    void mostActiveUserId() {
        Long mostActiveUserId = ticketService.mostActiveUserId();
        List<Long> userIds = ticketService
                .getAll()
                .stream()
                .map(ticket -> ticket.getUser().getId())
                .toList();

        Map<Long, Integer> mostFrequent = new HashMap<>();
        userIds
                .forEach(id -> {
                    Integer count = mostFrequent.get(id) == null ? 1 : mostFrequent.get(id) + 1;
                    mostFrequent.put(id, count);
                });

        Integer max = Collections.max(mostFrequent.values());
        Long mostFrequentUserId = null;
        for (Map.Entry<Long, Integer> entry : mostFrequent.entrySet()) {
            if (Objects.equals(entry.getValue(), max)) {
                mostFrequentUserId = entry.getKey();
            }
        }
        Assertions.assertEquals(mostActiveUserId, mostFrequentUserId);
    }

    @Test
    void mostBoughtTicketsPlaneId() {
        Long planeId = ticketService.mostBoughtTicketsPlaneId();

        List<Long> planeIds = ticketService
                .getAll()
                .stream()
                .map(ticket -> ticket.getPlane().getId())
                .toList();

        Map<Long, Integer> mostFrequent = new HashMap<>();
        planeIds
                .forEach(id -> {
                    Integer count = mostFrequent.get(id) == null ? 1 : mostFrequent.get(id) + 1;
                    mostFrequent.put(id, count);
                });

        Integer max = Collections.max(mostFrequent.values());
        Long mostFrequentPlaneId = null;
        for (Map.Entry<Long, Integer> entry : mostFrequent.entrySet()) {
            if (Objects.equals(entry.getValue(), max)) {
                mostFrequentPlaneId = entry.getKey();
            }
        }
        Assertions.assertEquals(planeId, mostFrequentPlaneId);
    }

    @Test
    void deleteById() {
        long count = ticketRepository.count();

        Ticket ticketToDelete = getTicket();
        Ticket savedTicket = ticketService.save(ticketToDelete);
        Assertions.assertEquals(count + 1, ticketRepository.count());

        ticketRepository.deleteById(savedTicket.getId());
        Assertions.assertEquals(count, ticketRepository.count());
    }

    private Ticket getTicket() {
        Ticket ticket1 = new Ticket();
        Optional<Order> orderOpt1 = orderService.getById(1L);
        Optional<Plane> planeOpt3 = planeService.getById(3L);
        planeOpt3.ifPresent(plane -> {
            orderOpt1.ifPresent(order -> {
                Optional<UserEntity> userOpt = userService.getById(order.getUser().getId());
                userOpt.ifPresent(user -> {
                    ticket1.setOrder(order);
                    ticket1.setPlane(plane);
                    ticket1.setUser(user);
                    ticket1.setInitialPrice(155);
                });
            });
        });
        return ticket1;
    }

}