package pl.pjwstk.kodabackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("KODA - Car Trading Platform API")
                        .version("1.0.0")
                        .description("""
                                🚗 **KODA Car Trading Platform REST API**
                                
                                A comprehensive API for car trading platform that allows users to:
                                - Browse and search car offers with advanced filtering
                                - Create and manage car listings with detailed specifications
                                - Handle user authentication and authorization
                                - Manage car equipment and technical details
                                - Upload offer images
                                - Messaging between buyers and sellers
                                - User favorites and saved searches
                                
                                **Features:**
                                - JWT-based authentication
                                - Advanced search and filtering capabilities
                                - File upload support for car images
                                - Comprehensive car specification database
                                
                                **Tech Stack:**
                                - Spring Boot 3.4.1
                                - Spring Security 6
                                - Spring Data JPA
                                - Spring WebSocket & STOMP
                                - Spring Modulith (modular architecture)
                                - PostgreSQL database
                                - Liquibase for DB migrations
                                - JWT authentication (jwt)
                                - Jakarta Validation API
                                - SpringDoc OpenAPI
                                """))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8137")
                                .description("Local Development Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token for API authentication")));
    }
}