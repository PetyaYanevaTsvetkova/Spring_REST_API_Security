package coherentsolutions.airapp.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllOrdersDTO {

    private final List<OrderDTO> orders;
}
