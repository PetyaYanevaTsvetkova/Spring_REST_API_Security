package coherentsolutions.airapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    private final WebClient webClient;

    public WebClientConfiguration(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(url1).build();
    }

    @Value("${app.webClientConfiguration.url1}")
    String url1;
    @Value("${app.webClientConfiguration.url2}")
    String url2;
    @Value("${app.webClientConfiguration.key}")
    String key;

    public String getWeatherByCityResponse(String city) {
        return webClient
                .get()
                .uri(url1 + city + url2 + key)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
