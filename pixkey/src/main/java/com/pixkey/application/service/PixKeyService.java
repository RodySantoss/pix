package com.pixcrud.demo.application.service;

import com.pixcrud.demo.application.port.PixKeyRepository;
import com.pixcrud.demo.domain.model.PixKey;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PixKeyService {
    private final PixKeyRepository pixKeyRepository;

    public PixKeyService(PixKeyRepository pixKeyRepository) {
        this.pixKeyRepository = pixKeyRepository;
    }

    public PixKey createPixKey(PixKey pixKey) {
        // Adicionar validações conforme as regras do PIX
        validatePixKey(pixKey);

        return pixKeyRepository.save(pixKey);
    }

    public Optional<PixKey> getPixKeyById(Long id) {
        return pixKeyRepository.findById(id);
    }

    public List<PixKey> getAllPixKeys() {
        return pixKeyRepository.findAll();
    }

    public void deletePixKey(Long id) {
        pixKeyRepository.deleteById(id);
    }

    private void validatePixKey(PixKey pixKey) {
        List<PixKey> allKeys = pixKeyRepository.findAll();

        // Verificar o limite total de chaves
        if (allKeys.size() >= 5) {
            throw new IllegalArgumentException("Não é permitido cadastrar mais de 5 chaves PIX.");
        }

        // Contar os tipos de chave já existentes
        long cpfCount = allKeys.stream().filter(key -> "CPF".equals(key.getKeyType())).count();
        long phoneCount = allKeys.stream().filter(key -> "PHONE".equals(key.getKeyType())).count();
        long emailCount = allKeys.stream().filter(key -> "EMAIL".equals(key.getKeyType())).count();
        long randomCount = allKeys.stream().filter(key -> "RANDOM".equals(key.getKeyType())).count();

        switch (pixKey.getKeyType()) {
            case "CPF":
                if (cpfCount >= 1) {
                    throw new IllegalArgumentException("Não é permitido cadastrar mais de uma chave CPF.");
                }
                if (allKeys.stream().anyMatch(key -> key.getKeyValue().equals(pixKey.getKeyValue()))) {
                    throw new IllegalArgumentException("CPF já cadastrado.");
                }
                break;
            case "PHONE":
                if (phoneCount >= 1) {
                    throw new IllegalArgumentException("Não é permitido cadastrar mais de uma chave PHONE.");
                }
                if (allKeys.stream().anyMatch(key -> key.getKeyValue().equals(pixKey.getKeyValue()))) {
                    throw new IllegalArgumentException("Telefone já cadastrado.");
                }
                break;
            case "EMAIL":
                if (emailCount >= 1) {
                    throw new IllegalArgumentException("Não é permitido cadastrar mais de uma chave EMAIL.");
                }
                if (allKeys.stream().anyMatch(key -> key.getKeyValue().equals(pixKey.getKeyValue()))) {
                    throw new IllegalArgumentException("Email já cadastrado.");
                }
                break;
            case "RANDOM":
                if (randomCount >= 5) {
                    throw new IllegalArgumentException("Não é permitido cadastrar mais de cinco chaves RANDOM.");
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de chave PIX inválido.");
        }
    }
}
