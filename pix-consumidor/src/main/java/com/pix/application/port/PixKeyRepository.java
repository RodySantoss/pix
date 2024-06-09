package com.pix.application.port;

import com.pix.domain.model.PixKey;

public interface PixKeyRepository {
    PixKey findByKeyValue(String key);
}
