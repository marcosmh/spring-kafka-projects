package com.markcode.springcloud.kafka.app.repositories;

import com.markcode.springcloud.kafka.app.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product,Long> {

}
