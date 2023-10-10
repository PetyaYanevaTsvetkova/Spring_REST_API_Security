package coherentsolutions.airapp.mapper;

import coherentsolutions.airapp.model.dto.OrderDTO;

import coherentsolutions.airapp.model.entity.Order;

import coherentsolutions.airapp.model.entity.UserEntity;

import coherentsolutions.airapp.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Autowired
    private UserService userService;

    @Mapping(source = "user.id", target = "userId")
    public abstract OrderDTO toOrderDTO(Order order);

    public Order toOrder(OrderDTO orderDTO) {
        Optional<UserEntity> userOpt = userService.getById(orderDTO.getUserId());
        Order order = new Order();
        userOpt.ifPresent(order::setUser);
        return order;
    }
}
