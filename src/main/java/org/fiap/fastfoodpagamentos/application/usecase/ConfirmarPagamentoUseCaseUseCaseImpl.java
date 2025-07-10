package org.fiap.fastfoodpagamentos.application.usecase;

import lombok.AllArgsConstructor;
import org.fiap.fastfoodpagamentos.application.port.driven.ManipularPagamento;
import org.fiap.fastfoodpagamentos.application.port.driven.PublicarMensagemPagamento;
import org.fiap.fastfoodpagamentos.application.port.driver.ConfirmarPagamentoUseCase;
import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;
import org.fiap.fastfoodpagamentos.domain.exception.ImpossivelConfirmarPagamentoException;
import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.messaging.ResultadoPagamentoDTO;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class ConfirmarPagamentoUseCaseUseCaseImpl implements ConfirmarPagamentoUseCase {

  private final ManipularPagamento manipularPagamento;
  private final PublicarMensagemPagamento publicarMensagemPagamento;

  @Override
  @Transactional
  public Pagamento execute(String idExternoPagamento) {
    Pagamento pagamento = manipularPagamento.buscarPagamentoPorIdExterno(idExternoPagamento);

    if (!PagamentoStatus.PENDENTE.equals(pagamento.getStatus())) {
      throw new ImpossivelConfirmarPagamentoException();
    }

    pagamento.setStatus(PagamentoStatus.REALIZADO);

    publicarMensagemPagamento.send(
            new ResultadoPagamentoDTO(pagamento.getPedidoId(), pagamento.getStatus()));

    pagamento = manipularPagamento.alterarPagamento(pagamento);

    return pagamento;
  }
}
