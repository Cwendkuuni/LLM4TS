package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues emptyValues;

 @Before
 public void setup() {
 emptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToStringMethod() {
 Mockito.when(Mockito.mock(String.class)).thenAnswer(emptyValues);
 String mockString = Mockito.mock(String.class);
 assertEquals("Mock for String, hashCode: " + mockString.hashCode(), emptyValues.answer(new InvocationOnMock(Mockito.method(String.class, "toString"))));
 }

 @Test
 public void testCompareToMethod() {
 Mockito.when(Mockito.mock(Comparable.class)).thenAnswer(emptyValues);
 Comparable mockComparable = Mockito.mock(Comparable.class);
 assertEquals(1, emptyValues.answer(new InvocationOnMock(Mockito.method(Comparable.class, "compareTo", ArgumentMatchers.any(Comparable.class)))));
 }

 @Test
 public void testPrimitiveMethods() {
 Mockito.when(Mockito.mock(Integer.class)).thenAnswer(emptyValues);
 Integer mockInteger = Mockito.mock(Integer.class);
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(Integer.class, "intValue"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(Integer.class, "intValue")))); //Integer-returning method

 Mockito.when(Mockito.mock(Character.class)).thenAnswer(emptyValues);
 Character mockCharacter = Mockito.mock(Character.class);
 assertEquals('\u0000', emptyValues.answer(new InvocationOnMock(Mockito.method(Character.class, "charValue"))));
 assertEquals('\u0000', emptyValues.answer(new InvocationOnMock(Mockito.method(Character.class, "charValue")))); //Character-returning method
 }

 @Test
 public void testEmptyCollection() {
 Mockito.when(Mockito.mock(Collection.class)).thenAnswer(emptyValues);
 Collection<String> mockCollection = Mockito.mock(Collection.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(Collection.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(Collection.class, "size"))));
 }

 @Test
 public void testEmptySet() {
 Mockito.when(Mockito.mock(Set.class)).thenAnswer(emptyValues);
 Set<String> mockSet = Mockito.mock(Set.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(Set.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(Set.class, "size"))));
 }

 @Test
 public void testEmptyHashSet() {
 Mockito.when(Mockito.mock(HashSet.class)).thenAnswer(emptyValues);
 HashSet<String> mockHashSet = Mockito.mock(HashSet.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(HashSet.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(HashSet.class, "size"))));
 }

 @Test
 public void testEmptyTreeSet() {
 Mockito.when(Mockito.mock(TreeSet.class)).thenAnswer(emptyValues);
 TreeSet<String> mockTreeSet = Mockito.mock(TreeSet.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(TreeSet.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(TreeSet.class, "size"))));
 }

 @Test
 public void testEmptyLinkedHashSet() {
 Mockito.when(Mockito.mock(LinkedHashSet.class)).thenAnswer(emptyValues);
 LinkedHashSet<String> mocLinkedHashSet = Mockito.mock(LinkedHashSet.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(LinkedHashSet.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(LinkedHashSet.class, "size"))));
 }

 @Test
 public void testEmptyArrayList() {
 Mockito.when(Mockito.mock(List.class)).thenAnswer(emptyValues);
 List<String> mockArrayList = Mockito.mock(List.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(List.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(List.class, "size"))));
 }

 @Test
 public void testEmptyLinkedList() {
 Mockito.when(Mockito.mock(LinkedList.class)).thenAnswer(emptyValues);
 LinkedList<String> mockLinkedList = Mockito.mock(LinkedList.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(LinkedList.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(LinkedList.class, "size"))));
 }

 @Test
 public void testEmptyArrayList() {
 Mockito.when(Mockito.mock(ArrayList.class)).thenAnswer(emptyValues);
 ArrayList<String> mockArrayList = Mockito.mock(ArrayList.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(ArrayList.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(ArrayList.class, "size"))));
 }

 @Test
 public void testEmptyHashMap() {
 Mockito.when(Mockito.mock(Map.class)).thenAnswer(emptyValues);
 Map<String, String> mockHashMap = Mockito.mock(Map.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(Map.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(Map.class, "size"))));
 }

 @Test
 public void testEmptyHashMap() {
 Mockito.when(Mockito.mock(HashMap.class)).thenAnswer(emptyValues);
 HashMap<String, String> mockHashMap = Mockito.mock(HashMap.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(HashMap.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(HashMap.class, "size"))));
 }

 @Test
 public void testEmptyTreeMap() {
 Mockito.when(Mockito.mock(SortedMap.class)).thenAnswer(emptyValues);
 SortedMap<String, String> mockTreeMap = Mockito.mock(SortedMap.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(SortedMap.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(SortedMap.class, "size"))));
 }

 @Test
 public void testEmptyTreeMap() {
 Mockito.when(Mockito.mock(TreeMap.class)).thenAnswer(emptyValues);
 TreeMap<String, String> mockTreeMap = Mockito.mock(TreeMap.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(TreeMap.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(TreeMap.class, "size"))));
 }

 @Test
 public void testEmptyLinkedHashMap() {
 Mockito.when(Mockito.mock(LinkedHashMap.class)).thenAnswer(emptyValues);
 LinkedHashMap<String, String> mockLinkedHashMap = Mockito.mock(LinkedHashMap.class);
 assertNotNull(emptyValues.answer(new InvocationOnMock(Mockito.method(LinkedHashMap.class, "isEmpty"))));
 assertEquals(0, emptyValues.answer(new InvocationOnMock(Mockito.method(LinkedHashMap.class, "size"))));
 }
}