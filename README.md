# Aplicativo de Feedback para Produtos

Projeto acadêmico desenvolvido para a disciplina de Programação Web.
Aplicação web Java seguindo o padrão arquitetural MVC (Model-View-Controller).

**Autores:**
- André — RA 5169692 (Controller + View)
- Otávio — RA 5167958 (Model + Banco de Dados)

---

## Tecnologias Utilizadas

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Servidor | Apache Tomcat 10.1 |
| Banco de Dados | MySQL 8.0 |
| Acesso ao banco | JDBC puro (sem frameworks) |
| Build | Maven |
| Containerização | Docker + Docker Compose |
| Frontend | JSP + HTML/CSS puro |

---

## Arquitetura MVC

```
src/main/java/br/edu/faculdade/feedback/
├── controller/          → Servlets (requisição HTTP) — André
│   ├── ProdutoController.java
│   └── FeedbackController.java
├── service/             → Regras de negócio
│   ├── FeedbackService.java
│   └── ProdutoService.java
├── model/
│   ├── entity/          → Entidades / tabelas do banco — Otávio
│   │   ├── Usuario.java
│   │   ├── Produto.java
│   │   └── Feedback.java
│   └── dao/             → Acesso ao banco (SQL) — Otávio
│       ├── UsuarioDAO.java
│       ├── ProdutoDAO.java
│       └── FeedbackDAO.java
└── util/
    └── ConnectionFactory.java  → Conexão JDBC — Otávio

src/main/webapp/         → Telas JSP — André
├── index.jsp
├── lista_produtos.jsp
├── form_produto.jsp
├── form-feedback.jsp
└── detalhes_produto.jsp
```

---

## Banco de Dados

Banco MySQL com 3 tabelas:

- **Usuarios** — armazena quem envia o feedback (`id`, `nome`, `email`)
- **Produtos** — catálogo de produtos avaliáveis (`id`, `nome`, `descricao`)
- **Feedback** — avaliação de um usuário sobre um produto (`id`, `usuario_id`, `produto_id`, `nota`, `comentario`, `data_criacao`)

> O script completo de criação está em `schema.sql` e é executado automaticamente ao subir o Docker.

---

## Fluxo Completo do Código

### 1. Listagem de Produtos
```
Navegador → GET /produto/listar
  → ProdutoController.listar()
    → ProdutoService.listar()
      → ProdutoDAO.listarTodos()        [SELECT no banco]
        → List<Produto> retornada
  → request.setAttribute("produtos", lista)
  → forward para lista_produtos.jsp     [exibe a tabela]
```

### 2. Cadastro de Produto
```
Usuário preenche form_produto.jsp → POST /produto/cadastrar
  → ProdutoController.cadastrar()
    → ProdutoService.cadastrar(nome, descricao)
      [valida: nome não pode ser vazio]
      → ProdutoDAO.inserir(produto)     [INSERT no banco]
  → redirect para /produto/listar?sucesso_produto=true
```

### 3. Envio de Feedback
```
Usuário clica "Deixar Feedback" → GET /produto/avaliar?id=X
  → ProdutoController.avaliar()
    → ProdutoService.buscarPorId(id)   [SELECT no banco]
  → forward para form-feedback.jsp     [exibe formulário]

Usuário preenche o formulário → POST /feedback/salvar
  → FeedbackController.doPost()
    → FeedbackService.registrarFeedback(usuarioId, produtoId, nota, comentario)
      [valida: nota entre 1 e 5]
      [valida: comentário não vazio]
      [valida: usuário existe no banco]
      [valida: produto existe no banco]
      → FeedbackDAO.inserir(feedback)  [INSERT no banco]
  → redirect para /produto/listar?sucesso=true
```

### 4. Detalhes e Avaliações de um Produto
```
Usuário clica "★ Avaliações" → GET /produto/detalhes?id=X
  → ProdutoController.detalhes()
    → ProdutoService.buscarPorId(id)              [SELECT Produtos]
    → FeedbackService.listarFeedbacksDoProduto(id) [SELECT Feedback ORDER BY data DESC]
    → FeedbackService.calcularMediaAvaliacoes(feedbacks) [cálculo em memória]
  → request.setAttribute("produto", produto)
  → request.setAttribute("feedbacks", lista)
  → request.setAttribute("media", media)
  → forward para detalhes_produto.jsp  [exibe cards de avaliação + média]
```

---

## Como Executar

### Pré-requisito
- [Docker Desktop](https://www.docker.com/products/docker-desktop) instalado e em execução.

### Passos

**1. Clone o repositório:**
```bash
git clone https://github.com/Andre13Filho/feedbacks-para-produtos.git
cd feedbacks-para-produtos
```

**2. Suba a aplicação:**
```bash
docker-compose up --build
```
> Na primeira execução o Docker irá baixar as imagens e compilar o projeto (aguarde ~3 minutos).

**3. Acesse no navegador:**
```
http://localhost:8081
```

**4. Para encerrar:**
```bash
docker-compose down
```

---

## Funcionalidades

- [x] Listar todos os produtos cadastrados
- [x] Cadastrar novo produto
- [x] Deixar feedback (nota de 1 a 5 + comentário) em um produto
- [x] Ver todas as avaliações de um produto com média de notas
- [x] Validação de dados antes de salvar no banco
