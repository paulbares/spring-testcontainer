package me.paulbares;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.util.Arrays;
import java.util.List;

public class JdbcDbContainerContextCustomizer implements ContextCustomizer {

  private final JdbcDatabaseContainer container;

  public JdbcDbContainerContextCustomizer(JdbcDatabaseContainer container) {
    this.container = container;
  }

  @Override
  public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
    TestPropertyValues.of(getProperties()).applyTo(context.getEnvironment());
  }

  protected List<String> getProperties() {
    return Arrays.asList(
            "spring.datasource.url=" + container.getJdbcUrl(),
            "spring.datasource.username=" + container.getUsername(),
            "spring.datasource.password=" + container.getPassword(),
            "spring.datasource.driver-class-name=" + container.getDriverClassName(),
            "spring.jpa.hibernate.ddl-auto=update"
    );
  }
}
