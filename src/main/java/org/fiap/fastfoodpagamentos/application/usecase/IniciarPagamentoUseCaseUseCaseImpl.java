package org.fiap.fastfoodpagamentos.application.usecase;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiap.fastfoodpagamentos.application.port.driven.ManipularPagamento;
import org.fiap.fastfoodpagamentos.application.port.driven.SolicitarPagamento;
import org.fiap.fastfoodpagamentos.application.port.driver.IniciarPagamentoUseCase;
import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;
import org.fiap.fastfoodpagamentos.domain.exception.PedidoNaoAplicavelParaPagamentoException;
import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.domain.model.Pedido;

@Slf4j
@AllArgsConstructor
public class IniciarPagamentoUseCaseUseCaseImpl implements IniciarPagamentoUseCase {

    private final SolicitarPagamento solicitarPagamento;
    private final ManipularPagamento manipularPagamento;


    @Override
    public Pagamento execute(Pedido pedido) {
        Pagamento pagamentoDoPedido = this.manipularPagamento.buscarPagamentoPorIdPedido(pedido.getId());
        if (pagamentoDoPedido != null) {
            log.error("Pedido já possui pagamento atrelado");
            throw new PedidoNaoAplicavelParaPagamentoException();
        }

        log.info("Pedido sem pagamento");

        Pagamento pagamento = this.solicitarPagamento.gerarQRCode(pedido);
        pagamento.setStatus(PagamentoStatus.PENDENTE);
        pagamento = this.manipularPagamento.criarPagamento(pagamento);
        log.info("Pagamento {} salvo", pagamento.getId());

        return pagamento;
    }
}
