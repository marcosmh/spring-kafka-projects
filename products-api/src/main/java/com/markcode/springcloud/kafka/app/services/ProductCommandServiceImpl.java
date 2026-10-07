package com.markcode.springcloud.kafka.app.services;

import com.markcode.springcloud.kafka.app.models.Command;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;


@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final StreamBridge bridge;

    public ProductCommandServiceImpl(StreamBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void sendCreate(ProductDTO dto) {
        Command<ProductDTO> cmd = new Command<>("CREATE",null,dto);
        boolean send = this.bridge.send("commands-out-0", cmd);

        if(!send) {
            throw new IllegalStateException("No se pudo enviar el comando a Kafka");
        }
    }




}
