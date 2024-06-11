package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<Integer> intType = Integer.TYPE;
 SimpleType simpleType = new SimpleType(intType);
 SimpleType simpleTypeConstruct = SimpleType.construct(intType);
 assertEquals(simpleType, simpleTypeConstruct);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructForMap() {
 SimpleType.construct(Map.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructForCollection() {
 SimpleType.construct(ArrayList.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructForArray() {
 SimpleType.construct(int[].class);
 }

 @Test
 public void testNarrowingType() {
 Class<Integer> intType = Integer.TYPE;
 Class<Integer> intSubType = Integer.TYPE;
 SimpleType simpleType = new SimpleType(intType);
 SimpleType narrowedType = simpleType._narrow(intSubType);
 assertEquals(simpleType, narrowedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowingToUnrelatedType() {
 Class<Integer> intType = Integer.TYPE;
 Class<Double> doubleType = Double.TYPE;
 SimpleType simpleType = new SimpleType(intType);
 simpleType._narrow(doubleType);
 }

 @Test
 public void testGetCanonicalName() {
 Class<String> stringClass = String.class;
 SimpleType simpleType = new SimpleType(stringClass);
 assertEquals("class java.lang.String", simpleType.buildCanonicalName());
 }

 @Test
 public void testWithContentType() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType javaType = new SimpleType(String[].class);
 simpleType.withContentType(javaType);
 assertNull(simpleType.withContentType(javaType));
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object handler = new Object();
 SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(handler);
 assertEquals(simpleType, simpleTypeWithHandler);
 simpleTypeWithHandler = simpleTypeWithHandler.withTypeHandler(handler);
 assertNotEquals(simpleType, simpleTypeWithHandler);
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType staticType = simpleType.withStaticTyping();
 assertTrue(staticType._asStatic);
 }

 @Test
 public void testRefine() {
 SimpleType simpleType = new SimpleType(Integer.class);
 assertNull(simpleType.refine(String.class, null, null, null));
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = new SimpleType(String.class);
 StringBuilder sb = new StringBuilder(10);
 simpleType.getErasedSignature(sb);
 assertEquals("class java.lang.String", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = new SimpleType(String[].class);
 StringBuilder sb = new StringBuilder(10);
 simpleType.getGenericSignature(sb);
 assertEquals("[Ljava.lang.String;", sb.toString());
 }

 @Test
 public void testEqualTypes() {
 Class<Integer> intType1 = Integer.TYPE;
 Class<Integer> intType2 = Integer.TYPE;
 SimpleType simpleType1 = new SimpleType(intType1);
 SimpleType simpleType2 = new SimpleType(intType2);
 SimpleType simpleType3 = new SimpleType(intType1);
 SimpleType simpleType4 = new SimpleType(intType2);
 assertEquals(simpleType1, simpleType3);
 assertNotEquals(simpleType1, simpleType4);
 }
}