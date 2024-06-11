import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeToken;
import com.google.gson.reflect.TypeTokenTypeAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConstructorConstructorTest {

  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(new TypeTokenTypeAdapterFactory())
      .create();

  private static class TestInstanceCreator implements InstanceCreator<Object> {
    @Override public Object createInstance(Type type) {
      return new Object();
    }
  }

  @Test
  public void testGet_Collection() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(ArrayList.class, new TestInstanceCreator());
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(typeToken);
    ArrayList<String> list = constructor.construct();

    Assert.assertNotNull(list);
    Assert.assertEquals(0, list.size());

    list.add("Test");
    Assert.assertEquals(1, list.size());
  }

  @Test
  public void testGet_Collection_SortedSet() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(TreeSet.class, new TestInstanceCreator<Set<Object>>() {
      @Override public Set<Object> createInstance(Type type) {
        return new TreeSet<Object>();
      }
    });
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<SortedSet<String>> typeToken = new TypeToken<SortedSet<String>>() {};
    ObjectConstructor<SortedSet<String>> constructor = constructorConstructor.get(typeToken);
    SortedSet<String> set = constructor.construct();

    Assert.assertNotNull(set);
    Assert.assertEquals(0, set.size());

    set.add("Test");
    Assert.assertEquals(1, set.size());

    Set<String> emptySet = constructor.construct();
    Assert.assertEquals(0, emptySet.size());
  }

  @Test
  public void testGet_Map() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(LinkedHashMap.class, new TestInstanceCreator<Map<Object, Object>>() {
      @Override public Map<Object, Object> createInstance(Type type) {
        return new LinkedHashMap<Object, Object>();
      }
    });
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
    ObjectConstructor<Map<String, Integer>> constructor = constructorConstructor.get(typeToken);
    Map<String, Integer> map = constructor.construct();

    Assert.assertNotNull(map);
    Assert.assertEquals(0, map.size());

    map.put("Test", 1);
    Assert.assertEquals(1, map.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testGet_UnknownType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<Object> typeToken = new TypeToken<Object>() {};
    ObjectConstructor<Object> constructor = constructorConstructor.get(typeToken);
    constructor.construct();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGet_InvalidType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(ArrayList.class, new TestInstanceCreator<ArrayList<Object>>() {
      @Override public ArrayList<Object> createInstance(Type type) {
        return new ArrayList<Object>();
      }
    });
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<String> typeToken = new TypeToken<String>() {};
    ObjectConstructor<String> constructor = constructorConstructor.get(typeToken);
    constructor.construct();
  }

  @Test(expected = NullPointerException.class)
  public void testGet_NullTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
    constructorConstructor.get(null);
  }
}