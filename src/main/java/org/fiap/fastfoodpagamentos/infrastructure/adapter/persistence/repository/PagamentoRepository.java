package org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.repository;

import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.entity.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Integer> {

    Optional<PagamentoEntity> findByIdExterno(String idExterno);

    Optional<PagamentoEntity> findByPedidoId(Integer idPedido);

}
