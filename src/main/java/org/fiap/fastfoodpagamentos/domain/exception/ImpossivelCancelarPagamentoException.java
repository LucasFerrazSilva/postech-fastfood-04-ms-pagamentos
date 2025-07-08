package org.fiap.fastfoodpagamentos.domain.exception;

import org.fiap.fastfoodpagamentos.infrastructure.adapter.rest.exception.BadRequestAlertException;

public class ImpossivelCancelarPagamentoException extends BadRequestAlertException {


    public ImpossivelCancelarPagamentoException() {
        super("Este pagamento não é elegível para cancelamento.", "pagamento", "impossivelCancelarPagamento");
    }
}
