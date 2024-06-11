package com.yourpackage.mockito.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

 private static final long serialVersionUID = 1998191268711234347L;

 @Test
 public void testToString() {
 Mock <String> mock = mock(String.class);
 Object answer = new ReturnsEmptyValues().answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("toString")));
 String result = (String) answer;
 assertNotNull(result);
 assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
 }

 @Test
 public void testCompareToMethod() {
 Object mock = mock(Comparable.class);
 Object answer = new ReturnsEmptyValues().answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("compareTo")));
 assertEquals(1, (int) answer);
 }

 @Test
 public void testReturnPrimitiveTypes() {
 Mockito.when(mock(Integer.class)).thenReturn(new ReturnsEmptyValues());
 Integer mockInt = mock(Integer.class);
 int intAnswer = (int) new ReturnsEmptyValues().answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("intValue")));
 assertEquals(0, intAnswer);
 int intWrapperAnswer = (int) new ReturnsEmptyValues().answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("intValue") && inv.getArguments().length > 0));
 assertEquals(0, intWrapperAnswer);

 Mockito.when(mock(Double.class)).thenReturn(new ReturnsEmptyValues());
 Double mockDouble = mock(Double.class);
 double doubleAnswer = (double) new ReturnsEmptyValues().answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("doubleValue")));
 assertEquals(0.0, doubleAnswer, 0.0);
 double doubleWrapperAnswer = (double) new ReturnsEmptyValues().answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("doubleValue") && inv.getArguments().length > 0));
 assertEquals(0.0, doubleWrapperAnswer, 0.0);

 Mockito.when(mock(Boolean.class)).thenReturn(new ReturnsEmptyValues());
 Boolean mockBoolean = mock(Boolean.class);
 boolean booleanAnswer = (boolean) new ReturnsEmptyValues().answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("booleanValue")));
 assertEquals(false, booleanAnswer);
 boolean booleanWrapperAnswer = (boolean) new ReturnsEmptyValues().answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("booleanValue") && inv.getArguments().length > 0));
 assertEquals(false, booleanWrapperAnswer);
 }

 @Test
 public void testCollectionsEmptyValues() {
 Collection<Object> collection = new ReturnsEmptyValues();
 List<Object> list = new ReturnsEmptyValues();
 Set<Object> set = new ReturnsEmptyValues();
 HashSet<Object> hashSet = new ReturnsEmptyValues();
 LinkedHashSet<Object> linkedHashSet = new ReturnsEmptyValues();
 SortedSet<Object> sortedSet = new ReturnsEmptyValues();
 TreeSet<Object> treeSet = new ReturnsEmptyValues();

 assertEquals(0, collection.size());
 assertEquals(false, collection.isEmpty());
 assertEquals(0, list.size());
 assertEquals(false, list.isEmpty());
 assertEquals(0, set.size());
 assertEquals(false, set.isEmpty());
 assertEquals(0, hashSet.size());
 assertEquals(false, hashSet.isEmpty());
 assertEquals(0, linkedHashSet.size());
 assertEquals(false, linkedHashSet.isEmpty());
 assertEquals(0, sortedSet.size());
 assertEquals(false, sortedSet.isEmpty());
 assertEquals(0, treeSet.size());
 assertEquals(false, treeSet.isEmpty());
 }

 @Test
 public void testMapsEmptyValues() {
 Map<Object, Object> map = new ReturnsEmptyValues();
 HashMap<Object, Object> hashMap = new ReturnsEmptyValues();
 SortedMap<Object, Object> sortedMap = new ReturnsEmptyValues();
 TreeMap<Object, Object> treeMap = new ReturnsEmptyValues();
 LinkedHashMap<Object, Object> linkedHashMap = new ReturnsEmptyValues();

 assertEquals(0, map.size());
 assertEquals(false, map.isEmpty());
 assertEquals(0, hashMap.size());
 assertEquals(false, hashMap.isEmpty());
 assertEquals(0, sortedMap.size());
 assertEquals(false, sortedMap.isEmpty());
 assertEquals(0, treeMap.size());
 assertEquals(false, treeMap.isEmpty());
 assertEquals(0, linkedHashMap.size());
 assertEquals(false, linkedHashMap.isEmpty());
 }
}