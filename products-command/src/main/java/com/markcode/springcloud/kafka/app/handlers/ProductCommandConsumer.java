package com.markcode.springcloud.kafka.app.handlers;

import com.markcode.springcloud.kafka.app.entities.Product;
import com.markcode.springcloud.kafka.app.models.Command;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import com.markcode.springcloud.kafka.app.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ProductCommandConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandConsumer.class);

    private final ProductService productService;

    public ProductCommandConsumer(ProductService productService) {
        this.productService = productService;
    }

    @Bean
    public Consumer<Command<ProductDTO>> handleCommands() {
        return cmd -> {

            String type = cmd.type() == null ? "" : cmd.type().toUpperCase();

            switch (type) {
                case "CREATE" -> {
                    if(cmd.body() == null) {
                        log.warn("Create empty body");
                        return;
                    }

                    ProductDTO productDTO = cmd.body();
                    productService.create(productDTO);

                    log.info("Creating product:  name={}, price={}", productDTO.name(), productDTO.price());
                }
                case "UPDATE" -> {
                    log.info("Creating product:  name=, price=");
                }
                case "DELETE" -> {
                    log.info("Creating product:  name=, price=");
                }
                case "READ_ALL" -> {
                    log.info("Creating product:  name=, price=");
                }
                default -> log.warn("Unknown command type={}", type);
            }

        };
    }
}
