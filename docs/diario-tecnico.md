\# Projeto Dança — Diário Técnico e Aprendizado



> \*\*Documento complementar de aprendizado\*\*

>

> Este documento explica as decisões técnicas tomadas durante o desenvolvimento do projeto.

>

> O objetivo não é apenas registrar \*\*o que foi feito\*\*, mas principalmente explicar:

>

> \* o que foi feito;

> \* por que foi feito;

> \* qual problema foi resolvido;

> \* quais alternativas existiam;

> \* quais conceitos técnicos estão envolvidos.



\---



\# 1. Como estamos desenvolvendo



> O projeto está sendo desenvolvido de forma incremental.



Isso significa que não vamos tentar construir frontend, backend, banco e Docker simultaneamente.



A estratégia é:



```text

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

```



> A vantagem dessa abordagem é reduzir a quantidade de problemas acontecendo simultaneamente.



Se algo quebrar, conseguimos identificar em qual etapa ocorreu.



\---



\# 2. Por que começamos pelo banco e Docker?



O sistema precisa persistir dados.



Antes de construir uma tela Angular para cadastrar uma pessoa, precisamos saber:



> "Onde esses dados serão armazenados?"



Por isso definimos primeiro o banco:



```text

projeto\_danca

```



e a tabela:



```text

interessado

```



Depois colocamos o MySQL dentro de um container Docker.



Isso cria um ambiente reproduzível.



Em vez de depender de:



```text

"Meu computador tem MySQL instalado e configurado"

```



queremos chegar a:



```text

"Qualquer desenvolvedor executa docker compose up e possui o MySQL necessário."

```



Esse é um conceito importante de desenvolvimento profissional:



> \*\*Ambientes reproduzíveis reduzem problemas de configuração.\*\*



\---



\# 3. Por que usamos Docker?



Docker permite executar aplicações isoladas em containers.



Neste projeto, o primeiro container é:



```text

projeto-danca-mysql

```



Ele executa:



```text

MySQL 8.4

```



O Docker Compose permite definir essa infraestrutura em código.



Isso é importante porque a configuração deixa de existir apenas na máquina do desenvolvedor.



Ela passa a estar registrada no projeto:



```text

docker-compose.yml

```



\---



\# 4. Por que a porta virou 3307?



Inicialmente planejamos:



```text

3306:3306

```



Mas o Windows já possuía um MySQL utilizando:



```text

localhost:3306

```



Quando tentamos iniciar o container, ocorreu conflito de porta.



A solução foi:



```text

3307:3306

```



Isso significa:



```text

Windows

localhost:3307

&#x20;      ↓

Docker

container:3306

&#x20;      ↓

MySQL

```



> A porta interna do MySQL continua sendo 3306.



Apenas a porta exposta no computador foi alterada para 3307.



\---



\# 5. O que significa o volume do Docker?



Configuramos:



```yaml

volumes:

&#x20; - mysql\_data:/var/lib/mysql

```



O MySQL armazena seus dados em:



```text

/var/lib/mysql

```



O volume faz com que esses dados não desapareçam simplesmente porque o container foi recriado.



Conceito:



```text

Container

&#x20;   ↓

MySQL

&#x20;   ↓

/var/lib/mysql

&#x20;   ↓

mysql\_data

```



> Container e dados são conceitos diferentes.



O container pode ser removido.



O volume pode continuar existindo.



\---



\# 6. Por que criamos um arquivo SQL?



Criamos:



```text

database/init/001-create-schema.sql

```



Esse arquivo contém a criação da tabela.



A vantagem é que a estrutura do banco também fica versionada no projeto.



Sem o arquivo:



> "Eu criei a tabela manualmente no meu computador."



Com o arquivo:



> "A estrutura necessária do banco está documentada e reproduzível."



Isso é muito mais adequado para um projeto profissional.



\---



\# 7. O que significa `/docker-entrypoint-initdb.d`?



No Compose configuramos:



```yaml

\- ./database/init:/docker-entrypoint-initdb.d

```



Estamos mapeando:



```text

projeto

database/init

&#x20;      ↓

container

/docker-entrypoint-initdb.d

```



O MySQL possui um mecanismo que executa scripts colocados nesse diretório durante a inicialização inicial do banco.



Isso permite que:



```text

001-create-schema.sql

```



seja executado automaticamente.



\---



\# 8. Por que usamos `docker compose down -v`?



Durante a configuração do banco, precisamos recriar o ambiente inicial.



O comando:



```powershell

docker compose down -v

```



remove:



> \* containers;

> \* redes criadas pelo Compose;

> \* volumes associados.



Isso foi necessário porque os scripts de:



```text

/docker-entrypoint-initdb.d

```



são executados na \*\*inicialização de um banco novo\*\*.



Se o volume já contém um banco inicializado, o MySQL não executa novamente esses scripts automaticamente.



> Em um projeto com dados reais, nunca devemos executar `down -v` sem entender as consequências, porque o volume contém os dados persistidos.



Neste momento isso é seguro porque ainda estamos montando o ambiente de desenvolvimento.



\---



\# 9. Por que telefone é String?



O telefone foi definido como:



```text

VARCHAR(20)

```



e não:



```text

INT

```



Isso ocorre porque telefone não é uma quantidade matemática.



Exemplos:



```text

+55 11 99999-9999

(77) 99999-9999

77999999999

```



Podem existir:



> \* código do país;

> \* DDD;

> \* espaços;

> \* parênteses;

> \* hífen;

> \* zeros.



Portanto:



> \*\*Telefone é dado textual, não numérico.\*\*



\---



\# 10. Por que o e-mail possui UNIQUE?



A tabela possui:



```sql

CONSTRAINT uk\_interessado\_email UNIQUE (email)

```



Isso significa que o banco não permite dois registros com o mesmo e-mail.



Exemplo:



```text

Maria@email.com

Maria@email.com

```



O segundo cadastro deverá ser rejeitado.



Essa regra é importante porque o e-mail foi escolhido como identificador único do interessado.



\---



\# 11. Por que usamos camadas no Backend?



A arquitetura planejada é:



```text

Controller

&#x20;   ↓

Service

&#x20;   ↓

Repository

&#x20;   ↓

Database

```



Cada camada possui uma responsabilidade.



\### Controller



Recebe:



```text

HTTP Request

```



e devolve:



```text

HTTP Response

```



\### Service



Executa:



```text

Regras de negócio

```



\### Repository



Cuida da persistência:



```text

Java ↔ Banco

```



\### Database



Armazena os dados.



\---



\# 12. Por que não colocar tudo no Controller?



Seria possível criar um Controller que:



> recebe requisição → valida → acessa banco → transforma dados → devolve resposta.



Mas isso criaria uma classe com responsabilidades demais.



Com o tempo ela ficaria difícil de:



> \* testar;

> \* manter;

> \* alterar;

> \* reutilizar.



Separar responsabilidades torna o código mais organizado.



Esse conceito está relacionado ao princípio:



> \*\*Separation of Concerns — Separação de Responsabilidades.\*\*



\---



\# 13. Por que teremos DTOs?



Planejamos:



```text

InteressadoRequest

InteressadoResponse

```



em vez de simplesmente expor a Entity.



A Entity representa a persistência.



O DTO representa o contrato da API.



Conceitualmente:



```text

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

```



Na resposta:



```text

Database

&#x20;↓

Entity

&#x20;↓

Service

&#x20;↓

Response DTO

&#x20;↓

HTTP

```



Isso reduz o acoplamento entre:



```text

Banco de dados

```



e:



```text

API pública

```



\---



\# 14. Por que usamos Enums?



Nível de experiência:



```java

NUNCA\_DANCEI

INICIANTE

INTERMEDIARIO

AVANCADO

```



Estilo:



```java

SAMBA

FORRO

SALSA

...

```



Em vez de deixar qualquer texto entrar.



Sem Enum:



```text

iniciante

INICIANTE

Iniciante

iniciant

```



poderiam representar a mesma coisa.



Com Enum:



```text

INICIANTE

```



torna-se um valor controlado pela aplicação.



\---



\# 15. Por que a validação existe no Frontend e Backend?



É importante entender esta diferença.



\### Frontend



Serve principalmente para:



> melhorar a experiência do usuário.



Exemplo:



```text

"E-mail inválido"

```



pode aparecer imediatamente no formulário.



\### Backend



É a autoridade da aplicação.



Mesmo que alguém ignore o Angular e faça uma requisição diretamente para a API:



```text

POST /api/interessados

```



o backend ainda deverá validar os dados.



Portanto:



```text

Frontend = UX

Backend = regra confiável

Banco = integridade estrutural

```



\---



\# 16. Por que estamos fazendo uma etapa por vez?



O objetivo não é apenas terminar o projeto.



O objetivo também é aprender como um sistema Full Stack é construído.



Por isso, cada etapa deverá seguir:



```text

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

```



Isso também facilita identificar exatamente qual alteração causou um problema.



\---



\# 17. Git e commits



Estamos utilizando commits pequenos e relacionados a uma mudança.



Exemplo:



```text

feat: configura banco de dados com MySQL e Docker

```



O prefixo:



```text

feat

```



indica uma mudança relacionada a funcionalidade/recurso.



Outros exemplos:



```text

fix: corrige conexão com banco

docs: atualiza documentação

test: adiciona testes do interessado

chore: atualiza configuração do projeto

```



A ideia é que, olhando o histórico:



```text

git log

```



seja possível entender a evolução do projeto.



\---



\# 18. Regra de documentação adotada no projeto



A partir deste ponto, ao terminar uma seção significativa:



> \*\*1. Atualizaremos o documento oficial.\*\*



Depois:



> \*\*2. Atualizaremos este documento de aprendizado.\*\*



E então:



> \*\*3. Faremos o commit correspondente.\*\*



O fluxo passa a ser:



```text

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

```



\---



\# 19. Estado atual do aprendizado



Até agora foram trabalhados os seguintes conceitos:



> \* estrutura de projeto;

> \* Git;

> \* `.gitignore`;

> \* variáveis de ambiente;

> \* Docker;

> \* Docker Compose;

> \* containers;

> \* volumes;

> \* portas;

> \* MySQL;

> \* criação de schema;

> \* scripts de inicialização;

> \* modelagem inicial de banco;

> \* chave primária;

> \* chave única;

> \* tipos de dados;

> \* desenvolvimento incremental;

> \* Conventional Commits.



\---



\# 20. Próxima etapa



A próxima etapa técnica será:



```text

TASK-004

README

```



Depois iniciaremos o backend:



```text

TASK-010

Conexão da aplicação com o banco



TASK-011

Criar projeto Spring Boot



TASK-012

Configurar Maven



TASK-013

Configurar dependências

```



> A partir do backend, começaremos a trabalhar diretamente com Java e Spring Boot e explicaremos cada conceito antes de utilizá-lo.


> # Diário Técnico — Configuração inicial do Backend
>
> ## Data
>
> 04/09/2026
>
> ## Objetivo da seção
>
> Configurar a aplicação Spring Boot para iniciar corretamente e estabelecer sua primeira comunicação com o banco de dados MySQL executado através do Docker.
>
> ## 1. Criação do projeto Spring Boot
>
> O backend foi criado utilizando:
>
> * Java 25
> * Spring Boot 4.1.1
> * Maven
> * Spring Web
> * Spring Data JPA
> * Bean Validation
> * MySQL Driver
>
> O Maven Wrapper foi mantido no projeto para permitir que os comandos Maven sejam executados sem depender da instalação global do Maven na máquina.
>
> O comando utilizado para validar o ambiente foi:
>
> ```powershell
> .\mvnw.cmd test
> ```
>
> ## 2. Primeiro problema encontrado
>
> Ao executar os testes inicialmente, o Spring Boot não conseguiu carregar o `ApplicationContext`.
>
> A causa encontrada no relatório do Surefire foi:
>
> ```text
> Failed to determine a suitable driver class
> ```
>
> Isso indicava que o Spring Boot possuía as dependências necessárias para trabalhar com banco de dados, mas ainda não havia uma configuração de datasource suficiente para determinar como acessar o MySQL.
>
> ### Aprendizado
>
> Adicionar o MySQL Driver ao Maven não configura automaticamente a conexão com o banco.
>
> São responsabilidades diferentes:
>
> ```text
> MySQL Driver
>     ↓
> Permite comunicação com MySQL
>
> application.yaml
>     ↓
> Define como e onde realizar a conexão
> ```
>
> ## 3. Configuração do application.yaml
>
> Foi configurado:
>
> ```yaml
> spring:
>   application:
>     name: projeto-danca
>
>   datasource:
>     url: jdbc:mysql://localhost:3307/projeto_danca
>     username: ${DB_USERNAME}
>     password: ${DB_PASSWORD}
>
>   jpa:
>     hibernate:
>       ddl-auto: validate
>     open-in-view: false
> ```
>
> ## 4. Variáveis de ambiente
>
> As credenciais não foram colocadas diretamente no código Java.
>
> Foi utilizada a seguinte abordagem:
>
> ```yaml
> username: ${DB_USERNAME}
> password: ${DB_PASSWORD}
> ```
>
> Durante a execução local pelo PowerShell, as variáveis foram disponibilizadas na sessão:
>
> ```powershell
> $env:DB_USERNAME="projeto"
> $env:DB_PASSWORD="projeto123"
> ```
>
> ## 5. Segundo problema encontrado
>
> Após configurar o `application.yaml`, surgiu um erro de sintaxe YAML:
>
> ```text
> mapping values are not allowed here
> ```
>
> O problema estava relacionado a uma duplicação acidental na configuração:
>
> ```text
> open-in-view: falseen-in-view: false
> ```
>
> Após a correção, o YAML passou a ser interpretado corretamente.
>
> ### Aprendizado
>
> YAML é sensível à sintaxe e à estrutura. Um pequeno erro de edição pode impedir o Spring Boot de iniciar antes mesmo da execução da lógica Java.
>
> ## 6. Validação final
>
> Após a correção, o teste foi executado novamente:
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
> Isso confirma que:
>
> * o Spring Boot conseguiu carregar o contexto;
> * a configuração do datasource foi reconhecida;
> * o JPA/Hibernate conseguiu inicializar;
> * o teste `contextLoads` passou.
>
> ## 7. Decisão sobre ddl-auto
>
> Foi utilizado:
>
> ```yaml
> ddl-auto: validate
> ```
>
> O Hibernate deverá verificar a compatibilidade entre as entidades Java e o schema MySQL, mas não deverá criar ou alterar automaticamente as tabelas.
>
> ## 8. Estado ao final da seção
>
> A infraestrutura inicial do backend está validada.
>
> Fluxo atual:
>
> ```text
> Spring Boot
>     ↓
> DataSource / HikariCP
>     ↓
> MySQL Driver
>     ↓
> localhost:3307
>     ↓
> Docker
>     ↓
> MySQL
>     ↓
> projeto_danca
> ```
>
> ## Próxima etapa
>
> **TASK-015 — Criar a entidade `Interessado`.**



