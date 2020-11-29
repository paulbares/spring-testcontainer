package me.paulbares;

import org.springframework.test.context.ContextCustomizer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringContainerTestDatabase
class TestPostgresSQL extends CustomerBaseTest {

  @Container
  static PostgreSQLContainer container = new PostgreSQLContainer("postgres");

  @AnnotationContextCustomizer
  static ContextCustomizer customizer = new JdbcDbContainerContextCustomizer(container);

  @Override
  protected String getDbName() {
    return "postgres";
  }
}
