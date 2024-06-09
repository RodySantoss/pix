package com.pixcrud.demo.infrastructure.controller;

import com.pixcrud.demo.application.service.PixKeyService;
import com.pixcrud.demo.domain.model.PixKey;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pix-keys")
public class PixKeyController {
    private final PixKeyService pixKeyService;

    public PixKeyController(PixKeyService pixKeyService) {
        this.pixKeyService = pixKeyService;
    }

    @PostMapping
    public ResponseEntity<PixKey> createPixKey(@RequestBody PixKey pixKey) {
        return ResponseEntity.ok(pixKeyService.createPixKey(pixKey));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PixKey> getPixKeyById(@PathVariable Long id) {
        return pixKeyService.getPixKeyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PixKey>> getAllPixKeys() {
        return ResponseEntity.ok(pixKeyService.getAllPixKeys());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePixKey(@PathVariable Long id) {
        pixKeyService.deletePixKey(id);
        return ResponseEntity.noContent().build();
    }
}