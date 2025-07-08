package org.fiap.fastfoodpagamentos.application.usecase;

import lombok.AllArgsConstructor;
import org.fiap.fastfoodpagamentos.application.port.driven.ManipularPagamento;
import org.fiap.fastfoodpagamentos.application.port.driven.PublicarMensagemPagamento;
import org.fiap.fastfoodpagamentos.application.port.driver.CancelarPagamentoUseCase;
import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;
import org.fiap.fastfoodpagamentos.domain.exception.ImpossivelCancelarPagamentoException;
import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.messaging.ResultadoPagamentoDTO;

@AllArgsConstructor
public class CancelarPagamentoUseCaseUseCaseImpl implements CancelarPagamentoUseCase {

  private final ManipularPagamento manipularPagamento;
  private final PublicarMensagemPagamento publicarMensagemPagamento;

  @Override
  public Pagamento execute(String idExternoPagamento) {
    Pagamento pagamento = manipularPagamento.buscarPagamentoPorIdExterno(idExternoPagamento);

    if (!PagamentoStatus.PENDENTE.equals(pagamento.getStatus())) {
      throw new ImpossivelCancelarPagamentoException();
    }

    pagamento.setStatus(PagamentoStatus.CANCELADO);

    publicarMensagemPagamento.send(
        new ResultadoPagamentoDTO(pagamento.getPedidoId(), pagamento.getStatus()));

    return manipularPagamento.alterarPagamento(pagamento);
  }
}
