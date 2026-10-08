package com.markcode.springcloud.kafka.app.controller;

import com.markcode.springcloud.kafka.app.models.Reply;
import com.markcode.springcloud.kafka.app.models.ReplyStatus;
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
        return getResponseEntity(commandService.sendCreateAndWait(dto, Duration.ofSeconds(5)));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return getResponseEntity(commandService.sendReadAndWait(id, Duration.ofSeconds(5)));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return getResponseEntity(commandService.sendReadAllAndWait(Duration.ofSeconds(5)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return getResponseEntity(commandService.sendUpdateAndWait(dto, id, Duration.ofSeconds(5)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        return getResponseEntity(commandService.sendDeleteAndWait(id, Duration.ofSeconds(5)));
    }

    private static @NonNull ResponseEntity<?> getResponseEntity(Reply<?> reply) {
        if(reply.status().isSuccess()) {
            return ResponseEntity.ok(reply.body());
        }
        return ResponseEntity.badRequest().body(Map.of("error", reply.message()));
    }


}
