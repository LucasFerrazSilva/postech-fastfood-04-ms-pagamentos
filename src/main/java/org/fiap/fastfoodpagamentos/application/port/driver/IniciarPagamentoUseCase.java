package org.fiap.fastfoodpagamentos.application.port.driver;

import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.domain.model.Pedido;

public interface IniciarPagamentoUseCase {

    Pagamento execute(Pedido pedido);

}
