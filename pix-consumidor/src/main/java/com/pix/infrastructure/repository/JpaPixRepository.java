package com.pix.infrastructure.repository;

import com.pix.domain.model.Pix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPixRepository extends JpaRepository<Pix, Integer> {
    Pix findByIdentifier(String identifier);
}
