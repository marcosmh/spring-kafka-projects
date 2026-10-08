package com.markcode.springcloud.kafka.app.services;

import com.markcode.springcloud.kafka.app.models.Reply;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;

import java.time.Duration;

public interface ProductCommandService {

    Reply<?> sendCreateAndWait(ProductDTO dto, Duration timeout);

    Reply<?> sendReadAndWait(Long id, Duration timeout);

    Reply<?> sendReadAllAndWait(Duration timeout);

    Reply<?> sendUpdateAndWait(ProductDTO dto, Long id, Duration timeout);

    Reply<?> sendDeleteAndWait(Long id, Duration timeout);

}
