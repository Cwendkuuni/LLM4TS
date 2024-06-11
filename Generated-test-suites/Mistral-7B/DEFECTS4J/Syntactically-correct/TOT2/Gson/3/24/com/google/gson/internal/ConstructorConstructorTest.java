import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

public class ConstructorConstructorTest {
  @Test
  public void testGetInstance() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> constructor = constructorConstructor.get(stringTypeToken);

    String instance = constructor.construct();
    assertEquals("", instance);

    Type rawType = String.class;
    ObjectConstructor<String> constructorForRawType = constructorConstructor.get(new TypeToken<String>() {}.getType());

    String instance2 = constructorForRawType.construct();
    assertEquals("", instance2);

    TypeToken<Integer[][]> array2DIntTypeToken = new TypeToken<Integer[][]>() {};
    constructor = constructorConstructor.get(array2DIntTypeToken);

    Integer[][] instance2D = constructor.construct();
    assertEquals(0, instance2D.length);
    assertEquals(0, instance2D[0].length);

    // Edge cases
    Map<Type, InstanceCreator<?>> emptyInstanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorWithEmptyInstanceCreators = new ConstructorConstructor(emptyInstanceCreators);

    TypeToken<Object> objectTypeToken = new TypeToken<Object>() {};
    assertNotNull(constructorConstructor.get(objectTypeToken));

    TypeToken<String> nullTypeToken = null;
    assertNull(constructorConstructor.get(nullTypeToken));

    TypeToken<Object> nonExistentTypeToken = new TypeToken<Object>() {};
    assertNull(constructorConstructor.get(nonExistentTypeToken));

    Type rawInterfaceType = Map.class;
    assertNull(constructorConstructor.get(new TypeToken<Map<Object, Object>>() {}.getType()));

    Type invalidTypeToken = new TypeToken<Object>() {};
    assertNull(constructorConstructor.get(invalidTypeToken));

    Type nonExistentRawType = new TypeToken<Object>() {}.getType();
    assertNull(constructorConstructor.get(nonExistentRawType));

    TypeList<Object> typeList = new TypeList<Object>() {};
    assertNull(constructorConstructor.get(typeList.getType()));

    TypeMap<Object, Object> mapType = new TypeMap<Object, Object>() {};
    assertNull(constructorConstructor.get(mapType.getType()));

    Type enumSetType = EnumSet.class;
    assertNull(constructorConstructor.get(new TypeToken<EnumSet<Object>>() {}.getType()));

    Type setType = Set.class;
    assertNull(constructorConstructor.get(new TypeToken<SortedSet<Object>>() {}.getType()));

    Type queueType = Queue.class;
    assertNull(constructorConstructor.get(new TypeToken<Queue<Object>>() {}.getType()));
  }
}