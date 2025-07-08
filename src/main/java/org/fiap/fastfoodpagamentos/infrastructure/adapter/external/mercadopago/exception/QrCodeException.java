package org.fiap.fastfoodpagamentos.infrastructure.adapter.external.mercadopago.exception;

public class QrCodeException extends RuntimeException {
    public QrCodeException(Exception e) {
        super("Erro ao gerar QR Code de pagamento!", e);
    }
}
