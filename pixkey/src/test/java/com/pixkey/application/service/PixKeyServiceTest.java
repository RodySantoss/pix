package com.pixcrud.demo.application.service;

import com.pixcrud.demo.application.port.PixKeyRepository;
import com.pixcrud.demo.domain.model.PixKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class PixKeyServiceTest {

    @Mock
    private PixKeyRepository pixKeyRepository;

    @InjectMocks
    private PixKeyService pixKeyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePixKeySuccessfully() {
        PixKey pixKey = new PixKey(null, "CPF", "12345678900", "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900");
        when(pixKeyRepository.findAll()).thenReturn(new ArrayList<>());
        when(pixKeyRepository.save(pixKey)).thenReturn(pixKey);


        PixKey createdPixKey = pixKeyService.createPixKey(pixKey);

        assertEquals(pixKey, createdPixKey);
    }

    @Test
    void shouldNotAllowMoreThanFiveKeys() {
        List<PixKey> existingKeys = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            existingKeys.add(new PixKey((long) i, "RANDOM", "randomKey" + i, "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900"));
        }
        when(pixKeyRepository.findAll()).thenReturn(existingKeys);

        PixKey newPixKey = new PixKey(null, "CPF", "12345678900", "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pixKeyService.createPixKey(newPixKey);
        });
        assertEquals("Não é permitido cadastrar mais de 5 chaves PIX.", thrown.getMessage());
    }

    @Test
    void shouldNotAllowMoreThanOneCpfKey() {
        List<PixKey> existingKeys = new ArrayList<>();
        existingKeys.add(new PixKey(1L, "CPF", "12345678900", "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900"));
        when(pixKeyRepository.findAll()).thenReturn(existingKeys);

        PixKey newPixKey = new PixKey(null, "CPF", "09876543210", "CONTA_CORRENTE", "123456", "0001", "Jane Doe", "09876543210");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pixKeyService.createPixKey(newPixKey);
        });
        assertEquals("Não é permitido cadastrar mais de uma chave CPF.", thrown.getMessage());
    }

    @Test
    void shouldNotAllowDuplicateCpfKey() {
        List<PixKey> existingKeys = new ArrayList<>();
        existingKeys.add(new PixKey(1L, "CPF", "12345678900", "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900"));
        when(pixKeyRepository.findAll()).thenReturn(existingKeys);

        PixKey newPixKey = new PixKey(null, "CPF", "12345678900", "CONTA_CORRENTE", "123456", "0001", "Jane Doe", "12345678900");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pixKeyService.createPixKey(newPixKey);
        });
        assertEquals("Não é permitido cadastrar mais de uma chave CPF.", thrown.getMessage());
    }

    @Test
    void shouldNotAllowMoreThanOnePhoneKey() {
        List<PixKey> existingKeys = new ArrayList<>();
        existingKeys.add(new PixKey(1L, "PHONE", "5511999999999", "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900"));
        when(pixKeyRepository.findAll()).thenReturn(existingKeys);

        PixKey newPixKey = new PixKey(null, "PHONE", "5511888888888", "CONTA_CORRENTE", "123456", "0001", "Jane Doe", "12345678900");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pixKeyService.createPixKey(newPixKey);
        });
        assertEquals("Não é permitido cadastrar mais de uma chave PHONE.", thrown.getMessage());
    }

    @Test
    void shouldNotAllowDuplicatePhoneKey() {
        List<PixKey> existingKeys = new ArrayList<>();
        existingKeys.add(new PixKey(1L, "PHONE", "5511999999999", "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900"));
        when(pixKeyRepository.findAll()).thenReturn(existingKeys);

        PixKey newPixKey = new PixKey(null, "PHONE", "5511999999999", "CONTA_CORRENTE", "123456", "0001", "Jane Doe", "12345678900");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pixKeyService.createPixKey(newPixKey);
        });
        assertEquals("Não é permitido cadastrar mais de uma chave PHONE.", thrown.getMessage());
    }

    @Test
    void shouldNotAllowMoreThanOneEmailKey() {
        List<PixKey> existingKeys = new ArrayList<>();
        existingKeys.add(new PixKey(1L, "EMAIL", "john.doe@example.com", "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900"));
        when(pixKeyRepository.findAll()).thenReturn(existingKeys);

        PixKey newPixKey = new PixKey(null, "EMAIL", "jane.doe@example.com", "CONTA_CORRENTE", "123456", "0001", "Jane Doe", "12345678900");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pixKeyService.createPixKey(newPixKey);
        });
        assertEquals("Não é permitido cadastrar mais de uma chave EMAIL.", thrown.getMessage());
    }

    @Test
    void shouldNotAllowDuplicateEmailKey() {
        List<PixKey> existingKeys = new ArrayList<>();
        existingKeys.add(new PixKey(1L, "EMAIL", "john.doe@example.com", "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900"));
        when(pixKeyRepository.findAll()).thenReturn(existingKeys);

        PixKey newPixKey = new PixKey(null, "EMAIL", "john.doe@example.com", "CONTA_CORRENTE", "123456", "0001", "Jane Doe", "12345678900");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pixKeyService.createPixKey(newPixKey);
        });
        assertEquals("Não é permitido cadastrar mais de uma chave EMAIL.", thrown.getMessage());
    }

    @Test
    void shouldNotAllowMoreThanFiveRandomKeys() {
        List<PixKey> existingKeys = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            existingKeys.add(new PixKey((long) i, "RANDOM", "randomKey" + i, "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900"));
        }
        when(pixKeyRepository.findAll()).thenReturn(existingKeys);

        PixKey newPixKey = new PixKey(null, "RANDOM", "randomKeyNew", "CONTA_CORRENTE", "123456", "0001", "John Doe", "12345678900");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pixKeyService.createPixKey(newPixKey);
        });
        assertEquals("Não é permitido cadastrar mais de 5 chaves PIX.", thrown.getMessage());
    }
}
