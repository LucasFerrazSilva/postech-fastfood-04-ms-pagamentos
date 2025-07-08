package org.fiap.fastfoodpagamentos.domain.exception;

import org.fiap.fastfoodpagamentos.infrastructure.adapter.rest.exception.BadRequestAlertException;

public class EntidadeNaoEncontradaException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EntidadeNaoEncontradaException(String entity) {
        super(entity + " não encontrado(a).", entity, "entity-not-found");
    }
}
