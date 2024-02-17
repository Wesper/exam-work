package ru.candle.store.ui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UIConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
