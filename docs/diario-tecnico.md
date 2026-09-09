> # Projeto Dança — Diário Técnico e Aprendizado
>
> > **Documento complementar de aprendizado**
> >
> > Este documento explica as decisões técnicas tomadas durante o desenvolvimento do projeto.
> >
> > O objetivo não é apenas registrar **o que foi feito**, mas principalmente explicar:
> >
> > * o que foi feito;
> > * por que foi feito;
> > * qual problema foi resolvido;
> > * quais alternativas existiam;
> > * quais conceitos técnicos estão envolvidos.
>
> ---
>
> # 1. Como estamos desenvolvendo
>
> > O projeto está sendo desenvolvido de forma incremental.
>
> Isso significa que não vamos tentar construir frontend, backend, banco e Docker simultaneamente.
>
> A estratégia é:
>
> ```text
> Planejamento
>      ↓
> Fundação
>      ↓
> Banco
>      ↓
> Backend
>      ↓
> Frontend
>      ↓
> Integração
>      ↓
> Docker completo
>      ↓
> Testes
>      ↓
> GitHub / Release
> ```
>
> A vantagem dessa abordagem é reduzir a quantidade de problemas acontecendo simultaneamente.
>
> Se algo quebrar, conseguimos identificar em qual etapa ocorreu.
>
> ---
>
> # 2. Por que começamos pelo banco e Docker?
>
> O sistema precisa persistir dados.
>
> Antes de construir uma tela Angular para cadastrar uma pessoa, precisamos saber:
>
> > "Onde esses dados serão armazenados?"
>
> Por isso definimos primeiro o banco:
>
> ```text
> projeto_danca
> ```
>
> e a tabela:
>
> ```text
> interessado
> ```
>
> Depois colocamos o MySQL dentro de um container Docker.
>
> Isso cria um ambiente reproduzível.
>
> Em vez de depender de:
>
> ```text
> "Meu computador tem MySQL instalado e configurado"
> ```
>
> queremos chegar a:
>
> ```text
> "Qualquer desenvolvedor executa docker compose up e possui o MySQL necessário."
> ```
>
> Esse é um conceito importante de desenvolvimento profissional:
>
> > **Ambientes reproduzíveis reduzem problemas de configuração.**
>
> ---
>
> # 3. Por que usamos Docker?
>
> Docker permite executar aplicações isoladas em containers.
>
> Neste projeto, o primeiro container é:
>
> ```text
> projeto-danca-mysql
> ```
>
> Ele executa:
>
> ```text
> MySQL 8.4
> ```
>
> O Docker Compose permite definir essa infraestrutura em código.
>
> Isso é importante porque a configuração deixa de existir apenas na máquina do desenvolvedor.
>
> Ela passa a estar registrada no projeto:
>
> ```text
> docker-compose.yml
> ```
>
> ---
>
> # 4. Por que a porta virou 3307?
>
> Inicialmente planejamos:
>
> ```text
> 3306:3306
> ```
>
> Mas o Windows já possuía um MySQL utilizando:
>
> ```text
> localhost:3306
> ```
>
> Quando tentamos iniciar o container, ocorreu conflito de porta.
>
> A solução foi:
>
> ```text
> 3307:3306
> ```
>
> Isso significa:
>
> ```text
> Windows
> localhost:3307
>       ↓
> Docker
> container:3306
>       ↓
> MySQL
> ```
>
> A porta interna do MySQL continua sendo `3306`.
>
> Apenas a porta exposta no computador foi alterada para `3307`.
>
> ---
>
> # 5. O que significa o volume do Docker?
>
> Configuramos:
>
> ```yaml
> volumes:
>   - mysql_data:/var/lib/mysql
> ```
>
> O MySQL armazena seus dados em:
>
> ```text
> /var/lib/mysql
> ```
>
> O volume faz com que esses dados não desapareçam simplesmente porque o container foi recriado.
>
> Conceito:
>
> ```text
> Container
>    ↓
> MySQL
>    ↓
> /var/lib/mysql
>    ↓
> mysql_data
> ```
>
> > Container e dados são conceitos diferentes.
>
> O container pode ser removido.
>
> O volume pode continuar existindo.
>
> ---
>
> # 6. Por que criamos um arquivo SQL?
>
> Criamos:
>
> ```text
> database/init/001-create-schema.sql
> ```
>
> Esse arquivo contém a criação da tabela.
>
> A vantagem é que a estrutura do banco também fica versionada no projeto.
>
> Sem o arquivo:
>
> > "Eu criei a tabela manualmente no meu computador."
>
> Com o arquivo:
>
> > "A estrutura necessária do banco está documentada e reproduzível."
>
> Isso é muito mais adequado para um projeto profissional.
>
> ---
>
> # 7. O que significa `/docker-entrypoint-initdb.d`?
>
> No Compose configuramos:
>
> ```yaml
> - ./database/init:/docker-entrypoint-initdb.d
> ```
>
> Estamos mapeando:
>
> ```text
> projeto
> database/init
>       ↓
> container
> /docker-entrypoint-initdb.d
> ```
>
> O MySQL possui um mecanismo que executa scripts colocados nesse diretório durante a inicialização inicial do banco.
>
> Isso permite que:
>
> ```text
> 001-create-schema.sql
> ```
>
> seja executado automaticamente.
>
> ---
>
> # 8. Por que usamos `docker compose down -v`?
>
> Durante a configuração do banco, precisamos recriar o ambiente inicial.
>
> O comando:
>
> ```powershell
> docker compose down -v
> ```
>
> remove:
>
> * containers;
> * redes criadas pelo Compose;
> * volumes associados.
>
> Isso foi necessário porque os scripts de:
>
> ```text
> /docker-entrypoint-initdb.d
> ```
>
> são executados na **inicialização de um banco novo**.
>
> Se o volume já contém um banco inicializado, o MySQL não executa novamente esses scripts automaticamente.
>
> > Em um projeto com dados reais, nunca devemos executar `down -v` sem entender as consequências, porque o volume contém os dados persistidos.
>
> Neste momento isso é seguro porque ainda estamos montando o ambiente de desenvolvimento.
>
> ---
>
> # 9. Por que telefone é String?
>
> O telefone foi definido como:
>
> ```text
> VARCHAR(20)
> ```
>
> e não:
>
> ```text
> INT
> ```
>
> Isso ocorre porque telefone não é uma quantidade matemática.
>
> Exemplos:
>
> ```text
> +55 11 99999-9999
> (77) 99999-9999
> 77999999999
> ```
>
> Podem existir:
>
> * código do país;
> * DDD;
> * espaços;
> * parênteses;
> * hífen;
> * zeros.
>
> Portanto:
>
> > **Telefone é dado textual, não numérico.**
>
> ---
>
> # 10. Por que o e-mail possui UNIQUE?
>
> A tabela possui:
>
> ```sql
> CONSTRAINT uk_interessado_email UNIQUE (email)
> ```
>
> Isso significa que o banco não permite dois registros com o mesmo e-mail.
>
> Exemplo:
>
> ```text
> Maria@email.com
> Maria@email.com
> ```
>
> O segundo cadastro deverá ser rejeitado.
>
> Além da restrição no banco, o Service também verifica a existência do e-mail antes de cadastrar ou atualizar.
>
> Isso permite apresentar uma resposta de negócio específica:
>
> ```text
> HTTP 409
> EMAIL_JA_CADASTRADO
> ```
>
> Essa combinação ensina uma ideia importante:
>
> ```text
> Service
>     ↓
> Regra de negócio
>
> Database
>     ↓
> Integridade estrutural
> ```
>
> ---
>
> # 11. Por que usamos camadas no Backend?
>
> A arquitetura utilizada é:
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
> Cada camada possui uma responsabilidade.
>
> ### Controller
>
> Recebe:
>
> ```text
> HTTP Request
> ```
>
> e devolve:
>
> ```text
> HTTP Response
> ```
>
> ### Service
>
> Executa:
>
> ```text
> Regras de negócio
> ```
>
> ### Repository
>
> Cuida da persistência:
>
> ```text
> Java ↔ Banco
> ```
>
> ### Database
>
> Armazena os dados.
>
> ---
>
> # 12. Por que não colocar tudo no Controller?
>
> Seria possível criar um Controller que:
>
> > recebe requisição → valida → acessa banco → transforma dados → devolve resposta.
>
> Mas isso criaria uma classe com responsabilidades demais.
>
> Com o tempo ela ficaria difícil de:
>
> * testar;
> * manter;
> * alterar;
> * reutilizar.
>
> Separar responsabilidades torna o código mais organizado.
>
> Esse conceito está relacionado ao princípio:
>
> > **Separation of Concerns — Separação de Responsabilidades.**
>
> ---
>
> # 13. Por que usamos DTOs?
>
> Foram criados:
>
> ```text
> InteressadoRequest
> InteressadoResponse
> ```
>
> em vez de utilizar a Entity diretamente como contrato público.
>
> A Entity representa a persistência.
>
> O DTO representa o contrato da API.
>
> Conceitualmente:
>
> ```text
> HTTP
>  ↓
> Request DTO
>  ↓
> Controller
>  ↓
> Service
>  ↓
> Entity
>  ↓
> Repository
>  ↓
> Database
> ```
>
> Na resposta:
>
> ```text
> Database
>  ↓
> Entity
>  ↓
> Service
>  ↓
> Response DTO
>  ↓
> HTTP
> ```
>
> Isso reduz o acoplamento entre:
>
> ```text
> Banco de dados
> ```
>
> e:
>
> ```text
> API pública
> ```
>
> ---
>
> # 14. Por que usamos Enums?
>
> Nível de experiência:
>
> ```java
> NUNCA_DANCEI
> INICIANTE
> INTERMEDIARIO
> AVANCADO
> ```
>
> Estilos:
>
> ```java
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
> Em vez de deixar qualquer texto entrar.
>
> Sem Enum:
>
> ```text
> iniciante
> INICIANTE
> Iniciante
> iniciant
> ```
>
> poderiam representar a mesma coisa.
>
> Com Enum:
>
> ```text
> INICIANTE
> ```
>
> torna-se um valor controlado pela aplicação.
>
> ---
>
> # 15. Por que a validação existe no Frontend e Backend?
>
> É importante entender esta diferença.
>
> ### Frontend
>
> Serve principalmente para:
>
> > melhorar a experiência do usuário.
>
> Exemplo:
>
> ```text
> "E-mail inválido"
> ```
>
> pode aparecer imediatamente no formulário.
>
> ### Backend
>
> É a autoridade da aplicação.
>
> Mesmo que alguém ignore o Angular e faça uma requisição diretamente para a API:
>
> ```text
> POST /api/interessados
> ```
>
> o backend ainda deverá validar os dados.
>
> Portanto:
>
> ```text
> Frontend = UX
> Backend = regra confiável
> Banco = integridade estrutural
> ```
>
> Nesta etapa, as validações do backend foram efetivamente implementadas utilizando Bean Validation.
>
> ---
>
> # 16. Por que estamos fazendo uma etapa por vez?
>
> O objetivo não é apenas terminar o projeto.
>
> O objetivo também é aprender como um sistema Full Stack é construído.
>
> Por isso, cada etapa deverá seguir:
>
> ```text
> Implementar
>     ↓
> Executar
>     ↓
> Testar
>     ↓
> Entender
>     ↓
> Documentar
>     ↓
> Commit
>     ↓
> Próxima etapa
> ```
>
> Isso também facilita identificar exatamente qual alteração causou um problema.
>
> ---
>
> # 17. Git e commits
>
> Estamos utilizando commits pequenos e relacionados a uma mudança.
>
> Exemplos:
>
> ```text
> feat: configura banco de dados com MySQL e Docker
> feat: adiciona CRUD de interessados e validacoes
> feat: ajusta cadastro e ordenacao de interessados
> fix: corrige pacote do teste de service
> ```
>
> O prefixo:
>
> ```text
> feat
> ```
>
> indica uma mudança relacionada a funcionalidade/recurso.
>
> Outros exemplos:
>
> ```text
> fix: corrige conexão com banco
> docs: atualiza documentação
> test: adiciona testes do interessado
> chore: atualiza configuração do projeto
> ```
>
> A ideia é que, olhando o histórico:
>
> ```text
> git log
> ```
>
> seja possível entender a evolução do projeto.
>
> ---
>
> # 18. Regra de documentação adotada no projeto
>
> A partir deste ponto, ao terminar uma seção significativa:
>
> > **1. Atualizaremos o documento oficial.**
>
> Depois:
>
> > **2. Atualizaremos este documento de aprendizado.**
>
> E então:
>
> > **3. Faremos o commit correspondente.**
>
> O fluxo passa a ser:
>
> ```text
> Implementação
>      ↓
> Teste
>      ↓
> Documentação oficial
>      ↓
> Explicação técnica
>      ↓
> Git commit
>      ↓
> Próxima seção
> ```
>
> ---
>
> # 19. Estado atual do aprendizado — Fundação
>
> Até a conclusão da fundação foram trabalhados os seguintes conceitos:
>
> > * estrutura de projeto;
> > * Git;
> > * `.gitignore`;
> > * variáveis de ambiente;
> > * Docker;
> > * Docker Compose;
> > * containers;
> > * volumes;
> > * portas;
> > * MySQL;
> > * criação de schema;
> > * scripts de inicialização;
> > * modelagem inicial de banco;
> > * chave primária;
> > * chave única;
> > * tipos de dados;
> > * desenvolvimento incremental;
> > * Conventional Commits.
>
> ---
>
> # 20. Configuração inicial do Backend
>
> ## Data
>
> 04/09/2026
>
> ## Objetivo da seção
>
> Configurar a aplicação Spring Boot para iniciar corretamente e estabelecer sua primeira comunicação com o banco de dados MySQL executado através do Docker.
>
> ### 20.1 Criação do projeto Spring Boot
>
> O backend foi criado utilizando:
>
> * Java 25;
> * Spring Boot 4.1.1;
> * Maven;
> * Spring Web;
> * Spring Data JPA;
> * Bean Validation;
> * MySQL Driver.
>
> O Maven Wrapper foi mantido no projeto para permitir que os comandos Maven sejam executados sem depender da instalação global do Maven na máquina.
>
> O comando utilizado para validar o ambiente foi:
>
> ```powershell
> .\mvnw.cmd test
> ```
>
> ### 20.2 Primeiro problema encontrado
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
> ### 20.3 Configuração do `application.yaml`
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
> ### 20.4 Variáveis de ambiente
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
> ### 20.5 Segundo problema encontrado
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
> ### 20.6 Validação final
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
> Isso confirmou que:
>
> * o Spring Boot conseguiu carregar o contexto;
> * a configuração do datasource foi reconhecida;
> * o JPA/Hibernate conseguiu inicializar;
> * o teste `contextLoads` passou.
>
> ### 20.7 Decisão sobre `ddl-auto`
>
> Foi utilizado:
>
> ```yaml
> ddl-auto: validate
> ```
>
> O Hibernate deverá verificar a compatibilidade entre as entidades Java e o schema MySQL, mas não deverá criar ou alterar automaticamente as tabelas.
>
> ---
>
> # 21. Criação da Entity `Interessado`
>
> A entidade `Interessado` foi criada para representar, no Java, a tabela:
>
> ```text
> interessado
> ```
>
> A entidade utiliza:
>
> ```java
> @Entity
> @Table(name = "interessado")
> ```
>
> O ID utiliza geração automática:
>
> ```java
> @GeneratedValue(strategy = GenerationType.IDENTITY)
> ```
>
> Isso mantém o controle do identificador no banco MySQL.
>
> Os campos `nivelExperiencia` e `estiloDanca` utilizam:
>
> ```java
> @Enumerated(EnumType.STRING)
> ```
>
> A escolha de `EnumType.STRING` é importante porque armazena o nome do enum no banco em vez de sua posição numérica.
>
> ---
>
> # 22. Criação do Repository
>
> Foi criado:
>
> ```text
> InteressadoRepository
> ```
>
> estendendo:
>
> ```java
> JpaRepository<Interessado, Long>
> ```
>
> Isso permite utilizar operações de persistência fornecidas pelo Spring Data JPA sem precisar implementar manualmente SQL para o CRUD básico.
>
> Também foram criados métodos derivados:
>
> ```java
> boolean existsByEmail(String email);
> ```
>
> e:
>
> ```java
> List<Interessado> findAllByOrderByDataCadastroDesc();
> ```
>
> O primeiro permite verificar a existência de um e-mail.
>
> O segundo permite recuperar os interessados ordenados pela data de cadastro, do mais recente para o mais antigo.
>
> ---
>
> # 23. Um erro importante no método de ordenação
>
> Durante a implementação da ordenação, foi escrito inicialmente:
>
> ```java
> findAllByOrderDataCadastroDesc()
> ```
>
> O Maven falhou durante a inicialização do Spring Data JPA.
>
> O erro indicava:
>
> ```text
> No property 'orderDataCadastroDesc' found for type 'Interessado'
> ```
>
> O problema estava na convenção utilizada pelo Spring Data para criar consultas através do nome do método.
>
> A forma correta é:
>
> ```java
> findAllByOrderByDataCadastroDesc()
> ```
>
> A expressão:
>
> ```text
> OrderBy
> ```
>
> indica a ordenação.
>
> Já:
>
> ```text
> DataCadastro
> ```
>
> indica o campo utilizado.
>
> E:
>
> ```text
> Desc
> ```
>
> indica ordem decrescente.
>
> Portanto:
>
> ```text
> findAllByOrderByDataCadastroDesc()
> ```
>
> pode ser entendido como:
>
> ```text
> findAll
>     ↓
> por
>     ↓
> ordenar por DataCadastro
>     ↓
> descendente
> ```
>
> ### Aprendizado
>
> Métodos derivados do Spring Data JPA não são nomes arbitrários. Eles seguem uma convenção que o framework interpreta para construir a consulta.
>
> ---
>
> # 24. DTO de Request
>
> Foi criado:
>
> ```text
> InteressadoRequest
> ```
>
> Esse DTO representa os dados recebidos pela API.
>
> Foram adicionadas anotações do Bean Validation:
>
> ```java
> @NotBlank
> @Email
> @NotNull
> @Past
> @Size
> ```
>
> Exemplo:
>
> ```java
> @NotBlank(message = "O nome é obrigatório.")
> @Size(
>     max = 150,
>     message = "O nome deve ter no máximo 150 caracteres."
> )
> String nome
> ```
>
> Isso permite que o Spring valide automaticamente o objeto quando o Controller utiliza:
>
> ```java
> @Valid
> @RequestBody
> ```
>
> ---
>
> # 25. DTO de Response
>
> Foi criado:
>
> ```text
> InteressadoResponse
> ```
>
> O objetivo é controlar exatamente quais dados serão devolvidos pela API.
>
> A resposta contém:
>
> * ID;
> * nome;
> * e-mail;
> * telefone;
> * data de nascimento;
> * nível de experiência;
> * estilo de dança;
> * observações;
> * data de cadastro.
>
> Isso reforça a separação:
>
> ```text
> Entity ≠ contrato da API
> ```
>
> ---
>
> # 26. Implementação do Service
>
> Foi criado:
>
> ```text
> InteressadoService
> ```
>
> O Service passou a concentrar regras como:
>
> * impedir e-mail duplicado;
> * buscar interessado;
> * detectar interessado inexistente;
> * atualizar dados;
> * verificar e-mail durante atualização;
> * excluir interessado.
>
> Um exemplo:
>
> ```java
> if (repository.existsByEmail(interessado.getEmail())) {
>     throw new EmailJaCadastradoException(
>         "Já existe um interessado cadastrado com este e-mail."
>     );
> }
> ```
>
> A decisão de colocar essa regra no Service segue a separação de responsabilidades:
>
> ```text
> Controller
>     ↓
> Entrada HTTP
>
> Service
>     ↓
> Regra de negócio
>
> Repository
>     ↓
> Persistência
> ```
>
> ---
>
> # 27. O problema do `dataCadastro`
>
> A coluna:
>
> ```sql
> data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
> ```
>
> pertence ao banco de dados.
>
> Portanto, a decisão foi manter o banco como responsável por gerar esse valor.
>
> Inicialmente, o `POST` retornava:
>
> ```text
> dataCadastro: null
> ```
>
> mesmo que o banco tivesse criado o timestamp.
>
> O motivo era que o objeto Java salvo em memória ainda não possuía necessariamente o valor gerado pelo banco.
>
> Foi utilizada a configuração:
>
> ```java
> @Column(
>     name = "data_cadastro",
>     nullable = false,
>     updatable = false,
>     insertable = false
> )
> ```
>
> Dessa forma:
>
> > **o banco é o proprietário do valor de `data_cadastro`.**
>
> ---
>
> # 28. Por que usamos `EntityManager.refresh()`?
>
> Após salvar o interessado:
>
> ```java
> Interessado salvo = repository.save(interessado);
> ```
>
> foi necessário sincronizar novamente a entidade com o banco:
>
> ```java
> entityManager.refresh(salvo);
> ```
>
> O objetivo é recuperar o valor de `dataCadastro` que foi gerado pelo MySQL.
>
> Porém, a primeira tentativa apresentou:
>
> ```text
> TransactionRequiredException
> ```
>
> Isso aconteceu porque `refresh()` exige uma transação ativa.
>
> A solução foi adicionar:
>
> ```java
> @Transactional
> ```
>
> ao método de cadastro.
>
> Depois disso, o fluxo passou a ser:
>
> ```text
> POST
>  ↓
> Service
>  ↓
> repository.save()
>  ↓
> MySQL gera CURRENT_TIMESTAMP
>  ↓
> entityManager.refresh()
>  ↓
> resposta contém dataCadastro
> ```
>
> ### Aprendizado
>
> Esse problema ajudou a entender a diferença entre:
>
> ```text
> objeto Java em memória
> ```
>
> e:
>
> ```text
> estado persistido no banco
> ```
>
> Também introduziu o conceito de transação no contexto do JPA.
>
> ---
>
> # 29. Implementação do Controller
>
> O Controller foi criado com:
>
> ```java
> @RestController
> @RequestMapping("/api/interessados")
> ```
>
> Foram implementados:
>
> ```text
> POST
> GET
> GET /{id}
> PUT /{id}
> DELETE /{id}
> ```
>
> O Controller converte:
>
> ```text
> InteressadoRequest
> ```
>
> em:
>
> ```text
> Interessado
> ```
>
> e posteriormente converte a entidade para:
>
> ```text
> InteressadoResponse
> ```
>
> ---
>
> # 30. Tratamento global de exceções
>
> Foi criado:
>
> ```text
> GlobalExceptionHandler
> ```
>
> utilizando:
>
> ```java
> @RestControllerAdvice
> ```
>
> Isso permite centralizar o tratamento de exceções da API.
>
> Foram tratados:
>
> ```text
> EmailJaCadastradoException
> InteressadoNaoEncontradoException
> MethodArgumentNotValidException
> HttpMessageNotReadableException
> ```
>
> Dessa forma, o Controller não precisa possuir blocos repetidos de tratamento de erro.
>
> ---
>
> # 31. Padronização das respostas de erro
>
> Foram criados DTOs específicos:
>
> ```text
> ErroResponse
> ErroValidacaoResponse
> ```
>
> Um erro comum possui:
>
> ```json
> {
>   "status": 404,
>   "codigo": "INTERESSADO_NAO_ENCONTRADO",
>   "mensagem": "Interessado não encontrado."
> }
> ```
>
> Já erros de validação possuem uma lista:
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
> Isso cria um contrato mais previsível para o futuro frontend Angular.
>
> ---
>
> # 32. Testes automatizados
>
> Foram criados testes para o Repository e Service.
>
> O teste do Repository verifica:
>
> ```text
> existsByEmail()
> ```
>
> O teste do Service cobre:
>
> ```text
> Cadastro
> E-mail duplicado
> Atualização
> Atualização com e-mail duplicado
> Exclusão
> Exclusão de interessado inexistente
> ```
>
> Os testes utilizam:
>
> ```java
> @SpringBootTest
> @Transactional
> ```
>
> O `@Transactional` nos testes permite que as alterações realizadas durante cada teste sejam isoladas e revertidas ao final da transação.
>
> ---
>
> # 33. Testes HTTP manuais
>
> Além dos testes automatizados, o comportamento da API foi validado através de requisições HTTP reais.
>
> Foram testados:
>
> ```text
> POST /api/interessados
> GET /api/interessados
> GET /api/interessados/{id}
> PUT /api/interessados/{id}
> DELETE /api/interessados/{id}
> ```
>
> Também foram testadas situações inválidas:
>
> * nome vazio;
> * nome acima do limite;
> * e-mail vazio;
> * e-mail inválido;
> * e-mail acima do limite;
> * telefone vazio;
> * telefone acima do limite;
> * data ausente;
> * data futura;
> * nível ausente;
> * estilo ausente;
> * observações acima do limite;
> * e-mail duplicado;
> * interessado inexistente.
>
> ---
>
> # 34. Um detalhe importante com PowerShell e UTF-8
>
> Durante os testes HTTP utilizando `Invoke-RestMethod`, alguns JSONs contendo caracteres acentuados apresentaram problemas de codificação:
>
> ```text
> Invalid UTF-8 middle byte
> ```
>
> Isso não representava um erro da regra de negócio da API.
>
> Era um problema relacionado à forma como o JSON estava sendo enviado pelo ambiente de teste.
>
> Para evitar esse ruído durante determinadas validações, foram utilizados valores ASCII, como:
>
> ```text
> Joao
> validacao
> ```
>
> ### Aprendizado
>
> Um erro apresentado durante um teste nem sempre pertence à aplicação que está sendo testada.
>
> É necessário identificar em qual camada o problema ocorreu:
>
> ```text
> PowerShell
>     ↓
> HTTP
>     ↓
> Spring Boot
>     ↓
> Java
>     ↓
> JPA
>     ↓
> MySQL
> ```
>
> ---
>
> # 35. Organização dos testes por pacote
>
> Durante a integração do CRUD com a branch `develop`, surgiu um erro:
>
> ```text
> NoClassDefFoundError: InteressadoService
> ```
>
> O relatório do Maven indicava um pacote diferente do esperado.
>
> O problema não estava no código do teste, mas na organização física do arquivo.
>
> O teste estava inicialmente em:
>
> ```text
> src/test/java/br/com/projetodanca/InteressadoServiceTest.java
> ```
>
> enquanto o pacote declarado era:
>
> ```java
> package br.com.projetodanca.service;
> ```
>
> A estrutura foi corrigida para:
>
> ```text
> src/test/java/br/com/projetodanca/service/InteressadoServiceTest.java
> ```
>
> utilizando:
>
> ```powershell
> git mv src/test/java/br/com/projetodanca/InteressadoServiceTest.java src/test/java/br/com/projetodanca/service/InteressadoServiceTest.java
> ```
>
> Depois da correção:
>
> ```powershell
> .\mvnw.cmd clean test
> ```
>
> retornou:
>
> ```text
> BUILD SUCCESS
> ```
>
> ### Aprendizado
>
> Em Java, existe uma relação importante entre:
>
> ```text
> package
> ```
>
> e:
>
> ```text
> estrutura de diretórios
> ```
>
> Manter essa correspondência ajuda ferramentas como Maven, compilador e IDE a localizar corretamente as classes.
>
> ---
>
> # 36. Git: feature → develop → main
>
> O desenvolvimento do CRUD foi realizado em uma branch específica:
>
> ```text
> feature/crud-interessados
> ```
>
> Essa branch recebeu as implementações do CRUD e suas validações.
>
> Depois, a feature foi integrada à `develop`:
>
> ```text
> feature/crud-interessados
>          ↓
>       develop
> ```
>
> A integração utilizou:
>
> ```powershell
> git merge --no-ff feature/crud-interessados
> ```
>
> O `--no-ff` foi utilizado para preservar explicitamente o ponto de integração da feature no histórico.
>
> ---
>
> # 37. Correção após a integração
>
> Após o merge da feature, os testes da `develop` revelaram o problema de organização do teste do Service.
>
> Isso é importante porque demonstra o propósito da branch `develop`:
>
> > **integrar alterações e descobrir problemas antes da versão estável.**
>
> A correção foi feita, testada e registrada em um commit específico:
>
> ```text
> eeeedae fix: corrige pacote do teste de service
> ```
>
> Depois:
>
> ```powershell
> .\mvnw.cmd clean test
> ```
>
> retornou:
>
> ```text
> BUILD SUCCESS
> ```
>
> ---
>
> # 38. Integração da develop na main
>
> Depois que a `develop` ficou estável e os testes passaram, ela foi integrada à `main`.
>
> O comando utilizado foi:
>
> ```powershell
> git merge --no-ff develop
> ```
>
> O resultado foi o commit de merge:
>
> ```text
> ace78ea Merge branch 'develop'
> ```
>
> A `main` terminou limpa, sem alterações pendentes.
>
> O histórico final demonstrou:
>
> ```text
> feature/crud-interessados
>          ↓
>       develop
>          ↓
>        main
> ```
>
> ### Aprendizado
>
> Esse fluxo mostra uma prática importante de Git:
>
> ```text
> feature
>    ↓
> implementação isolada
>    ↓
> develop
>    ↓
> integração e testes
>    ↓
> main
>    ↓
> versão estável
> ```
>
> O GitHub e Pull Requests ainda não foram utilizados porque o repositório remoto ainda não está configurado neste ambiente.
>
> ---
>
> # 39. Validação final do Backend
>
> Ao final da etapa, foi executado:
>
> ```powershell
> .\mvnw.cmd clean test
> ```
>
> com resultado:
>
> ```text
> BUILD SUCCESS
> ```
>
> Isso representa a validação automatizada do backend no estado atual.
>
> Além disso, os endpoints foram validados manualmente através de HTTP.
>
> Portanto, o backend atualmente possui:
>
> ```text
> Entity
>     ↓
> Repository
>     ↓
> Service
>     ↓
> Controller
>     ↓
> REST API
> ```
>
> com:
>
> ```text
> CRUD
> + DTOs
> + Bean Validation
> + Regras de negócio
> + Tratamento de exceções
> + Testes
> + Ordenação
> ```
>
> ---
>
> # 40. Estado atual do aprendizado
>
> Além dos conceitos da fundação, agora foram trabalhados:
>
> > * Java e Spring Boot;
> > * Maven Wrapper;
> > * Spring Web;
> > * Spring Data JPA;
> > * Entity;
> > * Repository;
> > * Service;
> > * Controller;
> > * DTOs;
> > * Records;
> > * Bean Validation;
> > * `@Valid`;
> > * `@NotBlank`;
> > * `@NotNull`;
> > * `@Email`;
> > * `@Past`;
> > * `@Size`;
> > * Enums;
> > * `@RestControllerAdvice`;
> > * tratamento global de exceções;
> > * códigos HTTP;
> > * `ResponseEntity`;
> > * regras de negócio;
> > * Spring Data derived queries;
> > * ordenação com `OrderBy`;
> > * transações;
> > * `EntityManager`;
> > * `refresh()`;
> > * geração de valores pelo banco;
> > * testes com Spring Boot;
> > * testes de Repository;
> > * testes de Service;
> > * testes HTTP;
> > * organização de pacotes Java;
> > * `git branch`;
> > * `git merge`;
> > * `git merge --no-ff`;
> > * integração `feature → develop → main`.
>
> ---
>
> # 41. Próxima etapa
>
> O backend CRUD está concluído e validado.
>
> A próxima etapa será avançar para o que ainda falta no backend antes da integração com o frontend, seguindo a ordem planejada:
>
> ```text
> Swagger/OpenAPI
>       ↓
> CORS
>       ↓
> Angular
>       ↓
> Service HTTP
>       ↓
> Formulário
>       ↓
> Listagem
>       ↓
> Edição
>       ↓
> Exclusão
> ```
>
> A documentação será novamente atualizada ao final de uma seção significativa, mantendo o ciclo:
>
> ```text
> Implementar
>     ↓
> Testar
>     ↓
> Entender
>     ↓
> Documentar
>     ↓
> Commit
> ```
>
> > **O objetivo continua sendo construir o projeto e, ao mesmo tempo, compreender tecnicamente cada decisão tomada.**
