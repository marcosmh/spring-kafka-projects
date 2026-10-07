package com.markcode.springcloud.kafka.app.controller;

import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import com.markcode.springcloud.kafka.app.services.ProductCommandService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

   private final ProductCommandService commandService;

    public ProductController(ProductCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductDTO dto) {
        commandService.sendCreate(dto);
        return ResponseEntity.ok().body(Map.of("message","Succes Sent"));
    }


}
