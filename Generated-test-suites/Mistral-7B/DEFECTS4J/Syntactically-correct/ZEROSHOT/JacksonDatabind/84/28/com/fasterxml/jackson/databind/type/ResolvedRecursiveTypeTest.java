package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeWithNullReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Assert.assertNull(type.getSelfReferencedType());
 }

 @Test
 public void testResolvedRecursiveTypeWithSetReferencedType() {
 JavaType referencedType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 type.setReference(referencedType);
 JavaType selfReferencedType = type.getSelfReferencedType();
 assertEquals(referencedType, selfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testResolvedRecursiveTypeSetReferencedTypeTwice() {
 JavaType referencedType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 type.setReference(referencedType);
 try {
 type.setReference(referencedType);
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testResolvedRecursiveTypeGetGenericSignature() {
 StringBuilder expectedSB = new StringBuilder("[recursive type; ");
 JavaType referencedType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 type.setReference(referencedType);
 StringBuilder actualSB = type.getGenericSignature(new StringBuilder());
 assertEquals(expectedSB.toString(), actualSB.toString());
 }

 @Test
 public void testResolvedRecursiveTypeGetErasedSignature() {
 StringBuilder expectedSB = new StringBuilder("[recursive type; ");
 JavaType referencedType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 type.setReference(referencedType);
 StringBuilder actualSB = type.getErasedSignature(new StringBuilder());
 assertEquals(expectedSB.toString(), actualSB.toString());
 }

 @Test
 public void testResolvedRecursiveTypeWithContentType() {
 JavaType contentType = new TypeReference<String>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType result = type.withContentType(contentType);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithTypeHandler() {
 Object handler = new Object() {};
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType result = type.withTypeHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithContentTypeHandler() {
 Object handler = new Object() {};
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType result = type.withContentTypeHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithValueHandler() {
 Object handler = new Object() {};
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType result = type.withValueHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithContentValueHandler() {
 Object handler = new Object() {};
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType result = type.withContentValueHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType result = type.withStaticTyping();
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Class<?> subclass = String[].class;
 JavaType result = type._narrow(subclass);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeRefine() {
 Class<?> rawType = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] { String[].class };
 ResolvedRecursiveType type = new ResolvedRecursiveType(rawType, bindings);
 JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testResolvedRecursiveTypeToString() {
 StringBuilder expectedSB = new StringBuilder("[recursive type; ");
 JavaType referencedType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 type.setReference(referencedType);
 StringBuilder actualSB = new StringBuilder();
 type.toString(actualSB);
 assertEquals(expectedSB.toString(), actualSB.toString());
 }

 @Test
 public void testResolvedRecursiveTypeEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type1.equals(type2));
 JavaType referencedType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
 type3.setReference(referencedType);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(Object.class, null);
 type4.setReference(referencedType);
 assertTrue(type3.equals(type4));
 assertTrue(type1.equals(type1));
 }

}