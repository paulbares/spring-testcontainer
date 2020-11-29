package me.paulbares;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Nothing here. Tests will use an embedded in-memory db. See {@link org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest}.
 */
@DataJpaTest
class TestH2 extends CustomerBaseTest {

  @Override
  protected String getDbName() {
    return "h2";
  }
}
