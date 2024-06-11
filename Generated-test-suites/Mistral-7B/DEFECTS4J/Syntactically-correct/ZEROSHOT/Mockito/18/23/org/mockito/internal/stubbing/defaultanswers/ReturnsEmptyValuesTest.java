package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setUp() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToString() {
 Object mock = mock(String.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 given(invocation.getMock()).willReturn(mock);
 given(mockUtil.getMockName(mock)).willReturn(mockName("TestMock"));

 assertEquals("Mock for TestMock, hashCode: 1532293117", returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testCompareToMethod() {
 Object mock1 = mock(Comparable.class);
 Object mock2 = mock(Comparable.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 given(invocation.getMock()).willReturn(mock1);
 given(invocation.getArguments()[0]).willReturn(mock2);

 assertEquals(0, returnsEmptyValues.answer(invocation));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyCollection() {
 Collection<String> collection = mock(Collection.class);
 given(collection.getClass()).willReturn(ArrayList.class);
 given(returnsEmptyValues.answer(mock(InvocationOnMock.class))).willReturn(collection);
 collection.add("test");
 }

 @Test
 public void testEmptyCollection() {
 Collection<String> collection = mock(Collection.class);
 given(collection.getClass()).willReturn(LinkedList.class);
 given(returnsEmptyValues.answer(mock(InvocationOnMock.class))).willReturn(collection);

 assertEquals(0, collection.size());
 }

 @Test
 public void testEmptySet() {
 Set<String> set = mock(Set.class);
 given(set.getClass()).willReturn(HashSet.class);
 given(returnsEmptyValues.answer(mock(InvocationOnMock.class))).willReturn(set);

 assertEquals(0, set.size());
 }

 @Test
 public void testEmptySortedSet() {
 SortedSet<String> sortedSet = mock(SortedSet.class);
 given(sortedSet.getClass()).willReturn(TreeSet.class);
 given(returnsEmptyValues.answer(mock(InvocationOnMock.class))).willReturn(sortedSet);

 assertEquals(0, sortedSet.size());
 }

 @Test
 public void testEmptyMap() {
 Map<String, String> map = mock(Map.class);
 given(map.getClass()).willReturn(HashMap.class);
 given(returnsEmptyValues.answer(mock(InvocationOnMock.class))).willReturn(map);

 assertEquals(0, map.size());
 }

 @Test
 public void testEmptySortedMap() {
 SortedMap<String, String> sortedMap = mock(SortedMap.class);
 given(sortedMap.getClass()).willReturn(TreeMap.class);
 given(returnsEmptyValues.answer(mock(InvocationOnMock.class))).willReturn(sortedMap);

 assertEquals(0, sortedMap.size());
 }

 @Test
 public void testEmptyLinkedHashMap() {
 Map<String, String> map = mock(Map.class);
 given(map.getClass()).willReturn(LinkedHashMap.class);
 given(returnsEmptyValues.answer(mock(InvocationOnMock.class))).willReturn(map);

 assertEquals(0, map.size());
 }
}