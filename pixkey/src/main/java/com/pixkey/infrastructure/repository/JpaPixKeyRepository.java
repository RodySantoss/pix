package com.pixcrud.demo.infrastructure.repository;

import com.pixcrud.demo.application.port.PixKeyRepository;
import com.pixcrud.demo.domain.model.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPixKeyRepository extends JpaRepository<PixKey, Long>, PixKeyRepository {
}
