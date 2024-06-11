package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeCreation() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.class, null);

 assertTrue(type1 instanceof ResolvedRecursiveType);
 assertTrue(type2 instanceof ResolvedRecursiveType);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType reference = new SimpleType(String.class);
 type.setReference(reference);
 JavaType actualReference = type.getSelfReferencedType();
 assertEquals(reference, actualReference);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType reference1 = new SimpleType(String.class);
 JavaType reference2 = new SimpleType(Integer.class);
 type.setReference(reference1);
 type.setReference(reference2);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType reference = new SimpleType(String.class);
 type.setReference(reference);
 JavaType actualType = type.getSelfReferencedType();
 assertEquals(reference, actualType);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 StringBuilder expectedSignature = new StringBuilder("[recursive type; ");
 StringBuilder actualSignature = type.getGenericSignature(new StringBuilder());
 assertEquals(expectedSignature.toString(), actualSignature.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 StringBuilder expectedSignature = new StringBuilder("[recursive type; ");
 StringBuilder actualSignature = type.getErasedSignature(new StringBuilder());
 assertEquals(expectedSignature.toString(), actualSignature.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType contentType = new SimpleType(Integer.class);
 JavaType actualType = type.withContentType(contentType);
 assertEquals(type, actualType);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 JavaType actualType = type.withTypeHandler(handler);
 assertEquals(type, actualType);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 JavaType actualType = type.withContentTypeHandler(handler);
 assertEquals(type, actualType);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 JavaType actualType = type.withValueHandler(handler);
 assertEquals(type, actualType);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 JavaType actualType = type.withContentValueHandler(handler);
 assertEquals(type, actualType);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType actualType = type.withStaticTyping();
 assertEquals(type, actualType);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Class<?> subclass = String[].class;
 JavaType actualType = type.narrow(subclass);
 fail("_narrow method is deprecated and should not be called");
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Class<?> rawType = String.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = null;
 JavaType actualType = type.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(actualType);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 boolean actualResult = type.isContainerType();
 assertFalse(actualResult);
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 String expectedString = "[recursive type; UNRESOLVED]";
 String actualString = type.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 assertTrue(type1.equals(type1));
 assertTrue(type1.equals(type2));
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Integer.class, null);
 assertFalse(type1.equals(type3));
 }
}