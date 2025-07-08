package org.fiap.fastfoodpagamentos.domain.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pedido {
    private Integer id;
    private BigDecimal total;
    private List<PedidoProduto> pedidoProdutos;
}
