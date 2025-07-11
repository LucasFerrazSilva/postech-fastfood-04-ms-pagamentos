package org.fiap.fastfoodpagamentos.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;
import org.fiap.fastfoodpagamentos.domain.model.Pedido;
import org.fiap.fastfoodpagamentos.domain.model.PedidoProduto;
import org.fiap.fastfoodpagamentos.domain.model.Produto;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.entity.PagamentoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestFixtureUtil {

  @Autowired
  private org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.repository
          .PagamentoRepository
      pagamentoRepository;

  public Pedido criarPedido() {
    BigDecimal valor = new BigDecimal("29.99");

    Produto produto =
        Produto.builder()
            .id(1)
            .nome("Produto teste")
            .descricao("Descrição do produto teste")
            .build();

    PedidoProduto pedidoProduto =
        PedidoProduto.builder()
            .produto(produto)
            .quantidade(1)
            .valorUnitarioProdutoMomentoVenda(valor)
            .build();

    return Pedido.builder()
        .id(new Random().nextInt(10000))
        .total(valor)
        .pedidoProdutos(List.of(pedidoProduto))
        .build();
  }

  @Transactional
  public PagamentoEntity criarPagamentoEntity() {
    Pedido pedido = criarPedido();

    PagamentoEntity pagamento = new PagamentoEntity();
    pagamento.setStatus(PagamentoStatus.PENDENTE);
    pagamento.setIdExterno(UUID.randomUUID().toString());
    pagamento.setPedidoId(pedido.getId());

    return pagamentoRepository.save(pagamento);
  }
}
