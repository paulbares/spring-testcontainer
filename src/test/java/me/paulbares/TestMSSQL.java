package me.paulbares;

import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringContainerTestDatabase
class TestMSSQL extends CustomerBaseTest {

  @Container
  static MSSQLServerContainer container = new MSSQLServerContainer("mcr.microsoft.com/mssql/server").acceptLicense();

  @Override
  protected String getDbName() {
    return "sqlserver";
  }
}
