package me.paulbares;

import me.paulbares.domain.Customer;
import me.paulbares.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;

@Transactional // annotation here because visibility of the tests (private)
abstract class CustomerBaseTest {

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  DataSource dataSource;

  @BeforeEach
  void before() throws SQLException {
    String url = this.dataSource.getConnection().getMetaData().getURL();
    String dbName = getDbName();
    Assertions.assertTrue(url.contains(dbName), url + " does not contain " + dbName);
  }

  @Test
  void test() {
    Customer c = new Customer();
    c.setFirstname("admin");
    c.setLastname("admin");
    this.customerRepository.save(c);
    Assertions.assertEquals(1, this.customerRepository.findAll().size()); // Whatever...
  }

  abstract protected String getDbName();
}
