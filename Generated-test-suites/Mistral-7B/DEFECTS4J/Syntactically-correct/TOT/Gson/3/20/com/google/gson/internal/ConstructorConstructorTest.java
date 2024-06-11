import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceOfMapType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TypeToken<SortedMap<String, Integer>> mapTypeToken = new TypeToken<SortedMap<String, Integer>>() {};
    ObjectConstructor<SortedMap<String, Integer>> mapConstructor = constructorConstructor.get(mapTypeToken);
    SortedMap<String, Integer> map = mapConstructor.construct();
    assertNotNull(map);
    assertEquals(new TreeMap<String, Integer>(), map);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfUnknownType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TypeToken<UnknownType> unknownTypeToken = new TypeToken<UnknownType>() {};
    ObjectConstructor<UnknownType> constructor = constructorConstructor.get(unknownTypeToken);
  }

  @Test
  public void testGetInstanceOfCollectionType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new ArrayList<>());
    TypeToken<Collection<String>> collectionTypeToken = new TypeToken<Collection<String>>() {};
    ObjectConstructor<Collection<String>> collectionConstructor = constructorConstructor.get(collectionTypeToken);
    Collection<String> collection = collectionConstructor.construct();
    assertNotNull(collection);
    assertEquals(new ArrayList<String>(), collection);
  }

  @Test
  public void testGetInstanceOfEnumSetType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new EnumSet<>(Foo.class));
    TypeToken<EnumSet<Foo>> enumSetTypeToken = new TypeToken<EnumSet<Foo>>() {};
    ObjectConstructor<EnumSet<Foo>> enumSetConstructor = constructorConstructor.get(enumSetTypeToken);
    EnumSet<Foo> enumSet = enumSetConstructor.construct();
    assertNotNull(enumSet);
    assertEquals(EnumSet.noneOf(Foo.class), enumSet);
  }

  @Test
  public void testGetInstanceOfListType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new ArrayList<>());
    TypeToken<List<String>> listTypeToken = new TypeToken<List<String>>() {};
    ObjectConstructor<List<String>> listConstructor = constructorConstructor.get(listTypeToken);
    List<String> list = listConstructor.construct();
    assertNotNull(list);
    assertEquals(new ArrayList<String>(), list);
  }

  @Test
  public void testGetInstanceOfMapWithStringKeyType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedTreeMap<String, Object>());
    TypeToken<Map<String, Object>> mapTypeToken = new TypeToken<Map<String, Object>>() {};
    ObjectConstructor<Map<String, Object>> mapConstructor = constructorConstructor.get(mapTypeToken);
    Map<String, Object> map = mapConstructor.construct();
    assertNotNull(map);
    assertEquals(new LinkedTreeMap<String, Object>(), map);
  }

  @Test
  public void testGetInstanceOfQueueType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedList<>());
    TypeToken<Queue<String>> queueTypeToken = new TypeToken<Queue<String>>() {};
    ObjectConstructor<Queue<String>> queueConstructor = constructorConstructor.get(queueTypeToken);
    Queue<String> queue = queueConstructor.construct();
    assertNotNull(queue);
    assertEquals(new LinkedList<String>(), queue);
  }

  @Test
  public void testGetInstanceOfParameterizedType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new ArrayList<>() {});
    TypeToken<ArrayList<String>> listTypeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> listConstructor = constructorConstructor.get(listTypeToken);
    ArrayList<String> list = listConstructor.construct();
    assertNotNull(list);
    assertEquals(new ArrayList<String>(), list);
  }

  @Test
  public void testGetInstanceOfRawType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new ArrayList<>());
    TypeToken<Object> objectTypeToken = new TypeToken<Object>() {};
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(objectTypeToken);
    Object object = objectConstructor.construct();
    assertNotNull(object);
  }
}