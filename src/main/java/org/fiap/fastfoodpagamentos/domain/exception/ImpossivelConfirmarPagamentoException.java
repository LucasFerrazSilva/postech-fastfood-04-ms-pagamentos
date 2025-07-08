package org.fiap.fastfoodpagamentos.domain.exception;

import org.fiap.fastfoodpagamentos.infrastructure.adapter.rest.exception.BadRequestAlertException;

public class ImpossivelConfirmarPagamentoException extends BadRequestAlertException {

    public ImpossivelConfirmarPagamentoException() {
        super("Este pagamento não é elegível para confirmação.", "pagamento", "impossivelConfirmarPagamento");
    }
}
