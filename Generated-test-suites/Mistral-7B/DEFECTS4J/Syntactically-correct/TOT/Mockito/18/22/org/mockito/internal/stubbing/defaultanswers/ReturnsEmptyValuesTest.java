package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*; // for BDD-style tests, if preferred

public class ReturnsEmptyValuesTest {

 @Test
 public void testToString() {
 Mock <String> mock = mock(String.class);
 when(mock.toString()).then(new ReturnsEmptyValues());

 assertEquals("Mock for String, hashCode: " + mock.hashCode(), mock.toString());
 }

 @Test
 public void testCompareTo() {
 Mock <Integer> mock1 = mock(Integer.class);
 Mock <Integer> mock2 = mock(Integer.class);

 given(mock1.compareTo(ArgumentMatchers.anyInt())).thenReturn(1);
 given(mock2.compareTo(ArgumentMatchers.anyInt())).thenReturn(1);

 assertEquals(1, mock1.compareTo(mock2));
 }

 @Test
 public void testEmptyCollection() {
 Mock <Collection<String>> mock = mock(Collection.class);

 assertEquals(Collections.emptyList(), mock.asList());
 assertEquals(Collections.emptySet(), mock.asSet());
 assertEquals(Collections.emptyMap().values(), mock.asMap().values());
 }

 @Test
 public void testPrimitiveTypes() {
 Mock <Byte> mockByte = mock(Byte.class);
 Mock <Short> mockShort = mock(Short.class);
 Mock <Integer> mockInt = mock(Integer.class);
 Mock <Long> mockLong = mock(Long.class);
 Mock <Float> mockFloat = mock(Float.class);
 Mock <Double> mockDouble = mock(Double.class);
 Mock <Character> mockChar = mock(Character.class);
 Mock <Boolean> mockBoolean = mock(Boolean.class);

 assertEquals((byte) 0, mockByte.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isPrimitiveType(invocation.getMethod().getReturnType())))));
 assertEquals((short) 0, mockShort.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isPrimitiveType(invocation.getMethod().getReturnType())))));
 assertEquals(0, mockInt.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isPrimitiveType(invocation.getMethod().getReturnType())))));
 assertEquals(0L, mockLong.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isPrimitiveType(invocation.getMethod().getReturnType())))));
 assertEquals(0f, mockFloat.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isPrimitiveType(invocation.getMethod().getReturnType())))));
 assertEquals(0d, mockDouble.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isPrimitiveType(invocation.getMethod().getReturnType())))));
 assertEquals('\u0000', mockChar.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isPrimitiveType(invocation.getMethod().getReturnType())))));
 assertEquals(false, mockBoolean.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isPrimitiveType(invocation.getMethod().getReturnType())))));
 }

 @Test
 public void testEmptyList() {
 Mock <List<String>> mock = mock(List.class);

 List<String> list = new ArrayList<>();
 list.add("test");

 given(mock.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isListType(invocation.getMethod().getReturnType()))))).willReturn(list);

 assertEquals(Collections.emptyList(), mock.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isListType(invocation.getMethod().getReturnType())))));
 }

 @Test
 public void testEmptySet() {
 Mock <Set<String>> mock = mock(Set.class);

 Set<String> set = new HashSet<>();
 set.add("test");

 given(mock.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isSetType(invocation.getMethod().getReturnType()))))).willReturn(set);

 assertEquals(Collections.emptySet(), mock.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isSetType(invocation.getMethod().getReturnType())))));
 }

 @Test
 public void testEmptyMap() {
 Mock <Map<String, String>> mock = mock(Map.class);

 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");

 given(mock.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isMapType(invocation.getMethod().getReturnType()))))).willReturn(map);

 assertEquals(Collections.emptyMap(), mock.answer(argThat(isA(InvocationOnMock.class).and(invocation -> methodsGuru.isMapType(invocation.getMethod().getReturnType())))));
 }

 @Test
 public void testNull() {
 Mock <Object> mock = mock(Object.class);

 assertEquals(null, mock.answer(argThat(isA(InvocationOnMock.class))));
 }
}