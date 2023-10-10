package coherentsolutions.airapp.controller;

import coherentsolutions.airapp.mapper.OrderMapper;

import coherentsolutions.airapp.model.dto.AllOrdersDTO;

import coherentsolutions.airapp.model.dto.OrderDTO;

import coherentsolutions.airapp.model.entity.Order;

import coherentsolutions.airapp.model.exception.ObjectNotFoundException;
import coherentsolutions.airapp.service.OrderService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    @ApiOperation(value = "Get order by id", response = OrderDTO.class, notes = "Order must exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Order not found", response = ObjectNotFoundException.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<OrderDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                orderMapper.toOrderDTO(orderService
                        .getById(id)
                        .orElseThrow(() -> new ObjectNotFoundException(id))));
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/all")
    @ApiOperation(value = "Get all orders", response = OrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public AllOrdersDTO getAll() {
        List<OrderDTO> orderDTOS = orderService.getAll()
                .stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
        return new AllOrdersDTO(orderDTOS);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/new")
    @ApiOperation(value = "Create order", response = OrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Invalid input supplied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        Order order = orderService.save(orderMapper.toOrder(orderDTO));
        OrderDTO orderResponse = orderMapper.toOrderDTO(order);
        return new ResponseEntity<OrderDTO>(orderResponse, HttpStatus.CREATED);
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete order")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("id") Long id) {
        orderService.delete(id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Edit order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void editOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
        orderService.edit(id, orderDTO);
    }

}
