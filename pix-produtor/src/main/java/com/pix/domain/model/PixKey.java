package com.pix.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PixKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keyType;
    private String keyValue;
    private String accountType;
    private String accountNumber;
    private String accountBranch;
    private String accountHolderName;
    private String accountHolderCpf;
}
