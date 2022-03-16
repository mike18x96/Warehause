package com.inetum.warehouse.config;

import com.inetum.warehouse.model.Purchase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Purchase purchase() {
        return new Purchase();
    }

}
