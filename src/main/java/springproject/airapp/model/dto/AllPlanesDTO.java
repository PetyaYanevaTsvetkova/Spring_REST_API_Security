package coherentsolutions.airapp.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllPlanesDTO {

    private final List<PlaneDTO> planes;
}
