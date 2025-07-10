package org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.repository;

import java.util.Optional;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.entity.PagamentoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends MongoRepository<PagamentoEntity, String> {

    Optional<PagamentoEntity> findByIdExterno(String idExterno);

    Optional<PagamentoEntity> findByPedidoId(Integer idPedido);

}
