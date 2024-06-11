package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private Object mock;

 @Before
 public void setUp() {
 mock = mock(Object.class);
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswer_toStringMethod() {
 // Given
 when(mock.toString()).thenCallRealMethod();

 // When
 String toString = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("toString")));

 // Then
 assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), toString);
 }

 @Test
 public void testAnswer_compareToMethod() {
 // Given
 Object otherMock = mock(Object.class);
 when(mock.compareTo(otherMock)).thenCallRealMethod();

 // When
 int compareTo = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("compareTo"))).getClass().getMethod("compareTo", Object.class).invoke(returnsEmptyValues, otherMock);

 // Then
 assertEquals(1, compareTo);
 }

 @Test
 public void testReturnValueFor_Collection() {
 // Given
 Class<?> collectionType = Collection.class;

 // When
 Object emptyCollection = returnsEmptyValues.returnValueFor(collectionType);

 // Then
 assertEquals(new LinkedList<>(), emptyCollection);
 }

 @Test
 public void testReturnValueFor_Set() {
 // Given
 Class<?> setType = Set.class;

 // When
 Object emptySet = returnsEmptyValues.returnValueFor(setType);

 // Then
 assertEquals(new HashSet<>(), emptySet);
 }

 @Test
 public void testReturnValueFor_HashSet() {
 // Given
 Class<?> hashSetType = HashSet.class;

 // When
 Object emptyHashSet = returnsEmptyValues.returnValueFor(hashSetType);

 // Then
 assertEquals(new HashSet<>(), emptyHashSet);
 }

 @Test
 public void testReturnValueFor_SortedSet() {
 // Given
 Class<?> sortedSetType = SortedSet.class;

 // When
 Object emptySortedSet = returnsEmptyValues.returnValueFor(sortedSetType);

 // Then
 assertEquals(new TreeSet<>(), emptySortedSet);
 }

 @Test
 public void testReturnValueFor_TreeSet() {
 // Given
 Class<?> treeSetType = TreeSet.class;

 // When
 Object emptyTreeSet = returnsEmptyValues.returnValueFor(treeSetType);

 // Then
 assertEquals(new TreeSet<>(), emptyTreeSet);
 }

 @Test
 public void testReturnValueFor_LinkedHashSet() {
 // Given
 Class<?> linkedHashSetType = LinkedHashSet.class;

 // When
 Object emptyLinkedHashSet = returnsEmptyValues.returnValueFor(linkedHashSetType);

 // Then
 assertEquals(new LinkedHashSet<>(), emptyLinkedHashSet);
 }

 @Test
 public void testReturnValueFor_List() {
 // Given
 Class<?> listType = List.class;

 // When
 Object emptyList = returnsEmptyValues.returnValueFor(listType);

 // Then
 assertEquals(new LinkedList<>(), emptyList);
 }

 @Test
 public void testReturnValueFor_LinkedList() {
 // Given
 Class<?> linkedListType = LinkedList.class;

 // When
 Object emptyLinkedList = returnsEmptyValues.returnValueFor(linkedListType);

 // Then
 assertEquals(new LinkedList<>(), emptyLinkedList);
 }

 @Test
 public void testReturnValueFor_ArrayList() {
 // Given
 Class<?> arrayListType = ArrayList.class;

 // When
 Object emptyArrayList = returnsEmptyValues.returnValueFor(arrayListType);

 // Then
 assertEquals(new ArrayList<>(), emptyArrayList);
 }

 @Test
 public void testReturnValueFor_Map() {
 // Given
 Class<?> mapType = Map.class;

 // When
 Object emptyMap = returnsEmptyValues.returnValueFor(mapType);

 // Then
 assertEquals(new HashMap<>(), emptyMap);
 }

 @Test
 public void testReturnValueFor_HashMap() {
 // Given
 Class<?> hashMapType = HashMap.class;

 // When
 Object emptyHashMap = returnsEmptyValues.returnValueFor(hashMapType);

 // Then
 assertEquals(new HashMap<>(), emptyHashMap);
 }

 @Test
 public void testReturnValueFor_SortedMap() {
 // Given
 Class<?> sortedMapType = SortedMap.class;

 // When
 Object emptySortedMap = returnsEmptyValues.returnValueFor(sortedMapType);

 // Then
 assertEquals(new TreeMap<>(), emptySortedMap);
 }

 @Test
 public void testReturnValueFor_TreeMap() {
 // Given
 Class<?> treeMapType = TreeMap.class;

 // When
 Object emptyTreeMap = returnsEmptyValues.returnValueFor(treeMapType);

 // Then
 assertEquals(new TreeMap<>(), emptyTreeMap);
 }

 @Test
 public void testReturnValueFor_LinkedHashMap() {
 // Given
 Class<?> linkedHashMapType = LinkedHashMap.class;

 // When
 Object emptyLinkedHashMap = returnsEmptyValues.returnValueFor(linkedHashMapType);

 // Then
 assertEquals(new LinkedHashMap<>(), emptyLinkedHashMap);
 }
}