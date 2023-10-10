package coherentsolutions.airapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.spring.web.json.Json;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {

    private String name;

    private TemperatureDTO main;

}
