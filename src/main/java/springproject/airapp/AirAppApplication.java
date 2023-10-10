package coherentsolutions.airapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "National Air application",
        version = "V1.0",
        description = "National Air is an application in which user can" +
                " create registration with several properties and buy a ticket"))
public class AirAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirAppApplication.class, args);
    }

}
