package com.thinkhumble.real_time_stock_quote.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Real-Time Stock Quote API")
                        .version("1.0")
                        .description("API to fetch real-time stock quotes")
                        .contact(new Contact()
                                .name("Dhawal Kamdar")
                                .email("dhawalkamdar12@gmail.com")
                        ));
    }
}
