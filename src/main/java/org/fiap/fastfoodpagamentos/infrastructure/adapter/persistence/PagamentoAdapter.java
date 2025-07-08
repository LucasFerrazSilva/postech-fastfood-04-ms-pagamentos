package org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence;

import lombok.RequiredArgsConstructor;
import org.fiap.fastfoodpagamentos.application.port.driven.ManipularPagamento;
import org.fiap.fastfoodpagamentos.domain.exception.EntidadeNaoEncontradaException;
import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.entity.PagamentoEntity;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.mapper.PagamentoPersistenceMapper;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.repository.PagamentoRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class PagamentoAdapter implements ManipularPagamento {

    private final PagamentoRepository pagamentoRepository;

    private final PagamentoPersistenceMapper pagamentoPersistenceMapper;

    @Override
    public Pagamento criarPagamento(Pagamento pagamento) {
        PagamentoEntity pagamentoEntity = this.pagamentoPersistenceMapper.toEntity(pagamento);
        pagamentoEntity = this.pagamentoRepository.save(pagamentoEntity);
        return this.pagamentoPersistenceMapper.toDomain(pagamentoEntity);
    }

    @Override
    public Pagamento buscarPagamentoPorIdExterno(String idExterno) {
        PagamentoEntity pagamentoEntity = this.pagamentoRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(Pagamento.class.getName()));
        return this.pagamentoPersistenceMapper.toDomain(pagamentoEntity);
    }

    @Override
    public Pagamento alterarPagamento(Pagamento pagamento) {
        this.pagamentoRepository.findById(pagamento.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException(Pagamento.class.getName()));

        PagamentoEntity pagamentoEntity = this.pagamentoPersistenceMapper.toEntity(pagamento);
        pagamentoEntity = this.pagamentoRepository.save(pagamentoEntity);
        return this.pagamentoPersistenceMapper.toDomain(pagamentoEntity);
    }

    @Override
    public Pagamento buscarPagamentoPorIdPedido(Integer idPedido) {
        Optional<PagamentoEntity> pagamentoEntity = this.pagamentoRepository.findByPedidoId(idPedido);
        return pagamentoEntity.map(this.pagamentoPersistenceMapper::toDomain).orElse(null);
    }
}
