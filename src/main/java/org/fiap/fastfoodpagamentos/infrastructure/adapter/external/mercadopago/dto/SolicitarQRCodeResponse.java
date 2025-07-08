package org.fiap.fastfoodpagamentos.infrastructure.adapter.external.mercadopago.dto;

public record SolicitarQRCodeResponse(
        String identificador,
        String QRCode
) {
}
