# 🛒 E-commerce API

Uma API REST completa de e-commerce construída com **Java 17**, **Spring Boot 3** e **Clean Architecture**.

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Arquitetura](#-arquitetura)
- [Tecnologias](#-tecnologias)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Funcionalidades](#-funcionalidades)
- [Como Executar](#-como-executar)
- [API Endpoints](#-api-endpoints)
- [Modelagem de Dados](#-modelagem-de-dados)
- [Testes](#-testes)
- [Deploy](#-deploy)
- [Contribuição](#-contribuição)

## 🎯 Sobre o Projeto

Este projeto é uma **API REST de E-commerce completa** que implementa:

- 🏛️ **Clean Architecture** com separação clara entre Domain, Application e Infrastructure
- 🔒 **Domain-Driven Design (DDD)** com Value Objects, Entities e Aggregates
- 📊 **Rich Domain Model** com regras de negócio encapsuladas nas entidades
- 🚀 **SOLID Principles** aplicados em toda a arquitetura
- ✅ **Testes unitários e de integração** com alta cobertura
- 📚 **Documentação automática** com OpenAPI/Swagger
- 🔐 **Autenticação JWT** e controle de acesso por roles
- 🐳 **Containerização** com Docker e Docker Compose
- 🚀 **CI/CD pipeline** automatizado

### Domínio de Negócio

A API gerencia um **e-commerce completo** com:

- **Catálogo de Produtos**: Criação, atualização e consulta de produtos
- **Gestão de Clientes**: Cadastro e gerenciamento de clientes
- **Carrinho de Compras**: Adição/remoção de itens
- **Pedidos**: Criação, confirmação e acompanhamento de pedidos
- **Pagamentos**: Integração com gateways de pagamento (Stripe, PayPal)
- **Autenticação**: Login, registro e controle de acesso (Admin/Cliente)

## 🏗️ Arquitetura

O projeto segue os princípios da **Clean Architecture** do Uncle Bob:

```
┌─────────────────────────────────────────────────────┐
│                   Infrastructure                    │
│  ┌─────────────────────────────────────────────────┤
│  │              Application                        │
│  │  ┌─────────────────────────────────────────────┤│
│  │  │                Domain                       ││
│  │  │  ┌─────────────────────────────────────────┤││
│  │  │  │  • Entities                            │││
│  │  │  │  • Value Objects                       │││
│  │  │  │  • Domain Services                     │││
│  │  │  │  • Repository Interfaces               │││
│  │  │  └─────────────────────────────────────────┤││
│  │  │  • Use Cases                               ││
│  │  │  • DTOs                                    ││
│  │  │  • Application Services                    ││
│  │  └─────────────────────────────────────────────┤│
│  │  • Controllers                                 │
│  │  • Repository Implementations                  │
│  │  • Configuration                               │
│  └─────────────────────────────────────────────────┤
└─────────────────────────────────────────────────────┘
```

### Camadas

#### 🎯 Domain (Núcleo)
- **Entities**: `Product`, `Customer`, `Order`, `OrderItem`
- **Value Objects**: `Money`, `Email`, `Address`
- **Repository Interfaces**: Contratos para persistência
- **Domain Services**: Lógicas complexas de domínio

#### 🎮 Application (Casos de Uso)
- **Use Cases**: `CreateProduct`, `RegisterCustomer`, `CreateOrder`
- **DTOs**: Objetos de transferência de dados
- **Exceptions**: Tratamento de erros específicos

#### 🔧 Infrastructure (Detalhes)
- **Controllers**: API REST endpoints
- **Repository Implementations**: JPA/Hibernate
- **Configuration**: Spring Boot, Security, etc.

## 🛠️ Tecnologias

### Core
- **Java 17** - LTS version
- **Spring Boot 3.2.x** - Framework principal
- **Maven** - Gerenciamento de dependências

### Persistence
- **Spring Data JPA** - ORM
- **Hibernate** - Provider JPA
- **H2 Database** - Desenvolvimento
- **PostgreSQL** - Produção

### Security
- **Spring Security** - Autenticação e autorização
- **JWT** - Tokens de acesso
- **BCrypt** - Hash de senhas

### Testing
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking
- **TestContainers** - Testes de integração
- **AssertJ** - Assertions fluidas

### Documentation
- **SpringDoc OpenAPI** - Documentação automática
- **Swagger UI** - Interface de documentação

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração local
- **GitHub Actions** - CI/CD
- **SonarQube** - Qualidade de código

## 📁 Estrutura do Projeto

```
ecommerce-api/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/
│   │   │   ├── domain/                     # 🎯 Camada de Domínio
│   │   │   │   ├── entities/               # Entidades de negócio
│   │   │   │   │   ├── Customer.java
│   │   │   │   │   ├── Order.java
│   │   │   │   │   ├── OrderItem.java
│   │   │   │   │   ├── OrderStatus.java
│   │   │   │   │   └── Product.java
│   │   │   │   ├── valueobjects/           # Value Objects
│   │   │   │   │   ├── Address.java
│   │   │   │   │   ├── Email.java
│   │   │   │   │   └── Money.java
│   │   │   │   └── repositories/           # Interfaces de repositório
│   │   │   │       ├── CustomerRepository.java
│   │   │   │       ├── OrderRepository.java
│   │   │   │       └── ProductRepository.java
│   │   │   ├── application/                # 🎮 Camada de Aplicação
│   │   │   │   ├── dto/                    # Data Transfer Objects
│   │   │   │   │   ├── AddressDTO.java
│   │   │   │   │   ├── CreateProductDTO.java
│   │   │   │   │   ├── CustomerDTO.java
│   │   │   │   │   ├── MoneyDTO.java
│   │   │   │   │   ├── ProductDTO.java
│   │   │   │   │   └── RegisterCustomerDTO.java
│   │   │   │   ├── exceptions/             # Exceções de aplicação
│   │   │   │   │   ├── BusinessException.java
│   │   │   │   │   ├── NotFoundException.java
│   │   │   │   │   └── ValidationException.java
│   │   │   │   └── usecases/               # Casos de uso
│   │   │   │       ├── customer/
│   │   │   │       │   └── RegisterCustomerUseCase.java
│   │   │   │       ├── order/
│   │   │   │       └── product/
│   │   │   │           ├── CreateProductUseCase.java
│   │   │   │           └── FindProductsUseCase.java
│   │   │   ├── infrastructure/             # 🔧 Camada de Infraestrutura
│   │   │   │   ├── adapters/
│   │   │   │   │   ├── controllers/        # REST Controllers
│   │   │   │   │   └── repositories/       # Implementações JPA
│   │   │   │   └── config/                 # Configurações
│   │   │   └── EcommerceApplication.java   # Classe principal
│   │   └── resources/
│   │       ├── application.yml             # Configurações da app
│   │       └── application-dev.yml         # Configurações de dev
│   └── test/                               # 🧪 Testes
│       └── java/
├── docker/
│   ├── Dockerfile                          # Container da aplicação
│   └── docker-compose.yml                  # Ambiente completo
├── .github/
│   └── workflows/
│       └── ci-cd.yml                       # Pipeline CI/CD
├── docs/                                   # Documentação adicional
├── pom.xml                                 # Dependências Maven
└── README.md                               # Este arquivo
```

## ✨ Funcionalidades

### 🛒 Produtos
- [x] Criar produto
- [x] Listar produtos
- [x] Buscar produto por ID
- [x] Buscar produtos por categoria
- [x] Buscar produtos por nome
- [x] Atualizar produto
- [x] Desativar produto
- [ ] Upload de imagens
- [ ] Gestão de estoque avançada

### 👥 Clientes
- [x] Registrar cliente
- [x] Buscar cliente por ID
- [x] Buscar cliente por email
- [x] Atualizar informações
- [ ] Histórico de compras
- [ ] Lista de desejos

### 🛍️ Pedidos
- [ ] Criar pedido
- [ ] Adicionar item ao pedido
- [ ] Remover item do pedido
- [ ] Confirmar pedido
- [ ] Cancelar pedido
- [ ] Acompanhar status
- [ ] Histórico de pedidos

### 💳 Pagamentos
- [ ] Integração Stripe
- [ ] Integração PayPal
- [ ] Simulação de pagamento
- [ ] Webhook de confirmação

### 🔐 Autenticação
- [ ] Registro de usuário
- [ ] Login JWT
- [ ] Refresh token
- [ ] Controle por roles (Admin/Cliente)
- [ ] Recuperação de senha

## 🚀 Como Executar

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **Docker** (opcional)

### 1. Executar Localmente

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/ecommerce-api.git
cd ecommerce-api

# Execute a aplicação
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080/api`

### 2. Executar com Docker

```bash
# Build da imagem
docker build -t ecommerce-api .

# Execute o container
docker run -p 8080:8080 ecommerce-api
```

### 3. Ambiente Completo (Docker Compose)

```bash
# Suba todos os serviços
docker-compose up -d

# Logs
docker-compose logs -f
```

### 4. Acesso às Ferramentas

- **API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console
    - JDBC URL: `jdbc:h2:mem:ecommerce`
    - User: `sa`
    - Password: (vazio)

## 🌐 API Endpoints

### 🛒 Products

```http
GET    /api/products                    # Listar produtos ativos
GET    /api/products/{id}               # Buscar produto por ID
GET    /api/products/category/{category} # Buscar por categoria
GET    /api/products/search?name={name}  # Buscar por nome
POST   /api/products                    # Criar produto
PUT    /api/products/{id}               # Atualizar produto
DELETE /api/products/{id}               # Desativar produto
```

### 👥 Customers

```http
GET    /api/customers/{id}              # Buscar cliente por ID
GET    /api/customers/email/{email}     # Buscar cliente por email
POST   /api/customers                  # Registrar cliente
PUT    /api/customers/{id}              # Atualizar cliente
```

### 🛍️ Orders

```http
GET    /api/orders/{id}                 # Buscar pedido por ID
GET    /api/orders/customer/{customerId} # Pedidos do cliente
POST   /api/orders                     # Criar pedido
POST   /api/orders/{id}/items          # Adicionar item
PUT    /api/orders/{id}/confirm         # Confirmar pedido
PUT    /api/orders/{id}/cancel          # Cancelar pedido
```

### Exemplos de Request

#### Criar Produto

```json
POST /api/products
{
  "name": "iPhone 15 Pro",
  "description": "Smartphone Apple iPhone 15 Pro 128GB",
  "price": 7999.99,
  "stockQuantity": 50,
  "category": "Smartphones",
  "currency": "BRL"
}
```

#### Registrar Cliente

```json
POST /api/customers
{
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao.silva@email.com",
  "phone": "+5511999999999",
  "address": {
    "street": "Rua das Flores",
    "number": "123",
    "complement": "Apt 45",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01234-567",
    "country": "Brasil"
  }
}
```

## 📊 Modelagem de Dados

### Entidades Principais

```mermaid
erDiagram
    CUSTOMER {
        UUID id PK
        string first_name
        string last_name
        string email UK
        string phone
        string address_street
        string address_number
        string address_complement
        string address_neighborhood
        string address_city
        string address_state
        string address_zip_code
        string address_country
        boolean active
        timestamp created_at
        timestamp updated_at
    }
    
    PRODUCT {
        UUID id PK
        string name UK
        string description
        decimal price_amount
        string price_currency
        integer stock_quantity
        string category
        boolean active
        timestamp created_at
        timestamp updated_at
    }
    
    ORDER {
        UUID id PK
        UUID customer_id FK
        string status
        string delivery_address_street
        string delivery_address_number
        string delivery_address_complement
        string delivery_address_neighborhood
        string delivery_address_city
        string delivery_address_state
        string delivery_address_zip_code
        string delivery_address_country
        decimal total_amount
        string total_currency
        timestamp created_at
        timestamp updated_at
    }
    
    ORDER_ITEM {
        UUID id PK
        UUID order_id FK
        UUID product_id FK
        string product_name
        decimal unit_price_amount
        string unit_price_currency
        integer quantity
        decimal total_price_amount
        string total_price_currency
    }
    
    CUSTOMER ||--o{ ORDER : places
    ORDER ||--o{ ORDER_ITEM : contains
    PRODUCT ||--o{ ORDER_ITEM : referenced_in
```

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
./mvnw test

# Apenas testes unitários
./mvnw test -Dtest="*UnitTest"

# Apenas testes de integração
./mvnw test -Dtest="*IntegrationTest"

# Com relatório de cobertura
./mvnw jacoco:prepare-agent test jacoco:report
```

### Estrutura de Testes

```
src/test/java/
├── unit/                               # Testes unitários
│   ├── domain/                         # Testes das entidades
│   │   ├── entities/
│   │   └── valueobjects/
│   └── application/                    # Testes dos use cases
│       └── usecases/
└── integration/                        # Testes de integração
    ├── controllers/                    # Testes de API
    └── repositories/                   # Testes de persistência
```

## 🚀 Deploy

### Ambientes Suportados

- **Heroku** - Deploy simples com Git
- **Railway** - Deploy moderno com Git
- **AWS ECS** - Container orquestrado
- **Google Cloud Run** - Serverless containers
- **Digital Ocean** - VPS tradicional

### Deploy no Heroku

```bash
# Login no Heroku
heroku login

# Criar aplicação
heroku create ecommerce-api-prod

# Configurar variáveis de ambiente
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set DATABASE_URL=postgresql://...

# Deploy
git push heroku main
```

### Variáveis de Ambiente

```env
# Perfil da aplicação
SPRING_PROFILES_ACTIVE=prod

# Banco de dados
DATABASE_URL=postgresql://user:pass@host:port/db
DATABASE_USERNAME=user
DATABASE_PASSWORD=pass

# JWT
JWT_SECRET=seu-jwt-secret-super-seguro
JWT_EXPIRATION=86400

# Integração pagamentos
STRIPE_SECRET_KEY=sk_live_...
PAYPAL_CLIENT_ID=...
PAYPAL_CLIENT_SECRET=...
```

## 📈 Roadmap

### v1.0 (MVP) - ✅ Em Desenvolvimento
- [x] CRUD de Produtos
- [x] CRUD de Clientes
- [x] Sistema de Pedidos básico
- [ ] Autenticação JWT
- [ ] Documentação Swagger

### v1.1 - Pagamentos
- [ ] Integração Stripe
- [ ] Integração PayPal
- [ ] Webhooks de confirmação

### v1.2 - Features Avançadas
- [ ] Sistema de cupons
- [ ] Avaliações de produtos
- [ ] Notificações por email

### v2.0 - Escalabilidade
- [ ] Cache com Redis
- [ ] Mensageria com RabbitMQ
- [ ] Logs estruturados
- [ ] Métricas com Prometheus

## 🤝 Contribuição

### Como Contribuir

1. **Fork** o projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/nova-feature`)
3. **Commit** suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. **Push** para a branch (`git push origin feature/nova-feature`)
5. Abra um **Pull Request**

### Padrões de Código

- **Java Code Style**: Google Java Style Guide
- **Commits**: Conventional Commits
- **Branches**: feature/, bugfix/, hotfix/
- **Testes**: Cobertura mínima de 80%

### Executar Quality Gates

```bash
# Checkstyle
./mvnw checkstyle:check

# SpotBugs
./mvnw spotbugs:check

# Testes + Cobertura
./mvnw clean test jacoco:report

# SonarQube (se configurado)
./mvnw sonar:sonar
```

## 📄 Licença

Este projeto está sob a licença **MIT**. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👤 Autor

**Seu Nome**
- GitHub: [@lorranlhss](https://github.com/lorranlhss)
- LinkedIn: [Lorran-Henrique](https://www.linkedin.com/in/lorran-henrique-1a89b316a/)
- Email: lorranlhss@outlook.com

---

⭐ **Star** este projeto se ele te ajudou!

🐛 **Issues** e **Pull Requests** são sempre bem-vindos!

---

*Foi Usado Clean Architecture e Spring Boot*