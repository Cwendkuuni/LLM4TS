package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private Object mock;

 @Before
 public void setup() {
 mock = mock(Object.class);
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToStringAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("toString", (Class[]) null));
 Mockito.when(mock.toString()).thenAnswer(returnsEmptyValues);

 String actual = (String) Mockito.invoke(mock, "toString");
 String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();

 assertEquals(expected, actual);
 }

 @Test
 public void testCompareToAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("compareTo", Object.class));

 int actual = (int) Mockito.invoke(mock, "compareTo", ArgumentMatchers.any());
 int expected = 1;

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyCollectionAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 Collection<Object> actual = (Collection<Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 Collection<Object> expected = new LinkedList<>();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptySetAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 Set<Object> actual = (Set<Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 Set<Object> expected = new HashSet<>();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyHashSetAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 Set<Object> actual = (Set<Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 Set<Object> expected = new LinkedHashSet<>();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptySortedSetAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 SortedSet<Object> actual = (SortedSet<Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 SortedSet<Object> expected = new TreeSet<>();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyTreeSetAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 SortedSet<Object> actual = (SortedSet<Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 SortedSet<Object> expected = new TreeSet<>();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyLinkedHashMapAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 Map<Object, Object> actual = (Map<Object, Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 Map<Object, Object> expected = new HashMap<>();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyHashMapAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 Map<Object, Object> actual = (Map<Object, Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 Map<Object, Object> expected = new HashMap<>();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyTreeMapAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 SortedMap<Object, Object> actual = (SortedMap<Object, Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 SortedMap<Object, Object> expected = new TreeMap<>();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyTreeMapAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 SortedMap<Object, Object> actual = (SortedMap<Object, Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 SortedMap<Object, Object> expected = new TreeMap<>();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyLinkedHashMapAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 Map<Object, Object> actual = (Map<Object, Object>) Mockito.invoke(returnsEmptyValues, "answer", invocation);
 Map<Object, Object> expected = new LinkedHashMap<>();

 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testGetNullAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(returnsEmptyValues.getClass().getDeclaredMethod("answer", InvocationOnMock.class));

 Object actual = Mockito.invoke(returnsEmptyValues, "answer", invocation);

 assertNull(actual);
 }
}