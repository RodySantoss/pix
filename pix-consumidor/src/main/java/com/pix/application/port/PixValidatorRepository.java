package com.pix.application.port;

import com.pix.domain.model.Pix;

public interface PixValidatorRepository {
    Pix save(Pix pixObj);
    void processaPix(Pix pixObj);
    Pix findByIdentifier(String identifier);
}
