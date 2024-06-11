import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConstructorConstructorTest {
  @Test
  public void testConstructorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    assertNotNull(constructorConstructor);
  }
}