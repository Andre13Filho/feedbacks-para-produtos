-- =============================================================
-- Script de criação do banco de dados
-- Projeto: Aplicativo de Feedback para Produtos
-- Autores: André (5169692) e Otávio (5167958)
-- Banco:   MySQL 8+
-- =============================================================

-- Cria o banco caso não exista e o seleciona
CREATE DATABASE IF NOT EXISTS feedback_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE feedback_db;

-- -------------------------------------------------------------
-- Tabela: Usuarios
-- Armazena os dados de quem envia o feedback
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Usuarios (
    id    INT          NOT NULL AUTO_INCREMENT,
    nome  VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

-- -------------------------------------------------------------
-- Tabela: Produtos
-- Catálogo de produtos que podem receber avaliações
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Produtos (
    id        INT          NOT NULL AUTO_INCREMENT,
    nome      VARCHAR(100) NOT NULL,
    descricao TEXT,
    PRIMARY KEY (id)
);

-- -------------------------------------------------------------
-- Tabela: Feedback
-- Relaciona um usuário a um produto com nota e comentário
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Feedback (
    id           INT       NOT NULL AUTO_INCREMENT,
    usuario_id   INT       NOT NULL,
    produto_id   INT       NOT NULL,
    nota         TINYINT   NOT NULL COMMENT 'Valor entre 1 e 5',
    comentario   TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_feedback_usuario FOREIGN KEY (usuario_id) REFERENCES Usuarios (id),
    CONSTRAINT fk_feedback_produto FOREIGN KEY (produto_id) REFERENCES Produtos (id),
    CONSTRAINT chk_nota CHECK (nota BETWEEN 1 AND 5)
);

-- -------------------------------------------------------------
-- Dados de exemplo para testes
-- -------------------------------------------------------------
INSERT INTO Usuarios (nome, email) VALUES
    ('André Silva',   'andre@email.com'),
    ('Otávio Santos', 'otavio@email.com');

INSERT INTO Produtos (nome, descricao) VALUES
    ('Notebook Gamer',  'Notebook de alto desempenho para jogos.'),
    ('Mouse Sem Fio',   'Mouse ergonômico com bateria de longa duração.'),
    ('Teclado Mecânico','Teclado com switches azuis e retroiluminação RGB.');

INSERT INTO Feedback (usuario_id, produto_id, nota, comentario) VALUES
    (1, 1, 5, 'Excelente produto, superou minhas expectativas!'),
    (2, 1, 4, 'Muito bom, mas poderia ser mais leve.'),
    (1, 2, 3, 'Mouse bom, porém a bateria dura menos do que o anunciado.');
