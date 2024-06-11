import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceFromInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<Map<String, String>> instanceCreatorMap = new InstanceCreator<Map<String, String>>() {
      @Override public Map<String, String> createInstance(Type type) {
        return new LinkedHashMap<String, String>();
      }
    };
    instanceCreators.put(new TypeToken<Map<String, String>>(){}.getType(), instanceCreatorMap);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Map<String, String>> typeTokenMap = new TypeToken<Map<String, String>>(){};
    ObjectConstructor<Map<String, String>> objectConstructor = constructorConstructor.get(typeTokenMap);
    Map<String, String> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertEquals(0, instance.size());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceFromNullInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Map<String, String>> typeTokenMap = new TypeToken<Map<String, String>>(){};
    ObjectConstructor<Map<String, String>> objectConstructor = constructorConstructor.get(typeTokenMap);
  }

  @Test
  public void testGetInstanceFromRawTypeInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<Map<String, String>> instanceCreatorMap = new InstanceCreator<Map<String, String>>() {
      @Override public Map<String, String> createInstance(Type type) {
        return new LinkedHashMap<String, String>();
      }
    };
    instanceCreators.put(Map.class, instanceCreatorMap);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Map<String, String>> typeTokenMap = new TypeToken<Map<String, String>>(){};
    ObjectConstructor<Map<String, String>> objectConstructor = constructorConstructor.get(typeTokenMap);
    Map<String, String> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertEquals(0, instance.size());
  }

  @Test
  public void testGetInstanceFromDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<ArrayList<Object>> typeTokenArrayList = new TypeToken<ArrayList<Object>>(){};
    ObjectConstructor<ArrayList<Object>> objectConstructor = constructorConstructor.get(typeTokenArrayList);
    ArrayList<Object> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertEquals(0, instance.size());
  }

  @Test
  public void testGetInstanceFromDefaultImplementationConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<SortedSet<Integer>> typeTokenSortedSet = new TypeToken<SortedSet<Integer>>(){};
    ObjectConstructor<SortedSet<Integer>> objectConstructor = constructorConstructor.get(typeTokenSortedSet);
    SortedSet<Integer> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof TreeSet);
    assertEquals(0, instance.size());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceFromInvalidType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<String> typeTokenString = new TypeToken<String>(){};
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeTokenString);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceFromUnsupportedType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Type type = new TypeToken<Object>() {};
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(new TypeToken<Object>(type));
  }

  @Test
  public void testGetInstanceFromUnsafeAllocator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<ArrayList<String>> typeTokenArrayListString = new TypeToken<ArrayList<String>>(){};
    ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.newUnsafeAllocator(typeTokenArrayListString.getType(), ArrayList.class);
    ArrayList<String> instance = objectConstructor.construct();
    assertNotNull(instance);
    assertEquals(0, instance.size());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceFromUnsafeAllocatorNullType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Object> objectConstructor = constructorConstructor.newUnsafeAllocator(null, Object.class);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceFromUnsafeAllocatorUnsupportedClass() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<String> objectConstructor = constructorConstructor.newUnsafeAllocator(String.class.getTypeName(), Integer.class);
  }
}