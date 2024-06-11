package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ResolvedRecursiveTypeTest {

 private TypeFactory typeFactory;

 @Test
 public void testResolvedRecursiveTypeCreation() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertNotNull(type);
 assertTrue(type instanceof ResolvedRecursiveType);
 }

 @Test
 public void testResolvedRecursiveTypeSetReference() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType referenceType = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());
 type.setReference(referenceType);
 JavaType selfType = type.getSelfReferencedType();
 assertNotNull(selfType);
 assertSame(referenceType, selfType);
 }

 @Test(expected = IllegalStateException.class)
 public void testResolvedRecursiveTypeSetReferenceTwice() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType referenceType = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());
 type.setReference(referenceType);
 type.setReference(typeFactory.constructType(new TypeReference<List<String>>() {}.getType()));
 }

 @Test
 public void testResolvedRecursiveTypeGetSignatures() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 StringBuilder sb = new StringBuilder(100);
 JavaType selfType = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());
 type.setReference(selfType);

 StringBuilder signature = type.getGenericSignature(sb);
 assertTrue(signature.toString().contains("List<String>"));
 signature = type.getErasedSignature(sb);
 assertTrue(signature.toString().contains("java.util.List<java.lang.String>"));
 }

 @Test
 public void testResolvedRecursiveTypeWithContentType() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType contentType = typeFactory.constructType(new TypeReference<Integer>() {}.getType());
 type.withContentType(contentType);
 JavaType result = type.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithTypeHandler() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 type.withTypeHandler(new Object() {});
 JavaType result = type.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithContentTypeHandler() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 type.withContentTypeHandler(new Object() {});
 JavaType result = type.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithValueHandler() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 type.withValueHandler(new Object() {});
 JavaType result = type.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithContentValueHandler() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 type.withContentValueHandler(new Object() {});
 JavaType result = type.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithStaticTyping() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType result = type.withStaticTyping().getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeIsContainerType() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(type.isContainerType());
 }

 @Test
 public void testResolvedRecursiveTypeToString() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType referenceType = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());
 type.setReference(referenceType);
 assertTrue(type.toString().contains("recursive type; [class java.util.List<java.lang.String>]"));
 }

 @Test
 public void testResolvedRecursiveTypeEquals() {
 typeFactory = new TypeFactory();
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(type1.equals(type2));

 JavaType referenceType = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());
 type1.setReference(referenceType);
 type2.setReference(referenceType);
 assertTrue(type1.equals(type2));
 }
}