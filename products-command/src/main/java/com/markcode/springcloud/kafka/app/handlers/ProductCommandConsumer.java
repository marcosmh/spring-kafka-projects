package com.markcode.springcloud.kafka.app.handlers;


import com.markcode.springcloud.kafka.app.entities.Product;
import com.markcode.springcloud.kafka.app.models.Command;
import com.markcode.springcloud.kafka.app.models.CommandType;
import com.markcode.springcloud.kafka.app.models.Reply;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import com.markcode.springcloud.kafka.app.services.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.function.Consumer;
import java.util.function.Function;


@Configuration
public class ProductCommandConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandConsumer.class);

    private final ProductService productService;

    public ProductCommandConsumer(ProductService productService) {
        this.productService = productService;
    }

    @Bean
    public Function<Message<Command<ProductDTO>>, Message<Reply<?>> > handleCommands() {
        return msg -> {
            Command<ProductDTO> cmd = msg.getPayload();
            //String type = cmd.type() == null ? "" : cmd.type().toUpperCase();
            Reply<?> reply = null;


            switch (cmd.type()) {
                case  CommandType.CREATE -> {
                    if(cmd.body() == null) {
                        log.warn("Create empty body");
                        reply = new Reply<>("ERROR", "Create Empty Body", null);
                    }

                    ProductDTO productSave =productService.create(cmd.body());

                    log.info("Creating product:  name={}, price={}", productSave.name(), productSave.price());
                    reply = new Reply<>("SUCCESS", "Create product: ", productSave);
                }
                case CommandType.READ -> {
                    if(cmd.id() == null) {
                        log.warn("Id is required");
                        reply = new Reply<>("ERROR", "Id is required ", null);
                    }

                    ProductDTO dto = productService.findById(cmd.id());
                    reply = (dto == null) ?
                            new Reply<>("ERROR", "Product not found. ", null) :
                            new Reply<>("SUCCESS", "Read producto name: ", dto);

                    log.info("Reading product by id");
                }
                case CommandType.READ_ALL -> {
                    reply = new Reply<>("SUCCESS", "Read all products ", productService.findAll());
                    log.info("Reading all prodcuts");
                }
                case CommandType.UPDATE -> {
                    if(cmd.body() == null && cmd.id() == null) {
                        log.warn("Id and body is required");
                        reply = new Reply<>("ERROR", "Id and body is required", null);
                    }

                    ProductDTO dto = productService.findById(cmd.id());
                    if(dto != null) {
                        reply = new Reply<>("SUCCESS", "Update product name: ", dto);
                        log.info("Creating product:  name={}, price={}", dto.name(), dto.price());
                    } else {
                        reply = new Reply<>("ERROR", "Product not found ", null);
                        log.warn("Product not found");
                    }

                }
                case CommandType.DELETE -> {
                    if(cmd.id() == null) {
                        log.warn("Id is required");
                        reply = new Reply<>("ERROR", "Id is required ", null);
                    }
                    boolean result = productService.delete(cmd.id());
                    reply = (result) ?
                            new Reply<>("SUCCESS", "Update product name: ", "Delete") :
                            new Reply<>("ERROR", "Product not found ", null);
                    log.info("Deleting product");
                }
                default -> {
                    log.warn("Unknown command type={}", cmd.type());
                    reply = new Reply<>("ERROR","Unknown command type", null);
                }
            }

            String correlationId = msg.getHeaders().get("correlationId",String.class);
            log.info("Recibiendo correlationId={}", correlationId);
            MessageBuilder<Reply<?>> out = MessageBuilder.withPayload(reply);
            if(correlationId != null) {
                out.setHeader("correlationId",correlationId);
            }
            return out.build();
        };
    }
}
