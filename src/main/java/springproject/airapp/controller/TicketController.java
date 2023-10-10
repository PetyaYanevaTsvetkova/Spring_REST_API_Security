package coherentsolutions.airapp.controller;

import coherentsolutions.airapp.mapper.TicketMapper;
import coherentsolutions.airapp.model.dto.AllTicketsDTO;
import coherentsolutions.airapp.model.dto.TicketDTO;
import coherentsolutions.airapp.model.dto.UserDTO;
import coherentsolutions.airapp.model.entity.Ticket;
import coherentsolutions.airapp.model.exception.ObjectNotFoundException;
import coherentsolutions.airapp.service.TicketService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/getTicketPrice/{id}")
    public String getActualTicketPrice(@PathVariable("id") Long id) {
        return String.format("Price for ticket with id %d is %d", id, ticketService.getActualTicketPrice(id));
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/buy-tickets")
    public String buyTickets(@Valid @RequestBody TicketDTO ticketDTO) {
        ticketService.save(ticketMapper.toTicket(ticketDTO));
        return "Тhe ticket has been purchased successfully!";
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("/cancel/{id}")
    public String cancellingTickets(@PathVariable("id") Long id) {
        AtomicReference<String> result = new AtomicReference<>();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String loggedUsername = ((UserDetails) principal).getUsername();
            Optional<Ticket> ticketOpt = ticketService.getById(id);
            ticketOpt.ifPresent(ticket -> {
                String email = ticket.getUser().getEmail();
                if (loggedUsername.equals(email)) {
                    ticketService.delete(id);
                    result.set(String.format("Тhe ticket with id %d has been cancelled successfully", id));
                } else {
                    result.set("The user has no right to cancel the ticket");
                }
            });
        }
        return result.toString();
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    @ApiOperation(value = "Get ticket by id", response = TicketDTO.class, notes = "Ticket must exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Ticket not found", response = ObjectNotFoundException.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public TicketDTO getById(@PathVariable("id") Long id) {
        return ticketMapper.toTicketDTO(
                ticketService
                        .getById(id)
                        .orElseThrow(() -> new ObjectNotFoundException(id))
        );
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/all")
    @ApiOperation(value = "Get all tickets", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public AllTicketsDTO getAll() {
        List<TicketDTO> ticketDTOS = ticketService.getAll()
                .stream()
                .map(ticketMapper::toTicketDTO)
                .collect(Collectors.toList());
        return new AllTicketsDTO(ticketDTOS);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/new")
    @ApiOperation(value = "Create ticket", response = TicketDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Invalid input supplied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public TicketDTO createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        Ticket ticket = ticketService.save(ticketMapper.toTicket(ticketDTO));
        return ticketMapper.toTicketDTO(ticket);
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete ticket")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable("id") Long id) {
        ticketService.delete(id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Edit ticket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void editTicket(@PathVariable Long id, @Valid @RequestBody TicketDTO ticketDTO) {
        ticketService.edit(id, ticketDTO);
    }

}
