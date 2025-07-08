# Desafio fase 4 - 10SOAT - Fastfood - Pagamentos

## 📝 Sobre o Projeto

Este projeto faz parte do **Tech Challenge - Fase 4** do curso de **Arquitetura de Software**. O objetivo é fornecer um microserviço responsável pelo processamento de pagamentos de pedidos em uma lanchonete de fast food, integrando-se ao Mercado Pago, gerando QR Codes para pagamento, confirmando/cancelando pagamentos e publicando eventos em uma fila (AWS SQS).

## ⚙️ Tecnologias e Dependências

- **Java 21+**
- **Spring Boot**
- **Spring Data JPA**
- **Flyway**
- **MongoDB**
- **AWS SQS (via LocalStack para desenvolvimento)**
- **Mercado Pago SDK**
- **Mapstruct**
- **Lombok**
- **Zalando Problem**
- **ZXing (geração de QR Code)**
- **OpenAPI/Swagger**
- **Docker e Docker Compose**

## 🚀 Como Subir o Projeto

### Pré-requisitos

- Java 21+
- Maven
- Docker
- Git

### Subindo dependências com Docker Compose

O projeto possui um arquivo `run/docker-compose.yml` para subir as dependências necessárias (como banco de dados e LocalStack):

```sh
docker compose -f run/docker-compose.yml up -d
```

### Rodando a aplicação

```sh
./mvnw spring-boot:run
```

## 📑 Documentação dos Endpoints

A documentação dos endpoints pode ser acessada via Swagger ou Redoc:

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)

### Principais Endpoints

- `POST /api/v1/pagamentos/iniciar`  
  Inicia um pagamento para um pedido e retorna o QR Code para pagamento via Mercado Pago.

- `GET /api/v1/pagamentos/confirmar/{idExterno}`  
  Callback para confirmar o pagamento de um pedido.

- `GET /api/v1/pagamentos/cancelar/{idExterno}`  
  Callback para cancelar o pagamento de um pedido.

## 🏗️ Fluxo de Pagamento

1. **Iniciar Pagamento:**  
   Recebe os dados do pedido, integra com o Mercado Pago, gera um QR Code e retorna para o cliente.

2. **Confirmação/Cancelamento:**  
   Permite confirmar ou cancelar o pagamento via endpoints, publicando o resultado em uma fila SQS.

3. **Mensageria:**  
   Utiliza AWS SQS para publicar eventos de pagamento, facilitando a comunicação assíncrona com outros serviços.

## 🔐 Configurações Importantes

As principais configurações estão em `src/main/resources/application.yml`, incluindo:

- Token do Mercado Pago
- URL do serviço
- Endereço da fila SQS
- Configuração do banco de dados PostgreSQL

## 🧩 Estrutura do Projeto

- `domain`: Entidades de domínio (Pagamento, Pedido, Cliente, etc)
- `application`: Casos de uso e portas
- `infrastructure`: Adapters REST, integração com Mercado Pago, mensageria (SQS), configuração

## 🛠️ Como Contribuir

1. Fork este repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nome`)
3. Commit suas alterações (`git commit -am 'Adiciona nova feature'`)
4. Push para o branch (`git push origin feature/nome`)
5. Abra um Pull Request


