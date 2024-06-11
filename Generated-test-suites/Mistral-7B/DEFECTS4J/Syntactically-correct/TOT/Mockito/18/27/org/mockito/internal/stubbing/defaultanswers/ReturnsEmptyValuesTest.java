package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Arrays;
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
import static org.junit.Assert.assertSame;

public class ReturnsEmptyValuesTest {

 @Test
 public void testToStringMethod() {
 Mockito.when(new Object()).toString().thenAnswer(new ReturnsEmptyValues());

 Mock mock = Mockito.mock(String.class);
 String result = Mockito.invokeMethod(mock, "toString");

 assertEquals("Mock for Object, hashCode: 1637491876", result);
 }

 @Test
 public void testCompareToMethod() {
 Mockito.when(Mockito.mock(Comparable.class)).thenAnswer(new ReturnsEmptyValues());

 Object mockObject1 = Mockito.mock(Comparable.class);
 Object mockObject2 = Mockito.mock(Comparable.class);

 assertEquals(0, mockObject1.compareTo(mockObject1));
 assertEquals(1, mockObject1.compareTo(mockObject2));
 }

 @Test
 public void testReturnsEmptyList() {
 Mockito.when(Mockito.mock(List.class)).thenAnswer(new ReturnsEmptyValues());

 List<String> mockList = Mockito.mock(List.class);
 List<String> result = Mockito.invokeMethod(mockList, "");

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptySet() {
 Mockito.when(Mockito.mock(Set.class)).thenAnswer(new ReturnsEmptyValues());

 Set<String> mockSet = Mockito.mock(Set.class);
 Set<String> result = Mockito.invokeMethod(mockSet, "add", ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyHashSet() {
 Mockito.when(Mockito.mock(HashSet.class)).thenAnswer(new ReturnsEmptyValues());

 HashSet<String> mockHashSet = Mockito.mock(HashSet.class);
 HashSet<String> result = Mockito.invokeMethod(mockHashSet, "add", ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyTreeSet() {
 Mockito.when(Mockito.mock(TreeSet.class)).thenAnswer(new ReturnsEmptyValues());

 TreeSet<String> mockTreeSet = Mockito.mock(TreeSet.class);
 TreeSet<String> result = Mockito.invokeMethod(mockTreeSet, "add", ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyLinkedHashSet() {
 Mockito.when(Mockito.mock(LinkedHashSet.class)).thenAnswer(new ReturnsEmptyValues());

 LinkedHashSet<String> mockLinkedHashSet = Mockito.mock(LinkedHashSet.class);
 LinkedHashSet<String> result = Mockito.invokeMethod(mockLinkedHashSet, "add", ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyArrayList() {
 Mockito.when(Mockito.mock(ArrayList.class)).thenAnswer(new ReturnsEmptyValues());

 ArrayList<String> mockArrayList = Mockito.mock(ArrayList.class);
 ArrayList<String> result = Mockito.invokeMethod(mockArrayList, "add", ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyLinkedList() {
 Mockito.when(Mockito.mock(LinkedList.class)).thenAnswer(new ReturnsEmptyValues());

 LinkedList<String> mockLinkedList = Mockito.mock(LinkedList.class);
 LinkedList<String> result = Mockito.invokeMethod(mockLinkedList, "add", ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyHashMap() {
 Mockito.when(Mockito.mock(Map.class)).thenAnswer(new ReturnsEmptyValues());

 Map<String, String> mockMap = Mockito.mock(Map.class);
 Map<String, String> result = Mockito.invokeMethod(mockMap, "put", ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyHashMap() {
 Mockito.when(Mockito.mock(HashMap.class)).thenAnswer(new ReturnsEmptyValues());

 HashMap<String, String> mockHashMap = Mockito.mock(HashMap.class);
 HashMap<String, String> result = Mockito.invokeMethod(mockHashMap, "put", ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyTreeMap() {
 Mockito.when(Mockito.mock(SortedMap.class)).thenAnswer(new ReturnsEmptyValues());

 SortedMap<String, String> mockSortedMap = Mockito.mock(SortedMap.class);
 SortedMap<String, String> result = Mockito.invokeMethod(mockSortedMap, "put", ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyTreeMap() {
 Mockito.when(Mockito.mock(TreeMap.class)).thenAnswer(new ReturnsEmptyValues());

 TreeMap<String, String> mockTreeMap = Mockito.mock(TreeMap.class);
 TreeMap<String, String> result = Mockito.invokeMethod(mockTreeMap, "put", ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsEmptyLinkedHashMap() {
 Mockito.when(Mockito.mock(LinkedHashMap.class)).thenAnswer(new ReturnsEmptyValues());

 LinkedHashMap<String, String> mockLinkedHashMap = Mockito.mock(LinkedHashMap.class);
 LinkedHashMap<String, String> result = Mockito.invokeMethod(mockLinkedHashMap, "put", ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReturnsNullForOtherTypes() {
 Mockito.when(Mockito.mock(Object.class)).thenAnswer(new ReturnsEmptyValues());

 Object mockObject = Mockito.mock(Object.class);
 Object result = Mockito.invokeMethod(mockObject, "hashCode");

 assertNull(result);
 }
}