package com.pixcrud.demo.application.port;

import com.pixcrud.demo.domain.model.PixKey;

import java.util.List;
import java.util.Optional;

public interface PixKeyRepository {
    PixKey save(PixKey pixKey);
    Optional<PixKey> findById(Long id);
    List<PixKey> findAll();
    void deleteById(Long id);
}
