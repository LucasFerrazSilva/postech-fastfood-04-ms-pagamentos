package org.fiap.fastfoodpagamentos.infrastructure.adapter.messaging;

import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;

public record ResultadoPagamentoDTO(
        Integer pedidoId,
        PagamentoStatus status
) {}
