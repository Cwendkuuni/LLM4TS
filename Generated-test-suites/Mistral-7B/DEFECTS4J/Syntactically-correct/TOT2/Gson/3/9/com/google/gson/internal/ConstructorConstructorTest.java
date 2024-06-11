import static org.junit.Assert.*;

import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testConstructorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ConstructorConstructor expectedConstructorConstructor = new ConstructorConstructor(instanceCreators);

    assertEquals(expectedConstructorConstructor, constructorConstructor);
  }
}