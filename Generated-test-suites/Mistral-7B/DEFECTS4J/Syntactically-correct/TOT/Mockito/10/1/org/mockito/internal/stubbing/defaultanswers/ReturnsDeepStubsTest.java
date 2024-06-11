package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReturnsDeepStubsTest {

 @Test
 void testReturnsDeepStubForSimpleType() {
 Mockito.initMocks(this.getClass());
 Object mockList = mock(List.class, new ReturnsDeepStubs());
 List<String> list = (List<String>) mockList;
 list.add("foo");
 list.add("bar");

 List<String> anotherList = (List<String>) mockList;
 Assertions.assertEquals(Arrays.asList("foo", "bar"), anotherList);

 verify(mockList).add(ArgumentMatchers.anyString());
 verify(mockList).size();
 }

 @Test
 void testReturnsDeepStubForComplexType() {
 Mockito.initMocks(this.getClass());
 @SuppressWarnings("unchecked")
 Map<String, Set<Number>> mockMap = (Map<String, Set<Number>>) mock(Map.class, new ReturnsDeepStubs());
 String key = "test";
 Set<Number> set = mock(Set.class, new ReturnsDeepStubs());
 mockMap.put(key, set);

 Set<Number> anotherSet = (Set<Number>) mockMap.get(key);
 Assertions.assertTrue(anotherSet.isEmpty());

 verify(mockMap).put(ArgumentMatchers.eq(key), ArgumentMatchers.isInstanceOf(Set.class));
 }

 @Test
 void testReturnsDeepStubForGenericInterface() {
 Mockito.initMocks(this.getClass());
 @SuppressWarnings("unchecked")
 InterfaceNest<String> mockInterface = (InterfaceNest<String>) mock(InterfaceNest.class, new ReturnsDeepStubs());

 InterfaceNest<Integer> deepStub = (InterfaceNest<Integer>) mockInterface;
 Assertions.assertNotNull(deepStub.getNestedMap());

 verify(mockInterface).getNestedMap();
 }

 @Test
 void testVerificationWithDefaultAnswer() {
 Mockito.initMocks(this.getClass());
 @SuppressWarnings("unchecked")
 Object mockObject = mock(Object.class, new ReturnsDeepStubs());

 verify(mockObject).hashCode();
 }

 @Test
 void testNestedVerificationWithDefaultAnswer() {
 Mockito.initMocks(this.getClass());
 @SuppressWarnings("unchecked")
 Object mockObject = mock(Object.class, new ReturnsDeepStubs());

 Map<String, Object> mockMap = (Map<String, Object>) mockObject;
 mockMap.put("key", "value");

 verify(mockMap).get("key");
 }

 @Test
 void testMultipleVerificationsWithDefaultAnswer() {
 Mockito.initMocks(this.getClass());
 @SuppressWarnings("unchecked")
 Object mockObject = mock(Object.class, new ReturnsDeepStubs());

 Mockito.verify(mockObject).hashCode();
 Mockito.verify(mockObject).toString();
 }

 @Test
 void testDeepStubbingWithFunction() {
 Mockito.initMocks(this.getClass());
 Function<String, String> function = mock(Function.class, new ReturnsDeepStubs());

 Function<String, String> deepStub = (Function<String, String>) function;
 Assertions.assertNotNull(deepStub);

 String result = deepStub.apply("test");
 Assertions.assertNotNull(result);

 verify(function).apply(ArgumentMatchers.anyString());
 }

 @Test
 void testDeepStubbingWithIgnoredInvocations() {
 Mockito.initMocks(this.getClass());
 Object mockObject = mock(Object.class, new ReturnsDeepStubs());
 Mockito.doThrow(new RuntimeException("Exception")).when(mockObject).ignoreMe();
 Mockito.doAnswer(answer -> { throw new RuntimeException("AnswerException"); }).when(mockObject).answerMe();

 try {
 mockObject.ignoreMe();
 } catch (RuntimeException e) {
 Assertions.assertEquals("Exception", e.getMessage());
 }

 try {
 mockObject.answerMe();
 Assertions.fail();
 } catch (RuntimeException e) {
 // expected
 }

 verify(mockObject).ignoreMe();
 verify(mockObject, Mockito.never()).answerMe();
 }

 @Test
 void testDeepStubbingWithAnswer() {
 Mockito.initMocks(this.getClass());
 Object mockObject = mock(Object.class, new ReturnsDeepStubs());
 Mockito.when(mockObject.answerMe()).thenReturn("answer");

 String result = (String) mockObject.answerMe();
 Assertions.assertEquals("answer", result);

 verify(mockObject).answerMe();
 }
}