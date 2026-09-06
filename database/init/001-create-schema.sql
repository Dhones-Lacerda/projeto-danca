CREATE DATABASE IF NOT EXISTS projeto_danca
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE projeto_danca;

CREATE TABLE interessado (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    data_nascimento DATE NOT NULL,
    nivel_experiencia VARCHAR(30) NOT NULL,
    estilo_danca VARCHAR(50) NOT NULL,
    observacoes VARCHAR(500),
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_interessado PRIMARY KEY (id),
    CONSTRAINT uk_interessado_email UNIQUE (email)
);