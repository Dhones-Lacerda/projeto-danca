# Projeto Dança - Cadastro de Interessados

> **Documento oficial do projeto**
>
> Este documento é a fonte oficial de documentação do projeto e será incrementado ao longo do desenvolvimento.
>
> **Status:** Em desenvolvimento
> **Versão:** 0.1.0
> **Tipo:** Projeto Full Stack
> **Objetivo:** Criar uma aplicação para cadastro e gerenciamento de pessoas interessadas em dança.

---

## 1. Visão Geral

O projeto consiste no desenvolvimento de uma aplicação web para cadastro de pessoas interessadas em aulas de dança.

A aplicação permitirá:

* cadastrar interessados;
* consultar interessados cadastrados;
* consultar um interessado específico;
* editar dados;
* excluir cadastros;
* validar os dados informados;
* armazenar as informações em banco de dados MySQL.

A aplicação será desenvolvida utilizando uma arquitetura Full Stack composta por:

* **Frontend:** Angular
* **Backend:** Java + Spring Boot
* **Banco de dados:** MySQL
* **Persistência:** Spring Data JPA
* **Build:** Maven
* **Containerização:** Docker / Docker Compose
* **Versionamento:** Git / GitHub
* **API:** REST
* **Documentação da API:** OpenAPI / Swagger

---

## 2. Objetivo do MVP

> Permitir que uma pessoa seja cadastrada como interessada em dança e que seus dados possam posteriormente ser consultados, alterados ou removidos.

O fluxo principal será:

```text
Usuário
  ↓
Angular
  ↓ HTTP/REST
Spring Boot
  ↓
Service
  ↓
Repository
  ↓
MySQL
```

No retorno:

```text
MySQL
  ↓
Repository
  ↓
Service
  ↓
Controller
  ↓ HTTP/REST
Angular
  ↓
Usuário
```

---

## 3. Funcionalidades

### 3.1 Cadastro

O sistema deverá permitir o cadastro de um interessado contendo:

* Nome completo
* E-mail
* Telefone
* Data de nascimento
* Nível de experiência
* Estilo de dança
* Observações

### 3.2 Consulta

O sistema deverá permitir:

* listar todos os interessados;
* consultar um interessado pelo ID.

A listagem apresenta os registros mais recentes primeiro.

### 3.3 Edição

O sistema deverá permitir alterar os dados de um interessado existente.

### 3.4 Exclusão

O sistema deverá permitir excluir um interessado.

---

## 4. Dados do Interessado

### 4.1 Campos

| Campo            | Tipo     | Obrigatório |                Limite |
| ---------------- | -------- | ----------: | --------------------: |
| nome             | String   |         Sim |        150 caracteres |
| email            | String   |         Sim |        150 caracteres |
| telefone         | String   |         Sim |         20 caracteres |
| dataNascimento   | Date     |         Sim | Deve estar no passado |
| nivelExperiencia | Enum     |         Sim |                     - |
| estiloDanca      | Enum     |         Sim |                     - |
| observacoes      | String   |         Não |        500 caracteres |
| dataCadastro     | DateTime |  Automático |     Gerado pelo banco |

---

## 5. Níveis de Experiência

Os níveis disponíveis são:

```text
NUNCA_DANCEI
INICIANTE
INTERMEDIARIO
AVANCADO
```

---

## 6. Estilos de Dança

Os estilos disponíveis inicialmente são:

```text
SAMBA
FORRO
SALSA
BACHATA
ZOUK
DANCA_DE_SALAO
HIP_HOP
BALLET
OUTRO
```

---

## 7. Regras de Negócio

* Nome é obrigatório.
* Nome possui limite de 150 caracteres.
* E-mail é obrigatório.
* E-mail deve possuir formato válido.
* E-mail possui limite de 150 caracteres.
* E-mail deve ser único.
* Telefone é obrigatório.
* Telefone possui limite de 20 caracteres.
* Data de nascimento é obrigatória.
* Data de nascimento deve estar no passado.
* Nível de experiência é obrigatório.
* Estilo de dança é obrigatório.
* Observações são opcionais.
* Observações possuem limite de 500 caracteres.

As validações deverão existir no frontend para melhorar a experiência do usuário.

Entretanto:

> **O backend será a autoridade final sobre as regras de validação.**

Isso evita que regras importantes dependam exclusivamente do frontend.

As principais validações do backend foram implementadas utilizando **Bean Validation**, por meio de anotações como `@NotBlank`, `@NotNull`, `@Email`, `@Size` e `@Past`.

---

## 8. Banco de Dados

### 8.1 Banco

```text
projeto_danca
```

### 8.2 Tabela

```text
interessado
```

### 8.3 Estrutura

```sql
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
```

A coluna `email` possui uma restrição de unicidade no banco de dados.

A coluna `data_cadastro` é preenchida automaticamente pelo MySQL no momento do cadastro.

O backend utiliza:

```text
ddl-auto: validate
```

Isso significa que o Hibernate valida a estrutura existente do banco, sem criar ou alterar automaticamente as tabelas.

---

## 9. API REST

A API utiliza como base:

```text
http://localhost:8080/api
```

Endpoints:

| Método | Endpoint             | Objetivo              | Status principal |
| ------ | -------------------- | --------------------- | ---------------- |
| POST   | `/interessados`      | Criar interessado     | 201              |
| GET    | `/interessados`      | Listar interessados   | 200              |
| GET    | `/interessados/{id}` | Buscar interessado    | 200              |
| PUT    | `/interessados/{id}` | Atualizar interessado | 200              |
| DELETE | `/interessados/{id}` | Excluir interessado   | 204              |

### 9.1 Códigos de resposta

A API utiliza os seguintes códigos:

```text
200 OK
201 Created
204 No Content
400 Bad Request
404 Not Found
409 Conflict
500 Internal Server Error
```

### 9.2 Tratamento de erros

O backend possui tratamento global de exceções utilizando `@RestControllerAdvice`.

Entre os erros tratados estão:

* dados de entrada inválidos;
* JSON inválido;
* interessado não encontrado;
* e-mail já cadastrado.

Exemplo de resposta para interessado não encontrado:

```json
{
  "status": 404,
  "codigo": "INTERESSADO_NAO_ENCONTRADO",
  "mensagem": "Interessado não encontrado."
}
```

Exemplo de resposta para e-mail duplicado:

```json
{
  "status": 409,
  "codigo": "EMAIL_JA_CADASTRADO",
  "mensagem": "Já existe um interessado cadastrado com este e-mail."
}
```

---

## 10. Documentação OpenAPI / Swagger

A API possui documentação utilizando **OpenAPI/Swagger**, integrada ao Spring Boot por meio do SpringDoc.

Dependência utilizada:

```text
org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0
```

A documentação contempla os cinco endpoints da API:

```text
POST   /api/interessados
GET    /api/interessados
GET    /api/interessados/{id}
PUT    /api/interessados/{id}
DELETE /api/interessados/{id}
```

A documentação inclui:

* resumo das operações;
* descrição dos endpoints;
* parâmetros de caminho;
* corpos das requisições;
* modelos de entrada e saída;
* códigos de resposta;
* principais cenários de erro.

Os modelos documentados incluem:

```text
InteressadoRequest
InteressadoResponse
```

A interface Swagger UI pode ser acessada localmente em:

```text
http://localhost:8080/swagger-ui.html
```

### 10.1 Validação do Swagger

A documentação foi validada visualmente no Swagger UI, endpoint por endpoint.

Foram conferidos:

* `POST /api/interessados`

  * 201
  * 400
  * 409

* `GET /api/interessados`

  * 200
  * `InteressadoResponse`

* `GET /api/interessados/{id}`

  * 200
  * 404
  * parâmetro `id`

* `PUT /api/interessados/{id}`

  * 200
  * 400
  * 404
  * 409
  * parâmetro `id`
  * `InteressadoRequest`

* `DELETE /api/interessados/{id}`

  * 204
  * 404
  * parâmetro `id`

Todos os itens foram conferidos e considerados corretos.

---

## 11. Arquitetura do Backend

É utilizada a separação:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

Responsabilidades:

* **Controller:** recebe requisições HTTP e devolve respostas.
* **Service:** concentra regras de negócio.
* **Repository:** realiza a comunicação com o banco através do Spring Data JPA.
* **Entity:** representa os dados persistidos.
* **DTO:** controla os dados que entram e saem da API.

A API não deverá expor diretamente a Entity como contrato público.

Os DTOs utilizados são:

```text
InteressadoRequest
InteressadoResponse
ErroResponse
ErroValidacaoResponse
```

---

## 12. Testes

O backend possui testes automatizados utilizando JUnit e o suporte de testes do Spring Boot.

Atualmente existem testes para:

* carregamento do contexto da aplicação;
* comportamento do Repository;
* comportamento do Service;
* cadastro;
* e-mail duplicado;
* atualização;
* atualização com e-mail duplicado;
* exclusão;
* tentativa de exclusão de interessado inexistente.

### 12.1 Validação dos testes

O comando utilizado para validação completa do backend é:

```powershell
.\mvnw.cmd clean test
```

A execução mais recente foi concluída com sucesso.

---

## 13. Estrutura do Projeto

A estrutura atual é:

```text
projeto-danca/
├── .github/
├── backend/
├── frontend/
├── database/
│   └── init/
├── docs/
├── docker-compose.yml
├── .env
├── .gitignore
└── README.md
```

> O arquivo `.env` contém configurações locais e não deve ser versionado.

---

## 14. Docker

O MySQL é executado através do Docker.

Configuração atual:

```text
Host: localhost
Porta externa: 3307
Porta interna do container: 3306
Database: projeto_danca
Container: projeto-danca-mysql
```

Mapeamento:

```text
localhost:3307
      ↓
container:3306
```

O motivo da porta externa `3307` é evitar conflito com uma instalação local do MySQL que utiliza a porta `3306`.

---

## 15. Docker Compose

O projeto possui um serviço MySQL:

```yaml
services:
  mysql:
    image: mysql:8.4
    container_name: projeto-danca-mysql
    restart: unless-stopped
    environment:
      MYSQL_DATABASE: projeto_danca
      MYSQL_USER: projeto
      MYSQL_PASSWORD: ${DB_PASSWORD}
      MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./database/init:/docker-entrypoint-initdb.d

volumes:
  mysql_data:
```

---

## 16. Variáveis de Ambiente

O projeto utiliza variáveis de ambiente para informações sensíveis e específicas do ambiente.

Configuração utilizada pelo backend:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3307/projeto_danca
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

Exemplo local:

```env
DB_USERNAME=projeto
DB_PASSWORD=projeto123
DB_ROOT_PASSWORD=root123
```

> **Observação:** os valores acima são exclusivamente para desenvolvimento local e devem ser substituídos conforme o ambiente.

O arquivo `.env` não deverá ser versionado.

O projeto disponibiliza:

```text
.env.example
```

como modelo para outros desenvolvedores.

---

## 17. Versionamento

O projeto utiliza Git e GitHub.

A estratégia de branches adotada é:

```text
main
 ↓
develop
 ↓
feature/*
fix/*
hotfix/*
```

Responsabilidades:

* **main:** versão estável/produção;
* **develop:** integração e testes;
* **feature/*:** desenvolvimento de funcionalidades;
* **fix/*:** correções;
* **hotfix/*:** correções urgentes na versão estável.

Não devem ser realizadas alterações diretamente em `main` ou `develop` durante o desenvolvimento normal.

O fluxo planejado é:

```text
feature
   ↓
commit
   ↓
push
   ↓
Pull Request
   ↓
Code Review / QA
   ↓
develop
   ↓
Integração
   ↓
Pull Request
   ↓
main
```

### 17.1 Padrão de commits

São utilizados os seguintes tipos:

```text
feat:
fix:
docs:
test:
chore:
```

Exemplo:

```text
feat: configura banco de dados com MySQL e Docker
```

### 17.2 Feature Swagger/OpenAPI

A documentação OpenAPI foi desenvolvida na branch:

```text
feature/backend/swagger-openapi
```

Commit:

```text
356ac25 feat: adiciona documentação OpenAPI à API
```

A branch foi publicada no GitHub e o próximo estágio do fluxo é a revisão por Pull Request direcionado para `develop`.

---

## 18. Backlog

### EPIC 01 - Fundação

* TASK-001 - Repositório GitHub
* TASK-002 - Estrutura do projeto
* TASK-003 - `.gitignore`
* TASK-004 - README

### EPIC 02 - Banco de Dados

* TASK-005 - Docker Compose
* TASK-006 - Container MySQL
* TASK-007 - Volume persistente
* TASK-008 - Banco de dados
* TASK-009 - Tabela interessado
* TASK-010 - Conexão da aplicação com o banco

### EPIC 03 - Backend

* TASK-011 - Criar projeto Spring Boot
* TASK-012 - Configurar Maven
* TASK-013 - Configurar dependências
* TASK-014 - Configurar conexão com banco
* TASK-015 - Criar Entity
* TASK-016 - Criar Enums
* TASK-017 - Criar Repository
* TASK-018 - Criar Request DTO
* TASK-019 - Criar Response DTO
* TASK-020 - Criar Service
* TASK-021 - Criar Controller
* TASK-022 - Implementar POST
* TASK-023 - Implementar GET
* TASK-024 - Implementar GET por ID
* TASK-025 - Implementar PUT
* TASK-026 - Implementar DELETE
* TASK-027 - Tratamento de exceções
* TASK-028 - Swagger/OpenAPI
* TASK-029 - Testes

### EPIC 04 - Frontend

* TASK-030 - Criar projeto Angular
* TASK-031 - Estrutura frontend
* TASK-032 - Modelos TypeScript
* TASK-033 - Service HTTP
* TASK-034 - Tela de cadastro
* TASK-035 - Reactive Forms
* TASK-036 - Validações
* TASK-037 - POST
* TASK-038 - Mensagens
* TASK-039 - Listagem
* TASK-040 - Edição
* TASK-041 - Exclusão
* TASK-042 - Loading e estado vazio
* TASK-043 - Testes

### EPIC 05 - Integração

* TASK-044 - CORS
* TASK-045 - Integração Angular/Backend
* TASK-046 - Teste de criação
* TASK-047 - Teste de listagem
* TASK-048 - Teste de edição
* TASK-049 - Teste de exclusão
* TASK-050 - Tratamento de erros

### EPIC 06 - Docker completo

* TASK-051 - Dockerfile Backend
* TASK-052 - Container Backend
* TASK-053 - Comunicação Backend/MySQL
* TASK-054 - Backend no Compose
* TASK-055 - Dockerfile Frontend
* TASK-056 - Container Frontend
* TASK-057 - Frontend no Compose
* TASK-058 - Teste completo

### EPIC 07 - GitHub

* TASK-059 - Branch develop
* TASK-060 - Feature branches
* TASK-061 - Pull Requests
* TASK-062 - Code Review
* TASK-063 - Correções
* TASK-064 - Merge develop -> main
* TASK-065 - Tag/Release
