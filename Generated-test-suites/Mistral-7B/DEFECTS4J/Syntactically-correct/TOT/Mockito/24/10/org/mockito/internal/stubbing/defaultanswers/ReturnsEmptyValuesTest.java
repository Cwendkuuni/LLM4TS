package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
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

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues answer;

 @Before
 public void setup() {
 answer = new ReturnsEmptyValues();
 }

 @Test
 public void testToString() {
 // Given
 Object mock = Mockito.mock(String.class);
 // When
 String result = (String) answer.answer(Mockito.argThat(invocation -> "toString".equals(invocation.getMethod().getName())));
 // Then
 assertEquals("Mock for String, hashCode: " + mock.hashCode(), result);
 }

 @Test
 public void testCompareTo() {
 // Given
 Object mock1 = Mockito.mock(Comparable.class);
 Object mock2 = Mockito.mock(Comparable.class);
 // When
 int result1 = (int) answer.answer(Mockito.argThat(invocation -> "compareTo".equals(invocation.getMethod().getName())));
 int result2 = (int) answer.answer(Mockito.argThat(invocation -> "compareTo".equals(invocation.getMethod().getName()) && mock1.equals(invocation.getArguments()[0])));
 // Then
 assertEquals(0, result1);
 assertEquals(1, result2);
 }

 @Test
 public void testPrimitiveTypes() {
 // Given
 int primitiveInt = 42;
 int mockInt = Mockito.mock(Integer.class);
 // When
 int result1 = (int) answer.answer(Mockito.argThat(invocation -> "intValue".equals(invocation.getMethod().getName()) && mockInt.equals(invocation.getArguments()[0])));
 int result2 = (int) answer.answer(Mockito.argThat(invocation -> "intValue".equals(invocation.getMethod().getName()) && primitiveInt.equals(invocation.getArguments()[0])));
 // Then
 assertEquals(0, result1);
 assertEquals(42, result2);
 }

 @Test
 public void testCollectionTypes() {
 // Given
 List<String> expectedList = new ArrayList<>();
 // When
 List<String> result1 = (List<String>) answer.answer(Mockito.argThat(invocation -> "size".equals(invocation.getMethod().getName()) && List.class.isAssignableFrom(invocation.getReturnType())));
 List<String> result2 = (List<String>) answer.answer(Mockito.argThat(invocation -> "isEmpty".equals(invocation.getMethod().getName()) && result1.getClass() == invocation.getReturnType()));
 // Then
 assertEquals(0, result1.size());
 assertEquals(true, result2.isEmpty());
 // And check that the returned collections can't be modified without throwing an UnsupportedOperationException
 result1.add("test");
 result2.add("test");
 assertEquals(0, result1.size());
 assertEquals(true, result2.isEmpty());
 }

 @Test
 public void testSetTypes() {
 // Given
 Set<String> expectedSet = new HashSet<>();
 // When
 Set<String> result1 = (Set<String>) answer.answer(Mockito.argThat(invocation -> "size".equals(invocation.getMethod().getName()) && Set.class.isAssignableFrom(invocation.getReturnType())));
 Set<String> result2 = (Set<String>) answer.answer(Mockito.argThat(invocation -> "isEmpty".equals(invocation.getMethod().getName()) && result1.getClass() == invocation.getReturnType()));
 // Then
 assertEquals(0, result1.size());
 assertEquals(true, result2.isEmpty());
 // And check that the returned sets can't be modified without throwing an UnsupportedOperationException
 result1.add("test");
 result2.add("test");
 assertEquals(0, result1.size());
 assertEquals(true, result2.isEmpty());
 }

 @Test
 public void testMapTypes() {
 // Given
 Map<String, String> expectedMap = new HashMap<>();
 // When
 Map<String, String> result1 = (Map<String, String>) answer.answer(Mockito.argThat(invocation -> "isEmpty".equals(invocation.getMethod().getName()) && Map.class.isAssignableFrom(invocation.getReturnType())));
 Map<String, String> result2 = (Map<String, String>) answer.answer(Mockito.argThat(invocation -> "size".equals(invocation.getMethod().getName()) && result1.getClass() == invocation.getReturnType()));
 // Then
 assertEquals(true, result1.isEmpty());
 assertEquals(0, result2.size());
 // And check that the returned maps can't be modified without throwing an UnsupportedOperationException
 result1.put("test", "test");
 result2.put("test", "test");
 assertEquals(true, result1.isEmpty());
 assertEquals(0, result2.size());
 }
}