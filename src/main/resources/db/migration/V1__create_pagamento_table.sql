CREATE TABLE pagamento
(
    id         SERIAL PRIMARY KEY,
    id_externo VARCHAR(255) NOT NULL,
    pedido_id  INT NOT NULL,
    status     VARCHAR(50)  NOT NULL,
    qr_code    TEXT
);
