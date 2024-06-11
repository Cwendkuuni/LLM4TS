package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockA;
 private Object mockB;
 private Object mockC;
 private Object mockD;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mockA = mock(Map.class, returnsDeepStubs);
 mockB = mock(Number.class);
 mockC = mock(Number.class);
 mockD = mock(Map.class, returnsDeepStubs);
 }

 @Test
 public void testDeepStubbingWithMaps() {
 Map<Integer, Map<Number, Number>> map = (Map<Integer, Map<Number, Number>>) mockA;
 map.put(1, new HashMap<>());
 Map<Number, Number> nestedMap = map.get(1);
 nestedMap.put(mockB, mockC);

 assertEquals(mockC, map.get(1).get(mockB));

 // Verification
 verify(mockB).equals(ArgumentMatchers.any());
 verify(mockC).equals(ArgumentMatchers.any());
 }

 @Test
 public void testDeepStubbingWithNestedClasses() {
 GenericsNest<?> genericNest = mock(GenericsNest.class, returnsDeepStubs);
 genericNest.entrySet().iterator().next().getValue().iterator().next();

 // Test error scenario
 try {
 genericNest.entrySet().iterator().next().getValue().iterator().next().equals(null);
 } catch (NullPointerException e) {
 // Expected
 }

 // Verification
 verify(genericNest).entrySet();
 verify(genericNest.entrySet()).iterator();
 verify(genericNest.entrySet().iterator()).next();
 verify(genericNest.entrySet().iterator().getValue()).iterator();
 verify(genericNest.entrySet().iterator().getValue().iterator()).next();
 }

 @Test
 public void testDeepStubbingWithCustomAnswer() {
 Answer<Object> customAnswer = Mockito.answer(invocation -> "Custom answer");
 Object customMock = Mockito.mock(String.class, customAnswer);

 when(mockD.get(ArgumentMatchers.anyInt())).thenReturn(customMock);

 assertEquals("Custom answer", mockD.get(1));

 // Verification
 verify(customMock).equals(ArgumentMatchers.any());
 }
}