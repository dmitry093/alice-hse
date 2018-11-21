package ru.domru.tv.alicehse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.domru.tv.alicehse.configs.ServicesConfig;

@SpringBootApplication
@Import(ServicesConfig.class)
public class AliceHseApplication {

    public static void main(String[] args) {
        SpringApplication.run(AliceHseApplication.class, args);
    }
}
