
import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {
  @Test
  public void testConstructorWithValidArguments() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<String> stringConstructor = constructor.get(new TypeToken<String>() {});
    assertNotNull(stringConstructor);
  }

  @Test(expected = NullPointerException.class)
  public void testGetWithNullTypeToken() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
    constructor.get(null);
  }

  @Test
  public void testNewDefaultConstructorWithValidArguments() {
    Class<String> stringClass = String.class;
    ObjectConstructor<String> stringConstructor = new ConstructorConstructor(new HashMap<>())
        .newDefaultConstructor(stringClass);
    String instance = stringConstructor.construct();
    assertEquals("", instance);
  }

  @Test(expected = NullPointerException.class)
  public void testNewDefaultConstructorWithNullRawType() {
    new ConstructorConstructor(new HashMap<>()).newDefaultConstructor(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewDefaultImplementationConstructorWithInvalidTypeOrRawType() {
    new ConstructorConstructor(new HashMap<>())
        .newDefaultImplementationConstructor(new TypeToken<Object>() {}.getType(), String.class);
  }

  @Test
  public void testNewDefaultImplementationConstructorWithValidArguments() {
    ObjectConstructor<ArrayList<String>> arrayListConstructor = new ConstructorConstructor(new HashMap<>())
        .newDefaultImplementationConstructor(new TypeToken<ArrayList<String>>() {}.getType(), ArrayList.class);
    ArrayList<String> instance = arrayListConstructor.construct();
    assertTrue(instance instanceof ArrayList);
    assertEquals(0, instance.size());
  }

  @Test
  public void testNewUnsafeAllocatorWithValidArguments() {
    Class<String> stringClass = String.class;
    ObjectConstructor<String> stringConstructor = new ConstructorConstructor(new HashMap<>())
        .newUnsafeAllocator(new TypeToken<String>() {}.getType(), String.class);
    String instance = stringConstructor.construct();
    assertEquals("", instance);
  }

  @Test(expected = NullPointerException.class)
  public void testNewUnsafeAllocatorWithNullType() {
    new ConstructorConstructor(new HashMap<>())
        .newUnsafeAllocator(null, String.class);
  }

  @Test(expected = NullPointerException.class)
  public void testNewUnsafeAllocatorWithNullRawType() {
    new ConstructorConstructor(new HashMap<>())
        .newUnsafeAllocator(new TypeToken<Object>() {}.getType(), null);
  }

  @Test(expected = ClassCastException.class)
  public void testNewUnsafeAllocatorWithClassCastException() {
    Class<?> wrongType = Integer.TYPE;
    ObjectConstructor<String> stringConstructor = new ConstructorConstructor(new HashMap<>())
        .newUnsafeAllocator(new TypeToken<String>() {}.getType(), wrongType.getClass());
    assertNull(stringConstructor.construct());
  }
}