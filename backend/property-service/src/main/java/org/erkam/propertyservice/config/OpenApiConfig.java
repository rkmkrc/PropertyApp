package org.erkam.propertyservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Erkam Karaca",
                        email = "erkamkaracaa@gmail.com",
                        url = "https://erkamkaraca.com"
                ),
                description = "OpenApi Documentation for Property App.",
                title = "API Documentation of Property App© - Erkam Karaca",
                version = "1,0",
                license = @License(
                        name = "My Licence",
                        url = "https://github.com/rkmkrc"
                ),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Development Env",
                        url = "https://github.com/rkmkrc"
                ),
                @Server(
                        description = "Production Env",
                        url = "https://github.com/rkmkrc"
                )
        }
)
public class OpenApiConfig {
}
