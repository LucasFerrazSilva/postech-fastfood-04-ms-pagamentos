package org.fiap.fastfoodpagamentos.infrastructure.adapter.external.mercadopago;

import org.fiap.fastfoodpagamentos.infrastructure.adapter.external.mercadopago.exception.QrCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.docker.compose.enabled=true")
class MercadoPagoIntegrationImplTest {

    @Autowired
    private MercadoPagoIntegrationImpl mercadoPagoIntegration;


    @Test
    @DisplayName(
            "Deve retornar exception quando chamar metodo passando pedido vazio")
    void testException() {
        assertThrows(QrCodeException.class, () -> mercadoPagoIntegration.gerarQRCode(null));
    }

}
