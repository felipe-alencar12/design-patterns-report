package com.report.report_api_pattern.config;

import com.report.report_api_pattern.client.DeepSeekClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DeepSeekConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DeepSeekClient deepSeekClient(
            @Value("${deepseek.api.key}") String apiKey,
            RestTemplate restTemplate
    ) {
        return new DeepSeekClient(apiKey, restTemplate);
    }
}
