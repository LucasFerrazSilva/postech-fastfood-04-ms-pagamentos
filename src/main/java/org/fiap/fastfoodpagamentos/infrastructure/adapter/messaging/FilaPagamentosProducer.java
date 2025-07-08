package org.fiap.fastfoodpagamentos.infrastructure.adapter.messaging;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.fiap.fastfoodpagamentos.application.port.driven.PublicarMensagemPagamento;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class FilaPagamentosProducer implements PublicarMensagemPagamento {

    @Value("${app.fila-pagamentos}")
    private String filaPagamentos;

    private final SqsTemplate sqsTemplate;

    public void send(ResultadoPagamentoDTO resultadoPagamentoDTO) {
        sqsTemplate.send(filaPagamentos, resultadoPagamentoDTO);
    }

}
