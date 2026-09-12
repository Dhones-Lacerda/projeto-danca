# Projeto Dança — Diário Técnico e Aprendizado



**Documento complementar de aprendizado**



Este documento explica as decisões técnicas tomadas durante o desenvolvimento do projeto.



O objetivo não é apenas registrar **o que foi feito**, mas principalmente explicar:



* o que foi feito;

* por que foi feito;

* qual problema foi resolvido;

* quais alternativas existiam;

* quais conceitos técnicos estão envolvidos.



---



# 1. Como estamos desenvolvendo



O projeto está sendo desenvolvido de forma incremental.



Isso significa que não vamos tentar construir frontend, backend, banco e Docker simultaneamente.



A estratégia é:




Planejamento

&#x20;   ↓

Fundação

&#x20;   ↓

Banco

&#x20;   ↓

Backend

&#x20;   ↓

Frontend

&#x20;   ↓

Integração

&#x20;   ↓

Docker completo

&#x20;   ↓

Testes

&#x20;   ↓

GitHub / Release




A vantagem dessa abordagem é reduzir a quantidade de problemas acontecendo simultaneamente.



Se algo quebrar, conseguimos identificar em qual etapa ocorreu.



---



# 2. Por que começamos pelo banco e Docker?



O sistema precisa persistir dados.



Antes de construir uma tela Angular para cadastrar uma pessoa, precisamos saber:



"Onde esses dados serão armazenados?"



Por isso definimos primeiro o banco:




projeto\_danca




e a tabela:




interessado




Depois colocamos o MySQL dentro de um container Docker.



Isso cria um ambiente reproduzível.



Em vez de depender de:




"Meu computador tem MySQL instalado e configurado"




queremos chegar a:




"Qualquer desenvolvedor executa docker compose up e possui o MySQL necessário."




Esse é um conceito importante de desenvolvimento profissional:



**Ambientes reproduzíveis reduzem problemas de configuração.**



---



# 3. Por que usamos Docker?



Docker permite executar aplicações isoladas em containers.



Neste projeto, o primeiro container é:




projeto-danca-mysql




Ele executa:




MySQL 8.4




O Docker Compose permite definir essa infraestrutura em código.



Isso é importante porque a configuração deixa de existir apenas na máquina do desenvolvedor.



Ela passa a estar registrada no projeto:




docker-compose.yml




---



# 4. Por que a porta virou 3307?



Inicialmente planejamos:




3306:3306




Mas o Windows já possuía um MySQL utilizando:




localhost:3306




Quando tentamos iniciar o container, ocorreu conflito de porta.



A solução foi:




3307:3306




Isso significa:




Windows

localhost:3307

&#x20;      ↓

Docker

container:3306

&#x20;      ↓

MySQL




A porta interna do MySQL continua sendo 3306.



Apenas a porta exposta no computador foi alterada para 3307.



---



# 5. O que significa o volume do Docker?



Configuramos:




volumes:

&#x20; - mysql\_data:/var/lib/mysql




O MySQL armazena seus dados em:




/var/lib/mysql




O volume faz com que esses dados não desapareçam simplesmente porque o container foi recriado.



Conceito:




Container

&#x20;   ↓

MySQL

&#x20;   ↓

/var/lib/mysql

&#x20;   ↓

mysql\_data




Container e dados são conceitos diferentes.



O container pode ser removido.



O volume pode continuar existindo.



---



# 6. Por que criamos um arquivo SQL?



Criamos:




database/init/001-create-schema.sql




Esse arquivo contém a criação da tabela.



A vantagem é que a estrutura do banco também fica versionada no projeto.



Sem o arquivo:



"Eu criei a tabela manualmente no meu computador."



Com o arquivo:



"A estrutura necessária do banco está documentada e reproduzível."



Isso é muito mais adequado para um projeto profissional.



---



# 7. O que significa /docker-entrypoint-initdb.d?



No Compose configuramos:




\- ./database/init:/docker-entrypoint-initdb.d




Estamos mapeando:




projeto

database/init

&#x20;      ↓

container

/docker-entrypoint-initdb.d




O MySQL possui um mecanismo que executa scripts colocados nesse diretório durante a inicialização inicial do banco.



Isso permite que:




001-create-schema.sql




seja executado automaticamente.



---



# 8. Por que usamos docker compose down -v?



Durante a configuração do banco, precisamos recriar o ambiente inicial.



O comando:




docker compose down -v




remove:



* containers;

* redes criadas pelo Compose;

* volumes associados.



Isso foi necessário porque os scripts de:




/docker-entrypoint-initdb.d




são executados na **inicialização de um banco novo**.



Se o volume já contém um banco inicializado, o MySQL não executa novamente esses scripts automaticamente.



Em um projeto com dados reais, nunca devemos executar down -v sem entender as consequências, porque o volume contém os dados persistidos.



Neste momento isso é seguro porque ainda estamos montando o ambiente de desenvolvimento.



---



# 9. Por que telefone é String?



O telefone foi definido como:




VARCHAR(20)




e não:




INT




Isso ocorre porque telefone não é uma quantidade matemática.



Exemplos:




+55 11 99999-9999

(77) 99999-9999

77999999999




Podem existir:



* código do país;

* DDD;

* espaços;

* parênteses;

* hífen;

* zeros.



Portanto:



**Telefone é dado textual, não numérico.**



---



# 10. Por que o e-mail possui UNIQUE?



A tabela possui:




CONSTRAINT uk\_interessado\_email UNIQUE (email)




Isso significa que o banco não permite dois registros com o mesmo e-mail.



Exemplo:




Maria@email.com

Maria@email.com




O segundo cadastro deverá ser rejeitado.



Essa regra é importante porque o e-mail foi escolhido como identificador único do interessado.



---



# 11. Por que usamos camadas no Backend?



A arquitetura planejada é:




Controller

&#x20;   ↓

Service

&#x20;   ↓

Repository

&#x20;   ↓

Database




Cada camada possui uma responsabilidade.



### Controller



Recebe:




HTTP Request




e devolve:




HTTP Response




### Service



Executa:




Regras de negócio




### Repository



Cuida da persistência:




Java ↔ Banco




### Database



Armazena os dados.



---



# 12. Por que não colocar tudo no Controller?



Seria possível criar um Controller que:



recebe requisição → valida → acessa banco → transforma dados → devolve resposta.



Mas isso criaria uma classe com responsabilidades demais.



Com o tempo ela ficaria difícil de:



* testar;

* manter;

* alterar;

* reutilizar.



Separar responsabilidades torna o código mais organizado.



Esse conceito está relacionado ao princípio:



**Separation of Concerns — Separação de Responsabilidades.**



---



# 13. Por que teremos DTOs?



Planejamos:




InteressadoRequest

InteressadoResponse




em vez de simplesmente expor a Entity.



A Entity representa a persistência.



O DTO representa o contrato da API.



Conceitualmente:




HTTP

&#x20;↓

Request DTO

&#x20;↓

Service

&#x20;↓

Entity

&#x20;↓

Repository

&#x20;↓

Database




Na resposta:




Database

&#x20;↓

Entity

&#x20;↓

Service

&#x20;↓

Response DTO

&#x20;↓

HTTP




Isso reduz o acoplamento entre:




Banco de dados




e:




API pública




---



# 14. Por que usamos Enums?



Nível de experiência:




NUNCA\_DANCEI

INICIANTE

INTERMEDIARIO

AVANCADO




Estilo:




SAMBA

FORRO

SALSA

...




Em vez de deixar qualquer texto entrar.



Sem Enum:




iniciante

INICIANTE

Iniciante

iniciant




poderiam representar a mesma coisa.



Com Enum:




INICIANTE




torna-se um valor controlado pela aplicação.



---



# 15. Por que a validação existe no Frontend e Backend?



É importante entender esta diferença.



### Frontend



Serve principalmente para:



melhorar a experiência do usuário.



Exemplo:




"E-mail inválido"




pode aparecer imediatamente no formulário.



### Backend



É a autoridade da aplicação.



Mesmo que alguém ignore o Angular e faça uma requisição diretamente para a API:




POST /api/interessados




o backend ainda deverá validar os dados.



Portanto:




Frontend = UX

Backend = regra confiável

Banco = integridade estrutural




---



# 16. Por que estamos fazendo uma etapa por vez?



O objetivo não é apenas terminar o projeto.



O objetivo também é aprender como um sistema Full Stack é construído.



Por isso, cada etapa deverá seguir:




Implementar

&#x20;   ↓

Executar

&#x20;   ↓

Testar

&#x20;   ↓

Entender

&#x20;   ↓

Documentar

&#x20;   ↓

Commit

&#x20;   ↓

Próxima etapa




Isso também facilita identificar exatamente qual alteração causou um problema.



---



# 17. Git e commits



Estamos utilizando commits pequenos e relacionados a uma mudança.



Exemplo:




feat: configura banco de dados com MySQL e Docker




O prefixo:




feat




indica uma mudança relacionada a funcionalidade/recurso.



Outros exemplos:




fix: corrige conexão com banco

docs: atualiza documentação

test: adiciona testes do interessado

chore: atualiza configuração do projeto




A ideia é que, olhando o histórico:




git log




seja possível entender a evolução do projeto.



---



# 18. Regra de documentação adotada no projeto



A partir deste ponto, ao terminar uma seção significativa:



**1. Atualizaremos o documento oficial.**



Depois:



**2. Atualizaremos este documento de aprendizado.**



E então:



**3. Faremos o commit correspondente.**



O fluxo passa a ser:




Implementação

&#x20;     ↓

Teste

&#x20;     ↓

Documentação oficial

&#x20;     ↓

Explicação técnica

&#x20;     ↓

Git commit

&#x20;     ↓

Próxima seção




---



# 19. Estado atual do aprendizado



Até agora foram trabalhados os seguintes conceitos:



* estrutura de projeto;

* Git;

* .gitignore;

* variáveis de ambiente;

* Docker;

* Docker Compose;

* containers;

* volumes;

* portas;

* MySQL;

* criação de schema;

* scripts de inicialização;

* modelagem inicial de banco;

* chave primária;

* chave única;

* tipos de dados;

* desenvolvimento incremental;

* Conventional Commits.



---



# 20. Próxima etapa



A próxima etapa técnica será:




TASK-004

README




Depois iniciaremos o backend:




TASK-010

Conexão da aplicação com o banco



TASK-011

Criar projeto Spring Boot



TASK-012

Configurar Maven



TASK-013

Configurar dependências




A partir do backend, começaremos a trabalhar diretamente com Java e Spring Boot e explicaremos cada conceito antes de utilizá-lo.

Diário Técnico — Configuração inicial do Backend

Data

04/09/2026

Objetivo da seção

Configurar a aplicação Spring Boot para iniciar corretamente e estabelecer sua primeira comunicação com o banco de dados MySQL executado através do Docker.

1. Criação do projeto Spring Boot

O backend foi criado utilizando:

Java 25

Spring Boot 4.1.1

Maven

Spring Web

Spring Data JPA

Bean Validation

MySQL Driver

O Maven Wrapper foi mantido no projeto para permitir que os comandos Maven sejam executados sem depender da instalação global do Maven na máquina.

O comando utilizado para validar o ambiente foi:

.\mvnw.cmd test

2. Primeiro problema encontrado

Ao executar os testes inicialmente, o Spring Boot não conseguiu carregar o ApplicationContext.

A causa encontrada no relatório do Surefire foi:

Failed to determine a suitable driver class

Isso indicava que o Spring Boot possuía as dependências necessárias para trabalhar com banco de dados, mas ainda não havia uma configuração de datasource suficiente para determinar como acessar o MySQL.

Aprendizado

Adicionar o MySQL Driver ao Maven não configura automaticamente a conexão com o banco.

São responsabilidades diferentes:

MySQL Driver
    ↓
Permite comunicação com MySQL

application.yaml
    ↓
Define como e onde realizar a conexão

3. Configuração do application.yaml

Foi configurado:

spring:
  application:
    name: projeto-danca

  datasource:
    url: jdbc:mysql://localhost:3307/projeto_danca
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false

4. Variáveis de ambiente

As credenciais não foram colocadas diretamente no código Java.

Foi utilizada a seguinte abordagem:

username: ${DB_USERNAME}
password: ${DB_PASSWORD}

Durante a execução local pelo PowerShell, as variáveis foram disponibilizadas na sessão:

$env:DB_USERNAME="projeto"
$env:DB_PASSWORD="projeto123"

5. Segundo problema encontrado

Após configurar o application.yaml, surgiu um erro de sintaxe YAML:

mapping values are not allowed here

O problema estava relacionado a uma duplicação acidental na configuração:

open-in-view: falseen-in-view: false

Após a correção, o YAML passou a ser interpretado corretamente.

Aprendizado

YAML é sensível à sintaxe e à estrutura. Um pequeno erro de edição pode impedir o Spring Boot de iniciar antes mesmo da execução da lógica Java.

6. Validação final

Após a correção, o teste foi executado novamente:

.\mvnw.cmd test

Resultado:

BUILD SUCCESS

Isso confirma que:

o Spring Boot conseguiu carregar o contexto;

a configuração do datasource foi reconhecida;

o JPA/Hibernate conseguiu inicializar;

o teste contextLoads passou.

7. Decisão sobre ddl-auto

Foi utilizado:

ddl-auto: validate

O Hibernate deverá verificar a compatibilidade entre as entidades Java e o schema MySQL, mas não deverá criar ou alterar automaticamente as tabelas.

8. Estado ao final da seção

A infraestrutura inicial do backend está validada.

Fluxo atual:

Spring Boot
    ↓
DataSource / HikariCP
    ↓
MySQL Driver
    ↓
localhost:3307
    ↓
Docker
    ↓
MySQL
    ↓
projeto_danca

Próxima etapa

TASK-015 — Criar a entidade Interessado.





Diário Técnico — Evolução do Backend, API REST e OpenAPI

Objetivo da seção: registrar o desenvolvimento realizado após a configuração inicial do Spring Boot, incluindo entidade, persistência, regras de negócio, DTOs, validações, CRUD, testes e documentação Swagger/OpenAPI.

21. Criação da entidade Interessado

A aplicação passou a representar o registro de interessado como uma entidade JPA.

A entidade foi criada em:

br.com.projetodanca.entity.Interessado

e mapeada para:

interessado

Campos:

id
nome
email
telefone
dataNascimento
nivelExperiencia
estiloDanca
observacoes
dataCadastro

O identificador utiliza:

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

Isso permite que o banco gere o id.

Os campos de experiência e estilo utilizam:

@Enumerated(EnumType.STRING)

Assim, o banco armazena valores como:

INICIANTE
SALSA

em vez da posição numérica do enum.

22. Problema com o identificador JPA

Durante a implementação da entidade, o Hibernate informou que não havia um identificador definido.

A causa foi a ausência de:

@Id

Aprendizado

Toda entidade JPA precisa possuir uma identidade que permita ao Hibernate diferenciar seus registros.

@Entity
   ↓
entidade persistente
   ↓
@Id
   ↓
identificação única

23. Validação do schema com Hibernate

O projeto manteve a decisão de utilizar:

ddl-auto: validate

O Hibernate verifica se a entidade Java é compatível com o schema existente, mas não cria nem altera automaticamente as tabelas.

Isso reforça a separação:

Código Java
     +
Schema SQL versionado

24. Problema com a tabela interessado

Após a entidade ser criada, a validação do Hibernate encontrou a ausência da tabela interessado.

A investigação mostrou que o banco projeto_danca existia, mas a tabela ainda não estava criada.

Isso reforçou uma diferença importante:

Banco criado

não significa necessariamente:

Schema criado

O script SQL em:

database/init/001-create-schema.sql

passou a ser a fonte versionada da estrutura inicial.

Como os scripts de inicialização do MySQL são executados na criação de uma base nova, foi necessário recriar o volume de desenvolvimento quando o schema ainda não estava presente.

docker compose down -v
docker compose up -d

Em ambientes com dados reais, down -v deve ser utilizado com muito cuidado, pois remove volumes persistidos.

25. Criação do Repository

Foi criada a camada de persistência:

br.com.projetodanca.repository.InteressadoRepository

Ela estende:

JpaRepository<Interessado, Long>

Isso disponibiliza operações como:

save
findById
findAll
delete

Também foram criadas consultas derivadas:

boolean existsByEmail(String email);

para verificar e-mails já cadastrados, e:

List<Interessado> findAllByOrderByDataCadastroDesc();

para retornar os registros mais recentes primeiro.

Aprendizado

O Spring Data JPA consegue interpretar nomes de métodos e construir consultas automaticamente.

26. Criação da camada Service

As regras de negócio foram concentradas em:

br.com.projetodanca.service.InteressadoService

O fluxo passou a ser:

Controller
    ↓
Service
    ↓
Repository
    ↓
Database

No cadastro, o Service verifica se o e-mail já existe.

Caso exista, é lançada:

EmailJaCadastradoException

Na busca por ID, a ausência do registro gera:

InteressadoNaoEncontradoException

Essas exceções posteriormente são transformadas em respostas HTTP apropriadas.

27. Problema com EntityManager.refresh

O banco é responsável por gerar data_cadastro usando CURRENT_TIMESTAMP.

Depois do repository.save(), foi necessário atualizar a entidade com o valor efetivamente gerado pelo banco:

entityManager.refresh(salvo);

Inicialmente isso provocou:

TransactionRequiredException

A causa era a ausência de uma transação ativa.

A solução foi utilizar:

@Transactional

no método de cadastro.

Fluxo

POST
 ↓
Service
 ↓
@Transactional
 ↓
save()
 ↓
MySQL gera data_cadastro
 ↓
refresh()
 ↓
Response

Aprendizado

A transação fornece o contexto necessário para operações de persistência que dependem do estado gerenciado pelo JPA.

28. DTOs e contrato da API

A API passou a utilizar DTOs para não expor diretamente a entidade JPA.

Foram criados:

InteressadoRequest
InteressadoResponse
ErroResponse
ErroValidacaoResponse

O fluxo de entrada é:

HTTP
 ↓
InteressadoRequest
 ↓
Service
 ↓
Entity
 ↓
Repository

E o retorno:

Repository
 ↓
Entity
 ↓
InteressadoResponse
 ↓
HTTP

Aprendizado

A entidade representa a persistência; o DTO representa o contrato da API.

Isso reduz o acoplamento entre banco e API pública.

29. Bean Validation

Foram implementadas validações no DTO de entrada.

Principais regras:

nome:
- obrigatório
- máximo de 150 caracteres

email:
- obrigatório
- formato válido
- máximo de 150 caracteres

telefone:
- obrigatório
- máximo de 20 caracteres

dataNascimento:
- obrigatória
- deve estar no passado

nivelExperiencia:
- obrigatório

estiloDanca:
- obrigatório

observacoes:
- máximo de 500 caracteres

Foram utilizadas anotações como:

@NotBlank
@Size
@Email
@NotNull
@Past

Aprendizado

O backend continua protegendo a aplicação mesmo quando alguém ignora o frontend e chama a API diretamente.

Frontend = experiência do usuário
Backend  = validação confiável
Banco    = integridade estrutural

30. Tratamento global de exceções

Foi implementado tratamento centralizado para transformar exceções em respostas HTTP padronizadas.

Principais casos:

400 → ERRO_VALIDACAO
400 → JSON_INVALIDO
404 → INTERESSADO_NAO_ENCONTRADO
409 → EMAIL_JA_CADASTRADO

O fluxo é:

Service
 ↓
Exception
 ↓
GlobalExceptionHandler
 ↓
HTTP Response

Isso evita espalhar lógica de tratamento de erros pelos Controllers.

31. Implementação do CRUD REST

O Controller passou a disponibilizar as operações de cadastro de interessados.

Endpoints:

POST   /api/interessados
GET    /api/interessados
GET    /api/interessados/{id}
PUT    /api/interessados/{id}
DELETE /api/interessados/{id}

Respostas principais:

POST   → 201 Created
GET    → 200 OK
PUT    → 200 OK
DELETE → 204 No Content

Erros principais:

400 → dados inválidos
404 → registro inexistente
409 → e-mail duplicado

32. Testes HTTP do CRUD

A API foi validada com requisições HTTP reais.

Foram testados:

POST de novo interessado
GET de todos
GET por ID
PUT existente
DELETE existente
GET após exclusão
PUT de ID inexistente
PUT com e-mail duplicado

Também foi confirmado que:

GET /api/interessados

retorna os registros mais recentes primeiro.

Após a exclusão de um registro, a busca pelo mesmo ID retornou:

404 Not Found

Na tentativa de alterar um registro para um e-mail já utilizado, a API retornou:

409 Conflict

e o registro original permaneceu sem a alteração indevida.

33. Testes de validação

Foram testados os limites definidos pelas validações.

Casos testados:

nome vazio
nome acima de 150 caracteres
e-mail vazio
e-mail inválido
e-mail acima de 150 caracteres
telefone vazio
telefone acima de 20 caracteres
data de nascimento ausente
data de nascimento futura
nível de experiência ausente
estilo de dança ausente
observações acima de 500 caracteres

Aprendizado

Uma API não deve ser testada somente com entradas válidas.

Os testes negativos verificam se as regras realmente protegem o sistema.

34. Problema de UTF-8 no PowerShell

Durante alguns testes pelo Windows PowerShell, caracteres acentuados em JSON provocaram:

Invalid UTF-8 middle byte

A investigação mostrou que o problema estava relacionado à codificação do conteúdo enviado pelo ambiente de teste.

Para os testes rápidos dessa etapa, strings ASCII foram utilizadas quando necessário.

Aprendizado

Uma falha de teste precisa ser localizada antes de ser atribuída ao código.

É necessário diferenciar problemas de:

API
JSON
HTTP
Banco
Ferramenta
Codificação
Ambiente

35. Testes automatizados

Foram executados os testes automatizados do backend.

Foram validados:

BackendApplicationTests
InteressadoRepositoryTest
InteressadoServiceTest

Também foi executado:

.\mvnw.cmd clean test

Resultado final:

BUILD SUCCESS

Houve uma execução inicial de clean test que apresentou:

Failed to determine a suitable driver class

Depois da verificação da configuração e da execução dos testes individualmente, a suíte completa passou.

Aprendizado

Uma falha isolada de build deve ser investigada e reproduzida antes de ser considerada uma falha definitiva da implementação.

36. Documentação OpenAPI e Swagger

Após a API estar funcional, foi adicionada documentação automática utilizando SpringDoc.

Dependência adicionada:

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.0.0</version>
</dependency>

A interface Swagger UI foi validada em:

http://localhost:8080/swagger-ui.html

37. Anotações OpenAPI

Os endpoints receberam informações descritivas utilizando anotações OpenAPI.

Foram utilizadas:

@Operation
@ApiResponse
@Parameter
@Content
@Schema

A documentação passou a apresentar:

resumo
descrição
parâmetros
request body
respostas HTTP
schemas

Durante a implementação surgiu um conflito entre duas anotações chamadas RequestBody:

Spring RequestBody
OpenAPI RequestBody

Para eliminar a ambiguidade, a anotação OpenAPI foi utilizada com seu nome totalmente qualificado.

Aprendizado

Bibliotecas diferentes podem possuir classes com o mesmo nome. O nome completo da classe permite deixar explícito qual tipo está sendo utilizado.

38. Validação da documentação Swagger

Os cinco endpoints foram conferidos na interface Swagger UI.

Foram verificados:

POST /api/interessados
GET /api/interessados
GET /api/interessados/{id}
PUT /api/interessados/{id}
DELETE /api/interessados/{id}

Também foram conferidas as respostas documentadas:

201
200
204
400
404
409

E os schemas:

InteressadoRequest
InteressadoResponse

Aprendizado

A documentação OpenAPI funciona como um contrato técnico da API.

Ela permite entender:

quais endpoints existem
o que enviar
o que receber
quais erros podem ocorrer
quais campos existem

Isso será especialmente importante quando o frontend Angular começar a consumir o backend.

39. Git e branch de funcionalidade

A documentação Swagger foi desenvolvida em uma branch específica:

feature/backend/swagger-openapi

Commit:

356ac25 feat: adiciona documentação OpenAPI à API

A branch foi publicada no GitHub.

O destino do Pull Request é:

develop

e não diretamente main.

Fluxo adotado:

feature/*
   ↓
commit
   ↓
push
   ↓
Pull Request
   ↓
develop
   ↓
revisão / QA
   ↓
integração
   ↓
main

Aprendizado

O uso de branches permite separar desenvolvimento, integração e produção, tornando o histórico mais organizado e permitindo revisão antes da integração.

40. Estado atual do backend

O backend possui uma base funcional para o cadastro de interessados.

Fluxo atual:

HTTP
 ↓
Controller
 ↓
DTO + Validation
 ↓
Service
 ↓
Repository
 ↓
JPA / Hibernate
 ↓
MySQL

Retorno:

MySQL
 ↓
Entity
 ↓
Service
 ↓
Response DTO
 ↓
HTTP

Funcionalidades já implementadas e validadas:

CRUD
validação
regras de negócio
tratamento de exceções
ordenação por data de cadastro
persistência MySQL
testes automatizados
testes HTTP
documentação OpenAPI/Swagger

41. Próxima etapa

A etapa de documentação OpenAPI foi concluída.

O próximo passo do fluxo profissional é:

Pull Request
    ↓
develop
    ↓
revisão do Dev 3 / QA
    ↓
integração

Depois da integração, o projeto poderá avançar para as próximas tarefas do backlog, especialmente as relacionadas ao frontend Angular e à integração frontend/backend.

Regra mantida: uma seção significativa só é encerrada após implementação, teste, entendimento, documentação e commit correspondente.