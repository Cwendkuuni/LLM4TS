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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setUp() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswerToStringMethod() {
 Mockito<String> stringMock = mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(stringMock);
 invocation.setMethod(Mockito.method(String.class, "toString"));
 String result = (String) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).contains("Mock for");
 }

 @Test
 public void testAnswerCompareToMethod() {
 Object mockObject1 = mock(Object.class);
 Object mockObject2 = mock(Object.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(mockObject1);
 invocation.setMethod(Mockito.method(Object.class, "compareTo", Object.class));
 int compareValue = (Integer) returnsEmptyValues.answer(invocation);

 assertThat(compareValue).isGreaterThan(0);

 invocation.setTarget(mockObject2);
 compareValue = (Integer) returnsEmptyValues.answer(invocation);

 assertThat(compareValue).isGreaterThan(0);

 verify(mockObject1, Mockito.times(1)).compareTo(mockObject2);
 verify(mockObject2, Mockito.times(1)).compareTo(mockObject1);
 }

 @Test
 public void testAnswerEmptyCollection() {
 Mockito<Collection<Integer>> collectionMock = mock(Collection.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(collectionMock);
 invocation.setMethod(Mockito.method(Collection.class, "size"));
 Collection<Integer> result = (Collection<Integer>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptySet() {
 Mockito<Set<String>> setMock = mock(Set.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(setMock);
 invocation.setMethod(Mockito.method(Set.class, "size"));
 Set<String> result = (Set<String>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptyHashSet() {
 Mockito<HashSet<Integer>> hashSetMock = mock(HashSet.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(hashSetMock);
 invocation.setMethod(Mockito.method(HashSet.class, "size"));
 Set<Integer> result = (Set<Integer>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptyTreeSet() {
 Mockito<TreeSet<String>> treeSetMock = mock(TreeSet.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(treeSetMock);
 invocation.setMethod(Mockito.method(TreeSet.class, "size"));
 Set<String> result = (Set<String>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerLinkedHashSet() {
 Mockito<LinkedHashSet<String>> linkedHashSetMock = mock(LinkedHashSet.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(linkedHashSetMock);
 invocation.setMethod(Mockito.method(LinkedHashSet.class, "size"));
 Set<String> result = (Set<String>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptyList() {
 Mockito<List<Integer>> listMock = mock(List.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(listMock);
 invocation.setMethod(Mockito.method(List.class, "size"));
 List<Integer> result = (List<Integer>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptyLinkedList() {
 Mockito<LinkedList<String>> linkedListMock = mock(LinkedList.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(linkedListMock);
 invocation.setMethod(Mockito.method(LinkedList.class, "size"));
 List<String> result = (List<String>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptyArrayList() {
 Mockito<ArrayList<Integer>> arrayListMock = mock(ArrayList.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(arrayListMock);
 invocation.setMethod(Mockito.method(ArrayList.class, "size"));
 List<Integer> result = (List<Integer>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptyMap() {
 Mockito<Map<String, Integer>> mapMock = mock(Map.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(mapMock);
 invocation.setMethod(Mockito.method(Map.class, "size"));
 Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptyHashMap() {
 Mockito<HashMap<String, Integer>> hashMapMock = mock(HashMap.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(hashMapMock);
 invocation.setMethod(Mockito.method(HashMap.class, "size"));
 Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptyTreeMap() {
 Mockito<TreeMap<String, Integer>> treeMapMock = mock(TreeMap.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(treeMapMock);
 invocation.setMethod(Mockito.method(TreeMap.class, "size"));
 Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerEmptyLinkedHashMap() {
 Mockito<LinkedHashMap<String, Integer>> linkedHashMapMock = mock(LinkedHashMap.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(linkedHashMapMock);
 invocation.setMethod(Mockito.method(LinkedHashMap.class, "size"));
 Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(invocation);

 assertThat(result).isNotNull();
 assertThat(result).isEmpty();
 }

 @Test
 public void testAnswerPrimitives() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 int intValue = (Integer) returnsEmptyValues.answer(invocation);
 double doubleValue = (Double) returnsEmptyValues.answer(invocation);
 boolean booleanValue = (Boolean) returnsEmptyValues.answer(invocation);
 char charValue = (Character) returnsEmptyValues.answer(invocation);
 byte byteValue = (Byte) returnsEmptyValues.answer(invocation);
 short shortValue = (Short) returnsEmptyValues.answer(invocation);
 long longValue = (Long) returnsEmptyValues.answer(invocation);
 float floatValue = (Float) returnsEmptyValues.answer(invocation);

 verify(invocation, Mockito.times(1)).getArgument(ArgumentMatchers.anyInt());

 assertThat(intValue).isZero();
 assertThat(doubleValue).isZero();
 assertThat(booleanValue).isFalse();
 assertThat(charValue).isNull();
 assertThat(byteValue).isZero();
 assertThat(shortValue).isZero();
 assertThat(longValue).isZero();
 assertThat(floatValue).isZero();
 }
}