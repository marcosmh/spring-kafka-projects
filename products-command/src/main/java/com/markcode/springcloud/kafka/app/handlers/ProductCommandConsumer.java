package com.markcode.springcloud.kafka.app.handlers;

import com.markcode.springcloud.kafka.app.models.Command;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ProductCommandConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandConsumer.class);

    @Bean
    public Consumer<Command<ProductDTO>> handleCommands() {
        return cmd -> {
            log.info("Comando recibido y consumido con exito : type={}, body={}",cmd.type(), cmd.body());
        };
    }
}
