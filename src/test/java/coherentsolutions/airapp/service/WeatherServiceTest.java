package coherentsolutions.airapp.service;

import coherentsolutions.airapp.config.WebClientConfiguration;
import coherentsolutions.airapp.mapper.WeatherMapper;
import coherentsolutions.airapp.model.dto.WeatherDto;
import coherentsolutions.airapp.model.entity.Weather;
import coherentsolutions.airapp.repository.WeatherRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class WeatherServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceTest.class);
    private static final String CITY = "Plovdiv";
    private static final String CITY_FROM_DB = "Sofia";

    @Autowired
    WeatherService weatherService;

    @Autowired
    WeatherRepository weatherRepository;

    @Autowired
    WebClientConfiguration webClientConfiguration;

    @Autowired
    WeatherMapper weatherMapper;

    @Autowired
    PlaneService planeService;

    @Test
    void temperatureDoubleFromWebClient() {
        String weatherByCity = webClientConfiguration.getWeatherByCityResponse(CITY_FROM_DB);
        Gson gson = new Gson();
        WeatherDto weather1 = gson.fromJson(weatherByCity, WeatherDto.class);
        Double temp = weather1.getMain().getTemp();

        Double temperature = weatherService.getTemperatureFromRepository(CITY_FROM_DB);
        Assertions.assertNotNull(temperature);
        Assertions.assertEquals(temp, temperature);
    }

    @Test
    void save() {
        long count = weatherRepository.count();
        Weather weather = getWeather(CITY);

        Weather savedWeather = weatherService.save(weather);
        Optional<Weather> weatherOpt = weatherRepository.findById(savedWeather.getId());
        weatherOpt.ifPresent(weather1 -> {
            Assertions.assertEquals(count + 1, weatherRepository.count());
            Assertions.assertEquals(savedWeather.getTemp(), weather1.getTemp());
            Assertions.assertEquals(savedWeather.getName(), weather1.getName());
        });

        weatherRepository.deleteById(savedWeather.getId());
        Assertions.assertEquals(count, weatherRepository.count());
    }

    @Test
    void getTemperatureFromRepository() {
        Double temperature = weatherService.getTemperatureFromRepository(CITY_FROM_DB);
        Weather weather = weatherService.weatherByCity(CITY_FROM_DB);
        Assertions.assertNotNull(temperature);
        Assertions.assertEquals(weather.getTemp(), temperature);
    }

    @Test
    void weatherByCity() {
        Weather weather = weatherService.weatherByCity(CITY_FROM_DB);
        Assertions.assertNotNull(weather);
        Double temperatureFromRepository = weatherService.getTemperatureFromRepository(CITY_FROM_DB);
        Assertions.assertEquals(weather.getTemp(), temperatureFromRepository);
    }

    private Weather getWeather(String city) {
        String weatherByCity = webClientConfiguration.getWeatherByCityResponse(city);
        Gson gson = new Gson();
        WeatherDto weatherDto = gson.fromJson(weatherByCity, WeatherDto.class);
        return weatherMapper.toWeatherEntity(weatherDto);
    }

}