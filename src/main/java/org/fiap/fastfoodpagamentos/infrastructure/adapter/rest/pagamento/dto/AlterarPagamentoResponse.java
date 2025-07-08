package org.fiap.fastfoodpagamentos.infrastructure.adapter.rest.pagamento.dto;

import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;

public record AlterarPagamentoResponse(
        Integer id,
        String idExterno,
        PagamentoStatus status,
        String qrCode
) {
}
