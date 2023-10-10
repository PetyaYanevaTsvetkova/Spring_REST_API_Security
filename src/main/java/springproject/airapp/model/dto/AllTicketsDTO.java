package coherentsolutions.airapp.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllTicketsDTO {

    private final List<TicketDTO> tickets;
}
