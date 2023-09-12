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

The lifecycle of the container is managed by the Testcontainer library itself. 

Edit or create the file `spring.factories` to add:
```
org.springframework.test.context.ContextCustomizerFactory=\
me.paulbares.InjectableContextCustomizerFactory
```

The `InjectableContextCustomizerFactory` is responsible to set the appropriate jdbc properties in the Spring context. 

Don't forget to add as dependencies the Testcontainers library of the db you want to use and the associated JDBC driver library, 
for instance for MySQL:
```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <version>1.19.0</version>
    <scope>test</scope>
</dependency>
```
Driver dependency:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
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

To add a dependency on spring-testcontainer using Maven, use the following:
```xml
<dependency>
    <groupId>me.paulbares</groupId>
    <artifactId>spring-testcontainer</artifactId>
    <version>1.1.0</version>
</dependency>
```
# Important
Per test class, only one field annotated with `@Container` is expected and this field must be static meaning only one docker 
container will be started for all the tests within this class. 

# FAQ
- How can I check my test is correctly setup?
You can verify in your test the connection url with the datasource:
```java
@Autowired
DataSource dataSource;
...
String url = this.dataSource.getConnection().getMetaData().getURL();  // Value is jdbc:mysql://localhost:33349/test for a mysql db.
```
