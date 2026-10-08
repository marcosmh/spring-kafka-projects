package com.markcode.springcloud.kafka.app.services;

import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO create(ProductDTO dto);

    ProductDTO update(Long id, ProductDTO dto);

    ProductDTO findById(Long id);

    List<ProductDTO> findAll();

    boolean delete(Long id);



}
