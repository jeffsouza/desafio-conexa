package com.conexa.hospital.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:messages.properties")
public class MessageProperties {
    
    @Value("${resource_not_found}")
    private String resourceNotFound;

    public String resourceNotFound() {
        return resourceNotFound;
    }

}
