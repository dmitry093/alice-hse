package ru.hse.alice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hse.alice.contracts.IUserService;
import ru.hse.alice.services.UserService;

@Configuration
public class ServicesConfig {
    @Bean
    public IUserService getUserService() {
        return new UserService();
    }
}
