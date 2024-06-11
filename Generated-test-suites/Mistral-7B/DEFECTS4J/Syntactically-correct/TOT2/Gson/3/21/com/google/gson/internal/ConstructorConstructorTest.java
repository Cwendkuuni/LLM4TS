import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConstructorConstructorTest {

  @Test
  public void testGetInstance() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringTypeToken);
    assertNotNull(stringConstructor);
    assertEquals("", stringConstructor.construct());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceNotFound() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
    constructorConstructor.get(intTypeToken); // should throw RuntimeException
  }

  @Test
  public void testGetInstanceInstanceCreatorPresent() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> stringInstanceCreator = new InstanceCreator<String>() {
      @Override public String createInstance(Type type) {
        return "test";
      }
    };
    instanceCreators.put(new TypeToken<String>() {}.getType(), stringInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringTypeToken);
    assertNotNull(stringConstructor);
    assertEquals("test", stringConstructor.construct());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceRawTypePresentButNotInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> stringInstanceCreator = new InstanceCreator<String>() {
      @Override public String createInstance(Type type) {
        return "test";
      }
    };
    instanceCreators.put(String.class, stringInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Object> objectTypeToken = new TypeToken<Object>() {};
    constructorConstructor.get(objectTypeToken); // should throw RuntimeException
  }

  @Test
  public void testGetInstanceDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Type token = new TypeToken<Integer>() {}.getType();
    ObjectConstructor<Integer> integerConstructor = constructorConstructor.get(token);
    assertEquals(0, integerConstructor.construct());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceNullInstanceCreatorMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringTypeToken);
    assertNull(stringConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceUnsupportedType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Type type = new TypeToken<Object>() {}.getType();
    constructorConstructor.get(type); // should throw RuntimeException
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceUnsupportedRawType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Class<String> rawType = String.class;
    constructorConstructor.get(new TypeToken<String>() {}.getType()); // should throw RuntimeException
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceNoConstructorFound() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Type type = new TypeToken<Object>() {}.getType();
    constructorConstructor.get(type); // should throw RuntimeException
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceUnsafeAllocatorException() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Type type = new TypeToken<Object>() {}.getType();
    constructorConstructor.newUnsafeAllocator(type, Object.class).construct(); // should throw RuntimeException
  }
}