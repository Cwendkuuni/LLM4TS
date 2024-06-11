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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private MockitoMock mock;

 @Before
 public void setUp() {
 returnsEmptyValues = new ReturnsEmptyValues();
 mock = mock(Object.class);
 }

 @Test
 public void testToStringMethod() {
 InvocationOnMock invocation = Mockito.methodCall(mock, "toString");
 String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
 Object result = returnsEmptyValues.answer(invocation);
 assertEquals(expected, result);
 }

 @Test
 public void testComparatorMethod() {
 InvocationOnMock invocation = Mockito.methodCall(Mockito.mock(Comparable.class), "compareTo", ArgumentMatchers.any());
 int result = (int) returnsEmptyValues.answer(invocation);
 assertEquals(1, result);
 }

 @Test
 public void testPrimitiveReturningMethods() {
 InvocationOnMock intInvocation = Mockito.methodCall(mock, "intValue");
 int intResult = (int) returnsEmptyValues.answer(intInvocation);
 assertEquals(0, intResult);

 InvocationOnMock booleanInvocation = Mockito.methodCall(mock, "booleanValue");
 boolean booleanResult = (boolean) returnsEmptyValues.answer(booleanInvocation);
 assertEquals(false, booleanResult);
 }

 @Test
 public void testCollectionReturningMethods() {
 InvocationOnMock collectionInvocation = Mockito.methodCall(mock, "values");
 Collection<Object> result = (Collection<Object>) returnsEmptyValues.answer(collectionInvocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testSetReturningMethods() {
 InvocationOnMock setInvocation = Mockito.methodCall(mock, "keySet");
 Set<Object> result = (Set<Object>) returnsEmptyValues.answer(setInvocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testListReturningMethods() {
 InvocationOnMock listInvocation = Mockito.methodCall(mock, "list");
 List<Object> result = (List<Object>) returnsEmptyValues.answer(listInvocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testMapReturningMethods() {
 InvocationOnMock mapInvocation = Mockito.methodCall(mock, "entrySet");
 Set<Map.Entry<Object, Object>> result = (Set<Map.Entry<Object, Object>>) returnsEmptyValues.answer(mapInvocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testSortedMapReturningMethods() {
 InvocationOnMock sortedMapInvocation = Mockito.methodCall(mock, "entrySet");
 SortedSet<Map.Entry<Object, Object>> result = (SortedSet<Map.Entry<Object, Object>>) returnsEmptyValues.answer(sortedMapInvocation);
 assertNotNull(result);
 assertEquals(0, result.size());
 }
}