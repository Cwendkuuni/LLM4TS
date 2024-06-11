package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Map<String, Number> map;
 private Number number;

 @BeforeEach
 void setUp() {
 map = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 number = map.entrySet().iterator().next().getValue().iterator().next();
 returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
 }

 @Test
 void testWithValidArguments() {
 // Test with valid arguments
 Object result = map.remove(null);
 Assertions.assertNotNull(result);
 verify(map).remove(null);
 }

 @Test
 void testWithNullArguments() {
 // Test with null arguments
 Object result = map.remove(null);
 Assertions.assertNull(result);
 verify(map).remove(null);
 }

 @Test
 void testWithEmptyMap() {
 // Test with empty map
 Map<String, Number> emptyMap = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 Object result = emptyMap.remove(null);
 Assertions.assertNull(result);
 verify(emptyMap).remove(null);
 }

 @Test
 void testWithValidArgumentsAndException() {
 // Test with valid arguments and exception
 try {
 Object result = map.remove("key");
 throw new RuntimeException("Exception message");
 } catch (RuntimeException e) {
 // expected exception
 }
 verify(map).remove("key");
 }

 @Test
 void testDeepStubbing() {
 // Test deep stubbing
 Number expectedNumber = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 Number actualNumber = map.entrySet().iterator().next().getValue().iterator().next();
 verify(expectedNumber).equals(actualNumber);
 }

 @Test
 void testDeepStubbingWithNull() {
 // Test deep stubbing with null
 Number expectedNumber = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 Number actualNumber = map.entrySet().iterator().next() == null ? null : map.entrySet().iterator().next().getValue().iterator().next();
 verify(expectedNumber).equals(actualNumber);
 }

 @Test
 void testDeepStubbingWithEmptyMap() {
 // Test deep stubbing with empty map
 Number expectedNumber = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 Map<String, Number> emptyMap = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 Number actualNumber = emptyMap.entrySet().iterator().next() == null ? null : emptyMap.entrySet().iterator().next().getValue().iterator().next();
 verify(expectedNumber).equals(actualNumber);
 }

 @Test
 void testDeepStubbingWithException() {
 // Test deep stubbing with exception
 Number expectedNumber = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 Map<String, Number> map = mock(Map.class, new ReturnsDeepStubs() {
 @Override
 Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("Exception message");
 }
 });
 Number actualNumber = map.entrySet().iterator().next().getValue().iterator().next();
 verify(expectedNumber).equals(actualNumber);
 }

 @Test
 void testDeepStubbingWithNestedGenericInfo() {
 // Test deep stubbing with nested generic information
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }
 GenericsNest<Number> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 Number number = mock.entrySet().iterator().next().getValue().iterator().next();
 verify(number).equals(number);
 }

 @Test
 void testDeepStubbingWithNestedGenericInfoAndException() {
 // Test deep stubbing with nested generic information and exception
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }
 GenericsNest<Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs() {
 @Override
 Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("Exception message");
 }
 });
 Number number = mock.entrySet().iterator().next().getValue().iterator().next();
 try {
 verify(number).equals(number);
 } catch (RuntimeException e) {
 // expected exception
 }
 }
}