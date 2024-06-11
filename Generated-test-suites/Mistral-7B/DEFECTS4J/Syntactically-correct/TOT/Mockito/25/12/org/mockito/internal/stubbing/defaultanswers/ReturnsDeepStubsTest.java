package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Map<String, Map<String, Number>> mockMap;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 mockMap = new HashMap<>();
 }

 @Test
 public void testReturnsDeepStubForSingleLevelGenericType() {
 Map<String, Number> map = mock(Map.class, new ReturnsDeepStubs());
 Number number = map.get("key");
 assertEquals(number, map.get("key"));
 }

 @Test
 public void testReturnsDeepStubForNestedGenericType() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<String> genericsNest = mock(GenericsNest.class, new ReturnsDeepStubs());
 Number number = genericsNest.entrySet().iterator().next().getValue().iterator().next();
 assertEquals(number, genericsNest.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void testReturnsDeepStubForPrimitiveTypes() {
 Map<Integer, Integer> map = mock(Map.class, new ReturnsDeepStubs());
 Integer integer = map.get(1);
 assertEquals(integer, map.get(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testThrowsExceptionForNonMockableType() {
 String string = new String("test");
 when(mockMap.get(ArgumentMatchers.anyString())).thenReturn(string);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMethod().setReturnType(String.class);
 when(returnsDeepStubs.answer(invocation)).thenCallRealMethod();
 returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testReturnsEmptyValuesForNonMockableType() {
 when(mockMap.get(ArgumentMatchers.anyString())).thenReturn((Map<?, ?>) Collections.emptyMap());
 Number number = (Number) returnsDeepStubs.answer(mock(InvocationOnMock.class).withMethod(Mockito.method(Map.class, "get", ArgumentMatchers.anyString())));
 assertEquals(null, number);
 }

 @Test
 public void testVerifiesDeepStubs() {
 Map<String, Number> map = mock(Map.class, new ReturnsDeepStubs());
 Number number = map.get("key");
 map.put("key", number);
 verify(map).put(ArgumentMatchers.eq("key"), ArgumentMatchers.eq(number));
 }

 @Test
 public void testDeepStubsCanBeRetrievedMultipleTimes() {
 Map<String, Number> map = mock(Map.class, new ReturnsDeepStubs());
 Number number1 = map.get("key1");
 Number number2 = map.get("key1");
 assertEquals(number1, number2);
 }

 @Test
 public void testDeepStubsCanBeRetrievedThroughNestedInvocations() {
 Map<String, Map<String, Number>> nestedMap = mock(Map.class, new ReturnsDeepStubs());
 Map<String, Number> map = nestedMap.get("key");
 Number number1 = map.get("key1");
 Number number2 = map.get("key1");
 assertEquals(number1, number2);
 }

 @Test
 public void testDeepStubsCanBeUsedAsValuesInCollections() {
 Map<String, Number> map = mock(Map.class, new ReturnsDeepStubs());
 Number number = map.get("key");
 assertEquals(number, Arrays.asList(map.values().toArray()[0]));
 }

 @Test
 public void testDeepStubsCanBeUsedAsKeysInCollections() {
 Map<Number, Number> map = mock(Map.class, new ReturnsDeepStubs());
 Number number1 = map.keySet().iterator().next();
 Number number2 = map.keySet().iterator().next();
 assertEquals(number1, number2);
 }
}