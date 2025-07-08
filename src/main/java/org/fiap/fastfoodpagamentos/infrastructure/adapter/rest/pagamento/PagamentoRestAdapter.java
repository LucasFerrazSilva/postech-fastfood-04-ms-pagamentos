package org.fiap.fastfoodpagamentos.infrastructure.adapter.rest.pagamento;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiap.fastfoodpagamentos.application.port.driver.CancelarPagamentoUseCase;
import org.fiap.fastfoodpagamentos.application.port.driver.ConfirmarPagamentoUseCase;
import org.fiap.fastfoodpagamentos.application.port.driver.IniciarPagamentoUseCase;
import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.domain.model.Pedido;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.HeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/pagamentos")
@RequiredArgsConstructor
public class PagamentoRestAdapter {

  public static final String ENTITY_NAME = Pagamento.class.getName();
  private final IniciarPagamentoUseCase iniciarPagamentoUseCase;
  private final ConfirmarPagamentoUseCase confirmarPagamentoUseCase;
  private final CancelarPagamentoUseCase cancelarPagamentoUseCase;

  @Value("${app.name}")
  private String applicationName;

  @PostMapping(value = "/iniciar")
  public ResponseEntity<Resource> iniciarPagamento(@RequestBody Pedido pedido) throws IOException {
    log.debug("Requisição Rest para Iniciar Pagamento para o pedido : {}", pedido.getId());

    Pagamento pagamento = this.iniciarPagamentoUseCase.execute(pedido);

    Path path = new File(pagamento.getQrCode()).toPath();
    FileSystemResource resource = new FileSystemResource(pagamento.getQrCode());

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
        .body(resource);
  }

  @GetMapping(value = "/confirmar/{idExterno}")
  public ResponseEntity<String> confirmarPagmaento(@PathVariable final String idExterno) {
    log.debug("Requisição Rest para confirmar Pagamento para o id externo : {}", idExterno);

    this.confirmarPagamentoUseCase.execute(idExterno);

    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idExterno))
        .body("Pagamento realizado com sucesso!");
  }

  @GetMapping(value = "/cancelar/{idExterno}")
  public ResponseEntity<String> cancelarPagmaento(@PathVariable final String idExterno) {
    log.debug("Requisição Rest para cancelar Pagamento para o id externo : {}", idExterno);

    this.cancelarPagamentoUseCase.execute(idExterno);

    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idExterno))
        .body("Pagamento não realizado. Pedido cancelado!");
  }
}
