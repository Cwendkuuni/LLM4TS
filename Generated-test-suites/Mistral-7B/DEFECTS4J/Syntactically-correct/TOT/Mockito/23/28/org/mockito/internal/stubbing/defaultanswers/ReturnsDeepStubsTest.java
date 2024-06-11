package org.mockito.internal.stubbing.defaultanswers.tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

 private static class GenericsNest<K extends Comparable<K> & Cloneable> implements Map<K, Set<Number>> {
 }

 @Test
 public void testReturnsDeepStubsForSimpleTypes() {
 Object mock = Mockito.mock(String.class, with(Answers.RETURNS_DEEP_STUBS));

 String result = mock.toUpperCase();
 assertEquals("MOCK", result);
 }

 @Test
 public void testReturnsDeepStubsForComplexTypes() {
 Object mock = Mockito.mock(GenericsNest.class, with(Answers.RETURNS_DEEP_STUBS));

 GenericsNest<Integer> genericMock = (GenericsNest<Integer>) mock;
 Set<Number> set = new HashSet<>();
 set.add(1);
 genericMock.put(1, set);
 Set<Number> returnedSet = genericMock.entrySet().iterator().next().getValue().iterator().next();

 Number number = returnedSet.iterator().next();
 assertEquals(1, number.intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowsIllegalArgumentExceptionForNonMockableTypes() {
 Object mock = Mockito.mock(Number[].class, with(Answers.RETURNS_DEEP_STUBS));

 // This should throw an IllegalArgumentException as Number[] is not mockable
 // with the current settings
 // However, for completeness, we're testing that it does indeed throw an exception
 Arrays.toString((Number[]) mock);
 }

 @Test
 public void testReturnsSameInstanceForMultipleInvocationsWithIdenticalArguments() {
 Object mock = Mockito.mock(String.class, with(Answers.RETURNS_DEEP_STUBS));

 String result1 = mock.toUpperCase();
 String result2 = mock.toUpperCase();

 assertEquals(result1, result2);
 }

 @Test
 public void testReturnsSameInstanceForMultipleInvocationsWithDifferentArguments() {
 Object mock = Mockito.mock(GenericsNest.class, with(Answers.RETURNS_DEEP_STUBS));

 GenericsNest<String> firstMock = (GenericsNest<String>) mock;
 GenericsNest<Integer> secondMock = (GenericsNest<Integer>) mock;

 Set<String> firstSet = new HashSet<>();
 firstSet.add("MockString");
 firstMock.put("MockString", firstSet);

 Set<Integer> secondSet = new HashSet<>();
 secondSet.add(1);
 secondMock.put(1, secondSet);

 Set<String> firstReturnedSet = firstMock.entrySet().iterator().next().getValue().iterator().next();
 Set<Integer> secondReturnedSet = secondMock.entrySet().iterator().next().getValue().iterator().next();

 assertNotEquals(firstReturnedSet, secondReturnedSet);
 }

 @Test
 public void testReturnsDeepStubForNestedInvocations() {
 Object mock = Mockito.mock(GenericsNest.class, with(Answers.RETURNS_DEEP_STUBS));

 GenericsNest<GenericsNest<String>> nestedGenericsMock = (GenericsNest<GenericsNest<String>>) mock;
 GenericsNest<String> nestedStringMock = nestedGenericsMock.put(new GenericsNest<String>(), new HashSet<>());
 Set<String> nestedStringSet = nestedStringMock.entrySet().iterator().next().getValue().iterator().next();

 String nestedStringResult = nestedStringSet.iterator().next();
 assertEquals("MOCK", nestedStringResult);
 }

 @Test
 public void testReturnsDeepStubForNestedInvocationsWithDifferentTypes() {
 Object mock = Mockito.mock(GenericsNest.class, with(Answers.RETURNS_DEEP_STUBS));

 GenericsNest<GenericsNest<String>> nestedGenericsMock = (GenericsNest<GenericsNest<String>>) mock;
 GenericsNest<Number> nestedNumberMock = nestedGenericsMock.put(new GenericsNest<Number>(), new HashSet<>());
 Number nestedNumberResult = nestedNumberMock.entrySet().iterator().next().getValue().iterator().next();

 int nestedNumberIntResult = nestedNumberResult.intValue();
 assertEquals(0, nestedNumberIntResult);
 }

 @Test
 public void testVerifiesInvocationsWithDeepStubs() {
 Object mock = Mockito.mock(GenericsNest.class, with(Answers.RETURNS_DEEP_STUBS));

 GenericsNest<String> genericsMock = (GenericsNest<String>) mock;
 genericsMock.put(1, new HashSet<>());
 genericsMock.put(2, new HashSet<>());

 verify(mock).put(anyInt(), anySetOf(anySet()));
 verify(mock).put(eq(1), anySetOf(anySet()));
 verify(mock).put(eq(2), anySetOf(anySet()));
 }

 @Test
 public void testRecordsDeepStubsForVerification() {
 Object mock = Mockito.mock(GenericsNest.class, with(Answers.RETURNS_DEEP_STUBS));

 GenericsNest<String> genericsMock = (GenericsNest<String>) mock;
 genericsMock.put(1, new HashSet<>());
 genericsMock.put(2, new HashSet<>());

 verify(mock).put(anyInt(), anySetOf(anySet()));
 verify(mock).put(eq(1), eq(Collections.emptySet()));
 verify(mock).put(eq(2), eq(Collections.emptySet()));
 }
}