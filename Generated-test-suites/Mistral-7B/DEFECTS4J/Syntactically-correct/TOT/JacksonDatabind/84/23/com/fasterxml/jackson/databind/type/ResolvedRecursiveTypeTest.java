package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 // Test case for constructor and setting self reference
 @Test
 public void testConstructorAndSetReference() {
 Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType erasedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 JavaType refType = new TypeReference<JavaType>() {}.getType();
 type.setReference(refType);
 JavaType selfRefType = type.getSelfReferencedType();

 assertNotNull(selfRefType);
 assertNotNull(selfRefType.getErasedType());
 assertNotNull(selfRefType.getRawClass());
 assertSame(refType, selfRefType);
 }

 // Test case for setting self reference multiple times
 @Test(expected = IllegalStateException.class)
 public void testSetReferenceMultipleTimes() {
 Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType erasedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 JavaType refType1 = new TypeReference<JavaType>() {}.getType();
 JavaType refType2 = new TypeReference<JavaType>() {}.getType();
 type.setReference(refType1);
 type.setReference(refType2);
 }

 // Test case for getSelfReferencedType method with null reference
 @Test
 public void testGetSelfReferencedTypeNullReference() {
 Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType erasedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 JavaType selfRefType = type.getSelfReferencedType();

 assertNull(selfRefType);
 }

 // Test case for getSelfReferencedType method with non-null reference
 @Test
 public void testGetSelfReferencedTypeNonnullReference() {
 Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
 TypeBindings bindings = new TypeBindings();
 JavaType erasedType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 JavaType refType = new TypeReference<JavaType>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 type.setReference(refType);
 JavaType selfRefType = type.getSelfReferencedType();

 assertNotNull(selfRefType);
 assertNotNull(selfRefType.getErasedType());
 assertNotNull(selfRefType.getRawClass());
 }
}