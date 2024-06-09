package com.pix.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pix_key")
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