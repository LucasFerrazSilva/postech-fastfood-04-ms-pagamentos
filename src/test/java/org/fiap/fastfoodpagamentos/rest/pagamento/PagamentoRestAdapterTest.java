package org.fiap.fastfoodpagamentos.rest.pagamento;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;
import org.fiap.fastfoodpagamentos.domain.model.Pedido;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.entity.PagamentoEntity;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.persistence.repository.PagamentoRepository;
import org.fiap.fastfoodpagamentos.util.TestFixtureUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.docker.compose.enabled=true")
class PagamentoRestAdapterTest {

  private final String ENDPOINT = "/api/v1/pagamentos";

  @Autowired private JacksonTester<Pedido> pedidoJacksonTester;

  @Autowired private MockMvc mvc;

  @Autowired private TestFixtureUtil testFixtureUtil;

  @Autowired private PagamentoRepository pagamentoRepository;

  @Value("${DB_NAME}")
  private String dbName;

  @BeforeAll
  static void setup(@Autowired SqsAsyncClient sqsClient) {
    sqsClient.createQueue(CreateQueueRequest.builder().queueName("fila-pagamentos").build());
  }

  @Test
  @DisplayName(
          "Deve retornar 200 quando chamar via POST o endpoint /api/v1/pagamentos/iniciar passando um pedido valido")
  void testIniciar() throws Exception {
    // Given
    Pedido pedido = testFixtureUtil.criarPedido();
    String requestBody = pedidoJacksonTester.write(pedido).getJson();

    // When
    MockHttpServletResponse response =
            mvc.perform(
                            post(String.format("%s/iniciar", ENDPOINT))
                                    .content(requestBody)
                                    .contentType(APPLICATION_JSON))
                    .andReturn()
                    .getResponse();

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();
  }

  @Test
  @DisplayName(
          "Deve retornar 400 quando chamar via POST o endpoint /api/v1/pagamentos/iniciar passando um pedido ja utilizado")
  void testIniciarException() throws Exception {
    // Given
    PagamentoEntity pagamentoEntity = testFixtureUtil.criarPagamentoEntity();
    Pedido pedido = testFixtureUtil.criarPedido();
    pedido.setId(pagamentoEntity.getPedidoId());
    String requestBody = pedidoJacksonTester.write(pedido).getJson();

    // When
    MockHttpServletResponse response =
            mvc.perform(
                            post(String.format("%s/iniciar", ENDPOINT))
                                    .content(requestBody)
                                    .contentType(APPLICATION_JSON))
                    .andReturn()
                    .getResponse();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonResponse = objectMapper.readTree(response.getContentAsString());

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(jsonResponse.path("title").asText())
            .isEqualTo("Esse pedido não é aplicável para pagamento.");
  }

  @Test
  @DisplayName(
      "Deve retornar 200 quando chamar via GET o endpoint /api/v1/pagamentos/confirmar/{idExterno} passando um pagamento valido")
  void testConfirm() throws Exception {
    // Given
    PagamentoEntity pagamentoEntity = testFixtureUtil.criarPagamentoEntity();

    RequestBuilder request =
        get(String.format("%s/confirmar/%s", ENDPOINT, pagamentoEntity.getIdExterno()))
            .contentType(APPLICATION_JSON);

    // When
    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo("Pagamento realizado com sucesso!");
  }

  @Test
  @DisplayName(
          "Deve retornar 400 quando chamar via POST o endpoint /api/v1/pagamentos/confirmar/{idExterno} passando um pagamento invalido")
  void testConfirmException() throws Exception {
    // Given
    PagamentoEntity pagamentoEntity = testFixtureUtil.criarPagamentoEntity();
    pagamentoEntity.setStatus(PagamentoStatus.REALIZADO);
    pagamentoRepository.save(pagamentoEntity);

    RequestBuilder request =
            get(String.format("%s/confirmar/%s", ENDPOINT, pagamentoEntity.getIdExterno()))
                    .contentType(APPLICATION_JSON);

    // When
    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonResponse = objectMapper.readTree(response.getContentAsString());

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(jsonResponse.path("title").asText())
            .isEqualTo("Este pagamento não é elegível para confirmação.");
  }

  @Test
  @DisplayName(
          "Deve retornar 400 quando chamar via POST o endpoint /api/v1/pagamentos/confirmar/{idExterno} passando um id invalido")
  void testConfirmException_InvalidId() throws Exception {
    // Given
    Integer idInvalido = 123456789;

    RequestBuilder request =
            get(String.format("%s/confirmar/%s", ENDPOINT, idInvalido))
                    .contentType(APPLICATION_JSON);

    // When
    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(response.getContentAsString()).isNotBlank();
  }

  @Test
  @DisplayName(
          "Deve retornar 200 quando chamar via POST o endpoint /api/v1/pagamentos/cancelar/{idExterno} passando um pagamento valido")
  void testCancel() throws Exception {
    // Given
    PagamentoEntity pagamentoEntity = testFixtureUtil.criarPagamentoEntity();

    RequestBuilder request =
            get(String.format("%s/cancelar/%s", ENDPOINT, pagamentoEntity.getIdExterno()))
                    .contentType(APPLICATION_JSON);

    // When
    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
            .isEqualTo("Pagamento não realizado. Pedido cancelado!");
  }

  @Test
  @DisplayName(
          "Deve retornar 400 quando chamar via POST o endpoint /api/v1/pagamentos/cancelar/{idExterno} passando um pagamento invalido")
  void testCancelException() throws Exception {
    // Given
    PagamentoEntity pagamentoEntity = testFixtureUtil.criarPagamentoEntity();
    pagamentoEntity.setStatus(PagamentoStatus.REALIZADO);
    pagamentoRepository.save(pagamentoEntity);

    RequestBuilder request =
            get(String.format("%s/cancelar/%s", ENDPOINT, pagamentoEntity.getIdExterno()))
                    .contentType(APPLICATION_JSON);

    // When
    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonResponse = objectMapper.readTree(response.getContentAsString());

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(jsonResponse.path("title").asText())
            .isEqualTo("Este pagamento não é elegível para cancelamento.");
  }

}
