package me.paulbares;

import org.springframework.test.context.ContextCustomizer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringContainerTestDatabase
class TestMSSQL extends CustomerBaseTest {

  @Container
  static MSSQLServerContainer container = new MSSQLServerContainer("mcr.microsoft.com/mssql/server").acceptLicense();

  @AnnotationContextCustomizer
  static ContextCustomizer customizer = new JdbcDbContainerContextCustomizer(container);

  @Override
  protected String getDbName() {
    return "sqlserver";
  }
}
