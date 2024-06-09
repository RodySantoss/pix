package com.pix.infrastructure.repository;


import com.pix.application.port.PixRepository;
import com.pix.domain.model.Pix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPixRepository extends JpaRepository<Pix, Long>, PixRepository {
}
