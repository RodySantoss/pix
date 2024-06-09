package com.pix.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Pix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifier;
    private String chaveOrigem;
    private String chaveDestino;
    private Double valor;
    private LocalDateTime dataTransferencia;
    @Enumerated(EnumType.STRING)
    private PixStatus status;

    public static Pix toEntity(Pix pixObj) {
        Pix pix = new Pix();
        pix.setIdentifier(pixObj.getIdentifier());
        pix.setChaveDestino(pixObj.getChaveDestino());
        pix.setStatus(pixObj.getStatus());
        pix.setValor(pixObj.getValor());
        pix.setDataTransferencia(pixObj.getDataTransferencia());
        pix.setChaveOrigem(pixObj.getChaveOrigem());
        return pix;
    }
}
