package coherentsolutions.airapp;

import coherentsolutions.airapp.annotations.Audit;

import coherentsolutions.airapp.model.entity.*;
import coherentsolutions.airapp.model.enums.UserRoleEnum;

import coherentsolutions.airapp.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommandLineImpl implements CommandLineRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(CommandLineImpl.class);

    private final UserRoleService userRoleService;
    private final UserService userService;
    private final OrderService orderService;
    private final TicketService ticketService;
    private final PlaneService planeService;
    private final AirportService airportService;
    private final WeatherService weatherService;
    private final AopService aopService;

    @Override
    public void run(String... args) {

        createDefaultData();
        storeWeather();

        try {
            aopService.makeToUpperCase("world");
            aopService.sayMessage();
            aopService.errorAppears();
        } catch (Exception ex) {
            LOGGER.info("Exception from errorAppears method called.");
        }
    }

    @Scheduled(fixedRate = 10000)
    public void storeWeather() {
        if (weatherService.isEmpty()) {
            planeService.getAll()
                    .stream()
                    .map(plane -> {
                        String destination = plane.getDestination();
                        Weather weather = new Weather();
                        weather.setName(destination);
                        weather.setTemp(weatherService.temperatureDouble(destination));
                        weatherService.save(weather);
                        return null;
                    }).collect(Collectors.toList());
        } else {
            planeService.getAll()
                    .stream()
                    .map(plane -> {
                        String destination = plane.getDestination();
                        Weather weatherByCity = weatherService.weatherByCity(destination);
                        weatherByCity.setTemp(weatherService.temperatureDouble(destination));
                        weatherService.save(weatherByCity);
                        return null;
                    })
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public void createDefaultData() {
        createDefaultRoles();
        createDefaultUsers();
        createDefaultAirports();
        createDefaultPlanes();
        createDefaultOrders();
        createDefaultTickets();
    }

    @Audit
    public void createDefaultRoles() {
        UserRoleEntity roleUser = new UserRoleEntity();
        roleUser.setUserRole(UserRoleEnum.ROLE_USER);
        userRoleService.save(roleUser);

        UserRoleEntity roleAdmin = new UserRoleEntity();
        roleAdmin.setUserRole(UserRoleEnum.ROLE_ADMIN);
        userRoleService.save(roleAdmin);

    }

    private void createDefaultAirports() {
        Airport airportSofia = new Airport();
        airportSofia.setId(1L);
        airportSofia.setCityName("Sofia");
        Optional<UserEntity> userOpt1 = userService.getById(1L);
        userOpt1.ifPresent(airportSofia::setAirportAdministrator);

        Airport airportLondon = new Airport();
        airportLondon.setId(2L);
        airportLondon.setCityName("London");
        Optional<UserEntity> userOpt2 = userService.getById(2L);
        userOpt2.ifPresent(airportLondon::setAirportAdministrator);

        Airport santiagoDeCompostela = new Airport();
        santiagoDeCompostela.setId(3L);
        santiagoDeCompostela.setCityName("Santiago de Compostela");
        Optional<UserEntity> userOpt3 = userService.getById(3L);
        userOpt3.ifPresent(santiagoDeCompostela::setAirportAdministrator);

        airportService.save(airportSofia);
        airportService.save(airportLondon);
        airportService.save(santiagoDeCompostela);
    }

    private void createDefaultPlanes() {
        Plane plane1 = new Plane();
        plane1.setId(1L);
        plane1.setAirport(airportService.getByCity("Sofia"));
        plane1.setDestination("Paris");

        Plane plane2 = new Plane();
        plane2.setId(2L);
        plane2.setAirport(airportService.getByCity("London"));
        plane2.setDestination("London");

        Plane plane3 = new Plane();
        plane3.setId(3L);
        plane3.setAirport(airportService.getByCity("Santiago de Compostela"));
        plane3.setDestination("Sofia");

        planeService.save(plane1);
        planeService.save(plane2);
        planeService.save(plane3);
    }

    private void createDefaultUsers() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1L);
        userEntity1.setFirstName("Nick");
        userEntity1.setLastName("Doe");
        userEntity1.setEmail("nick@yahoo.com");
        userEntity1.setPhone("889754612");
        userEntity1.setPassword("123");
        userEntity1.setUserRole(userRoleService.getByUserRole(UserRoleEnum.ROLE_ADMIN));
        userService.save(userEntity1);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(2L);
        userEntity2.setFirstName("Jack");
        userEntity2.setLastName("Lee");
        userEntity2.setEmail("jack@yahoo.com");
        userEntity2.setPhone("8897545542");
        userEntity2.setPassword("123");
        userEntity2.setUserRole(userRoleService.getByUserRole(UserRoleEnum.ROLE_ADMIN));
        userService.save(userEntity2);

        UserEntity userEntity3 = new UserEntity();
        userEntity3.setId(3L);
        userEntity3.setFirstName("Bob");
        userEntity3.setLastName("John");
        userEntity3.setEmail("bob@yahoo.com");
        userEntity3.setPhone("88947545542");
        userEntity3.setPassword("123");
        userEntity3.setUserRole(userRoleService.getByUserRole(UserRoleEnum.ROLE_USER));
        userService.save(userEntity3);
    }

    private void createDefaultOrders() {
        Optional<UserEntity> userOpt1 = userService.getById(1L);
        Optional<UserEntity> userOpt2 = userService.getById(2L);
        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();

        userOpt1.ifPresent(user -> {
            order1.setId(1L);
            order1.setUser(user);
        });

        userOpt2.ifPresent(user -> {
            order2.setId(2L);
            order2.setUser(user);
            order3.setId(3L);
            order3.setUser(user);
        });

        orderService.save(order1);
        orderService.save(order2);
        orderService.save(order3);
    }

    private void createDefaultTickets() {
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        Ticket ticket3 = new Ticket();
        Ticket ticket4 = new Ticket();
        Ticket ticket5 = new Ticket();
        Ticket ticket6 = new Ticket();
        Ticket ticket7 = new Ticket();
        Ticket ticket8 = new Ticket();

        Optional<Order> orderOpt1 = orderService.getById(1L);
        Optional<Order> orderOpt2 = orderService.getById(2L);
        Optional<Order> orderOpt3 = orderService.getById(3L);

        Optional<Plane> planeOpt1 = planeService.getById(1L);
        Optional<Plane> planeOpt2 = planeService.getById(2L);
        Optional<Plane> planeOpt3 = planeService.getById(3L);

        planeOpt1.ifPresent(plane -> {
            orderOpt1.ifPresent(order -> {
                Optional<UserEntity> userOpt = userService.getById(order.getUser().getId());

                userOpt.ifPresent(user -> {

                    ticket1.setId(1L);
                    ticket1.setOrder(order);
                    ticket1.setPlane(plane);
                    ticket1.setUser(user);
                    ticket1.setInitialPrice(155);

                    ticket2.setId(2L);
                    ticket2.setOrder(order);
                    ticket2.setPlane(plane);
                    ticket2.setUser(user);
                    ticket2.setInitialPrice(155);
                });
            });
        });

        planeOpt2.ifPresent(plane -> {
            orderOpt2.ifPresent(order -> {
                Optional<UserEntity> userOpt1 = userService.getById(order.getUser().getId());

                userOpt1.ifPresent(user -> {

                    ticket3.setId(3L);
                    ticket3.setOrder(order);
                    ticket3.setPlane(plane);
                    ticket3.setUser(user);
                    ticket3.setInitialPrice(155);

                    ticket4.setId(4L);
                    ticket4.setOrder(order);
                    ticket4.setPlane(plane);
                    ticket4.setUser(user);
                    ticket4.setInitialPrice(155);

                    ticket5.setId(5L);
                    ticket5.setOrder(order);
                    ticket5.setPlane(plane);
                    ticket5.setUser(user);
                    ticket5.setInitialPrice(155);

                    ticket6.setId(6L);
                    ticket6.setOrder(order);
                    ticket6.setPlane(plane);
                    ticket6.setUser(user);
                    ticket6.setInitialPrice(155);
                });
            });
        });

        planeOpt3.ifPresent(plane -> {
            orderOpt3.ifPresent(order -> {
                Optional<UserEntity> userOpt = userService.getById(order.getUser().getId());

                userOpt.ifPresent(user -> {

                    ticket7.setId(7L);
                    ticket7.setOrder(order);
                    ticket7.setPlane(plane);
                    ticket7.setUser(order.getUser());
                    ticket7.setInitialPrice(155);

                    ticket8.setId(8L);
                    ticket8.setOrder(order);
                    ticket8.setPlane(plane);
                    ticket8.setUser(order.getUser());
                    ticket8.setInitialPrice(155);
                });
            });
        });

        ticketService.save(ticket1);
        ticketService.save(ticket2);
        ticketService.save(ticket3);
        ticketService.save(ticket4);
        ticketService.save(ticket5);
        ticketService.save(ticket6);
        ticketService.save(ticket7);
        ticketService.save(ticket8);
    }

}
