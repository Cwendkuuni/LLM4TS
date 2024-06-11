import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;

  @Before
  public void setup() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }

  @Test
  public void testGetInstanceOfArrayList() {
    ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(new TypeToken<ArrayList<Integer>>() {}.getType());
    ArrayList<Integer> list = constructor.construct();
    assertNotNull(list);
    assertEquals(0, list.size());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfNullType() {
    constructorConstructor.get(null);
  }

  @Test
  public void testGetInstanceOfTreeMap() {
    ObjectConstructor<TreeMap<String, Object>> constructor = constructorConstructor.get(new TypeToken<TreeMap<String, Object>>() {}.getType());
    TreeMap<String, Object> map = constructor.construct();
    assertNotNull(map);
    assertTrue(map instanceof TreeMap);
    assertEquals(0, map.size());
  }

  @Test
  public void testGetInstanceOfLinkedHashSet() {
    ObjectConstructor<LinkedHashSet<String>> constructor = constructorConstructor.get(new TypeToken<LinkedHashSet<String>>() {}.getType());
    LinkedHashSet<String> set = constructor.construct();
    assertNotNull(set);
    assertTrue(set instanceof LinkedHashSet);
    assertEquals(0, set.size());
  }

  @Test
  public void testGetInstanceOfEnumSet() {
    ObjectConstructor<EnumSet<DaysOfWeek>> constructor = constructorConstructor.get(new TypeToken<EnumSet<DaysOfWeek>>() {}.getType());
    EnumSet<DaysOfWeek> enumSet = constructor.construct();
    assertNotNull(enumSet);
    assertTrue(enumSet instanceof EnumSet);
    assertEquals(0, enumSet.size());
  }

  @Test
  public void testGetInstanceOfLinkedList() {
    ObjectConstructor<LinkedList<String>> constructor = constructorConstructor.get(new TypeToken<LinkedList<String>>() {}.getType());
    LinkedList<String> list = constructor.construct();
    assertNotNull(list);
    assertTrue(list instanceof LinkedList);
    assertEquals(0, list.size());
  }

  @Test
  public void testGetInstanceOfTreeSet() {
    ObjectConstructor<TreeSet<Integer>> constructor = constructorConstructor.get(new TypeToken<TreeSet<Integer>>() {}.getType());
    TreeSet<Integer> set = constructor.construct();
    assertNotNull(set);
    assertTrue(set instanceof TreeSet);
    assertEquals(0, set.size());
  }

  @Test
  public void testGetInstanceOfSortedMap() {
    ObjectConstructor<SortedMap<String, Integer>> constructor = constructorConstructor.get(new TypeToken<SortedMap<String, Integer>>() {}.getType());
    SortedMap<String, Integer> map = constructor.construct();
    assertNotNull(map);
    assertTrue(map instanceof TreeMap);
    assertEquals(0, map.size());
  }

  @Test
  public void testGetInstanceOfMap() {
    ObjectConstructor<Map<String, Object>> constructor = constructorConstructor.get(new TypeToken<Map<String, Object>>() {}.getType());
    Map<String, Object> map = constructor.construct();
    assertNotNull(map);
    assertTrue(map instanceof LinkedTreeMap);
    assertEquals(0, map.size());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfUnknownType() {
    constructorConstructor.get(new TypeToken<String[][]>(){}.getType());
  }

  @Test
  public void testGetInstanceOfRawType() {
    ObjectConstructor<ArrayList> constructor = constructorConstructor.get(ArrayList.class);
    ArrayList list = constructor.construct();
    assertNotNull(list);
    assertEquals(0, list.size());
  }

  @Test
  public void testGetInstanceOfParameterizedType() {
    ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(new TypeToken<ArrayList<String>>() {}.getType());
    ArrayList<String> list = constructor.construct();
    assertNotNull(list);
    assertEquals(0, list.size());
  }
}

enum DaysOfWeek {
  MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}