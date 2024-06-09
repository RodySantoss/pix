package com.pix.infrastructure.repository;

import com.pix.domain.model.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPixKeyRepository extends JpaRepository<PixKey, Integer> {
    PixKey findByKeyValue(String key);
}
