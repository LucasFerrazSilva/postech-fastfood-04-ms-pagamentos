package org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "pagamento")
@Data
public class PagamentoEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "id_externo", nullable = false)
    private String idExterno;

    @Column(name = "pedido_id", nullable = false)
    private Integer pedidoId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PagamentoStatus status;

    @Column(name = "qr_code")
    private String qrCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagamentoEntity)) {
            return false;
        }
        return id != null && id.equals(((PagamentoEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PagamentoEntity{" +
                "id=" + id +
                ", idExterno='" + idExterno + '\'' +
                ", idPedido=" + pedidoId +
                ", status=" + status +
                ", qrCode='" + qrCode + '\'' +
                '}';
    }
}
