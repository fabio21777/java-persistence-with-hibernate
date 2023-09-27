# Resumo do Livro "Java Persistence with Hibernate" (2ª Edição) por Christian Bauer, Gavin King, Gary Gregory

## 1 Entendendo objetos/relacional e persistência

### 1.1 O que é Persistência?

Persistência é o processo de salvar os dados de forma que os mesmos possam ser recuperados futuramente pela aplicação, mesmo em caso de desligamento da aplicação, ou seja, os dados são salvos fisicamente e não apenas em memória. Em Java, a persistência pode ser caracterizada pelos mapeamentos entre entidades, relações e objetos.

#### 1.1.1 Banco de Dados Relacional

É uma abordagem flexível e robusta para solução de banco de dados, além de ter diversas pesquisas que embasam e garantem aspectos como segurança e integridade das informações. Vale ressaltar que os bancos de dados relacionais têm uma importante característica chamada independência de dados, em que a base de dados não depende da aplicação, o que é um fato importante já que os dados normalmente são válidos bem mais tempo que a aplicação. Os bancos de dados relacionais não estão associados a uma linguagem de programação específica.

#### 1.1.2 Entendendo SQL

Esta seção basicamente é uma breve introdução a SQL, introduzindo conceitos como DDL, DML e suas principais cláusulas.

#### 1.1.3 Usando SQL no Java

Esta seção discute a interação entre aplicações Java e bancos de dados SQL através da API JDBC, destacando as tarefas de baixo nível envolvidas no acesso a dados e como isso pode ser tedioso para os engenheiros de aplicação. Ela questiona se o modelo de dados relacional e o SQL são as escolhas certas para a persistência em aplicações orientadas a objetos, respondendo afirmativamente devido à dominância e comprovada eficácia dos bancos de dados SQL na indústria. No entanto, reconhece que há cenários, especialmente em sistemas distribuídos em larga escala, que podem necessitar de abordagens diferentes para o gerenciamento de dados. A seção também aborda a ideia de trabalhar com um modelo de domínio orientado a objetos em uma aplicação Java, em vez de interagir diretamente com os dados tabulares de um banco de dados, para melhorar a reutilização e manutenção do código, especialmente em aplicações com lógica de negócios não trivial. Ela menciona o desafio do descompasso de paradigma entre a modelagem orientada a objetos e a modelagem relacional, e sugere uma exploração mais aprofundada desse descompasso para entender os problemas associados.

### 1.2 Descompasso de Paradigma

Iniciamos os estudos dos mapeamentos de entidades com seu modelo de dados.

```java
public class User {
    String username;
    String address;
    Set billingDetails;
// Accessor methods (getter/setter), business methods, etc.
}
public class BillingDetails {
    String account;
    String bankname;
    User user;
// Accessor methods (getter/setter), business methods, etc.
}
```

```sql
create table USERS (
    USERNAME varchar(15) not null primary key,
    ADDRESS varchar(255) not null
);
create table BILLINGDETAILS (
    ACCOUNT varchar(15) not null primary key,
    BANKNAME varchar(255) not null,
    USERNAME varchar(15) not null,
    foreign key (USERNAME) references USERS
);
 ```

#### 1.2.1 Problema com Granularidade

O descompasso entre o paradigma objeto/relacional é evidenciado em vários aspectos. Inicialmente, o endereço pode ser considerado uma string, no entanto, decidimos que a maneira mais prática é transformar o endereço em um objeto que contém os atributos relacionados ao endereço. A solução pragmática muitas vezes adotada é a de adicionar várias colunas de tipos de dados definidos pelo fornecedor na tabela USERS, o que reflete uma representação menos flexível e mais "achatada" dos dados, em contraste com o modelo orientado a objetos mais rico e granular. Este descompasso de granularidade é apenas um dos muitos desafios enfrentados ao tentar mapear entre o modelo de domínio orientado a objetos e o modelo relacional, e é um exemplo de como o descompasso entre os paradigmas objeto/relacional pode manifestar-se na prática.

#### 1.2.2 Problemas com Subtipos

Essa seção faz um breve discurso sobre herança e seu comportamento no banco de dados.

#### 1.2.3 Problemas com Objetos Idênticos

Essa seção nos traz insights sobre o comportamento de certos atributos que precisam ter sua validação em banco, como no caso do `USERNAME`. Além disso, idealmente, as entidades devem ter um identificador que normalmente precisa ser gerado pelo próprio sistema.

#### 1.2.4 Problemas Relacionados a Associações

Essa seção mostra como seriam os relacionamentos de associações entre as classes no modelo de dados.

#### 1.2.5 Problema Relacionado à Navegação de Dados

Essa seção exemplifica uma situação em que objetos podem ser gigantescos, causando problemas de memória. Uma forma de contornar esse problema é com lazy loading, assim podemos evitar o famoso problema de n+1 consultas e buscamos a lista de dados apenas se necessitarmos do mesmo.

### 1.3 ORM e JPA

Essa seção introduz o conceito de ORM em projetos Java, com alguns dos benefícios citados:

* Produtividade
* Manutenção
* Performance
* Independência do gerenciador do banco de dados

#### 1.3.1 JPA

* Uma facilidade para especificar metadados de mapeamento.
* APIs para realizar operações CRUD básicas em instâncias de classes persistentes, mais proeminentemente `javax.persistence`.
* Uma linguagem e APIs para especificar consultas que se referem a classes e propriedades de classes.
* Como o mecanismo de persistência interage com instâncias transacionais para realizar verificação de sujeira, busca de associação e outras funções de otimização.
