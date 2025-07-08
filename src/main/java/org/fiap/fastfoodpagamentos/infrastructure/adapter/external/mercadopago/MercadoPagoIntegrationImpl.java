package org.fiap.fastfoodpagamentos.infrastructure.adapter.external.mercadopago;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import org.fiap.fastfoodpagamentos.application.port.driven.SolicitarPagamento;
import org.fiap.fastfoodpagamentos.domain.enumeration.PagamentoStatus;
import org.fiap.fastfoodpagamentos.domain.model.Pagamento;
import org.fiap.fastfoodpagamentos.domain.model.Pedido;
import org.fiap.fastfoodpagamentos.domain.model.PedidoProduto;
import org.fiap.fastfoodpagamentos.domain.model.Produto;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.external.mercadopago.dto.SolicitarQRCodeResponse;
import org.fiap.fastfoodpagamentos.infrastructure.adapter.external.mercadopago.exception.QrCodeException;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Slf4j
public class MercadoPagoIntegrationImpl implements SolicitarPagamento {

    public static final String BRL = "BRL";
    public static final String API_V_1_PAGAMENTOS_CONFIRMAR = "/api/v1/pagamentos/confirmar/";
    public static final String API_V_1_PAGAMENTOS_CANCELAR = "/api/v1/pagamentos/cancelar/";

    @Value("${app.mercadopagotoken}")
    private String mercadoPagoToken;

    @Value("${app.url}")
    private String url;

    @Override
    public Pagamento gerarQRCode(Pedido pedido) {
        try {
            SolicitarQRCodeResponse qrcode = gerarQRCodeStub(pedido);
            return Pagamento.builder()
                    .idExterno(qrcode.identificador())
                    .pedidoId(pedido.getId())
                    .status(PagamentoStatus.PENDENTE)
                    .qrCode(qrcode.QRCode())
                    .build();
        } catch (Exception e) {
            throw new QrCodeException(e);
        }
    }

    private SolicitarQRCodeResponse gerarQRCodeStub(Pedido pedido) throws Exception {
        UUID uuid = UUID.randomUUID();

        Preference preference = buildPreference(pedido, uuid);

        String qrCodeText = preference.getSandboxInitPoint();
        String filePath = "qrcode.png";
        int width = 300;
        int height = 300;
        Path path = FileSystems.getDefault().getPath(filePath);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (WriterException | IOException e) {
            log.error("Não foi possível gerar o qrcode.");
        }

        return new SolicitarQRCodeResponse(uuid.toString(), path.toString());
    }

    private Preference buildPreference(Pedido pedido, UUID uuid) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(mercadoPagoToken);

        List<PreferenceItemRequest> items =
                pedido.getPedidoProdutos().stream().map(this::mapToPreferenceItemRequest).toList();

        PreferenceBackUrlsRequest preferenceBackUrlsRequest =
                PreferenceBackUrlsRequest.builder()
                        .success(url + API_V_1_PAGAMENTOS_CONFIRMAR + uuid)
                        .failure(url + API_V_1_PAGAMENTOS_CANCELAR + uuid)
                        .pending(url + API_V_1_PAGAMENTOS_CANCELAR + uuid)
                        .build();

        PreferenceRequest preferenceRequest =
                PreferenceRequest.builder().items(items).backUrls(preferenceBackUrlsRequest).build();

        return new PreferenceClient().create(preferenceRequest);
    }

    private PreferenceItemRequest mapToPreferenceItemRequest(PedidoProduto pedidoProduto) {
        Produto produto = pedidoProduto.getProduto();
        return PreferenceItemRequest.builder()
                .id(String.valueOf(produto.getId()))
                .title(produto.getNome())
                .description(produto.getDescricao())
                .quantity(pedidoProduto.getQuantidade())
                .currencyId(BRL)
                .unitPrice(pedidoProduto.getValorUnitarioProdutoMomentoVenda())
                .build();
    }
}