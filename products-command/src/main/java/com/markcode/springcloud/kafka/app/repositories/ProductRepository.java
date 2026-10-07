package com.markcode.springcloud.kafka.app.repositories;

import com.markcode.springcloud.kafka.app.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Long> {

}
