package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeCreation() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertNull(type.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testResolvedRecursiveTypeDuplicateReferenceSet() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 type.setReference(new TypeReference<>() {}.getType());
 type.setReference(new TypeReference<>() {}.getType());
 }

 @Test
 public void testResolvedRecursiveTypeGetSelfReferencedType() {
 JavaType referenceType = new TypeReference<>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 type.setReference(referenceType);
 assertEquals(referenceType, type.getSelfReferencedType());
 }

 @Test
 public void testResolvedRecursiveTypeGetGenericSignature() {
 StringBuilder stringBuilder = new StringBuilder();
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 type.getGenericSignature(stringBuilder);
 String expected = "[recursive type; UNRESOLVED]";
 assertEquals(expected, stringBuilder.toString());

 JavaType referenceType = new TypeReference<>() {}.getType();
 type.setReference(referenceType);
 type.getGenericSignature(stringBuilder);
 expected = "[recursive type; " + referenceType.getErasedSignature() + "]";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testResolvedRecursiveTypeGetErasedSignature() {
 StringBuilder stringBuilder = new StringBuilder();
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 type.getErasedSignature(stringBuilder);
 String expected = "ResolvedRecursiveType";
 assertEquals(expected, stringBuilder.toString());

 JavaType referenceType = new TypeReference<>() {}.getType();
 type.setReference(referenceType);
 type.getErasedSignature(stringBuilder);
 expected = "[recursive type; " + referenceType.getErasedSignature() + "]";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testResolvedRecursiveTypeWithContentType() {
 JavaType contentType = new TypeReference<Integer[][]>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertEquals(type, type.withContentType(contentType));
 }

 @Test
 public void testResolvedRecursiveTypeWithTypeHandler() {
 Object handler = new Object();
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertEquals(type, type.withTypeHandler(handler));
 }

 @Test
 public void testResolvedRecursiveTypeWithContentTypeHandler() {
 Object handler = new Object();
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertEquals(type, type.withContentTypeHandler(handler));
 }

 @Test
 public void testResolvedRecursiveTypeWithValueHandler() {
 Object handler = new Object();
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertEquals(type, type.withValueHandler(handler));
 }

 @Test
 public void testResolvedRecursiveTypeWithContentValueHandler() {
 Object handler = new Object();
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertEquals(type, type.withContentValueHandler(handler));
 }

 @Test
 public void testResolvedRecursiveTypeWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertEquals(type, type.withStaticTyping());
 }

 @Test
 public void testResolvedRecursiveTypeNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertNull(type._narrow(Object.class));
 }

 @Test
 public void testResolvedRecursiveTypeRefine() {
 Class<?> rawType = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = new TypeReference<ResolvedRecursiveType>() {}.getType();
 JavaType[] superInterfaces = new JavaType[0];
 assertNull(new ResolvedRecursiveType(rawType, bindings).refine(rawType, bindings, superClass, superInterfaces));
 }

 @Test
 public void testResolvedRecursiveTypeIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testResolvedRecursiveTypeToString() {
 StringBuilder stringBuilder = new StringBuilder();
 ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 type.toString(stringBuilder);
 String expected = "[recursive type; UNRESOLVED]";
 assertEquals(expected, stringBuilder.toString());

 JavaType referenceType = new TypeReference<>() {}.getType();
 type.setReference(referenceType);
 expected = "[recursive type; " + referenceType.getErasedSignature() + "]";
 type.toString(stringBuilder);
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testResolvedRecursiveTypeEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 assertFalse(type1.equals(type2));

 JavaType referenceType = new TypeReference<>() {}.getType();
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 type3.setReference(referenceType);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
 type4.setReference(referenceType);
 assertTrue(type3.equals(type4));
 }
}