package me.paulbares;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringContainerTestDatabase
class TestPostgresSQL extends CustomerBaseTest {

  @Container
  static PostgreSQLContainer container = new PostgreSQLContainer("postgres");

  @Override
  protected String getDbName() {
    return "postgres";
  }
}
