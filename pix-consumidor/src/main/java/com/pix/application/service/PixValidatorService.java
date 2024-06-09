package com.pix.application.service;

import com.pix.application.port.PixValidatorRepository;
import com.pix.domain.model.PixStatus;
import com.pix.domain.model.PixKey;
import com.pix.domain.model.Pix;
import com.pix.application.port.PixKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PixValidatorService  implements PixValidatorRepository{

    @Autowired
    private PixKeyRepository keyRepository;

    @Autowired
    private PixValidatorRepository pixRepository;

    @Override
    public Pix save(Pix pixObj) {
        return null;
    }

    @Override
    @KafkaListener(topics = "pix-topic", groupId = "grupo")
    public void processaPix(Pix pixObj) {
        System.out.println("Pix recebido: " + pixObj.getIdentifier());

        Pix pix = pixRepository.findByIdentifier(pixObj.getIdentifier());

        PixKey origem = keyRepository.findByKeyValue(pixObj.getChaveOrigem());
        PixKey destino = keyRepository.findByKeyValue(pixObj.getChaveDestino());

        if (origem == null || destino == null) {
            pix.setStatus(PixStatus.ERRO);
        } else {
            pix.setStatus(PixStatus.PROCESSADO);
        }
        pixRepository.save(pix);
    }

    @Override
    public Pix findByIdentifier(String identifier) {
        return null;
    }

}
