import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators;
 private ConstructorConstructor constructorConstructor;

 @Before
 public void setup() {
 instanceCreators = new LinkedHashMap<>();
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGetInstanceCreatorForArrayList() {
 Type listType = new TypeToken<ArrayList<String>>() {}.getType();
 ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(listType);
 InstanceCreator<ArrayList<String>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(ArrayList.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForArrayListWithGenericType() {
 Type listTypeWithGenericType = new TypeToken<ArrayList<Integer>>() {}.getType();
 ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(listTypeWithGenericType);
 InstanceCreator<ArrayList<Integer>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(ArrayList.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForLinkedList() {
 Type listType = new TypeToken<LinkedList<String>>() {}.getType();
 ObjectConstructor<LinkedList<String>> constructor = constructorConstructor.get(listType);
 InstanceCreator<LinkedList<String>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(LinkedList.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForLinkedListWithGenericType() {
 Type listTypeWithGenericType = new TypeToken<LinkedList<Integer>>() {}.getType();
 ObjectConstructor<LinkedList<Integer>> constructor = constructorConstructor.get(listTypeWithGenericType);
 InstanceCreator<LinkedList<Integer>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(LinkedList.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForLinkedHashSet() {
 Type setType = new TypeToken<LinkedHashSet<String>>() {}.getType();
 ObjectConstructor<LinkedHashSet<String>> constructor = constructorConstructor.get(setType);
 InstanceCreator<LinkedHashSet<String>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(LinkedHashSet.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForLinkedHashSetWithGenericType() {
 Type setTypeWithGenericType = new TypeToken<LinkedHashSet<Integer>>() {}.getType();
 ObjectConstructor<LinkedHashSet<Integer>> constructor = constructorConstructor.get(setTypeWithGenericType);
 InstanceCreator<LinkedHashSet<Integer>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(LinkedHashSet.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForTreeSet() {
 Type setType = new TypeToken<TreeSet<String>>() {}.getType();
 ObjectConstructor<TreeSet<String>> constructor = constructorConstructor.get(setType);
 InstanceCreator<TreeSet<String>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(TreeSet.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForTreeSetWithGenericType() {
 Type setTypeWithGenericType = new TypeToken<TreeSet<Integer>>() {}.getType();
 ObjectConstructor<TreeSet<Integer>> constructor = constructorConstructor.get(setTypeWithGenericType);
 InstanceCreator<TreeSet<Integer>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(TreeSet.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForTreeMap() {
 Type mapType = new TypeToken<TreeMap<String, String>>() {}.getType();
 ObjectConstructor<TreeMap<String, String>> constructor = constructorConstructor.get(mapType);
 InstanceCreator<TreeMap<String, String>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(TreeMap.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForTreeMapWithGenericKeyType() {
 Type mapTypeWithKeyType = new TypeToken<TreeMap<String, Integer>>() {}.getType();
 ObjectConstructor<TreeMap<String, Integer>> constructor = constructorConstructor.get(mapTypeWithKeyType);
 InstanceCreator<TreeMap<String, Integer>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(TreeMap.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForLinkedHashMap() {
 Type mapType = new TypeToken<LinkedHashMap<String, String>>() {}.getType();
 ObjectConstructor<LinkedHashMap<String, String>> constructor = constructorConstructor.get(mapType);
 InstanceCreator<LinkedHashMap<String, String>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(LinkedHashMap.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForLinkedHashMapWithKeyValueTypes() {
 Type mapTypeWithKeyValueTypes = new TypeToken<LinkedHashMap<String, Integer>>() {}.getType();
 ObjectConstructor<LinkedHashMap<String, Integer>> constructor = constructorConstructor.get(mapTypeWithKeyValueTypes);
 InstanceCreator<LinkedHashMap<String, Integer>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(LinkedHashMap.class, instanceCreator.getClass());
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceCreatorForUnknownType() {
 Type unknownType = new TypeToken<Integer>() {}.getType();
 ObjectConstructor<Integer> constructor = constructorConstructor.get(unknownType);
 constructor.construct();
 fail("InstanceCreator should not be created for this type");
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceCreatorForUnsupportedType() {
 Type unsupportedType = new TypeToken<EnumSet<String>>() {}.getType();
 ObjectConstructor<EnumSet<String>> constructor = constructorConstructor.get(unsupportedType);
 constructor.construct();
 fail("InstanceCreator should not be created for this type");
 }

 @Test
 public void testDefaultConstructorForCollection() {
 Type collectionType = Collection.class;
 ObjectConstructor<Collection> constructor = constructorConstructor.get(new TypeToken<Collection>() {}.getType());
 Collection collection = constructor.construct();
 assertNotNull(collection);
 assertTrue(collection instanceof Collection);
 assertEquals(0, collection.size());
 }

 @Test
 public void testDefaultConstructorForMap() {
 Type mapType = Map.class;
 ObjectConstructor<Map> constructor = constructorConstructor.get(new TypeToken<Map>() {}.getType());
 Map map = constructor.construct();
 assertNotNull(map);
 assertTrue(map instanceof Map);
 assertEquals(0, map.size());
 }

 @Test
 public void testDefaultConstructorForSortedMap() {
 Type sortedMapType = SortedMap.class;
 ObjectConstructor<SortedMap> constructor = constructorConstructor.get(new TypeToken<SortedMap>() {}.getType());
 SortedMap sortedMap = constructor.construct();
 assertNotNull(sortedMap);
 assertTrue(sortedMap instanceof SortedMap);
 assertEquals(0, sortedMap.size());
 }

 @Test
 public void testDefaultConstructorForSortedSet() {
 Type sortedSetType = SortedSet.class;
 ObjectConstructor<SortedSet> constructor = constructorConstructor.get(new TypeToken<SortedSet>() {}.getType());
 SortedSet sortedSet = constructor.construct();
 assertNotNull(sortedSet);
 assertTrue(sortedSet instanceof SortedSet);
 assertEquals(0, sortedSet.size());
 }

 @Test
 public void testGetInstanceCreatorForEnumSet() {
 Type enumSetType = new TypeToken<EnumSet<String>>() {}.getType();
 ObjectConstructor<EnumSet<String>> constructor = constructorConstructor.get(enumSetType);
 InstanceCreator<EnumSet<String>> instanceCreator = constructor.construct();
 assertNotNull(instanceCreator);
 assertEquals(EnumSet.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForEnumSetWithEmptyEnumSet() {
 Type enumSetType = new TypeToken<EnumSet<String>>() {}.getType();
 InstanceCreator<EnumSet<String>> instanceCreator = constructorConstructor.get(enumSetType);
 EnumSet<String> emptyEnumSet = instanceCreator.createInstance(enumSetType);
 assertNotNull(emptyEnumSet);
 assertTrue(emptyEnumSet.isEmpty());
 }

 @Test
 public void testGetInstanceCreatorForEnumSetWithOneElement() {
 Type enumSetType = new TypeToken<EnumSet<String>>() {}.getType();
 InstanceCreator<EnumSet<String>> instanceCreator = constructorConstructor.get(enumSetType);
 EnumSet<String> oneElementEnumSet = instanceCreator.createInstance(enumSetType, "ONE");
 assertNotNull(oneElementEnumSet);
 assertEquals(1, oneElementEnumSet.size());
 assertTrue(oneElementEnumSet.contains("ONE"));
 }

 @Test
 public void testGetInstanceCreatorForEnumSetWithMultipleElements() {
 Type enumSetType = new TypeToken<EnumSet<String>>() {}.getType();
 InstanceCreator<EnumSet<String>> instanceCreator = constructorConstructor.get(enumSetType);
 EnumSet<String> multipleElementsEnumSet = instanceCreator.createInstance(enumSetType, "ONE", "TWO", "THREE");
 assertNotNull(multipleElementsEnumSet);
 assertEquals(3, multipleElementsEnumSet.size());
 assertTrue(multipleElementsEnumSet.containsAll(Collections.singletonList("ONE")));
 assertTrue(multipleElementsEnumSet.containsAll(Collections.singletonList("TWO")));
 assertTrue(multipleElementsEnumSet.containsAll(Collections.singletonList("THREE")));
 }
}