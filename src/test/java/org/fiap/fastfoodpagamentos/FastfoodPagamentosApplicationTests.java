package org.fiap.fastfoodpagamentos;

import static org.assertj.core.api.Assertions.assertThat;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.repository.PagamentoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FastfoodPagamentosApplicationTests {

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Test
	void contextLoads() {
		assertThat(pagamentoRepository).isNotNull();
	}

}
