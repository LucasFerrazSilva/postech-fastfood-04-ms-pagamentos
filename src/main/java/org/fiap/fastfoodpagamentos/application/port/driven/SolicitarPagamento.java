package org.fiap.fastfoodpagamentos.application.port.driven;

import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.domain.model.Pedido;

public interface SolicitarPagamento {

    Pagamento gerarQRCode(Pedido pedido);

}
