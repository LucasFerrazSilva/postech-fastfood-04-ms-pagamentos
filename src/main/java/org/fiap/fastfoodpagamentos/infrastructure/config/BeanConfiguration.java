package org.fiap.fastfoodpagamentos.infrastructure.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.fiap.fastfoodpagamentos.application.port.driven.ManipularPagamento;
import org.fiap.fastfoodpagamentos.application.port.driven.PublicarMensagemPagamento;
import org.fiap.fastfoodpagamentos.application.port.driven.SolicitarPagamento;
import org.fiap.fastfoodpagamentos.application.port.driver.CancelarPagamentoUseCase;
import org.fiap.fastfoodpagamentos.application.port.driver.ConfirmarPagamentoUseCase;
import org.fiap.fastfoodpagamentos.application.port.driver.IniciarPagamentoUseCase;
import org.fiap.fastfoodpagamentos.application.usecase.CancelarPagamentoUseCaseUseCaseImpl;
import org.fiap.fastfoodpagamentos.application.usecase.ConfirmarPagamentoUseCaseUseCaseImpl;
import org.fiap.fastfoodpagamentos.application.usecase.IniciarPagamentoUseCaseUseCaseImpl;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.external.mercadopago.MercadoPagoIntegrationImpl;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.messaging.FilaPagamentosProducer;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.PagamentoAdapter;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.mapper.PagamentoPersistenceMapper;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.repository.PagamentoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public SolicitarPagamento solicitarPagamento() {
        return new MercadoPagoIntegrationImpl();
    }

    @Bean
    public ManipularPagamento salvarPagamento(PagamentoRepository pagamentoRepository, PagamentoPersistenceMapper pagamentoPersistenceMapper) {
        return new PagamentoAdapter(pagamentoRepository, pagamentoPersistenceMapper);
    }

    @Bean
    public IniciarPagamentoUseCase iniciarPagamento(final SolicitarPagamento solicitarPagamento, final ManipularPagamento manipularPagamento) {
        return new IniciarPagamentoUseCaseUseCaseImpl(solicitarPagamento, manipularPagamento);
    }

    @Bean
    public PublicarMensagemPagamento publicarMensagemPagamento(final SqsTemplate sqsTemplate) {
        return new FilaPagamentosProducer(sqsTemplate);
    }

    @Bean
    public ConfirmarPagamentoUseCase confirmarPagamento(final ManipularPagamento manipularPagamento, PublicarMensagemPagamento publicarMensagemPagamento) {
        return new ConfirmarPagamentoUseCaseUseCaseImpl(manipularPagamento, publicarMensagemPagamento);
    }

    @Bean
    public CancelarPagamentoUseCase cancelarPagamento(final ManipularPagamento manipularPagamento, PublicarMensagemPagamento publicarMensagemPagamento) {
        return new CancelarPagamentoUseCaseUseCaseImpl(manipularPagamento, publicarMensagemPagamento);
    }

}
