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

## 4 - Mapeamento e Persistência das Classes

Nos modelos conceituais existem duas formas de mapeamentos, podemos entender que no caso de duas pessoas morarem na mesma casa existe apenas 1 endereço vinculado, ou um mapeamento em que um endereço está estritamente ligado a um usuário havendo 1 endereço para cada usuário.

* Você pode recuperar uma instância de tipo de entidade usando sua identidade persistente: por exemplo, uma instância de Usuário, Item ou Categoria. 
* Uma instância de tipo de valor não tem propriedade de identificador persistente; pertence a uma instância de entidade. Sua vida útil está vinculada à instância de entidade proprietária.

![4.1](./imgs/Figure%204.1.png)

![4.2](imgs/4.2.png)
O diagrama do seu modelo de domínio e implemente POJOs para todas as entidades e tipos de valor. Você terá que cuidar de três coisas:

* Referências compartilhadas - Evite referências compartilhadas para instâncias de tipo de valor quando escrever suas classes POJO. Por exemplo, certifique-se de que apenas um Usuário pode referenciar um Endereço. Você pode tornar o Endereço imutável sem um método público `setUser()` e impor o relacionamento com um construtor público que tem um argumento de Usuário. Claro, você ainda precisa de um construtor sem argumentos, provavelmente protegido, como discutimos no capítulo anterior, para que o Hibernate também possa criar uma instância.

* Dependências de ciclo de vida - Se um Usuário for excluído, sua dependência de Endereço também deve ser excluída. Os metadados de persistência incluirão as regras de cascata para todas essas dependências, para que o Hibernate (ou o banco de dados) possa cuidar da remoção do Endereço obsoleto. Você deve projetar seus procedimentos de aplicativo e interface de usuário para respeitar e esperar tais dependências - escreva seus POJOs de modelo de domínio de acordo.

* Identidade - As classes de entidade precisam de uma propriedade de identificador em quase todos os casos. As classes de tipo de valor (e, claro, classes JDK como String e Integer) não têm uma propriedade de identificador, porque as instâncias são identificadas através da entidade proprietária.
  
![4.3](./imgs/4.3.png)

A persistência complica esse cenário. Com a persistência objeto/relacional, uma instância persistente é uma representação em memória de uma determinada linha (ou linhas) de uma tabela de banco de dados (ou tabelas). Juntamente com a identidade e igualdade Java, definimos a identidade do banco de dados. Agora você tem três métodos para distinguir referências:

* Objetos são idênticos se ocuparem o mesmo local de memória na JVM. Isso pode ser verificado com o operador `a == b`. Este conceito é conhecido como identidade de objeto.
* Objetos são iguais se tiverem o mesmo estado, conforme definido pelo método `a.equals(Object b)`. Classes que não sobrescrevem explicitamente este método herdam a implementação definida por `java.lang.Object`, que compara a identidade do objeto com `==`. Este conceito é conhecido como igualdade de objeto.
* Objetos armazenados em um banco de dados relacional são idênticos se compartilharem a mesma tabela e valor de chave primária. Este conceito, mapeado no espaço Java, é conhecido como identidade de banco de dados.

O identificador de banco de dados de uma entidade é mapeado para alguma chave primária de tabela, então vamos primeiro obter algum conhecimento sobre chaves primárias sem nos preocuparmos com mapeamentos. Dê um passo atrás e pense sobre como você identifica entidades.

Uma chave candidata é uma coluna ou conjunto de colunas que você poderia usar para identificar uma linha particular em uma tabela. Para se tornar a chave primária, uma chave candidata deve satisfazer os seguintes requisitos:

* O valor de qualquer coluna de chave candidata nunca é nulo. Você não pode identificar algo com dados que são desconhecidos, e não há nulos no modelo relacional. Alguns produtos SQL permitem definir chaves primárias (compostas) com colunas anuláveis, então você deve ter cuidado.
* O valor da(s) coluna(s) da chave candidata é um valor único para qualquer linha.
* O valor da(s) coluna(s) da chave candidata nunca muda; é imutável.

É possível mapear na própria anotação `@Entity(name = "message")` o nome da tabela.

Para desativar a geração de instruções INSERT e UPDATE na inicialização, você precisa de anotações nativas do Hibernate:
Ao habilitar inserções e atualizações dinâmicas, você diz ao Hibernate para produzir as strings SQL quando necessário, não antecipadamente. O UPDATE conterá apenas colunas com valores atualizados, e o INSERT conterá apenas colunas não nulas.
`DynamicInsert` e `DynamicUpdate`

Podemos marcar a entidade como imutável `Immutable`.
