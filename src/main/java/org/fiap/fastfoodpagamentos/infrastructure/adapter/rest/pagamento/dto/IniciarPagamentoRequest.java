package org.fiap.fastfoodpagamentos.infrastructure.adapter.rest.pagamento.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record IniciarPagamentoRequest(

        @NotEmpty(message = "Namo não pode estar vazio")
        @Size(max = 100)
        String nome,
        @Size(max = 150)
        @Email
        String email,
        @CPF
        String cpf
) {
}
