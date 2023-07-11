package com.app.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;

@Configuration
public class TextractConfig {

    @Bean
    public TextractClient textractClient() {
        // Add your AWS credentials and configuration here
        return TextractClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
