# Aplicativo de Feedback para Produtos

Projeto acadêmico desenvolvido para a disciplina de Programação Web.
API REST em Java seguindo o padrão arquitetural MVC (Model-View-Controller).

**Autores:**
- André — RA 5169692 (Controller + View + JWT + PUT/DELETE)
- Otávio — RA 5167958 (Model + Banco de Dados + Singleton + Login)

---

## Tecnologias Utilizadas

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Servidor | Apache Tomcat 10.1 |
| Banco de Dados | MySQL 8.0 |
| Acesso ao banco | JDBC puro (sem frameworks) |
| Build | Maven |
| Serialização | Gson (JSON) |
| Autenticação | JWT (java-jwt) |
| Frontend | JSP + HTML/CSS puro |

---

## Arquitetura MVC

```
src/main/java/br/edu/faculdade/feedback/
├── controller/              → Servlets (requisição HTTP)
│   ├── ProdutoController.java   → CRUD de produtos (GET/POST/PUT/DELETE)
│   ├── FeedbackController.java  → CRUD de feedbacks
│   ├── UsuarioController.java   → Login e cadastro de usuários
│   └── AuthController.java      → Login com geração de token JWT
├── controller/api/
│   └── ApiFeedbackController.java → API REST de feedbacks
├── filter/
│   └── AuthFilter.java          → Filtro JWT para proteger rotas /api/*
├── service/                 → Regras de negócio
│   ├── FeedbackService.java
│   ├── ProdutoService.java
│   └── UsuarioService.java
├── model/
│   ├── entity/              → Entidades / tabelas do banco
│   │   ├── Usuario.java
│   │   ├── Produto.java
│   │   └── Feedback.java
│   └── dao/                 → Acesso ao banco (SQL)
│       ├── UsuarioDAO.java
│       ├── ProdutoDAO.java
│       └── FeedbackDAO.java
└── util/
    ├── ConnectionFactory.java    → Conexão JDBC Singleton
    ├── DatabaseInitializer.java  → Cria o banco automaticamente ao iniciar
    ├── JsonUtil.java             → Leitura/escrita de JSON
    └── JwtUtil.java              → Geração e validação de tokens JWT
```

---

## Banco de Dados

O banco é criado **automaticamente** quando a aplicação inicia (via `DatabaseInitializer`).
Não é necessário rodar nenhum script SQL manualmente.

Tabelas:
- **Usuarios** — quem envia o feedback (`id`, `nome`, `email`, `senha`)
- **Produtos** — catálogo de produtos avaliáveis (`id`, `nome`, `descricao`)
- **Feedback** — avaliação de um usuário sobre um produto (`id`, `usuario_id`, `produto_id`, `nota`, `comentario`, `data_criacao`)

Dados de teste são inseridos automaticamente na primeira execução.

---

## Como Executar

### Pré-requisitos
- **Java 17+** (JDK)
- **Maven 3.9+**
- **MySQL 8.0** (usuário `root`, senha `root`)
- **Apache Tomcat 10.1**

### Passos

**1. Clone o repositório:**
```bash
git clone https://github.com/Andre13Filho/feedbacks-para-produtos.git
cd feedbacks-para-produtos
```

**2. Compile o projeto:**
```bash
mvn clean package
```

**3. Copie o WAR para o Tomcat:**
```bash
cp target/feedback-app.war <TOMCAT_HOME>/webapps/
```

**4. Inicie o Tomcat:**
```bash
<TOMCAT_HOME>/bin/startup.sh    # Linux/Mac
<TOMCAT_HOME>\bin\startup.bat   # Windows
```

**5. Acesse no navegador:**
```
http://localhost:8080/feedback-app/
```

> O banco de dados e as tabelas são criados automaticamente na primeira execução.

---

## Endpoints da API

Todas as rotas `/api/*` (exceto login) exigem o header `Authorization: Bearer <token>`.

### Autenticação
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/login` | Login com email e senha, retorna token JWT |

### Usuários
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/usuarios/login` | Login (retorna dados do usuário) |
| POST | `/api/usuarios` | Cadastrar novo usuário |

### Produtos
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/produtos/listar` | Lista todos os produtos |
| GET | `/api/produtos/detalhes?id=X` | Detalhes de um produto com feedbacks |
| POST | `/api/produtos/cadastrar` | Cadastrar novo produto |
| PUT | `/api/produtos/atualizar` | Atualizar produto |
| DELETE | `/api/produtos/deletar` | Deletar produto |

### Feedbacks
| Método | Rota | Descrição |
|---|---|---|
| POST | `/feedback` | Enviar feedback |
| PUT | `/feedback` | Atualizar feedback |
| DELETE | `/feedback` | Deletar feedback |

---

## Exemplo de uso com Postman

**1. Fazer login:**
```json
POST http://localhost:8080/feedback-app/api/login
Body: { "email": "andre@email.com", "senha": "senha123" }
```

**2. Usar o token retornado nas demais requisições:**
```
Header: Authorization: Bearer <token_recebido>
```

---

## Funcionalidades

- [x] API REST com respostas em JSON
- [x] Autenticação via JWT (token)
- [x] CRUD completo de Produtos (GET, POST, PUT, DELETE)
- [x] CRUD completo de Feedbacks
- [x] Login com e-mail e senha
- [x] Validação de dados antes de salvar no banco
- [x] Conexão Singleton com o banco de dados
- [x] Banco criado automaticamente ao iniciar a aplicação
- [x] Dados de exemplo inseridos automaticamente
