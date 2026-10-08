package com.markcode.springcloud.kafka.app.handlers;


import com.markcode.springcloud.kafka.app.models.Command;
import com.markcode.springcloud.kafka.app.models.CommandType;
import com.markcode.springcloud.kafka.app.models.Reply;
import com.markcode.springcloud.kafka.app.models.ReplyStatus;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import com.markcode.springcloud.kafka.app.services.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.function.Function;


@Configuration
public class ProductCommandConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandConsumer.class);

    private final ProductService productService;

    public ProductCommandConsumer(ProductService productService) {
        this.productService = productService;
    }

    @Bean
    public Function<Message<Command<ProductDTO>>, Message<Reply<Object>> > handleCommands() {
        return msg -> {

            String correlationId = msg.getHeaders().get("correlationId",String.class);
            log.info("Recibiendo correlationId={}", correlationId);
            if(correlationId == null || correlationId.isBlank()) {
                return  MessageBuilder
                        .withPayload(new Reply<>(ReplyStatus.ERROR, "Missing correlationId",null))
                        .build();
            }

            Command<ProductDTO> cmd = msg.getPayload();
            Reply<Object> reply =
            switch (cmd.type()) {
                case  CommandType.CREATE -> {
                    if(cmd.body() == null) {
                        log.warn("Create empty body");
                        yield new Reply<>(ReplyStatus.ERROR, "Create Empty Body", null);
                    }

                    ProductDTO productSave =productService.create(cmd.body());

                    log.info("Creating product:  name={}, price={}", productSave.name(), productSave.price());
                    yield new Reply<>(ReplyStatus.SUCCESS , "Create product: ", productSave);
                }
                case CommandType.READ -> {
                    if(cmd.id() == null) {
                        log.warn("Id is required");
                        yield new Reply<>(ReplyStatus.ERROR, "Id is required ", null);
                    }

                    ProductDTO dto = productService.findById(cmd.id());

                    log.info("Reading product by id");
                    yield (dto == null) ?
                            new Reply<>(ReplyStatus.ERROR, "Product not found. ", null) :
                            new Reply<>(ReplyStatus.SUCCESS, "Read producto name: ", dto);


                }
                case CommandType.READ_ALL -> {
                    log.info("Reading all prodcuts");
                    yield new Reply<>(ReplyStatus.SUCCESS, "Read all products ", productService.findAll());

                }
                case CommandType.UPDATE -> {
                    if(cmd.body() == null && cmd.id() == null) {
                        log.warn("Id and body is required");
                        yield new Reply<>(ReplyStatus.ERROR, "Id and body is required", null);
                    }

                    ProductDTO dto = productService.findById(cmd.id());
                    if(dto != null) {
                        log.info("Creating product:  name={}, price={}", dto.name(), dto.price());
                        yield new Reply<>(ReplyStatus.SUCCESS, "Update product name: ", dto);

                    } else {
                        log.warn("Product not found");
                        yield new Reply<>(ReplyStatus.ERROR, "Product not found ", null);
                    }

                }
                case CommandType.DELETE -> {
                    if(cmd.id() == null) {
                        log.warn("Id is required");
                        yield new Reply<>(ReplyStatus.ERROR, "Id is required ", null);
                    }
                    boolean result = productService.delete(cmd.id());
                    log.info("Deleting product");
                    yield (result) ?
                            new Reply<>(ReplyStatus.SUCCESS, "Update product name: ", "Delete") :
                            new Reply<>(ReplyStatus.ERROR, "Product not found ", null);

                }
                default -> {
                    log.warn("Unknown command type={}", cmd.type());
                    yield new Reply<>(ReplyStatus.ERROR,"Unknown command type", null);
                }
            };

            return MessageBuilder.withPayload(reply)
                    .setHeader("correlationId", correlationId)
                    .build();

        };
    }
}
