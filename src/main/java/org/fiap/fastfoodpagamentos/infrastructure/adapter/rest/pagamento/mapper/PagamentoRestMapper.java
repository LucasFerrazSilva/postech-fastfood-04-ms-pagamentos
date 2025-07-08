package org.fiap.fastfoodpagamentos.infrastructure.adapter.rest.pagamento.mapper;

import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.rest.pagamento.dto.AlterarPagamentoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PagamentoRestMapper {

    AlterarPagamentoResponse toAlterarPagamentoResponse(Pagamento pagamento);

}
