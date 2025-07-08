package org.fiap.fastfoodpagamentos.application.port.driven;

import org.fiap.fastfoodpagamentos.domain.model.Pagamento;

public interface ConsultarPagamento {

    Pagamento execute(Integer id);
}
