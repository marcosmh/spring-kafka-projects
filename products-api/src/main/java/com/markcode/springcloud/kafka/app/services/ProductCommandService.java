package com.markcode.springcloud.kafka.app.services;

import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;

public interface ProductCommandService {

    void sendCreate(ProductDTO dto);
}
