package com.pix.application.port;

import com.pix.domain.model.Pix;

public interface PixRepository {
    Pix save(Pix pixObj);
}
