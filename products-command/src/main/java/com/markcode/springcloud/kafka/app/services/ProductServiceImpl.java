package com.markcode.springcloud.kafka.app.services;

import com.markcode.springcloud.kafka.app.entities.Product;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import com.markcode.springcloud.kafka.app.models.mapper.Mappers;
import com.markcode.springcloud.kafka.app.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        return Mappers.toDto(productRepository.save(Mappers.toEntity(dto)));
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product entity = productRepository.findById(id).orElse(null);
        if(entity == null) {
            return null;
        }
        entity.setName(dto.name());
        entity.setPrice(dto.price());
        return Mappers.toDto(productRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return productRepository.findById(id)
                .map(Mappers::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(Mappers::toDto)
                .toList();
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        boolean result = productRepository.existsById(id);
        if(result) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
