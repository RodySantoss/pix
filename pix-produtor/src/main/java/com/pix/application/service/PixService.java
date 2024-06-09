package com.pix.application.service;

import com.pix.domain.model.Pix;
import com.pix.application.port.PixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PixService {

    @Autowired
    private final PixRepository pixRepository;
    @Autowired
    private final KafkaTemplate<String, Pix> kafkaTemplate;

    public Pix salvarPix(Pix pixObj) {
        pixRepository.save(Pix.toEntity(pixObj));
        kafkaTemplate.send("pix-topic", pixObj.getIdentifier(), pixObj);
        return pixObj;
    }
}