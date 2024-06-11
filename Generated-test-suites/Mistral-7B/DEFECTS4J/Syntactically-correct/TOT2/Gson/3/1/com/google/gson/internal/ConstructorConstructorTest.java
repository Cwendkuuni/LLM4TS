import static org.junit.Assert.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeToken;
import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testConstructorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> stringCreator = new StringCreator();
    instanceCreators.put(String.class, stringCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertNotNull(constructorConstructor);
  }

  static class StringCreator implements InstanceCreator<String> {

    @Override
    public String createInstance(Type type) {
      return "Test String";
    }
  }
}