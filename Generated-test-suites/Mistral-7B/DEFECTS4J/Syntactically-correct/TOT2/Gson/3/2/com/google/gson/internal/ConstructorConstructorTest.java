import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ConstructorConstructorTest {

  private static final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

  @Test
  public void testGet_TypeToken_ReturnsInstanceCreator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringObjectConstructor = constructorConstructor.get(stringTypeToken);
    InstanceCreator<String> stringInstanceCreator = stringObjectConstructor.getInstanceCreator();
    assertNotNull(stringInstanceCreator);
  }

  @Test
  public void testGet_TypeToken_ReturnsInstanceWhenInstanceCreatorFound() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<ArrayList<Integer>> arrayListIntegerTypeToken = new TypeToken<ArrayList<Integer>>() {};
    ObjectConstructor<ArrayList<Integer>> arrayListIntegerObjectConstructor = constructorConstructor.get(arrayListIntegerTypeToken);
    ArrayList<Integer> arrayList = arrayListIntegerObjectConstructor.construct();
    assertNotNull(arrayList);
    assertTrue(arrayList instanceof ArrayList);
  }

  @Test
  public void testGet_TypeToken_ReturnsDefaultInstanceWhenInstanceCreatorNotFound() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<EnumSet<String>> enumSetStringTypeToken = new TypeToken<EnumSet<String>>() {};
    ObjectConstructor<EnumSet<String>> enumSetStringObjectConstructor = constructorConstructor.get(enumSetStringTypeToken);
    EnumSet<String> enumSet = enumSetStringObjectConstructor.construct();
    assertNotNull(enumSet);
    assertTrue(enumSet instanceof EnumSet);

    // Test default implementation
    assertEquals(Collections.emptyEnumSet(), enumSet);
  }

  @Test(expected = RuntimeException.class)
  public void testGet_TypeToken_ThrowsExceptionWhenDefaultConstructorNotFound() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<LinkedHashMap<String, Object>> linkedHashMapStringObjectTypeToken = new TypeToken<LinkedHashMap<String, Object>>() {};
    ObjectConstructor<LinkedHashMap<String, Object>> linkedHashMapStringObjectObjectConstructor = constructorConstructor.get(linkedHashMapStringObjectTypeToken);
    linkedHashMapStringObjectObjectConstructor.construct();
  }

  @Test(expected = RuntimeException.class)
  public void testGet_TypeToken_ThrowsExceptionWhenUnsafeCreationFailed() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<TreeMap<Integer, Integer>> treeMapIntegerIntegerTypeToken = new TypeToken<TreeMap<Integer, Integer>>() {};
    ObjectConstructor<TreeMap<Integer, Integer>> treeMapIntegerIntegerObjectConstructor = constructorConstructor.get(treeMapIntegerIntegerTypeToken);
    treeMapIntegerIntegerObjectConstructor.construct();
  }

  @Test
  public void testToString() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    assertEquals("{...}", constructorConstructor.toString());
  }
}