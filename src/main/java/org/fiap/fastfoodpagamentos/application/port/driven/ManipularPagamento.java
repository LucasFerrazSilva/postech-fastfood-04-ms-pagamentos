package org.fiap.fastfoodpagamentos.application.port.driven;

import org.fiap.fastfoodpagamentos.domain.model.Pagamento;

public interface ManipularPagamento {

    Pagamento criarPagamento(Pagamento pagamento);

    Pagamento buscarPagamentoPorIdExterno(String idExterno);

    Pagamento alterarPagamento(Pagamento pagamento);

    Pagamento buscarPagamentoPorIdPedido(Integer idPedido);

}
