package org.fiap.fastfoodpagamentos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    private String nome;
    private Double preco;
    private Integer quantidade;
}
