# Introduction
spring-testcontainer provides integration between the Spring (Data JPA) testing framework and Testcontainer library to 
easily test your application with different containerized database instances. 

# Configuration
The simplest way to start is to use the annotation `@SpringContainerTestDatabase` on your test class and add a static 
container attribute that exposes a JDBC connection:
```java
@Container
static MySQLContainer container = new MySQLContainer("mysql");
```
and another one that will setup the Spring context:
```java
@AnnotationContextCustomizer
static ContextCustomizer customizer = new JdbcDbContainerContextCustomizer(container);
```
The lifecycle of the container is managed by the Testcontainer library itself. 

Don't forget to add as dependencies the Testcontainers library of the db you want to use and the associated JDBC driver library, 
for instance for MySQL:
```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <version>1.15.0</version>
    <scope>test</scope>
</dependency>
```
Driver dependency:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.22</version>
</dependency>
```
If you want to customize the properties datasource, you can extend and override `JdbcDbContainerContextCustomizer#getProperties`.

# Example

```java
@Transactional
@SpringContainerTestDatabase
class TestMySQL {

  @Container
  static MySQLContainer container = new MySQLContainer("mysql");

  @AnnotationContextCustomizer
  static ContextCustomizer customizer = new JdbcDbContainerContextCustomizer(container);

  @Autowired
  CustomerRepository customerRepository;

  @Test
  void test() {
    Customer c = new Customer();
    c.setFirstname("admin");
    c.setLastname("admin");
    this.customerRepository.save(c);
    Assertions.assertEquals(1, this.customerRepository.findAll().size()); // Whatever...
  }
}
```
# Maven
In your pom.xml:
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/paulbares/spring-testcontainer</url>
    </repository>
</repositories>
```

Due to Github packages limitation (you need to authenticate to download both public and private packages), add this to your settings.xml:
```xml
<servers>
    <server>
        <id>github</id>
        <username>USERNAME</username>
        <password>TOKEN</password>
    </server>
</servers>
```
replacing `USERNAME` with your GitHub username, and `TOKEN` with your personal access token. [More here](https://docs.github.com/en/free-pro-team@latest/packages/guides/configuring-apache-maven-for-use-with-github-packages#authenticating-to-github-packages).

`servers.server.id` must be the same value than `repositories.repository.id`.
# Important
Per test class, only one field annotated with `@Container` is expected and this field must be static meaning only one docker 
container will be started for all the tests within this class. 
DON'T FORGET to add a `ContextCustomizer` to your class otherwise your datasource will be misconfigured and Spring will fall
back to an embedded in-memory H2 database.

# FAQ
- How can I check my test is correctly setup?
You can verify in your test the connection url with the datasource:
```java
@Autowired
DataSource dataSource;
...
String url = this.dataSource.getConnection().getMetaData().getURL();  // Value is jdbc:mysql://localhost:33349/test for a mysql db.
```
