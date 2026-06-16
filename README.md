# Aplicativo de Feedback para Produtos

Projeto acadêmico desenvolvido para a disciplina de Programação Web.
API REST nativa em Java (usando Servlets) seguindo o padrão arquitetural MVC.

**Autores:**
- André — RA 5169692 (Controller + View + JWT + PUT/PATCH/DELETE)
- Otávio — RA 5167958 (Model + Banco de Dados + Singleton + Login)

---

## Tecnologias Utilizadas

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Servidor | Apache Tomcat 10.1 (via Docker) |
| Banco de Dados | MySQL 8.0 (via Docker) |
| Acesso ao banco | JDBC puro (sem frameworks) |
| Build | Maven |
| Serialização | Gson (JSON) |
| Autenticação | JWT (java-jwt da Auth0) |
| Containerização | Docker e Docker Compose |

---

## Arquitetura MVC

```text
src/main/java/br/edu/faculdade/feedback/
├── controller/              → Servlets REST (requisição HTTP)
│   ├── ProdutoRestController.java   → CRUD de produtos (GET/POST/PUT/PATCH/DELETE)
│   ├── FeedbackRestController.java  → CRUD de feedbacks (GET/POST/PUT/PATCH/DELETE)
│   ├── UsuarioController.java       → Cadastro e login de usuários
│   └── AuthController.java          → Geração de token JWT (/api/login)
├── filter/
│   └── AuthFilter.java          → Filtro para proteger rotas /api/* exigindo JWT
├── service/                 → Regras de negócio
│   ├── FeedbackService.java
│   ├── ProdutoService.java
│   └── UsuarioService.java
├── model/
│   ├── entity/              → Entidades / tabelas do banco
│   └── dao/                 → Acesso ao banco (SQL)
└── util/
    ├── ConnectionFactory.java    → Conexão JDBC Singleton
    ├── JsonUtil.java             → Leitura/escrita de JSON (Respostas 200, 201, 204)
    └── JwtUtil.java              → Geração e validação de tokens JWT
```

---

## Banco de Dados

O banco de dados e suas tabelas são criados **automaticamente** pelo script `schema.sql` montado na imagem do MySQL pelo Docker Compose.
Não é necessário rodar nenhum script SQL manualmente.

Tabelas:
- **Usuarios** — quem envia o feedback (`id`, `nome`, `email`, `senha`)
- **Produtos** — catálogo de produtos avaliáveis (`id`, `nome`, `descricao`)
- **Feedback** — avaliação de um usuário sobre um produto (`id`, `usuario_id`, `produto_id`, `nota`, `comentario`, `data_criacao`)

Dados de teste são inseridos automaticamente na inicialização do container de banco de dados.

---

## Como Executar

Toda a aplicação agora está containerizada, tornando a execução extremamente simples!

### Pré-requisitos
- **Docker**
- **Docker Compose**

### Passos

**1. Clone o repositório:**
```bash
git clone https://github.com/Andre13Filho/feedbacks-para-produtos.git
cd feedbacks-para-produtos
```

**2. Inicie a aplicação com Docker Compose:**
```bash
docker-compose up -d --build
```
*Isso fará o build do Java via Maven e iniciará os containers do Tomcat e do MySQL.*

**3. Acesse a API:**
A aplicação estará disponível na porta `8081`:
```
http://localhost:8081/api/...
```

Para parar a aplicação, rode: `docker-compose down`

---

## Endpoints da API REST

Todas as rotas `/api/*` exigem o header `Authorization: Bearer <token>`, com exceção do Login e Cadastro de Usuários.

### Autenticação & Usuários
| Método | Rota | Descrição | Requer Token? |
|---|---|---|---|
| POST | `/api/login` | Login com email e senha, retorna token JWT | Não |
| POST | `/api/usuarios` | Cadastrar novo usuário | Não |

### Produtos
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/produtos` | Lista todos os produtos |
| GET | `/api/produtos/{id}` | Detalhes de um produto e seus feedbacks |
| POST | `/api/produtos` | Cadastrar novo produto |
| PUT | `/api/produtos/{id}` | Atualização completa do produto |
| PATCH | `/api/produtos/{id}` | Atualização parcial do produto |
| DELETE | `/api/produtos/{id}` | Deleta um produto (Retorna 204 No Content) |

### Feedbacks
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/feedbacks?produtoId={id}`| Lista feedbacks de um produto |
| POST | `/api/feedbacks` | Cadastrar feedback (usuarioId extraído do JWT) |
| PUT | `/api/feedbacks/{id}` | Atualização completa do feedback |
| PATCH | `/api/feedbacks/{id}` | Atualização parcial do feedback |
| DELETE | `/api/feedbacks/{id}` | Deleta um feedback (Retorna 204 No Content) |

---

## Testando com Postman

**1. Fazer login:**
```http
POST http://localhost:8081/api/login
Body (raw -> JSON):
{
    "email": "andre@email.com",
    "senha": "senha123"
}
```

**2. Copie o token da resposta:**
```json
{
    "token": "eyJhbGci...",
    "userId": 1,
    "nome": "André Silva"
}
```

**3. Usar o token nas requisições protegidas (Ex: GET /api/produtos):**
No Postman, vá na aba **Authorization** > selecione **Bearer Token** > Cole o seu token no campo *Token*.

---

## Funcionalidades Implementadas

- [x] API REST Nativa com Servlets
- [x] Padrão JSON para respostas e recebimento de payload no corpo (Body)
- [x] Autenticação Stateless via JWT (java-jwt da Auth0)
- [x] Filtro de Segurança (AuthFilter) barrando acessos indevidos com 401 Unauthorized
- [x] CRUD completo e aderente aos métodos HTTP (GET, POST, PUT, PATCH, DELETE)
- [x] Status Codes HTTP apropriados (200 OK, 201 Created, 204 No Content, 400, 401, 404, 500)
- [x] Containerização via Docker (Aplicação + Banco de Dados MySQL)
