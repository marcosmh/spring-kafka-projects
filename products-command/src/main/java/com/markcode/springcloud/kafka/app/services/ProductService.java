package com.markcode.springcloud.kafka.app.services;

import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;

public interface ProductService {

    ProductDTO create(ProductDTO dto);


}
