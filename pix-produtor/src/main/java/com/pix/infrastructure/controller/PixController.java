package com.pix.infrastructure.controller;

import com.pix.domain.model.Pix;
import com.pix.domain.model.PixStatus;
import com.pix.application.service.PixService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/pix-teste")
@RequiredArgsConstructor
public class PixController {

    private final PixService pixService;

    @PostMapping
    public Pix save(@RequestBody Pix pixObj) {
        pixObj.setDataTransferencia(LocalDateTime.now());
        pixObj.setStatus(PixStatus.EM_PROCESSAMENTO);

        return pixService.salvarPix(pixObj);
    }
}
