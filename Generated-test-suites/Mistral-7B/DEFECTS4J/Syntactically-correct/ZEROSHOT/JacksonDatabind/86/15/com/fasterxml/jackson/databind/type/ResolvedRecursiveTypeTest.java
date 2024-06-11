package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeCreation() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertEquals("java.lang.String", type.getErasedSignature(new StringBuilder()).toString());
 assertNull(type.getSelfReferencedType());
 }

 @Test
 public void testResolvedRecursiveTypeSettingReference() {
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 type.setReference(refType);
 JavaType selfRefType = type.getSelfReferencedType();
 assertEquals("class java.util.List", selfRefType.getRawClass().getName());
 assertEquals(refType, selfRefType);
 }

 @Test(expected = IllegalStateException.class)
 public void testResolvedRecursiveTypeSettingReferenceTwice() {
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 type.setReference(refType);
 type.setReference(new TypeReference<String>() {}.getType());
 }

 @Test
 public void testResolvedRecursiveTypeGetSignatures() {
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 type.setReference(refType);

 StringBuilder erasedSig = new StringBuilder(100);
 StringBuilder genericSig = new StringBuilder(100);
 type.getErasedSignature(erasedSig);
 type.getGenericSignature(genericSig);

 assertEquals("java.lang.String", erasedSig.toString());
 assertEquals("java.util.List<java.lang.Integer>", genericSig.toString());
 }

 @Test
 public void testResolvedRecursiveTypeWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType contentType = new TypeReference<Integer>() {}.getType();
 JavaType result = type.withContentType(contentType);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object() {};
 JavaType result = type.withTypeHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object() {};
 JavaType result = type.withContentTypeHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object() {};
 JavaType result = type.withValueHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object() {};
 JavaType result = type.withContentValueHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType result = type.withStaticTyping();
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeDeprecatedNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Class<?> subclass = String[].class;
 JavaType result = type._narrow(subclass);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveTypeRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 Class<?> rawType = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[] { new TypeReference<Integer>() {}.getType() };
 JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(type.isContainerType());
 }

 @Test
 public void testResolvedRecursiveTypeToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 type.setReference(refType);
 assertEquals("[recursive type; java.util.List]", type.toString());
 }

 @Test
 public void testResolvedRecursiveTypeEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(type1.equals(type2));

 JavaType refType = new TypeReference<List<Integer>>() {}.getType();
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, new TypeBindings());
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(String.class, new TypeBindings());
 type3.setReference(refType);
 type4.setReference(refType);
 assertTrue(type3.equals(type4));
 assertTrue(type3.equals(type1.equals(type4)));
 }
}