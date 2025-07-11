package org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.entity;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "pagamento")
public class PagamentoEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    private String idExterno;

    private Integer pedidoId;

    @NotNull
    private PagamentoStatus status;

    private String qrCode;

}
