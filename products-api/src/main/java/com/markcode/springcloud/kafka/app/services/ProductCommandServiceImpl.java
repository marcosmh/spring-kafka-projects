package com.markcode.springcloud.kafka.app.services;

import com.markcode.springcloud.kafka.app.messaging.ReplyInbox;
import com.markcode.springcloud.kafka.app.models.Command;
import com.markcode.springcloud.kafka.app.models.Reply;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private static final Logger logger = LoggerFactory.getLogger(ProductCommandServiceImpl.class);
    private final StreamBridge bridge;
    private final ReplyInbox replyInbox;

    public ProductCommandServiceImpl(StreamBridge bridge, ReplyInbox replyInbox) {
        this.bridge = bridge;
        this.replyInbox = replyInbox;
    }

    @Override
    public Reply<?> sendCreateAndWait(ProductDTO dto, Duration timeout) {

        Command<ProductDTO> cmd = new Command<>("CREATE",null,dto);
        return sendAndWait(cmd, timeout);
    }

    @Override
    public Reply<?> sendReadAndWait(Long id, Duration timeout) {
        Command<ProductDTO> cmd = new Command<>("READ",id,null);
        return sendAndWait(cmd, timeout);
    }

    @Override
    public Reply<?> sendReadAllAndWait(Duration timeout) {
        Command<ProductDTO> cmd = new Command<>("READ_ALL",null,null);
        return sendAndWait(null, timeout);
    }

    @Override
    public Reply<?> sendUpdateAndWait(ProductDTO dto, Long id, Duration timeout) {
        Command<ProductDTO> cmd = new Command<>("UPDATE",id,dto);
        return sendAndWait(cmd, timeout);
    }

    @Override
    public Reply<?> sendDeleteAndWait(Long id, Duration timeout) {
        Command<ProductDTO> cmd = new Command<>("DELETE",id,null);
        return sendAndWait(cmd, timeout);
    }

    private Reply<?> sendAndWait(Command<?> cmd, Duration timeout) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("Api Products Client Creating product with correlationId {}",  correlationId);

        var future = replyInbox.register(correlationId);

        var msg = MessageBuilder.withPayload(cmd)
                .setHeader("correlationId", correlationId).build();

        boolean send = this.bridge.send("commands-out-0", msg);

        if(!send) {
            throw new IllegalStateException("No se pudo enviar el comando a Kafka");
        }

        try {
            return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Timeout esperando respuesta de products-commands desde Kafka",e);
        }
    }



    

}
