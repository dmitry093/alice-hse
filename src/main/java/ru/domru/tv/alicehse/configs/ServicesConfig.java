package ru.domru.tv.alicehse.configs;

import org.springframework.context.annotation.Bean;
import ru.domru.tv.alicehse.contracts.IRequestProcessor;
import ru.domru.tv.alicehse.contracts.IUserService;
import ru.domru.tv.alicehse.services.RequestProcessor;
import ru.domru.tv.alicehse.services.UserService;

public class ServicesConfig {
    @Bean
    public IUserService getUserService() {
        return new UserService();
    }

    @Bean
    public IRequestProcessor getRequestProcessor(IUserService userService) {
        return new RequestProcessor(userService);
    }
}
