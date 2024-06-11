package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testCreateResolvedRecursiveType() {
 Class<Integer> intType = Integer.TYPE;
 ResolvedRecursiveType intTypeInstance = createTestType(intType);
 assertEquals(intType, intTypeInstance.getRawClass());
 assertNull(intTypeInstance.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 Class<Integer> intType = Integer.TYPE;
 ResolvedRecursiveType intTypeInstance = createTestType(intType);
 JavaType javaType = createTestType(Integer.class);
 intTypeInstance.setReference(javaType);
 intTypeInstance.setReference(javaType);
 }

 @Test
 public void testGetSelfReferencedType() {
 Class<Integer> intType = Integer.TYPE;
 ResolvedRecursiveType intTypeInstance = createTestType(intType);
 JavaType javaType = createTestType(Integer.class);
 intTypeInstance.setReference(javaType);
 assertEquals(javaType, intTypeInstance.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 Class<Map<Integer, List<String>>> mapType = Map.class.getTypeParameters()[0].getGenericComponentType();
 Class<List<String>> listType = List.class.getTypeParameters()[0].getType();
 ResolvedRecursiveType mapTypeInstance = createTestType(Map.class);
 JavaType mapValueType = createTestType(Map.class).getGenericType(mapType);
 JavaType listTypeInstance = createTestType(List.class);
 mapTypeInstance.setReference(mapValueType);
 listTypeInstance.setReference(listType);
 StringBuilder sb = new StringBuilder();
 mapTypeInstance.getGenericSignature(sb);
 listTypeInstance.getGenericSignature(sb);
 String result = sb.toString();
 String expected = "[Ljava.util.List;[Ljava.lang.String;]";
 assertEquals(expected, result);
 }

 @Test
 public void testGetErasedSignature() {
 Class<Map<Integer, List<String>>> mapType = Map.class.getTypeParameters()[0].getGenericComponentType();
 Class<List<String>> listType = List.class.getTypeParameters()[0].getType();
 ResolvedRecursiveType mapTypeInstance = createTestType(Map.class);
 JavaType mapValueType = createTestType(Map.class).getGenericType(mapType);
 JavaType listTypeInstance = createTestType(List.class);
 mapTypeInstance.setReference(mapValueType);
 listTypeInstance.setReference(listType);
 StringBuilder sb = new StringBuilder();
 mapTypeInstance.getErasedSignature(sb);
 listTypeInstance.getErasedSignature(sb);
 String result = sb.toString();
 String expected = "Ljava/util/Map;Ljava/util/List;";
 assertEquals(expected, result);
 }

 @Test
 public void testEquals() {
 Class<Integer> intType = Integer.TYPE;
 ResolvedRecursiveType intTypeInstance1 = createTestType(intType);
 ResolvedRecursiveType intTypeInstance2 = createTestType(intType);
 JavaType javaType = createTestType(Integer.class);
 intTypeInstance1.setReference(javaType);
 ResolvedRecursiveType intTypeInstance3 = new ResolvedRecursiveType(intType, null);
 assertFalse(intTypeInstance1.equals(intTypeInstance2));
 assertFalse(intTypeInstance1.equals(intTypeInstance3));
 JavaType javaType2 = createTestType(Integer.class);
 intTypeInstance1.setReference(javaType2);
 intTypeInstance2.setReference(javaType);
 assertFalse(intTypeInstance1.equals(intTypeInstance2));
 intTypeInstance1.setReference(javaType);
 assertTrue(intTypeInstance1.equals(intTypeInstance2));
 }
}