import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators;
 private ConstructorConstructor constructorConstructor;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 instanceCreators = new LinkedHashMap<>();
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGetInstanceWithTypeToken() {
 Type typeToken = new TypeToken<ArrayList<String>>() {}.getType();
 ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(
 (TypeToken<ArrayList<String>>) typeToken);
 ArrayList<String> list = objectConstructor.construct();
 assertNotNull(list);
 assertEquals(0, list.size());
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceWithInvalidTypeToken() {
 Type typeToken = new TypeToken<Integer>() {}.getType();
 ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
 fail("ObjectConstructor for Integer should not be created");
 }

 @Test
 public void testGetInstanceWithRawType() {
 Class<ArrayList<String>> rawType = ArrayList.class;
 ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(rawType);
 ArrayList<String> list = objectConstructor.construct();
 assertNotNull(list);
 assertEquals(0, list.size());
 }

 @Test
 public void testGetInstanceWithArrayType() {
 Type arrayType = new TypeToken<Integer[]>() {}.getType();
 ObjectConstructor<Integer[]> objectConstructor = constructorConstructor.get(arrayType);
 Integer[] array = objectConstructor.construct();
 assertNotNull(array);
 assertEquals(0, array.length);
 }

 @Test
 public void testGetInstanceWithMapType() {
 Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
 ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.get(mapType);
 Map<String, Integer> map = objectConstructor.construct();
 assertNotNull(map);
 assertEquals(0, map.size());
 }

 @Test
 public void testGetInstanceWithCollectionType() {
 Type collectionType = new TypeToken<Collection<String>>() {}.getType();
 ObjectConstructor<Collection<String>> objectConstructor = constructorConstructor.get(collectionType);
 Collection<String> collection = objectConstructor.construct();
 assertNotNull(collection);
 assertEquals(0, collection.size());
 }

 @Test
 public void testGetInstanceWithSortedSetType() {
 Type sortedSetType = new TypeToken<SortedSet<String>>() {}.getType();
 ObjectConstructor<SortedSet<String>> objectConstructor = constructorConstructor.get(sortedSetType);
 SortedSet<String> sortedSet = objectConstructor.construct();
 assertNotNull(sortedSet);
 assertEquals(0, sortedSet.size());
 }

 @Test
 public void testGetInstanceWithEnumSetType() {
 Type enumSetType = new TypeToken<EnumSet<DaysOfWeek>>() {}.getType();
 ObjectConstructor<EnumSet<DaysOfWeek>> objectConstructor = constructorConstructor.get(enumSetType);
 EnumSet<DaysOfWeek> enumSet = objectConstructor.construct();
 assertNotNull(enumSet);
 assertTrue(enumSet.isEmpty());
 }

 @Test
 public void testGetInstanceWithQueueType() {
 Type queueType = new TypeToken<Queue<String>>() {}.getType();
 ObjectConstructor<Queue<String>> objectConstructor = constructorConstructor.get(queueType);
 Queue<String> queue = objectConstructor.construct();
 assertNotNull(queue);
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testGetInstanceWithSortedMapType() {
 Type sortedMapType = new TypeToken<SortedMap<String, Integer>>() {}.getType();
 ObjectConstructor<SortedMap<String, Integer>> objectConstructor = constructorConstructor.get(sortedMapType);
 SortedMap<String, Integer> sortedMap = objectConstructor.construct();
 assertNotNull(sortedMap);
 assertEquals(0, sortedMap.size());
 }

 @Test
 public void testGetInstanceWithLinkedHashMapType() {
 Type linkedHashMapType = new TypeToken<Map<String, Integer>>() {}.getType();
 ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.get(linkedHashMapType);
 Map<String, Integer> linkedHashMap = objectConstructor.construct();
 assertNotNull(linkedHashMap);
 assertEquals(0, linkedHashMap.size());
 }

 @Test
 public void testGetInstanceWithLinkedTreeMapType() {
 Type linkedTreeMapType = new TypeToken<Map<String, Integer>>() {}.getType();
 ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.get(linkedTreeMapType);
 Map<String, Integer> linkedTreeMap = objectConstructor.construct();
 assertNotNull(linkedTreeMap);
 assertEquals(0, linkedTreeMap.size());
 }

 @Test
 public void testGetInstanceWithCustomClassType() {
 Type customType = new TypeToken<CustomClass>() {}.getType();
 ObjectConstructor<CustomClass> objectConstructor = constructorConstructor.get(customType);
 CustomClass customClass = objectConstructor.construct();
 assertNotNull(customClass);
 assertTrue(customClass instanceof CustomClass);
 }

 @Test
 public void testToString() {
 assertEquals("{instanceCreators=[]}", constructorConstructor.toString());
}

 @Test
 public void testInstanceCreator() {
 InstanceCreator<String> stringCreator = new InstanceCreator<String>() {
 @Override public String createInstance(Type type) {
 return "test";
 }
 };
 instanceCreators.put(String.class, stringCreator);
 ObjectConstructor<String> stringObjectConstructor = constructorConstructor.get(TypeToken.get(String.class));
 String string = stringObjectConstructor.construct();
 assertEquals("test", string);
 }
}

class CustomClass {
 // empty class
}

enum DaysOfWeek {
 MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

class UnsafeAllocator {
 // Unsafe allocator implementation
}