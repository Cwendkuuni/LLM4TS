import com.google.gson.InstanceCreator;
import com.google.gson.TypeToken;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();

 @Test
 public void testGetInstanceOfSimpleType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<String> typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> constructor = constructorConstructor.get(typeToken);
 String result = constructor.construct();
 assertEquals("", result);
 }

 @Test
 public void testGetInstanceOfCollectionType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<List<Integer>> listTypeToken = new TypeToken<List<Integer>>() {};
 ObjectConstructor<List<Integer>> listConstructor = constructorConstructor.get(listTypeToken);
 List<Integer> listResult = listConstructor.construct();
 assertTrue(listResult instanceof ArrayList);
 listResult.add(1);
 assertEquals(1, listResult.size());
 }

 @Test
 public void testGetInstanceOfMapType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<Map<String, Integer>> mapTypeToken = new TypeToken<Map<String, Integer>>() {};
 ObjectConstructor<Map<String, Integer>> mapConstructor = constructorConstructor.get(mapTypeToken);
 Map<String, Integer> mapResult = mapConstructor.construct();
 assertTrue(mapResult instanceof LinkedHashMap);
 mapResult.put("key", 1);
 assertEquals(1, mapResult.size());
 }

 @Test
 public void testGetInstanceOfSortedMapType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<SortedMap<String, Integer>> sortedMapTypeToken = new TypeToken<SortedMap<String, Integer>>() {};
 ObjectConstructor<SortedMap<String, Integer>> sortedMapConstructor = constructorConstructor.get(sortedMapTypeToken);
 SortedMap<String, Integer> sortedMapResult = sortedMapConstructor.construct();
 assertTrue(sortedMapResult instanceof TreeMap);
 sortedMapResult.put("key", 1);
 assertEquals(1, sortedMapResult.size());
 }

 @Test
 public void testGetInstanceOfEnumSetType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type enumType = EnumSet.valueOf(ClassWithEnumMembers.FOO).getDeclaringClass();
 TypeToken<EnumSet<ClassWithEnumMembers>> enumSetTypeToken = new TypeToken<EnumSet<ClassWithEnumMembers>>() {
 };
 ObjectConstructor<EnumSet<ClassWithEnumMembers>> enumSetConstructor = constructorConstructor.get(enumSetTypeToken);
 EnumSet<ClassWithEnumMembers> enumSetResult = enumSetConstructor.construct();
 assertTrue(enumSetResult instanceof EnumSet);
 assertEquals(EnumSet.noneOf(enumType), enumSetResult);
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceOfUnknownType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<UnknownType> unknownTypeToken = new TypeToken<UnknownType>() {};
 ObjectConstructor<UnknownType> unknownTypeConstructor = constructorConstructor.get(unknownTypeToken);
 unknownTypeConstructor.construct();
 fail("Should throw a RuntimeException");
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceOfClassWithPrivateConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<PrivateConstructorClass> privateConstructorTypeToken = new TypeToken<PrivateConstructorClass>() {};
 ObjectConstructor<PrivateConstructorClass> privateConstructorConstructor = constructorConstructor.get(privateConstructorTypeToken);
 privateConstructorConstructor.construct();
 fail("Should throw a RuntimeException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInstanceOfArrayType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<int[]> arrayTypeToken = new TypeToken<int[]>() {};
 ObjectConstructor<int[]> arrayConstructor = constructorConstructor.get(arrayTypeToken);
 arrayConstructor.construct();
 fail("Should throw an IllegalArgumentException");
 }
}