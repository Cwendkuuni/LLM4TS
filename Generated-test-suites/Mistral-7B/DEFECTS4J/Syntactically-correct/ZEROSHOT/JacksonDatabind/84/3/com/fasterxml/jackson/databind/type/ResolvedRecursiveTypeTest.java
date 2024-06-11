package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeCreation() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 assertEquals(type, resolvedType.getRawClass());
 assertNull(resolvedType.getSelfReferencedType());
 }

 @Test
 public void testSettingSelfReferencedType() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 JavaType referencedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 resolvedType.setReference(referencedType);
 ResolvedRecursiveType expectedType = new ResolvedRecursiveType(type, bindings);
 expectedType.setReference(referencedType);
 assertEquals(expectedType, resolvedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSettingSelfReferencedTypeTwice() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 JavaType referencedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 resolvedType.setReference(referencedType);
 resolvedType.setReference(referencedType);
 }

 @Test
 public void testGetSelfReferencedType() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 JavaType referencedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 resolvedType.setReference(referencedType);
 JavaType expectedType = referencedType;
 JavaType actualType = resolvedType.getSelfReferencedType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericSignature() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 StringBuilder sb = new StringBuilder(100);
 JavaType expectedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 String expectedSignature = expectedType.getGenericSignature(sb).toString();
 String actualSignature = resolvedType.getGenericSignature(sb).toString();
 assertEquals(expectedSignature, actualSignature);
 }

 @Test
 public void testGetErasedSignature() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 StringBuilder sb = new StringBuilder(100);
 JavaType expectedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 String expectedSignature = expectedType.getErasedSignature(sb).toString();
 String actualSignature = resolvedType.getErasedSignature(sb).toString();
 assertEquals(expectedSignature, actualSignature);
 }

 @Test
 public void testWithContentType() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 JavaType contentType = new TypeReference<String>() {}.getType();
 JavaType expectedType = resolvedType;
 JavaType actualType = resolvedType.withContentType(contentType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testWithTypeHandler() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 Object handler = new Object();
 JavaType expectedType = resolvedType;
 JavaType actualType = resolvedType.withTypeHandler(handler);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 Object handler = new Object();
 JavaType expectedType = resolvedType;
 JavaType actualType = resolvedType.withContentTypeHandler(handler);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testWithValueHandler() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 Object handler = new Object();
 JavaType expectedType = resolvedType;
 JavaType actualType = resolvedType.withValueHandler(handler);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testWithContentValueHandler() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 Object handler = new Object();
 JavaType expectedType = resolvedType;
 JavaType actualType = resolvedType.withContentValueHandler(handler);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testWithStaticTyping() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 JavaType expectedType = resolvedType;
 JavaType actualType = resolvedType.withStaticTyping();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testNarrow() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 Class<String> subclass = String.class;
 JavaType expectedType = resolvedType;
 JavaType actualType = resolvedType._narrow(subclass);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRefine() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 Class<String> rawType = String.class;
 TypeBindings newBindings = new TypeBindings();
 JavaType superClass = new TypeReference<Object>() {}.getType();
 JavaType[] superInterfaces = new JavaType[] {new TypeReference<Object>() {}.getType()};
 JavaType expectedException = new UnsupportedOperationException();
 JavaType actualType = resolvedType.refine(rawType, newBindings, superClass, superInterfaces);
 assertEquals(expectedException, actualType);
 }

 @Test
 public void testIsContainerType() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 boolean expected = false;
 boolean actual = resolvedType.isContainerType();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 JavaType referencedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 resolvedType.setReference(referencedType);
 String expected = "[recursive type; " + referencedType.getRawClass().getName() + "]";
 String actual = resolvedType.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(type, bindings);
 JavaType referencedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 resolvedType1.setReference(referencedType);
 ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(type, bindings);
 resolvedType2.setReference(referencedType);
 boolean expected = true;
 boolean actual = resolvedType1.equals(resolvedType2);
 assertEquals(expected, actual);
 }

 @Test
 public void testEqualsDifferentTypes() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(type, bindings);
 JavaType javaType2 = new TypeReference<Integer>() {}.getType();
 boolean expected = false;
 boolean actual = resolvedType1.equals(javaType2);
 assertEquals(expected, actual);
 }

 @Test
 public void testEqualsNull() {
 Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(type, bindings);
 boolean expected = false;
 boolean actual = resolvedType.equals(null);
 assertEquals(expected, actual);
 }
}