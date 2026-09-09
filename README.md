> # Projeto Dança - Cadastro de Interessados
>
> > **Documento oficial do projeto**
> >
> > Este documento é a fonte oficial de documentação do projeto e será incrementado ao longo do desenvolvimento.
> >
> > **Status:** Em desenvolvimento
> > **Versão:** 0.1.0
> > **Tipo:** Projeto Full Stack
> > **Objetivo:** Criar uma aplicação para cadastro e gerenciamento de pessoas interessadas em dança.
>
> ---
>
> ## 1. Visão Geral
>
> O projeto consiste no desenvolvimento de uma aplicação web para cadastro de pessoas interessadas em aulas de dança.
>
> A aplicação permitirá:
>
> * cadastrar interessados;
> * consultar interessados cadastrados;
> * consultar um interessado específico;
> * editar dados;
> * excluir cadastros;
> * validar os dados informados;
> * armazenar as informações em banco de dados MySQL.
>
> A aplicação será desenvolvida utilizando uma arquitetura Full Stack composta por:
>
> * **Frontend:** Angular
> * **Backend:** Java + Spring Boot
> * **Banco de dados:** MySQL
> * **Persistência:** Spring Data JPA
> * **Build:** Maven
> * **Containerização:** Docker / Docker Compose
> * **Versionamento:** Git / GitHub
> * **API:** REST
> * **Documentação da API:** OpenAPI / Swagger
>
> ---
>
> ## 2. Objetivo do MVP
>
> > Permitir que uma pessoa seja cadastrada como interessada em dança e que seus dados possam posteriormente ser consultados, alterados ou removidos.
>
> O fluxo principal será:
>
> ```text
> Usuário
>   ↓
> Angular
>   ↓ HTTP/REST
> Spring Boot
>   ↓
> Service
>   ↓
> Repository
>   ↓
> MySQL
> ```
>
> No retorno:
>
> ```text
> MySQL
>   ↓
> Repository
>   ↓
> Service
>   ↓
> Controller
>   ↓ HTTP/REST
> Angular
>   ↓
> Usuário
> ```
>
> ---
>
> ## 3. Funcionalidades
>
> ### 3.1 Cadastro
>
> O sistema permite o cadastro de um interessado contendo:
>
> * Nome completo
> * E-mail
> * Telefone
> * Data de nascimento
> * Nível de experiência
> * Estilo de dança
> * Observações
>
> O backend também registra automaticamente a data de cadastro através do banco de dados.
>
> ### 3.2 Consulta
>
> O sistema permite:
>
> * listar todos os interessados;
> * consultar um interessado pelo ID.
>
> A listagem apresenta os registros mais recentes primeiro, utilizando `dataCadastro` como critério de ordenação decrescente.
>
> ### 3.3 Edição
>
> O sistema permite alterar os dados de um interessado existente.
>
> Durante a atualização, o backend verifica se o novo e-mail já pertence a outro interessado.
>
> ### 3.4 Exclusão
>
> O sistema permite excluir um interessado existente.
>
> Caso o ID informado não exista, o backend retorna erro HTTP `404`.
>
> ---
>
> ## 4. Dados do Interessado
>
> ### 4.1 Campos
>
> | Campo            | Tipo     | Obrigatório |                Limite |
> | ---------------- | -------- | ----------: | --------------------: |
> | nome             | String   |         Sim |        150 caracteres |
> | email            | String   |         Sim |        150 caracteres |
> | telefone         | String   |         Sim |         20 caracteres |
> | dataNascimento   | Date     |         Sim | Deve estar no passado |
> | nivelExperiencia | Enum     |         Sim |                     - |
> | estiloDanca      | Enum     |         Sim |                     - |
> | observacoes      | String   |         Não |        500 caracteres |
> | dataCadastro     | DateTime |  Automático |                     - |
>
> A propriedade `dataCadastro` é gerenciada pelo banco de dados através de `DEFAULT CURRENT_TIMESTAMP`.
>
> ---
>
> ## 5. Níveis de Experiência
>
> Os níveis disponíveis são:
>
> ```text
> NUNCA_DANCEI
> INICIANTE
> INTERMEDIARIO
> AVANCADO
> ```
>
> ---
>
> ## 6. Estilos de Dança
>
> Os estilos disponíveis inicialmente são:
>
> ```text
> SAMBA
> FORRO
> SALSA
> BACHATA
> ZOUK
> DANCA_DE_SALAO
> HIP_HOP
> BALLET
> OUTRO
> ```
>
> ---
>
> ## 7. Regras de Negócio e Validação
>
> O backend possui validações para:
>
> * nome obrigatório;
> * nome com no máximo 150 caracteres;
> * e-mail obrigatório;
> * e-mail com formato válido;
> * e-mail com no máximo 150 caracteres;
> * e-mail único;
> * telefone obrigatório;
> * telefone com no máximo 20 caracteres;
> * data de nascimento obrigatória;
> * data de nascimento no passado;
> * nível de experiência obrigatório;
> * estilo de dança obrigatório;
> * observações opcionais;
> * observações com no máximo 500 caracteres.
>
> As validações de entrada são realizadas através do **Bean Validation** no DTO `InteressadoRequest`.
>
> Além das validações de campo, o backend possui regras de negócio para impedir:
>
> * cadastro com e-mail já existente;
> * atualização utilizando e-mail pertencente a outro interessado;
> * consulta de interessado inexistente;
> * exclusão de interessado inexistente.
>
> As validações deverão existir no frontend posteriormente para melhorar a experiência do usuário.
>
> Entretanto:
>
> > **O backend será a autoridade final sobre as regras de validação.**
>
> ---
>
> ## 8. Tratamento de Erros
>
> O backend possui um tratamento global de exceções através de `@RestControllerAdvice`.
>
> Os principais erros tratados são:
>
> | Situação                   | HTTP |
> | -------------------------- | ---: |
> | Dados inválidos            |  400 |
> | JSON inválido              |  400 |
> | E-mail já cadastrado       |  409 |
> | Interessado não encontrado |  404 |
>
> As respostas de erro possuem estrutura padronizada.
>
> Exemplo:
>
> ```json
> {
>   "status": 404,
>   "codigo": "INTERESSADO_NAO_ENCONTRADO",
>   "mensagem": "Interessado não encontrado."
> }
> ```
>
> Para erros de validação, a resposta também apresenta uma lista com os problemas encontrados:
>
> ```json
> {
>   "status": 400,
>   "codigo": "ERRO_VALIDACAO",
>   "mensagem": "Existem campos inválidos.",
>   "erros": [
>     "O nome é obrigatório."
>   ]
> }
> ```
>
> ---
>
> ## 9. Banco de Dados
>
> ### 9.1 Banco
>
> ```text
> projeto_danca
> ```
>
> ### 9.2 Tabela
>
> ```text
> interessado
> ```
>
> ### 9.3 Estrutura
>
> ```sql
> CREATE TABLE interessado (
>     id BIGINT NOT NULL AUTO_INCREMENT,
>     nome VARCHAR(150) NOT NULL,
>     email VARCHAR(150) NOT NULL UNIQUE,
>     telefone VARCHAR(20) NOT NULL,
>     data_nascimento DATE NOT NULL,
>     nivel_experiencia VARCHAR(30) NOT NULL,
>     estilo_danca VARCHAR(50) NOT NULL,
>     observacoes VARCHAR(500),
>     data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
>
>     CONSTRAINT pk_interessado PRIMARY KEY (id),
>     CONSTRAINT uk_interessado_email UNIQUE (email)
> );
> ```
>
> A criação do schema está versionada em:
>
> ```text
> database/init/001-create-schema.sql
> ```
>
> O Hibernate utiliza:
>
> ```yaml
> ddl-auto: validate
> ```
>
> Dessa forma, a aplicação verifica a compatibilidade entre as entidades e o schema existente, sem permitir que o Hibernate altere automaticamente a estrutura do banco.
>
> ---
>
> ## 10. API REST
>
> A API utiliza como base:
>
> ```text
> http://localhost:8080/api
> ```
>
> Endpoints implementados:
>
> | Método | Endpoint             | Objetivo              | Status de sucesso |
> | ------ | -------------------- | --------------------- | ----------------: |
> | POST   | `/interessados`      | Criar interessado     |               201 |
> | GET    | `/interessados`      | Listar interessados   |               200 |
> | GET    | `/interessados/{id}` | Buscar interessado    |               200 |
> | PUT    | `/interessados/{id}` | Atualizar interessado |               200 |
> | DELETE | `/interessados/{id}` | Excluir interessado   |               204 |
>
> O CRUD do backend foi implementado e validado através de testes automatizados e testes HTTP manuais.
>
> ---
>
> ## 11. Arquitetura do Backend
>
> É utilizada a separação:
>
> ```text
> Controller
>    ↓
> Service
>    ↓
> Repository
>    ↓
> Database
> ```
>
> Responsabilidades:
>
> * **Controller:** recebe requisições HTTP, valida os DTOs de entrada e devolve respostas HTTP.
> * **Service:** concentra as regras de negócio.
> * **Repository:** realiza a comunicação com o banco através do Spring Data JPA.
> * **Entity:** representa os dados persistidos.
> * **DTO:** controla os dados que entram e saem da API.
>
> A API não expõe diretamente a Entity como contrato público.
>
> Foram implementados:
>
> ```text
> Interessado
> InteressadoRequest
> InteressadoResponse
> InteressadoRepository
> InteressadoService
> InteressadoController
> ```
>
> Também foram criados enums para controlar os valores de nível de experiência e estilo de dança.
>
> ---
>
> ## 12. Estrutura do Projeto
>
> A estrutura atual relevante é:
>
> ```text
> projeto-danca/
> ├── backend/
> │   └── src/
> │       ├── main/
> │       │   └── java/
> │       │       └── br/com/projetodanca/
> │       │           ├── controller/
> │       │           ├── dto/
> │       │           ├── entity/
> │       │           ├── enums/
> │       │           ├── exception/
> │       │           ├── repository/
> │       │           └── service/
> │       └── test/
> │           └── java/
> │               └── br/com/projetodanca/
> │                   ├── repository/
> │                   └── service/
> ├── frontend/
> ├── database/
> │   └── init/
> ├── docs/
> ├── docker-compose.yml
> ├── .env.example
> ├── .gitignore
> └── README.md
> ```
>
> O teste do Service está organizado em:
>
> ```text
> backend/src/test/java/br/com/projetodanca/service/InteressadoServiceTest.java
> ```
>
> mantendo o pacote Java correspondente:
>
> ```java
> package br.com.projetodanca.service;
> ```
>
> ---
>
> ## 13. Docker
>
> O MySQL é executado através do Docker.
>
> Configuração atual:
>
> ```text
> Host: localhost
> Porta externa: 3307
> Porta interna do container: 3306
> Database: projeto_danca
> ```
>
> Mapeamento:
>
> ```text
> localhost:3307
>       ↓
> container:3306
> ```
>
> O motivo da porta externa `3307` é evitar conflito com uma instalação local do MySQL que utiliza a porta `3306`.
>
> ---
>
> ## 14. Docker Compose
>
> O projeto possui um serviço MySQL:
>
> ```yaml
> services:
>   mysql:
>     image: mysql:8.4
>     container_name: projeto-danca-mysql
>     restart: unless-stopped
>     environment:
>       MYSQL_DATABASE: projeto_danca
>       MYSQL_USER: projeto
>       MYSQL_PASSWORD: ${DB_PASSWORD}
>       MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
>     ports:
>       - "3307:3306"
>     volumes:
>       - mysql_data:/var/lib/mysql
>       - ./database/init:/docker-entrypoint-initdb.d
>
> volumes:
>   mysql_data:
> ```
>
> ---
>
> ## 15. Variáveis de Ambiente
>
> O projeto utiliza um arquivo `.env` local para informações de ambiente.
>
> Exemplo:
>
> ```env
> DB_USERNAME=projeto
> DB_PASSWORD=projeto123
> DB_ROOT_PASSWORD=root123
> ```
>
> > **Observação:** esses valores são exclusivamente para desenvolvimento local.
>
> O arquivo `.env` não deverá ser versionado.
>
> O projeto disponibiliza:
>
> ```text
> .env.example
> ```
>
> como modelo para outros desenvolvedores.
>
> ---
>
> ## 16. Testes
>
> O backend possui testes automatizados utilizando JUnit e Spring Boot.
>
> Foram implementados testes para o Repository e para o Service.
>
> O `InteressadoServiceTest` cobre:
>
> * cadastro de interessado;
> * impedimento de cadastro com e-mail duplicado;
> * atualização de interessado;
> * impedimento de atualização com e-mail duplicado;
> * exclusão de interessado;
> * impedimento de exclusão de interessado inexistente.
>
> Também foram realizados testes HTTP manuais para:
>
> * POST;
> * GET;
> * GET por ID;
> * PUT;
> * DELETE;
> * validações;
> * e-mail duplicado;
> * recursos inexistentes;
> * preenchimento de `dataCadastro`;
> * ordenação dos registros por data de cadastro.
>
> A validação final do backend foi realizada através de:
>
> ```powershell
> .\mvnw.cmd clean test
> ```
>
> Resultado:
>
> ```text
> BUILD SUCCESS
> ```
>
> ---
>
> ## 17. Versionamento
>
> O projeto utiliza Git.
>
> A estratégia adotada é:
>
> ```text
> main
>  ↓
> develop
>  ↓
> feature/*
> fix/*
> hotfix/*
> ```
>
> Responsabilidades:
>
> * `main`: versão estável/produção;
> * `develop`: integração e validação das alterações;
> * `feature/*`: desenvolvimento de funcionalidades;
> * `fix/*`: correções;
> * `hotfix/*`: correções urgentes da versão estável.
>
> Não realizamos desenvolvimento diretamente em `main` ou `develop`.
>
> O fluxo utilizado no desenvolvimento do CRUD foi:
>
> ```text
> feature/crud-interessados
>          ↓
>       testes
>          ↓
>       develop
>          ↓
>       validação
>          ↓
>        main
> ```
>
> O GitHub ainda não está configurado como repositório remoto neste ambiente. Portanto, a etapa de Pull Request e Code Review ainda não foi executada.
>
> Padrão de commits:
>
> ```text
> feat:
> fix:
> docs:
> test:
> chore:
> ```
>
> Commits importantes realizados nesta etapa:
>
> ```text
> 735434d feat: adiciona CRUD de interessados e validacoes
> c730601 feat: ajusta cadastro e ordenacao de interessados
> f7384f5 Merge branch 'feature/crud-interessados' into develop
> eeeedae fix: corrige pacote do teste de service
> ace78ea Merge branch 'develop'
> ```
>
> ---
>
> ## 18. Estado Atual do Projeto
>
> ### Concluído
>
> * [x] Estrutura inicial do projeto
> * [x] `.gitignore`
> * [x] README inicial
> * [x] Docker Compose
> * [x] Container MySQL
> * [x] Volume persistente
> * [x] Banco de dados
> * [x] Tabela `interessado`
> * [x] Conexão Spring Boot + MySQL
> * [x] Entity `Interessado`
> * [x] Enums
> * [x] Repository
> * [x] Request DTO
> * [x] Response DTO
> * [x] Service
> * [x] Controller
> * [x] POST
> * [x] GET
> * [x] GET por ID
> * [x] PUT
> * [x] DELETE
> * [x] Bean Validation
> * [x] Tratamento global de exceções
> * [x] Regra de e-mail único
> * [x] Registro automático de `dataCadastro`
> * [x] Ordenação por data de cadastro
> * [x] Testes automatizados
> * [x] Testes HTTP do CRUD
> * [x] Integração `feature/crud-interessados → develop`
> * [x] Integração `develop → main`
>
> ### Ainda pendente
>
> * [ ] Swagger/OpenAPI
> * [ ] CORS
> * [ ] Projeto Angular
> * [ ] Service HTTP do frontend
> * [ ] Tela de cadastro
> * [ ] Listagem
> * [ ] Edição
> * [ ] Exclusão pelo frontend
> * [ ] Integração completa Angular/Backend
> * [ ] Dockerfile do Backend
> * [ ] Container Backend
> * [ ] Dockerfile do Frontend
> * [ ] Container Frontend
> * [ ] Docker Compose completo
> * [ ] Configuração do GitHub remoto
> * [ ] Pull Requests
> * [ ] Code Review
> * [ ] Release/Tag
>
> ---
>
> ## 19. Backlog
>
> ### EPIC 01 - Fundação
>
> * TASK-001 - Repositório GitHub
> * TASK-002 - Estrutura do projeto
> * TASK-003 - `.gitignore`
> * TASK-004 - README
>
> ### EPIC 02 - Banco de Dados
>
> * TASK-005 - Docker Compose
> * TASK-006 - Container MySQL
> * TASK-007 - Volume persistente
> * TASK-008 - Banco de dados
> * TASK-009 - Tabela interessado
> * TASK-010 - Conexão da aplicação com o banco
>
> ### EPIC 03 - Backend
>
> * TASK-011 - Criar projeto Spring Boot
> * TASK-012 - Configurar Maven
> * TASK-013 - Configurar dependências
> * TASK-014 - Configurar conexão com banco
> * TASK-015 - Criar Entity
> * TASK-016 - Criar Enums
> * TASK-017 - Criar Repository
> * TASK-018 - Criar Request DTO
> * TASK-019 - Criar Response DTO
> * TASK-020 - Criar Service
> * TASK-021 - Criar Controller
> * TASK-022 - Implementar POST
> * TASK-023 - Implementar GET
> * TASK-024 - Implementar GET por ID
> * TASK-025 - Implementar PUT
> * TASK-026 - Implementar DELETE
> * TASK-027 - Tratamento de exceções
> * TASK-028 - Swagger/OpenAPI
> * TASK-029 - Testes
>
> **Situação atual do EPIC 03:** CRUD, validações, tratamento de exceções e testes concluídos. Swagger/OpenAPI permanece pendente.
>
> ### EPIC 04 - Frontend
>
> * TASK-030 - Criar projeto Angular
> * TASK-031 - Estrutura frontend
> * TASK-032 - Modelos TypeScript
> * TASK-033 - Service HTTP
> * TASK-034 - Tela de cadastro
> * TASK-035 - Reactive Forms
> * TASK-036 - Validações
> * TASK-037 - POST
> * TASK-038 - Mensagens
> * TASK-039 - Listagem
> * TASK-040 - Edição
> * TASK-041 - Exclusão
> * TASK-042 - Loading e estado vazio
> * TASK-043 - Testes
>
> ### EPIC 05 - Integração
>
> * TASK-044 - CORS
> * TASK-045 - Integração Angular/Backend
> * TASK-046 - Teste de criação
> * TASK-047 - Teste de listagem
> * TASK-048 - Teste de edição
> * TASK-049 - Teste de exclusão
> * TASK-050 - Tratamento de erros
>
> ### EPIC 06 - Docker completo
>
> * TASK-051 - Dockerfile Backend
> * TASK-052 - Container Backend
> * TASK-053 - Comunicação Backend/MySQL
> * TASK-054 - Backend no Compose
> * TASK-055 - Dockerfile Frontend
> * TASK-056 - Container Frontend
> * TASK-057 - Frontend no Compose
> * TASK-058 - Teste completo
>
> ### EPIC 07 - GitHub
>
> * TASK-059 - Branch develop
> * TASK-060 - Feature branches
> * TASK-061 - Pull Requests
> * TASK-062 - Code Review
> * TASK-063 - Correções
> * TASK-064 - Merge develop -> main
> * TASK-065 - Tag/Release
>
> As tarefas de Git local, branches, integração `develop → main` já foram praticadas. Pull Requests, Code Review e GitHub remoto continuam pendentes.
