package org.fiap.fastfoodpagamentos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProduto {
    private Produto produto;
    private Integer quantidade;
    private BigDecimal valorUnitarioProdutoMomentoVenda;
}
