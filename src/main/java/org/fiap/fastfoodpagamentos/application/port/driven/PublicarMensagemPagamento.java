package org.fiap.fastfoodpagamentos.application.port.driven;

import org.fiap.fastfoodpagamentos.infrastructure.adapter.messaging.ResultadoPagamentoDTO;

public interface PublicarMensagemPagamento {
    void send(ResultadoPagamentoDTO resultadoPagamentoDTO);
}
