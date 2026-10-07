package com.markcode.springcloud.kafka.app.models.mapper;

import com.markcode.springcloud.kafka.app.entities.Product;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;

public final class Mappers {

    private Mappers() {

    }

    static public ProductDTO toDto(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice());
    }

    static public Product toEntity(ProductDTO dto) {
        Product entity = new Product(dto.name(), dto.price());
        entity.setId(dto.id());
        return entity;
    }
}
