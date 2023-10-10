package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.dto.OrderDTO;
import coherentsolutions.airapp.model.entity.Order;
import coherentsolutions.airapp.model.entity.UserEntity;
import coherentsolutions.airapp.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserService userService;

    @Test
    void getById() {
        Order order1 = new Order();
        Optional<UserEntity> userOpt1 = userService.getById(1L);
        userOpt1.ifPresent(order1::setUser);

        Optional<Order> orderOpt = orderService.getById(1L);
        orderOpt.ifPresent(order -> {
            Assertions.assertEquals(order.getUser().getId(), order1.getUser().getId());
        });
    }

    @Test
    void getAll() {
        long count = orderRepository.count();
        List<Order> allOrders = orderService.getAll();
        Assertions.assertEquals(count, allOrders.size());
    }

    @Test
    void edit() {
        AtomicReference<Long> userId = new AtomicReference<>(null);
        Optional<Order> orderOpt = orderService.getById(3L);
        orderOpt.ifPresent(order -> {
            userId.set(order.getUser().getId());
        });

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderService.edit(3L, orderDTO);
        Optional<Order> orderOpt1 = orderService.getById(3L);
        orderOpt1.ifPresent(order -> {
            Assertions.assertEquals(orderDTO.getUserId(), order.getUser().getId());
        });

        orderDTO.setUserId(Long.parseLong(userId.toString()));
        orderService.edit(3L, orderDTO);
        Optional<Order> orderOpt2 = orderService.getById(3L);
        orderOpt2.ifPresent(order -> {
            Assertions.assertEquals(orderDTO.getUserId(), order.getUser().getId());
        });
    }

    @Test
    void save() {
        Order order = getOrder();
        long count = orderRepository.count();

        orderService.save(order);
        Assertions.assertEquals(count + 1, orderRepository.count());
        Optional<Order> orderOpt = orderService.getById(count + 1);
        orderOpt.ifPresent(order1 -> {
            Assertions.assertEquals(order.getUser().getId(), order1.getUser().getId());
        });

        orderService.delete(orderRepository.count());
        Assertions.assertEquals(count, orderRepository.count());
    }

    @Test
    void deleteById() {
        Order order = getOrder();
        long count = orderRepository.count();

        Order savedOrder = orderService.save(order);
        Assertions.assertEquals(count + 1, orderRepository.count());

        orderService.delete(savedOrder.getId());
        Assertions.assertEquals(count, orderRepository.count());
    }

    private Order getOrder() {
        Optional<UserEntity> userOpt1 = userService.getById(1L);
        Order order = new Order();
        userOpt1.ifPresent(order::setUser);
        return order;
    }

}