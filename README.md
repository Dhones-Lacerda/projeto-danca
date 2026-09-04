\# Projeto Dança — Cadastro de Interessados



> \*\*Documento oficial do projeto\*\*

>

> Este documento é a fonte oficial de documentação do projeto e será incrementado ao longo do desenvolvimento.

>

> \*\*Status:\*\* Em desenvolvimento

> \*\*Versão:\*\* 0.1.0

> \*\*Tipo:\*\* Projeto Full Stack

> \*\*Objetivo:\*\* Criar uma aplicação para cadastro e gerenciamento de pessoas interessadas em dança.



\---



\## 1. Visão Geral



O projeto consiste no desenvolvimento de uma aplicação web para cadastro de pessoas interessadas em aulas de dança.



A aplicação permitirá:



> \* cadastrar interessados;

> \* consultar interessados cadastrados;

> \* consultar um interessado específico;

> \* editar dados;

> \* excluir cadastros;

> \* validar os dados informados;

> \* armazenar as informações em banco de dados MySQL.



A aplicação será desenvolvida utilizando uma arquitetura Full Stack composta por:



> \*\*Frontend:\*\* Angular

> \*\*Backend:\*\* Java + Spring Boot

> \*\*Banco de dados:\*\* MySQL

> \*\*Persistência:\*\* Spring Data JPA

> \*\*Build:\*\* Maven

> \*\*Containerização:\*\* Docker / Docker Compose

> \*\*Versionamento:\*\* Git / GitHub

> \*\*API:\*\* REST

> \*\*Documentação da API:\*\* OpenAPI / Swagger



\---



\# 2. Objetivo do MVP



> Permitir que uma pessoa seja cadastrada como interessada em dança e que seus dados possam posteriormente ser consultados, alterados ou removidos.



O fluxo principal será:



```text

Usuário

&#x20;  ↓

Angular

&#x20;  ↓ HTTP/REST

Spring Boot

&#x20;  ↓

Service

&#x20;  ↓

Repository

&#x20;  ↓

MySQL

```



No retorno:



```text

MySQL

&#x20;  ↓

Repository

&#x20;  ↓

Service

&#x20;  ↓

Controller

&#x20;  ↓ HTTP/REST

Angular

&#x20;  ↓

Usuário

```



\---



\# 3. Funcionalidades



\## 3.1 Cadastro



O sistema deverá permitir o cadastro de um interessado contendo:



> \* Nome completo

> \* E-mail

> \* Telefone

> \* Data de nascimento

> \* Nível de experiência

> \* Estilo de dança

> \* Observações



\---



\## 3.2 Consulta



O sistema deverá permitir:



> \* listar todos os interessados;

> \* consultar um interessado pelo ID.



A listagem deverá apresentar os registros mais recentes primeiro.



\---



\## 3.3 Edição



O sistema deverá permitir alterar os dados de um interessado existente.



\---



\## 3.4 Exclusão



O sistema deverá permitir excluir um interessado.



\---



\# 4. Dados do Interessado



\## 4.1 Campos



| Campo            | Tipo     | Obrigatório |         Limite |

| ---------------- | -------- | ----------: | -------------: |

| nome             | String   |         Sim | 150 caracteres |

| email            | String   |         Sim | 150 caracteres |

| telefone         | String   |         Sim |  20 caracteres |

| dataNascimento   | Date     |         Sim |              — |

| nivelExperiencia | Enum     |         Sim |              — |

| estiloDanca      | Enum     |         Sim |              — |

| observacoes      | String   |         Não | 500 caracteres |

| dataCadastro     | DateTime |  Automático |              — |



\---



\# 5. Níveis de Experiência



Os níveis disponíveis serão:



```text

NUNCA\_DANCEI

INICIANTE

INTERMEDIARIO

AVANCADO

```



\---



\# 6. Estilos de Dança



Os estilos disponíveis inicialmente serão:



```text

SAMBA

FORRO

SALSA

BACHATA

ZOUK

DANCA\_DE\_SALAO

HIP\_HOP

BALLET

OUTRO

```



\---



\# 7. Regras de Negócio



> \* Nome é obrigatório.

> \* E-mail é obrigatório.

> \* E-mail deve possuir formato válido.

> \* E-mail deve ser único.

> \* Telefone é obrigatório.

> \* Data de nascimento é obrigatória.

> \* Nível de experiência é obrigatório.

> \* Estilo de dança é obrigatório.

> \* Observações são opcionais.

> \* Observações possuem limite de 500 caracteres.



As validações deverão existir no frontend para melhorar a experiência do usuário.



Entretanto:



> \*\*O backend será a autoridade final sobre as regras de validação.\*\*



Isso evita que regras importantes dependam exclusivamente do frontend.



\---



\# 8. Banco de Dados



\## 8.1 Banco



```text

projeto\_danca

```



\## 8.2 Tabela



```text

interessado

```



\## 8.3 Estrutura



```sql

CREATE TABLE interessado (

&#x20;   id BIGINT NOT NULL AUTO\_INCREMENT,

&#x20;   nome VARCHAR(150) NOT NULL,

&#x20;   email VARCHAR(150) NOT NULL,

&#x20;   telefone VARCHAR(20) NOT NULL,

&#x20;   data\_nascimento DATE NOT NULL,

&#x20;   nivel\_experiencia VARCHAR(30) NOT NULL,

&#x20;   estilo\_danca VARCHAR(50) NOT NULL,

&#x20;   observacoes VARCHAR(500),

&#x20;   data\_cadastro DATETIME NOT NULL DEFAULT CURRENT\_TIMESTAMP,



&#x20;   CONSTRAINT pk\_interessado PRIMARY KEY (id),

&#x20;   CONSTRAINT uk\_interessado\_email UNIQUE (email)

);

```



\---



\# 9. API REST



A API utilizará como base:



```text

http://localhost:8080/api

```



Endpoints planejados:



| Método | Endpoint             | Objetivo              |

| ------ | -------------------- | --------------------- |

| POST   | `/interessados`      | Criar interessado     |

| GET    | `/interessados`      | Listar interessados   |

| GET    | `/interessados/{id}` | Buscar interessado    |

| PUT    | `/interessados/{id}` | Atualizar interessado |

| DELETE | `/interessados/{id}` | Excluir interessado   |



\---



\# 10. Arquitetura do Backend



Será utilizada a separação:



```text

Controller

&#x20;   ↓

Service

&#x20;   ↓

Repository

&#x20;   ↓

Database

```



Responsabilidades:



> \*\*Controller:\*\* recebe requisições HTTP e devolve respostas.

>

> \*\*Service:\*\* concentra regras de negócio.

>

> \*\*Repository:\*\* realiza a comunicação com o banco através do Spring Data JPA.

>

> \*\*Entity:\*\* representa os dados persistidos.

>

> \*\*DTO:\*\* controla os dados que entram e saem da API.



A API não deverá expor diretamente a Entity como contrato público.



\---



\# 11. Estrutura do Projeto



A estrutura planejada é:



```text

projeto-danca/

│

├── backend/

│

├── frontend/

│

├── database/

│   └── init/

│

├── docs/

│

├── docker-compose.yml

│

├── .env.example

│

├── .gitignore

│

└── README.md

```



\---



\# 12. Docker



O MySQL será executado inicialmente através do Docker.



Configuração atual:



```text

Host: localhost

Porta externa: 3307

Porta interna do container: 3306

Database: projeto\_danca

```



Mapeamento:



```text

localhost:3307

&#x20;      ↓

container:3306

```



O motivo da porta externa `3307` é evitar conflito com uma instalação local do MySQL que utiliza a porta `3306`.



\---



\# 13. Docker Compose



O projeto possui um serviço MySQL:



```yaml

services:

&#x20; mysql:

&#x20;   image: mysql:8.4

&#x20;   container\_name: projeto-danca-mysql

&#x20;   restart: unless-stopped

&#x20;   environment:

&#x20;     MYSQL\_DATABASE: projeto\_danca

&#x20;     MYSQL\_USER: projeto

&#x20;     MYSQL\_PASSWORD: ${DB\_PASSWORD}

&#x20;     MYSQL\_ROOT\_PASSWORD: ${DB\_ROOT\_PASSWORD}

&#x20;   ports:

&#x20;     - "3307:3306"

&#x20;   volumes:

&#x20;     - mysql\_data:/var/lib/mysql

&#x20;     - ./database/init:/docker-entrypoint-initdb.d



volumes:

&#x20; mysql\_data:

```



\---



\# 14. Variáveis de Ambiente



O projeto utiliza um arquivo `.env` local para informações de ambiente.



Exemplo:



```env

DB\_USERNAME=projeto

DB\_PASSWORD=projeto123

DB\_ROOT\_PASSWORD=root123

```



> \*\*Observação:\*\* esses valores são exclusivamente para desenvolvimento local.



O arquivo `.env` não deverá ser versionado.



O projeto disponibiliza:



```text

.env.example

```



como modelo para outros desenvolvedores.



\---



\# 15. Versionamento



O projeto utiliza Git.



Estratégia planejada:



```text

main

&#x20; ↓

develop

&#x20; ↓

feature/\*

fix/\*

```



Padrão de commits:



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



\---



\# 16. Backlog



\## EPIC 01 — Fundação



> TASK-001 — Repositório GitHub

> TASK-002 — Estrutura do projeto

> TASK-003 — `.gitignore`

> TASK-004 — README



\## EPIC 02 — Banco de Dados



> TASK-005 — Docker Compose

> TASK-006 — Container MySQL

> TASK-007 — Volume persistente

> TASK-008 — Banco de dados

> TASK-009 — Tabela interessado

> TASK-010 — Conexão da aplicação com o banco



\## EPIC 03 — Backend



> TASK-011 — Criar projeto Spring Boot

> TASK-012 — Configurar Maven

> TASK-013 — Configurar dependências

> TASK-014 — Configurar conexão com banco

> TASK-015 — Criar Entity

> TASK-016 — Criar Enums

> TASK-017 — Criar Repository

> TASK-018 — Criar Request DTO

> TASK-019 — Criar Response DTO

> TASK-020 — Criar Service

> TASK-021 — Criar Controller

> TASK-022 — Implementar POST

> TASK-023 — Implementar GET

> TASK-024 — Implementar GET por ID

> TASK-025 — Implementar PUT

> TASK-026 — Implementar DELETE

> TASK-027 — Tratamento de exceções

> TASK-028 — Swagger/OpenAPI

> TASK-029 — Testes



\## EPIC 04 — Frontend



> TASK-030 — Criar projeto Angular

> TASK-031 — Estrutura frontend

> TASK-032 — Modelos TypeScript

> TASK-033 — Service HTTP

> TASK-034 — Tela de cadastro

> TASK-035 — Reactive Forms

> TASK-036 — Validações

> TASK-037 — POST

> TASK-038 — Mensagens

> TASK-039 — Listagem

> TASK-040 — Edição

> TASK-041 — Exclusão

> TASK-042 — Loading e estado vazio

> TASK-043 — Testes



\## EPIC 05 — Integração



> TASK-044 — CORS

> TASK-045 — Integração Angular/Backend

> TASK-046 — Teste de criação

> TASK-047 — Teste de listagem

> TASK-048 — Teste de edição

> TASK-049 — Teste de exclusão

> TASK-050 — Tratamento de erros



\## EPIC 06 — Docker completo



> TASK-051 — Dockerfile Backend

> TASK-052 — Container Backend

> TASK-053 — Comunicação Backend/MySQL

> TASK-054 — Backend no Compose

> TASK-055 — Dockerfile Frontend

> TASK-056 — Container Frontend

> TASK-057 — Frontend no Compose

> TASK-058 — Teste completo



\## EPIC 07 — GitHub



> TASK-059 — Branch develop

> TASK-060 — Feature branches

> TASK-061 — Pull Requests

> TASK-062 — Code Review

> TASK-063 — Correções

> TASK-064 — Merge develop → main

> TASK-065 — Tag/Release



\---



\# 17. Progresso Atual



\### EPIC 01



> TASK-001 — 🟡 Parcial

> TASK-002 — ✅ Concluída

> TASK-003 — ✅ Concluída

> TASK-004 — ⏳ Pendente



\### EPIC 02



> TASK-005 — ✅ Concluída

> TASK-006 — ✅ Concluída

> TASK-007 — ✅ Concluída

> TASK-008 — ✅ Concluída

> TASK-009 — 🟡 Aguardando confirmação final

> TASK-010 — ⏳ Pendente



\### Próxima etapa



> \*\*TASK-004 — README\*\*

>

> Depois:

>

> \*\*TASK-010 em diante — Backend Spring Boot\*\*



\---



\# 18. Definition of Done



Uma funcionalidade será considerada concluída quando:



> \* estiver implementada;

> \* estiver validada;

> \* possuir testes quando aplicável;

> \* estiver integrada às demais camadas;

> \* estiver documentada;

> \* estiver versionada no Git;

> \* passar por revisão quando aplicável;

> \* não quebrar funcionalidades existentes.



\---



\# 19. Escopo Fora do MVP



Não fazem parte da primeira versão:



> \* autenticação;

> \* recuperação de senha;

> \* pagamentos;

> \* integração com WhatsApp;

> \* envio de e-mail;

> \* agendamento de aulas;

> \* cadastro de professores;

> \* cadastro de turmas;

> \* upload de arquivos;

> \* sistema avançado de permissões;

> \* dashboard avançado.



Esses recursos poderão ser avaliados em versões futuras.



\---



\# 20. Histórico de Evolução



| Versão | Data       | Alteração                                            |

| ------ | ---------- | ---------------------------------------------------- |

| 0.1.0  | 04/09/2026 | Fundação do projeto, Docker e banco de dados inicial |



> Este documento deverá ser atualizado ao final de cada seção importante do desenvolvimento.


> ## Progresso do projeto
>
> ### Concluído
>
> #### Fundação e infraestrutura
>
> * [x] Estrutura inicial do projeto
> * [x] Git inicializado
> * [x] `.gitignore`
> * [x] Documentação inicial
> * [x] Docker Compose configurado
> * [x] Container MySQL configurado
> * [x] Volume persistente do MySQL
> * [x] Banco de dados `projeto_danca`
> * [x] Tabela `interessado`
> * [x] Projeto Spring Boot criado
> * [x] Maven Wrapper configurado
> * [x] Dependências iniciais adicionadas
> * [x] Configuração do `application.yaml`
> * [x] Conexão do Spring Boot com MySQL configurada
> * [x] Teste de carregamento do contexto Spring Boot executado com sucesso
>
> ### Em andamento
>
> #### Backend
>
> * [ ] Criar entidade `Interessado`
> * [ ] Criar enums
> * [ ] Criar repository
> * [ ] Criar DTOs
> * [ ] Criar service
> * [ ] Criar controller
> * [ ] Implementar endpoints CRUD
> * [ ] Implementar tratamento de exceções
> * [ ] Configurar Swagger/OpenAPI
> * [ ] Criar testes automatizados
>
> ### Próxima tarefa
>
> **TASK-015 — Criar a entidade `Interessado`.**
>
> A entidade será responsável por representar, no domínio Java/JPA, os dados persistidos na tabela `interessado`.
>
> ---
>
> ## Histórico de desenvolvimento
>
> ### Seção — Configuração inicial do Backend
>
> Nesta etapa foi criado o projeto Spring Boot utilizando Maven e Java 25.
>
> Foram adicionadas as dependências necessárias para a primeira fase do backend:
>
> * Spring Web
> * Spring Data JPA
> * Bean Validation
> * MySQL Driver
>
> Também foi configurado o arquivo `application.yaml`, estabelecendo a conexão com o banco MySQL executado em Docker.
>
> A aplicação utiliza:
>
> * `localhost:3307` para acessar o MySQL publicado pelo Docker;
> * banco `projeto_danca`;
> * variáveis de ambiente para usuário e senha;
> * `ddl-auto: validate`, evitando alterações automáticas no schema pelo Hibernate;
> * `open-in-view: false`.
>
> A aplicação foi validada por meio do Maven Wrapper:
>
> ```powershell
> .\mvnw.cmd test
> ```
>
> Resultado:
>
> ```text
> BUILD SUCCESS
> ```
>
> Com isso, a infraestrutura inicial do backend está validada e pronta para o desenvolvimento da camada de domínio.

