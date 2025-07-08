package org.fiap.fastfoodpagamentos.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private Integer id;
    String nome;
    String email;
    String cpf;
}
