package com.markcode.springcloud.kafka.app.controller;

import com.markcode.springcloud.kafka.app.models.Reply;
import com.markcode.springcloud.kafka.app.models.dto.ProductDTO;
import com.markcode.springcloud.kafka.app.services.ProductCommandService;

import jakarta.validation.Valid;

import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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
        Reply<?> reply = commandService.sendCreateAndWait(dto, Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Reply<?> reply = commandService.sendReadAndWait(id, Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        Reply<?> reply = commandService.sendReadAllAndWait(Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    @PutMapping
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        Reply<?> reply = commandService.sendUpdateAndWait(dto, id, Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        Reply<?> reply = commandService.sendDeleteAndWait(id, Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    private static @NonNull ResponseEntity<?> getResponseEntity(Reply<?> reply) {
        if("SUCCESS".equalsIgnoreCase(reply.status())) {
            return ResponseEntity.ok(reply.body());
        }
        return ResponseEntity.badRequest().body(Map.of("error", reply.message()));
    }


}
