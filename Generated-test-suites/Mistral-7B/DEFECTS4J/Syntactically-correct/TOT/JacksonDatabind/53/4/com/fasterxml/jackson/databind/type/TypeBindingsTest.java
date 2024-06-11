package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

 @Test
 public void testEmptyBindings() {
 TypeBindings bindings = TypeBindings.emptyBindings();
 assertArrayEquals(new String[0], bindings.getBoundNames());
 assertArrayEquals(JavaType.ValueType.VOID, bindings.getTypeParameters().toArray(new JavaType[0]));
 }

 @Test
 public void testCreateBindingsWithOneTypeParameter() {
 Class<List<Integer>> listIntType = new TypeReference<List<Integer>>() {}.getType();
 TypeBindings bindings = TypeBindings.create(listIntType, JavaType.of(int.class));
 String[] names = bindings.getBoundNames();
 JavaType[] types = bindings.getTypeParameters().toArray(new JavaType[0]);
 assertEquals("List", names[0]);
 assertEquals(JavaType.of(List.class), types[0]);
 assertEquals(JavaType.of(int.class), types[1]);
 }

 @Test
 public void testCreateBindingsWithTwoTypeParameters() {
 Class<Map<String, List<Integer>>> stringListMapType = new TypeReference<Map<String, List<Integer>>>() {}.getType();
 TypeBindings bindings = TypeBindings.create(stringListMapType, JavaType.of(String.class), JavaType.of(List.class));
 String[] names = bindings.getBoundNames();
 JavaType[] types = bindings.getTypeParameters().toArray(new JavaType[0]);
 assertEquals("Map", names[0]);
 assertEquals(JavaType.of(Map.class), types[0]);
 assertEquals("String", names[1]);
 assertEquals(JavaType.of(String.class), types[1]);
 assertEquals("List", names[2]);
 assertEquals(JavaType.of(List.class), types[2]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateBindingsWithMismatchingNumberOfTypeParameters() {
 Class<Map<String, List<Integer>>> stringListMapType = new TypeReference<Map<String, List<Integer>>>() {}.getType();
 TypeBindings bindings = TypeBindings.create(stringListMapType, JavaType.of(String.class), JavaType.of(int.class), JavaType.of(double.class));
 }

 @Test
 public void testCreateBindingsWithSelfReference() {
 Class<CircularType<Integer>> circularType = new TypeReference<CircularType<Integer>>() {}.getType();
 TypeBindings bindings = TypeBindings.create(circularType, JavaType.of(Integer.TYPE), null);
 JavaType selfType = bindings.withUnboundVariable("X").findBoundType("X");
 assertNotNull(selfType);
 assertEquals(circularType, selfType.getRawClass());
 }
}

class CircularType<T> {
 private final T value;
 private CircularType<T> next;

 public CircularType(T value) {
 this.value = value;
 }

 public CircularType<T> getNext() {
 return next;
 }

 public void setNext(CircularType<T> next) {
 this.next = next;
 }

 public T getValue() {
 return value;
 }
}