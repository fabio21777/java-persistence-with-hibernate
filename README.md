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

## 2.0 Estrategias de mapeamento

Neste capítulo, colocamos a mão na massa e começamos a usar o Hibernate para mapear nossas entidades e trabalhar com elas. Como meu foco é entender melhor o Hibernate para trabalhar com o Spring Boot, algumas coisas foram simplificadas, mas o comportamento continua o mesmo!

## Mapeando Entidades

* `@Entity`: Anotação para identificar que uma classe é uma entidade no banco de dados.
* `@Id`: Mapeando uma coluna que é o identificador único da tabela.
* `@GeneratedValue`: Geração do ID.

## Buscando e Atualizando Dados Gerenciando a Nossa Própria Sessão

* **EntityManager**:
   O `EntityManager` é uma interface fundamental no Java Persistence API (JPA), que é uma especificação padrão de Java para mapeamento objeto-relacional e gerenciamento de persistência. O `EntityManager` é responsável por gerenciar o ciclo de vida das entidades, o que inclui operações de persistência básicas como criar, ler, atualizar e excluir (CRUD), bem como outras operações de gerenciamento de transações e persistência.

``` java
    @Transactional
    public void save(BaseModel entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    @Transactional(readOnly = true)
    public List<Menssage> findAllMenssage() {
        return entityManager.createQuery("select m from Menssage m", Menssage.class).getResultList();
    }

```

## 3 modelo de dominio e metadados

### 3.1 Arquirtetura em camadas

* Presentation layer - Camada responsavel por envia  os dados para a apresentação, isso é nossas camada de controladores do spring
* Business layer - camada que cotêm as regras de negocio, pode ser visto como os services no spring-boot
* Persistence layer - pode ser nossos daos/ repositores, camada de persistencia de dados
* Database - no spring-boot normalmente esse cara é configuração do proprities
* Helper and utility classes - class auxiliares como constantes, exceções e outros .

![camadas](./imgs/layer.png)

### 3.2 - Implementando o modelo de domínio

Esta seção oferece uma visão mais detalhada sobre as classes e seu comportamento com o Hibernate, como associações:

* Metadados baseados em anotações - como `org.hibernate.annotations.Cache`, ou GLOBAL ANNOTATION METADATA, `@Future`
* Aplicando regras de validação Bean - `@NotNull` `@Size`
* A persistência transparente é importante se você deseja executar e testar seus objetos de negócios de maneira independente e fácil.
* Melhores práticas e requisitos para o modelo de programação POJO (Plain Old Java Object) e entidade JPA, e quais conceitos eles têm em comum com a antiga especificação JavaBean.
* Mapeamentos mais complexos, possivelmente com uma combinação de anotações JDK ou arquivos de mapeamento JPA/Hibernate XML.

```java
public class Bid {
   //associação
    protected Item item;

    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }

    public void addBid(Bid bid) {
        if (bid == null) throw new NullPointerException("Can't add null Bid"); // defensive
        if (bid.getItem() != null) throw new IllegalStateException("Bid is already assigned to an Item") // defensive;
        getBids().add(bid);
        bid.setItem(this);
    }
}

public class Item {
    protected Set<Bid> bids = new HashSet<Bid>();
    public Set<Bid> getBids() {
        return bids;
    }
    //associação
    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }
}
```
