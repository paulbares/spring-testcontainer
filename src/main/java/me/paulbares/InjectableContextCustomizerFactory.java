package me.paulbares;

import org.junit.platform.commons.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.test.context.MergedContextConfiguration;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.junit.jupiter.Container;

import java.lang.reflect.Field;
import java.util.List;

public class InjectableContextCustomizerFactory implements ContextCustomizerFactory {

  private static final Logger logger = LoggerFactory.getLogger(InjectableContextCustomizerFactory.class);

  @Override
  public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
    return new InjectableCustomizerAdapter(testClass);
  }

  public static class InjectableCustomizerAdapter implements ContextCustomizer {

    private final Class<?> testClass;

    public InjectableCustomizerAdapter(Class<?> testClass) {
      this.testClass = testClass;
    }

    @Override
    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
      List<Field> fields = ReflectionUtils.findFields(testClass,
              f -> ReflectionUtils.isStatic(f) && f.isAnnotationPresent(Container.class),
              ReflectionUtils.HierarchyTraversalMode.TOP_DOWN);
      if (fields.isEmpty()) {
        logger.debug("Cannot find a static field annotated with " + Container.class + " in class " + this.testClass);

        if (this.testClass.isAnnotationPresent(SpringContainerTestDatabase.class)) {
          // Likely a config problem.
          throw new RuntimeException("It seems you are trying to use " + SpringContainerTestDatabase.class.getSimpleName()
                  + " without adding a " + JdbcDatabaseContainer.class.getSimpleName()
                  + " static field annotated with "
                  + Container.class.getSimpleName() + ". You should check your configuration.");
        }
        return;
      } else if (fields.size() > 1) {
        throw new RuntimeException("Expected only 1 static field annotated with "
                + Container.class + " in class " + this.testClass);
      }

      try {
        Field field = fields.get(0);
        field.setAccessible(true);
        JdbcDbContainerContextCustomizer customizer = new JdbcDbContainerContextCustomizer((JdbcDatabaseContainer) field.get(null));
        customizer.customizeContext(context, mergedConfig);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
