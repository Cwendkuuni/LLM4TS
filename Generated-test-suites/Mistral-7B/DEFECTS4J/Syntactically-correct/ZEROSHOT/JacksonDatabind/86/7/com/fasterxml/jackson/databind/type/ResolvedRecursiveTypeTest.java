package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType javaType;

 @Before
 public void setUp() {
 Class<?> clazz = RecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType erasedType = TypeFactory.defaultInstance().constructType(clazz);
 javaType = new ResolvedRecursiveType(erasedType, bindings);
 }

 @Test
 public void testSetReference() {
 JavaType referenceType = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType recursiveType = (ResolvedRecursiveType) javaType;
 recursiveType.setReference(referenceType);
 ResolvedRecursiveType expectedType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 expectedType.setReference(referenceType);
 assertEquals(expectedType, javaType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType referenceType = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType recursiveType = (ResolvedRecursiveType) javaType;
 recursiveType.setReference(referenceType);
 recursiveType.setReference(referenceType);
 }

 @Test
 public void testGetSelfReferencedType() {
 assertNull(javaType.getSelfReferencedType());
 JavaType referenceType = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType recursiveType = (ResolvedRecursiveType) javaType;
 recursiveType.setReference(referenceType);
 assertEquals(referenceType, javaType.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType javaListStringType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>(){}.getType());
 JavaType recursiveType = new ResolvedRecursiveType(javaListStringType.getRawClass(), bindings);
 recursiveType.setReference(javaStringType);
 recursiveType.getGenericSignature(sb);
 String expectedSignature = "[Ljava.lang.String;";
 assertEquals(expectedSignature, sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType javaListStringType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>(){}.getType());
 JavaType recursiveType = new ResolvedRecursiveType(javaListStringType.getRawClass(), bindings);
 recursiveType.setReference(javaStringType);
 recursiveType.getErasedSignature(sb);
 String expectedSignature = "[Ljava.lang.Object;";
 assertEquals(expectedSignature, sb.toString());
 }

 @Test
 public void testWithContentType() {
 JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 JavaType expectedType = recursiveType;
 assertEquals(expectedType, recursiveType.withContentType(contentType));
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object();
 JavaType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 JavaType expectedType = recursiveType;
 assertEquals(expectedType, recursiveType.withTypeHandler(handler));
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object();
 JavaType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 JavaType expectedType = recursiveType;
 assertEquals(expectedType, recursiveType.withContentTypeHandler(handler));
 }

 @Test
 public void testWithValueHandler() {
 Object handler = new Object();
 JavaType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 JavaType expectedType = recursiveType;
 assertEquals(expectedType, recursiveType.withValueHandler(handler));
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = new Object();
 JavaType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 JavaType expectedType = recursiveType;
 assertEquals(expectedType, recursiveType.withContentValueHandler(handler));
 }

 @Test
 public void testWithStaticTyping() {
 JavaType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 JavaType expectedType = recursiveType;
 assertEquals(expectedType, recursiveType.withStaticTyping());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrow() {
 Class<?> subclass = String[].class;
 JavaType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 recursiveType.narrow(subclass);
 }

 @Test
 public void testRefine() {
 Class<?> rawType = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType expectedType = null;
 JavaType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 JavaType result = recursiveType.refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(expectedType, result);
 }

 @Test
 public void testIsContainerType() {
 boolean expected = false;
 assertTrue(javaType.isContainerType());
 assertEquals(expected, javaType.getClass().isInstance(expected));
 }

 @Test
 public void testToString() {
 StringBuilder sb = new StringBuilder();
 JavaType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 recursiveType.toString(sb);
 String expectedString = "[recursive type; UNRESOLVED]";
 assertEquals(expectedString, sb.toString());
 JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
 recursiveType.setReference(stringType);
 sb.setLength(0);
 recursiveType.toString(sb);
 expectedString = "[recursive type; "+stringType.getRawClass().getName()+"]";
 assertEquals(expectedString, sb.toString());
 }

 @Test
 public void testEquals() {
 JavaType referenceType = TypeFactory.defaultInstance().constructType(String.class);
 ResolvedRecursiveType recursiveType = (ResolvedRecursiveType) javaType;
 ResolvedRecursiveType recursiveTypeWithReference = new ResolvedRecursiveType(javaType.getRawClass(), bindings);
 recursiveTypeWithReference.setReference(referenceType);
 Object other = new Object();
 assertFalse(javaType.equals(other));
 assertTrue(javaType.equals(javaType));
 assertTrue(javaType.equals(recursiveType));
 assertTrue(javaType.equals(recursiveTypeWithReference));
 assertFalse(javaType.equals(referenceType));
 }
}