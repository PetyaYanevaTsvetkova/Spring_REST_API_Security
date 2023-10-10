package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.dto.OrderDTO;
import coherentsolutions.airapp.model.entity.Order;
import coherentsolutions.airapp.model.entity.UserEntity;
import coherentsolutions.airapp.repository.OrderRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    public Optional<Order> getById(Long id) {
        return orderRepository.findById(id);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getAll() {
        return (List<Order>) orderRepository.findAll();
    }

    public void edit(Long id, OrderDTO orderDTO) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        Optional<UserEntity> userOpt = userService.getById(orderDTO.getUserId());
        orderOpt.ifPresent(order -> {
            order.setId(id);
            order.setUser(userOpt.orElse(order.getUser()));
            orderRepository.save(order);
        });
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
