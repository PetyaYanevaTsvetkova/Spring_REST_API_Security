package coherentsolutions.airapp.mapper;

import coherentsolutions.airapp.model.dto.WeatherDto;

import coherentsolutions.airapp.model.entity.Weather;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    WeatherDto toWeatherDTO(Weather weather);

    Weather toWeatherEntity(WeatherDto weatherDto);
}
