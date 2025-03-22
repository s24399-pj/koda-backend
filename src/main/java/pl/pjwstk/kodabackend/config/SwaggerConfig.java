package pl.pjwstk.kodabackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${koda.frontend.url}") String frontendUrl) {

        return new OpenAPI()
                .info(new Info()
                        .title("KODA - API aplikacji handlu samochodowego")
                        .version("1.0.0")
                        .description("Aplikacja do handlu samochodami")
                        .contact(new Contact()
                                .name("Zespół KODA"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url(frontendUrl).description("Serwer lokalny")
                ));
    }
}