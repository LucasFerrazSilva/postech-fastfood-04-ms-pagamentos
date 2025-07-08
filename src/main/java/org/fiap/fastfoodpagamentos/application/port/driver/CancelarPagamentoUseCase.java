package org.fiap.fastfoodpagamentos.application.port.driver;

import org.fiap.fastfoodpagamentos.domain.model.Pagamento;

public interface CancelarPagamentoUseCase {

    Pagamento execute(String idExternoPagamento);

}
