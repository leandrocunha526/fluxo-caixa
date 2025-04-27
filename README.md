# Lançamentos API - Spring Boot

## Requerimentos

Desenvolvido com:

- MariaDB (mas possui possibilidade de conectar com MySQL apenas alterando jdbc:mariadb://localhost:3306 para jdbc:mysql://localhost:3306)
- Java JDK 21 (LTS)
- Maven (MVN)
- Docker
- IDE Intellij IDEA Ultimate
- ORM: JPA (Java Persistence API)
- Framework: Spring Boot 3.4.5.
- Padrão de projeto: MVC.

Sendo organizado com:  
Model → representa as entidades de negócio, por exemplo a classe Lancamento.java.  
Repository → faz a comunicação com o banco de dados.  
Service → contém a lógica de negócio (processamento, cálculos, regras).  
Controller → expõe os endpoints HTTP (API REST).  
DTO → classes para transportar dados (SaldoDiarioDTO.java).  

## Execução

```shell
mvn spring-boot:run
```

ou

```shell
./mvnw spring-boot:run
```

Usando Maven

### Com Docker

```shell
docker compoose -f compose.yml up
```

### Testes

```shell
mvn test
```

ou

```shell
./mvnw test
```

| **Teste**                    | **Objetivo**                                | **Descrição**                                                                                            |
|:-----------------------------|:--------------------------------------------|:---------------------------------------------------------------------------------------------------------|
| `testCriarLancamentos`       | Testar a criação de lançamentos.            | Verifica se a API cria corretamente diferentes lançamentos (crédito e débito) via `POST /api/criar`.     |
| `testConsultarSaldo`         | Testar a consulta de saldo diário.          | Verifica se a API retorna o saldo consolidado diário corretamente via `GET /api/saldo`.                  |
| `testPesquisarPorData`       | Testar a pesquisa de lançamentos por data.  | Verifica se a API retorna lançamentos de uma data específica via `GET /api/lancamentos/data/{data}`.     |
| `testListarTodosLancamentos` | Testar a listagem de todos os lançamentos.  | Verifica se a API retorna todos os lançamentos cadastrados via `GET /api/lancamentos`.                   |
| `testBuscarLancamentoPorId`  | Testar a busca de lançamento por ID.        | Verifica se a API retorna corretamente um lançamento específico pelo ID via `GET /api/lancamentos/{id}`. |
| `testEditarLancamento`       | Testar a edição de um lançamento existente. | Verifica se a API atualiza um lançamento existente corretamente via `PUT /api/lancamentos/{id}`.         |
| `testRemoverLancamento`      | Testar a remoção de um lançamento.          | Verifica se a API remove corretamente um lançamento via `DELETE /api/delete/{id}`.                       |

## Docs

Você pode conferir as documentações das rotas e realizar requisições em http://localhost:8080/swagger-ui/index.html.

## Abordagens

A justificativa de escolher o tipo que escolhi para valores em dinheiro ou atributo denominado como `valor`.  
Veja [BigDecimal and BigInteger in Java By Baeldung](https://www.baeldung.com/java-bigdecimal-biginteger)

Ao lidar com valores monetários em linguagem de programação como o Java, é essencial garantir a precisão nas operações aritméticas e no armazenamento de valores, evitando problemas de arredondamento e perda de casas decimais.
O tipo BigDecimal foi escolhido para o campo valor justamente por oferecer alta precisão no tratamento de números decimais, o que é indispensável para operações financeiras. Conforme demonstrado no artigo BigDecimal and BigInteger in Java by Baeldung, BigDecimal permite controlar exatamente o número de casas decimais e o modo de arredondamento.
Dessa forma, ao inserir um valor como `R$ 24,784917`, o sistema poderá aplicar uma estratégia de formatação ou arredondamento adequada, resultando, por exemplo, no valor R$ 24,78 com duas casas decimais. Isso impede que valores financeiros sejam registrados incorretamente, preservando a confiabilidade e a integridade dos dados.

Portanto, o uso de BigDecimal assegura:

- Precisão nas operações de soma, subtração, multiplicação e divisão.
- Controle explícito do número de casas decimais.
- Arredondamento seguro e previsível para o contexto financeiro.

### Routes

| Método | URL                             | Ação                              |
|--------|---------------------------------|-----------------------------------|
| POST   | /api/criar                      | Inserir um novo lançamento        |
| PUT    | /api/lancamento/{id}            | Atualizar um lançamento existente |
| GET    | /api/lancamentos                | Listar todos os lançamentos       |
| GET    | /api/lancamento/{id}            | Buscar um lançamento pelo ID      |
| DELETE | /api/delete/{id}                | Deletar um lançamento             |
| GET    | /api/lancamento?data=2025-04-23 | Buscar lançamentos por data       |
| GET    | /api/saldo                      | Mostra o saldo diário             |
