import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
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

 private ConstructorConstructor constructorConstructor;

 @Before
 public void setup() {
 Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGetInstanceCreatorForCollection() {
 Type collectionType = new TypeToken<Collection<Integer>>(){}.getType();
 ObjectConstructor<Collection<Integer>> constructor = constructorConstructor.get(new TypeToken<Collection<Integer>>(){});
 InstanceCreator<Collection<Integer>> instanceCreator = constructor.instanceCreator();
 assertEquals(Collection.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForSortedSet() {
 Type sortedSetType = new TypeToken<SortedSet<String>>(){}.getType();
 ObjectConstructor<SortedSet<String>> constructor = constructorConstructor.get(new TypeToken<SortedSet<String>>(){});
 InstanceCreator<SortedSet<String>> instanceCreator = constructor.instanceCreator();
 assertEquals(SortedSet.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForEnumSet() {
 Type enumSetType = new TypeToken<EnumSet<DaysOfWeek>>(){}.getType();
 ObjectConstructor<EnumSet<DaysOfWeek>> constructor = constructorConstructor.get(new TypeToken<EnumSet<DaysOfWeek>>(){});
 InstanceCreator<EnumSet<DaysOfWeek>> instanceCreator = constructor.instanceCreator();
 assertEquals(EnumSet.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForSet() {
 Type setType = new TypeToken<Set<Double>>(){}.getType();
 ObjectConstructor<Set<Double>> constructor = constructorConstructor.get(new TypeToken<Set<Double>>(){});
 InstanceCreator<Set<Double>> instanceCreator = constructor.instanceCreator();
 assertEquals(Set.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForQueue() {
 Type queueType = new TypeToken<Queue<Character>>(){}.getType();
 ObjectConstructor<Queue<Character>> constructor = constructorConstructor.get(new TypeToken<Queue<Character>>(){});
 InstanceCreator<Queue<Character>> instanceCreator = constructor.instanceCreator();
 assertEquals(Queue.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForMap() {
 Type mapType = new TypeToken<Map<String, Float>>(){}.getType();
 ObjectConstructor<Map<String, Float>> constructor = constructorConstructor.get(new TypeToken<Map<String, Float>>(){});
 InstanceCreator<Map<String, Float>> instanceCreator = constructor.instanceCreator();
 assertEquals(Map.class, instanceCreator.getClass());
 }

 @Test
 public void testGetInstanceCreatorForSortedMap() {
 Type sortedMapType = new TypeToken<SortedMap<Integer, Double>>(){}.getType();
 ObjectConstructor<SortedMap<Integer, Double>> constructor = constructorConstructor.get(new TypeToken<SortedMap<Integer, Double>>(){});
 InstanceCreator<SortedMap<Integer, Double>> instanceCreator = constructor.instanceCreator();
 assertEquals(SortedMap.class, instanceCreator.getClass());
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceCreatorForUnknownType() {
 Type unknownType = new TypeToken<Object>(){}.getType();
 ObjectConstructor<Object> constructor = constructorConstructor.get(new TypeToken<Object>(){});
 constructor.construct(); // should throw exception
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceCreatorForClassWithoutConstructor() {
 Class<?> classWithoutConstructor = Object.class;
 ObjectConstructor<Object> constructor = constructorConstructor.get(new TypeToken<Object>(){});
 constructor.construct(); // should throw exception
 }

 @Test
 public void testToString() {
 assertEquals("{}", constructorConstructor.toString());
}
}