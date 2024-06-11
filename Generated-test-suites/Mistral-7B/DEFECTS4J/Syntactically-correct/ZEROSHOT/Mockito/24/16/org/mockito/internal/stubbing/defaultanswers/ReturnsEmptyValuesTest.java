import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private ObjectMethodsGuru methodsGuru;
 private MockUtil mockUtil;

 @Before
 public void setUp() {
 methodsGuru = new ObjectMethodsGuru();
 mockUtil = new MockUtil();
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToStringAnswer() {
 Mockito.<String>stub(String.class).toReturn(returnValues -> returnsEmptyValues.answer(ArgumentMatchers.method(String.class, "toString"))).whenCalled();
 String mock = Mockito.mock(String.class);
 assertEquals("Mock for String, hashCode: " + mock.hashCode(), mock.toString());
 }

 @Test
 public void testCompareToAnswer() {
 Mockito.<Integer>stub(Integer.class).toReturn(returnValues -> (Integer) returnsEmptyValues.answer(ArgumentMatchers.method(Integer.class, "compareTo", ArgumentMatchers.any(Integer.class)))).whenCalled();
 Integer mock1 = Mockito.mock(Integer.class);
 Integer mock2 = Mockito.mock(Integer.class);
 assertEquals(1, mock1.compareTo(mock2));
 }

 @Test
 public void testPrimitivesAnswer() {
 Mockito.<Byte>stub(Byte.class).toReturn(returnValues -> (Byte) returnsEmptyValues.answer(ArgumentMatchers.method(Byte.class, "valueOf", (Class) byte[].class, ArgumentMatchers.anyIntArray()))).whenCalled();
 Byte byteValue = Mockito.mock(Byte.class);
 assertEquals((byte) 0, byteValue.byteValue());

 Mockito.<Short>stub(Short.class).toReturn(returnValues -> (Short) returnsEmptyValues.answer(ArgumentMatchers.method(Short.class, "valueOf", (Class) short[].class, ArgumentMatchers.anyIntArray()))).whenCalled();
 Short shortValue = Mockito.mock(Short.class);
 assertEquals((short) 0, shortValue.shortValue());

 Mockito.<Integer>stub(Integer.class).toReturn(returnValues -> (Integer) returnsEmptyValues.answer(ArgumentMatchers.method(Integer.class, "valueOf", (Class) int[].class, ArgumentMatchers.anyIntArray()))).whenCalled();
 Integer intValue = Mockito.mock(Integer.class);
 assertEquals(0, intValue.intValue());

 Mockito.<Long>stub(Long.class).toReturn(returnValues -> (Long) returnsEmptyValues.answer(ArgumentMatchers.method(Long.class, "valueOf", (Class) long[].class, ArgumentMatchers.anyLongArray()))).whenCalled();
 Long longValue = Mockito.mock(Long.class);
 assertEquals(0L, longValue.longValue());

 Mockito.<Float>stub(Float.class).toReturn(returnValues -> (Float) returnsEmptyValues.answer(ArgumentMatchers.method(Float.class, "valueOf", (Class) float[].class, ArgumentMatchers.anyFloatArray()))).whenCalled();
 Float floatValue = Mockito.mock(Float.class);
 assertEquals(0f, floatValue.floatValue(), 0.01f);

 Mockito.<Double>stub(Double.class).toReturn(returnValues -> (Double) returnsEmptyValues.answer(ArgumentMatchers.method(Double.class, "valueOf", (Class) double[].class, ArgumentMatchers.anyDoubleArray()))).whenCalled();
 Double doubleValue = Mockito.mock(Double.class);
 assertEquals(0d, doubleValue.doubleValue(), 0.01d);
 }

 @Test
 public void testEmptyCollectionAnswer() {
 Class<?> collectionType = Collection.class;
 Mockito.<Collection<String>>stub(collectionType).toReturn(returnValues -> (Collection<String>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 Collection<String> collection = Mockito.mock(collectionType);
 assertNotNull(collection);
 assertEquals(0, collection.size());
 }

 @Test
 public void testEmptySetAnswer() {
 Class<?> setType = Set.class;
 Mockito.<Set<String>>stub(setType).toReturn(returnValues -> (Set<String>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 Set<String> set = Mockito.mock(setType);
 assertNotNull(set);
 assertEquals(0, set.size());
 }

 @Test
 public void testEmptyHashSetAnswer() {
 Class<?> hashSetType = HashSet.class;
 Mockito.<HashSet<String>>stub(hashSetType).toReturn(returnValues -> (HashSet<String>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 HashSet<String> hashSet = Mockito.mock(hashSetType);
 assertNotNull(hashSet);
 assertEquals(0, hashSet.size());
 }

 @Test
 public void testEmptyTreeSetAnswer() {
 Class<?> treeSetType = TreeSet.class;
 Mockito.<TreeSet<String>>stub(treeSetType).toReturn(returnValues -> (TreeSet<String>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 TreeSet<String> treeSet = Mockito.mock(treeSetType);
 assertNotNull(treeSet);
 assertEquals(0, treeSet.size());
 }

 @Test
 public void testEmptyLinkedHashSetAnswer() {
 Class<?> linkedHashSetType = LinkedHashSet.class;
 Mockito.<LinkedHashSet<String>>stub(linkedHashSetType).toReturn(returnValues -> (LinkedHashSet<String>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 LinkedHashSet<String> linkedHashSet = Mockito.mock(linkedHashSetType);
 assertNotNull(linkedHashSet);
 assertEquals(0, linkedHashSet.size());
 }

 @Test
 public void testEmptyListAnswer() {
 Class<?> listType = List.class;
 Mockito.<List<String>>stub(listType).toReturn(returnValues -> (List<String>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 List<String> list = Mockito.mock(listType);
 assertNotNull(list);
 assertEquals(0, list.size());
 }

 @Test
 public void testEmptyLinkedListAnswer() {
 Class<?> linkedListType = LinkedList.class;
 Mockito.<LinkedList<String>>stub(linkedListType).toReturn(returnValues -> (LinkedList<String>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 LinkedList<String> linkedList = Mockito.mock(linkedListType);
 assertNotNull(linkedList);
 assertEquals(0, linkedList.size());
 }

 @Test
 public void testEmptyArrayListAnswer() {
 Class<?> arrayListType = ArrayList.class;
 Mockito.<ArrayList<String>>stub(arrayListType).toReturn(returnValues -> (ArrayList<String>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 ArrayList<String> arrayList = Mockito.mock(arrayListType);
 assertNotNull(arrayList);
 assertEquals(0, arrayList.size());
 }

 @Test
 public void testEmptyMapAnswer() {
 Class<?> mapType = Map.class;
 Mockito.<Map<String, Object>>stub(mapType).toReturn(returnValues -> (Map<String, Object>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 Map<String, Object> map = Mockito.mock(mapType);
 assertNotNull(map);
 assertEquals(0, map.size());
 }

 @Test
 public void testEmptyHashMapAnswer() {
 Class<?> hashMapType = HashMap.class;
 Mockito.<HashMap<String, Object>>stub(hashMapType).toReturn(returnValues -> (HashMap<String, Object>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 HashMap<String, Object> hashMap = Mockito.mock(hashMapType);
 assertNotNull(hashMap);
 assertEquals(0, hashMap.size());
 }

 @Test
 public void testEmptyTreeMapAnswer() {
 Class<?> treeMapType = TreeMap.class;
 Mockito.<TreeMap<String, Object>>stub(treeMapType).toReturn(returnValues -> (TreeMap<String, Object>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 TreeMap<String, Object> treeMap = Mockito.mock(treeMapType);
 assertNotNull(treeMap);
 assertEquals(0, treeMap.size());
 }

 @Test
 public void testEmptyLinkedHashMapAnswer() {
 Class<?> linkedHashMapType = LinkedHashMap.class;
 Mockito.<LinkedHashMap<String, Object>>stub(linkedHashMapType).toReturn(returnValues -> (LinkedHashMap<String, Object>) returnsEmptyValues.answer(ArgumentMatchers.any())).whenCalled();
 LinkedHashMap<String, Object> linkedHashMap = Mockito.mock(linkedHashMapType);
 assertNotNull(linkedHashMap);
 assertEquals(0, linkedHashMap.size());
 }
}