package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeConstructor() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
 assertEquals(erasedType, recursiveType.getRawClass());
 Assert.assertNull(recursiveType.getSelfReferencedType());
 }

 @Test
 public void testSetReference() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
 JavaType referencedType = new TypeReference<List<String>>(){}.getType();
 recursiveType.setReference(referencedType);

 JavaType selfReferencedType = recursiveType.getSelfReferencedType();
 assertEquals(referencedType, selfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
 JavaType referencedType = new TypeReference<List<String>>(){}.getType();
 recursiveType.setReference(referencedType);
 recursiveType.setReference(new TypeReference<List<String>>(){}.getType());
 }

 @Test
 public void testGetGenericSignature() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
 StringBuilder sb = new StringBuilder();
 JavaType referencedType = new TypeReference<List<String>>(){}.getType();
 recursiveType.setReference(referencedType);

 StringBuilder expected = new StringBuilder("[recursive type; [list, java.lang.String]]");
 recursiveType.getGenericSignature(sb);
 assertEquals(expected.toString(), sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
 JavaType referencedType = new TypeReference<List<String>>(){}.getType();
 recursiveType.setReference(referencedType);

 StringBuilder sb = new StringBuilder();
 recursiveType.getErasedSignature(sb);
 assertEquals("java.util.List<java.lang.String>", sb.toString());
 }

 @Test
 public void testWithContentType() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 JavaType contentType = new TypeReference<String>(){}.getType();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

 JavaType result = recursiveType.withContentType(contentType);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

 JavaType result = recursiveType.withTypeHandler(null);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

 JavaType result = recursiveType.withContentTypeHandler(null);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithValueHandler() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

 JavaType result = recursiveType.withValueHandler(null);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

 JavaType result = recursiveType.withContentValueHandler(null);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

 JavaType result = recursiveType.withStaticTyping();
 assertEquals(recursiveType, result);
 }

 @Test
 public void testNarrow() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
 Class<?> subclass = String.class;

 JavaType result = recursiveType.withStaticTyping()._narrow(subclass);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testRefine() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
 Class<?> rawType = List.class;
 TypeBindings newBindings = new TypeBindings();
 JavaType superClass = new TypeReference<Object>(){}.getType();
 JavaType[] superInterfaces = new JavaType[0];

 JavaType result = recursiveType.refine(rawType, newBindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testIsContainerType() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

 boolean result = recursiveType.isContainerType();
 assertFalse(result);
 }

 @Test
 public void testToString() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
 JavaType referencedType = new TypeReference<List<String>>(){}.getType();
 recursiveType.setReference(referencedType);

 String expected = "[recursive type; [list, java.lang.String]]";
 String result = recursiveType.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testEquals() {
 Class<?> erasedType1 = Object.class;
 Class<?> erasedType2 = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(erasedType1, bindings);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(erasedType2, bindings);
 JavaType referencedType = new TypeReference<List<String>>(){}.getType();
 recursiveType1.setReference(referencedType);
 JavaType referencedType2 = new TypeReference<List<String>>(){}.getType();
 recursiveType2.setReference(referencedType2);

 assertTrue(recursiveType1.equals(recursiveType1));
 assertTrue(recursiveType2.equals(recursiveType2));
 assertTrue(recursiveType1.equals(recursiveType2));
 }

 @Test
 public void testEqualsDifferentClasses() {
 Class<?> erasedType1 = Object.class;
 Class<?> erasedType2 = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(erasedType1, bindings);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(erasedType2, bindings);

 assertFalse(recursiveType1.equals(recursiveType2));
 }

 @Test
 public void testEqualsNull() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);

 assertFalse(recursiveType.equals(null));
 }

 @Test
 public void testEqualsUnresolved() {
 Class<?> erasedType = Object.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType unresolved = new ResolvedRecursiveType(erasedType, bindings);
 unresolved.setReference(null);

 assertFalse(recursiveType.equals(unresolved));
 }
}