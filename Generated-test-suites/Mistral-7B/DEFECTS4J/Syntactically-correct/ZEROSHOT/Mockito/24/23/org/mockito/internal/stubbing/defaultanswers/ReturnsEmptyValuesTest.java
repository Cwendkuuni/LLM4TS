package com.yourpackage.test;

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
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private Collection<String> collection;
 private Set<String> set;
 private List<String> list;
 private Map<String, String> map;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 collection = Mockito.mock(Collection.class);
 set = Mockito.mock(Set.class);
 list = Mockito.mock(List.class);
 map = Mockito.mock(Map.class);
 }

 @Test
 public void testToString() {
 Mockito.when(collection).toString().thenAnswer(returnsEmptyValues);
 Mockito.when(set).toString().thenAnswer(returnsEmptyValues);
 Mockito.when(list).toString().thenAnswer(returnsEmptyValues);
 Mockito.when(map).toString().thenAnswer(returnsEmptyValues);

 String collectionToString = (String) returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString")));
 String setToString = (String) returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString")));
 String listToString = (String) returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString")));
 String mapToString = (String) returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString")));

 assertEquals("Mock for Collection, hashCode: <hashcode>", collectionToString);
 assertEquals("Mock for Set, hashCode: <hashcode>", setToString);
 assertEquals("Mock for List, hashCode: <hashcode>", listToString);
 assertEquals("Mock for Map, hashCode: <hashcode>", mapToString);
 }

 @Test
 public void testCompareTo() {
 Mockito.when(collection).toString().thenAnswer(returnsEmptyValues);
 Mockito.when(set).toString().thenAnswer(returnsEmptyValues);
 Mockito.when(list).toString().thenAnswer(returnsEmptyValues);
 Mockito.when(map).toString().thenAnswer(returnsEmptyValues);

 Object a = new Object();
 Object b = new Object();

 Mockito.when(collection.compareTo(ArgumentMatchers.any())).thenAnswer(returnsEmptyValues);
 Mockito.when(set.compareTo(ArgumentMatchers.any())).thenAnswer(returnsEmptyValues);
 Mockito.when(list.compareTo(ArgumentMatchers.any())).thenAnswer(returnsEmptyValues);
 Mockito.when(map.compareTo(ArgumentMatchers.any())).thenAnswer(returnsEmptyValues);

 verify(collection).compareTo(a);
 verify(set).compareTo(b);
 verify(list).compareTo(a);
 verify(map).compareTo(b);

 // Verify that compareTo method was called with different objects to check if mocks are not equal
 assertEquals(1, returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("compareTo"))).hashCode());
 assertEquals(1, returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("compareTo"))).hashCode());
 }

 @Test
 public void testEmptyCollection() {
 Mockito.when(collection.isEmpty()).thenReturn(true);
 Mockito.when(collection.size()).thenReturn(0);

 Object returnValue = returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("size")));

 assertEquals(0, returnValue);
 }

 @Test
 public void testEmptySet() {
 Mockito.when(set.isEmpty()).thenReturn(true);
 Mockito.when(set.size()).thenReturn(0);

 Object returnValue = returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("size")));

 assertEquals(0, returnValue);
 }

 @Test
 public void testEmptyList() {
 Mockito.when(list.isEmpty()).thenReturn(true);
 Mockito.when(list.size()).thenReturn(0);

 Object returnValue = returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("size")));

 assertEquals(0, returnValue);
 }

 @Test
 public void testEmptyMap() {
 Mockito.when(map.isEmpty()).thenReturn(true);
 Mockito.when(map.size()).thenReturn(0);

 Object returnValue = returnsEmptyValues.answer(Mockito.argThat(i -> i.getMethod().getName().equals("size")));

 assertEquals(0, returnValue);
 }
}