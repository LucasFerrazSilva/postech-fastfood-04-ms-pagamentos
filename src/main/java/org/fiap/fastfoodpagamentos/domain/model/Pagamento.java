package org.fiap.fastfoodpagamentos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
    private String id;
    private String idExterno;
    private Integer pedidoId;
    private PagamentoStatus status;
    private String qrCode;
}
