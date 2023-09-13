package me.paulbares;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringContainerTestDatabase
class TestMySQL extends CustomerBaseTest {

  @Container
  static MySQLContainer container = new MySQLContainer("mysql");

  @Override
  protected String getDbName() {
    return "mysql";
  }
}