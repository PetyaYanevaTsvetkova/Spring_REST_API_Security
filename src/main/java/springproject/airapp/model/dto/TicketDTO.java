package coherentsolutions.airapp.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TicketDTO {

    private Long orderId;

    private Long userId;

    private Long planeId;

    private Integer initialPrice;

}
