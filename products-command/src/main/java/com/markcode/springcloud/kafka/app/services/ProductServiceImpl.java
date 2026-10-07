package com.markcode.springcloud.kafka.app.services;

import com.markcode.springcloud.kafka.app.entities.Product;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import com.markcode.springcloud.kafka.app.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product product = new Product(dto.name(), dto.price());
        Product productNew = productRepository.save(product);
        return new ProductDTO(productNew.getId(),
                productNew.getName(), productNew.getPrice());
    }



}
