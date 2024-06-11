import static org.junit.Assert.*;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceOfMap() {
    Map<String, Integer> map = new ConstructorConstructor(new HashMap<>())
        .get(new TypeToken<Map<String, Integer>>() {});
    assertInstanceOf(Map.class, map);
    assertInstanceOf(LinkedHashMap.class, map);
    assertEquals(0, map.size());
    map.put("key", 1);
    assertEquals(1, map.size());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfNonExistingType() {
    new ConstructorConstructor(Collections.emptyMap())
        .get(new TypeToken<Integer>() {});
  }

  @Test
  public void testGetInstanceOfEnumSet() {
    EnumSet<Color> colorEnumSet = new ConstructorConstructor(Collections.emptyMap())
        .get(new TypeToken<EnumSet<Color>>() {});
    assertInstanceOf(EnumSet.class, colorEnumSet);
    assertInstanceOf(LinkedHashSet.class, colorEnumSet);
    assertEquals(0, colorEnumSet.size());
    colorEnumSet.add(Color.RED);
    assertEquals(1, colorEnumSet.size());
  }

  @Test
  public void testGetInstanceOfArrayList() {
    ArrayList<Integer> list = new ConstructorConstructor(Collections.emptyMap())
        .get(new TypeToken<ArrayList<Integer>>() {});
    assertInstanceOf(ArrayList.class, list);
    assertEquals(0, list.size());
    list.add(1);
    assertEquals(1, list.size());
  }

  @Test
  public void testGetInstanceOfTreeMap() {
    SortedMap<String, Integer> sortedMap = new ConstructorConstructor(Collections.emptyMap())
        .get(new TypeToken<SortedMap<String, Integer>>() {});
    assertInstanceOf(SortedMap.class, sortedMap);
    assertInstanceOf(TreeMap.class, sortedMap);
    assertEquals(0, sortedMap.size());
    sortedMap.put("key", 1);
    assertEquals(1, sortedMap.size());
  }

  @Test
  public void testGetInstanceOfLinkedList() {
    Queue<Integer> queue = new ConstructorConstructor(Collections.emptyMap())
        .get(new TypeToken<Queue<Integer>>() {});
    assertInstanceOf(Queue.class, queue);
    assertInstanceOf(LinkedList.class, queue);
    assertEquals(0, queue.size());
    queue.add(1);
    assertEquals(1, queue.size());
  }

  @Test
  public void testRawTypeMatchForInstanceCreators() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    instanceCreators.put(new TypeToken<Map<String, Integer>>() {}.getType(),
        new InstanceCreator<Map<String, Integer>>() {
          @Override public Map<String, Integer> createInstance(Type type) {
            return new HashMap<String, Integer>();
          }
        });
    ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
    Map<String, Integer> map = constructor.get(new TypeToken<Map<String, Integer>>() {});
    assertInstanceOf(Map.class, map);
    assertInstanceOf(HashMap.class, map);
  }

  @Test(expected = RuntimeException.class)
  public void testNullInstanceCreatorMap() {
    new ConstructorConstructor(null);
  }

  @Test
  public void testGetDefaultImplementationConstructor() {
    Map<String, Integer> map = new ConstructorConstructor(Collections.emptyMap())
        .get(new TypeToken<Map<String, Integer>>() {});
    assertInstanceOf(Map.class, map);
    assertInstanceOf(LinkedHashMap.class, map);
    assertEquals(0, map.size());
    map.put("key", 1);
    assertEquals(1, map.size());
  }

  @Test(expected = RuntimeException.class)
  public void testUnsafeConstructor() {
    ConstructorConstructor constructor = new ConstructorConstructor(Collections.emptyMap());
    new Unsafe() {
      // Unsafe access to constructor
    }.allocateInstance(new TypeToken<Map<String, Integer>>() {}.getType());
  }
}