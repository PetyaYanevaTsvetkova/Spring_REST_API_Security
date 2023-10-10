package coherentsolutions.airapp.service;

import coherentsolutions.airapp.config.WebClientConfiguration;
import coherentsolutions.airapp.model.dto.WeatherDto;
import coherentsolutions.airapp.model.entity.Weather;
import coherentsolutions.airapp.repository.WeatherRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final WebClientConfiguration webClientConfiguration;

    public Double temperatureDouble(String city) {
        String weatherByCity = webClientConfiguration.getWeatherByCityResponse(city);
        Gson gson = new Gson();
        WeatherDto weather = gson.fromJson(weatherByCity, WeatherDto.class);
        return weather.getMain().getTemp();
    }

    public Weather save(Weather weather) {
        return weatherRepository.save(weather);
    }

    public Double getTemperatureFromRepository(String city) {
        return weatherRepository.findByName(city).getTemp();
    }

    public boolean isEmpty() {
        return weatherRepository.count() == 0;
    }

    public Weather weatherByCity(String city) {
        return weatherRepository.findByName(city);
    }

}
