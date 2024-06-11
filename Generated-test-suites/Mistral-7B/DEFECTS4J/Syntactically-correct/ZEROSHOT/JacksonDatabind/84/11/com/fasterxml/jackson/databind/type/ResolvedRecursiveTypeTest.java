package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeConstruction() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.class, null);

 assertEquals("ResolvedRecursiveType for String", type1.getErasedType(), String.class);
 assertEquals("ResolvedRecursiveType for Integer", type2.getErasedType(), Integer.class);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType ref = new TypeReference<List<String>>() {}.getType();
 type.setReference(ref);

 JavaType selfRefType = type.getSelfReferencedType();

 assertEquals("Self referenced type should match the set reference", ref, selfRefType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType ref1 = new TypeReference<List<String>>() {}.getType();
 JavaType ref2 = new TypeReference<List<List<String>>>() {}.getType();

 type.setReference(ref1);
 type.setReference(ref2);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 StringBuilder sb = new StringBuilder(10);
 type.getGenericSignature(sb);

 String signature = sb.toString();
 assertEquals("Signature should be empty", "", signature);
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 StringBuilder sb = new StringBuilder(10);
 type.getErasedSignature(sb);

 String signature = sb.toString();
 assertEquals("Signature should match the erased type", String.class.getName(), signature);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType contentType = new TypeReference<List<String>>() {}.getType();

 JavaType result = type.withContentType(contentType);

 assertEquals("Result should match the original type", type, result);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);

 JavaType result = type.withTypeHandler(null);

 assertEquals("Result should match the original type", type, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);

 JavaType result = type.withContentTypeHandler(null);

 assertEquals("Result should match the original type", type, result);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);

 JavaType result = type.withValueHandler(null);

 assertEquals("Result should match the original type", type, result);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);

 JavaType result = type.withContentValueHandler(null);

 assertEquals("Result should match the original type", type, result);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);

 JavaType result = type.withStaticTyping();

 assertEquals("Result should match the original type", type, result);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);

 Class<?> subclass = Object[].class;
 JavaType result = type.withContentType(subclass.asSubType(String[].class));

 assertNull("_narrow method should return null for ResolvedRecursiveType", result.getRawClass());
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, null);
 Class<?> rawType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = ArrayType.construct(String[].class, null);
 JavaType[] superInterfaces = new JavaType[] { new ArrayType(String[].class, null) };

 JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);

 assertNull("refine method should return null for ResolvedRecursiveType", result);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);

 assertFalse("IsContainerType should return false for ResolvedRecursiveType", type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(List.class, null);

 assertEquals("ToString should match the erased type for unresolved reference", "[unresolved type]", type1.toString());
 assertEquals("ToString should match the self-referenced type for resolved reference", "[recursive type; UNRESOLVED]", type2.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(List.class, null);
 JavaType ref = new TypeReference<List<String>>() {}.getType();

 assertTrue("Two identical instances should be equal", type1.equals(type1));
 assertTrue("Two identical instances should be equal", type1.equals(type2));
 assertFalse("Two different instances should not be equal", type1.equals(type3));
 assertFalse("An instance with a reference and an instance without should not be equal", type1.equals(ref));
 }
}