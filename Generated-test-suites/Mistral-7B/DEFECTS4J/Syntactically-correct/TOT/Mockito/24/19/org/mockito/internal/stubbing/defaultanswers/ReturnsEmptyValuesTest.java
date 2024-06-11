package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues answer;
 private ObjectMethodsGuru methodsGuru;

 @Before
 public void setUp() {
 answer = new ReturnsEmptyValues();
 methodsGuru = new ObjectMethodsGuru();
 }

 @Test
 public void testToStringMethod() {
 Object mock = mock(String.class);
 Answer<String> answerToMockToString = answer;
 when(mock.toString()).thenAnswer(answerToMockToString);

 String mockString = new String("Mockito Test");
 String expectedToString = "Mock for java.lang.String, hashCode: " + mockString.hashCode();

 String actualToString = answerToMockToString.answer(Mockito.argThat(ArgumentMatchers.isInstanceOf(String.class)));

 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testCompareToMethod() {
 Object mockA = mock(Comparable.class);
 Object mockB = mock(Comparable.class);
 Answer<Integer> answerToMockCompareTo = answer;
 when(mockA.compareTo(mockB)).thenAnswer(answerToMockCompareTo);

 int result = answerToMockCompareTo.answer(Mockito.argThat(ArgumentMatchers.isInstanceOf(Comparable.class)));

 assertNotNull(result);
 }

 @Test
 public void testGetEmptyCollection() {
 Collection<Integer> collection = answer.returnValueFor(Collection.class);

 assertNotNull(collection);
 assertEquals(0, collection.size());
 }

 @Test
 public void testGetEmptySet() {
 Set<Integer> set = answer.returnValueFor(Set.class);

 assertNotNull(set);
 assertEquals(0, set.size());
 }

 @Test
 public void testGetEmptyHashSet() {
 Set<Integer> hashSet = answer.returnValueFor(HashSet.class);

 assertNotNull(hashSet);
 assertEquals(0, hashSet.size());
 }

 @Test
 public void testGetEmptyTreeSet() {
 Set<Integer> treeSet = answer.returnValueFor(TreeSet.class);

 assertNotNull(treeSet);
 assertEquals(0, treeSet.size());
 }

 @Test
 public void testGetEmptyLinkedHashSet() {
 Set<Integer> linkedHashSet = answer.returnValueFor(LinkedHashSet.class);

 assertNotNull(linkedHashSet);
 assertEquals(0, linkedHashSet.size());
 }

 @Test
 public void testGetEmptyList() {
 List<Integer> list = answer.returnValueFor(List.class);

 assertNotNull(list);
 assertEquals(0, list.size());
 }

 @Test
 public void testGetEmptyLinkedList() {
 List<Integer> linkedList = answer.returnValueFor(LinkedList.class);

 assertNotNull(linkedList);
 assertEquals(0, linkedList.size());
 }

 @Test
 public void testGetEmptyArrayList() {
 List<Integer> arrayList = answer.returnValueFor(ArrayList.class);

 assertNotNull(arrayList);
 assertEquals(0, arrayList.size());
 }

 @Test
 public void testGetEmptyMap() {
 Map<String, Integer> map = answer.returnValueFor(Map.class);

 assertNotNull(map);
 assertEquals(0, map.size());
 }

 @Test
 public void testGetEmptyHashMap() {
 Map<String, Integer> hashMap = answer.returnValueFor(HashMap.class);

 assertNotNull(hashMap);
 assertEquals(0, hashMap.size());
 }

 @Test
 public void testGetEmptyTreeMap() {
 Map<String, Integer> treeMap = answer.returnValueFor(TreeMap.class);

 assertNotNull(treeMap);
 assertEquals(0, treeMap.size());
 }

 @Test
 public void testGetEmptyLinkedHashMap() {
 Map<String, Integer> linkedHashMap = answer.returnValueFor(LinkedHashMap.class);

 assertNotNull(linkedHashMap);
 assertEquals(0, linkedHashMap.size());
 }

 @Test
 public void testGetEmptySortedMap() {
 SortedMap<String, Integer> sortedMap = answer.returnValueFor(SortedMap.class);

 assertNotNull(sortedMap);
 assertEquals(0, sortedMap.size());
 }

 @Test
 public void testGetEmptyTreeMap() {
 SortedMap<String, Integer> treeMap = answer.returnValueFor(TreeMap.class);

 assertNotNull(treeMap);
 assertEquals(0, treeMap.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyEmptyList() {
 List<Integer> list = answer.returnValueFor(List.class);
 list.add(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyEmptySet() {
 Set<Integer> set = answer.returnValueFor(Set.class);
 set.add(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyEmptyHashSet() {
 Set<Integer> hashSet = answer.returnValueFor(HashSet.class);
 hashSet.add(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyEmptyTreeSet() {
 Set<Integer> treeSet = answer.returnValueFor(TreeSet.class);
 treeSet.add(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyEmptyLinkedHashSet() {
 Set<Integer> linkedHashSet = answer.returnValueFor(LinkedHashSet.class);
 linkedHashSet.add(1);
 }
}