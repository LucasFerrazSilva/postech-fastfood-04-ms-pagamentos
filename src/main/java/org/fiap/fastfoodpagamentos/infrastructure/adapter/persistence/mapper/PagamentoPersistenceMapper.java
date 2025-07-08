package org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.mapper;

import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.entity.PagamentoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PagamentoPersistenceMapper {

    Pagamento toDomain(PagamentoEntity entity);

    PagamentoEntity toEntity(Pagamento domain);

}
