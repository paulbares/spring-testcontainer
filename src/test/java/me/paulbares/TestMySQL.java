package me.paulbares;

import me.paulbares.domain.Customer;
import me.paulbares.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

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