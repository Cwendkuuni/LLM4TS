package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 JavaType intType = IntType.getInstance();

 @Test
 public void testGetReferencedType() {
 // Test getting referenced type
 JavaType referencedType = referenceType.getReferencedType();
 assertEquals(IntType.class, referencedType.getClass());
 }

 @Test
 public void testIsReferenceType() {
 // Test if it's a reference type
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testGetContainedTypeCount() {
 // Test contained type count
 assertEquals(1, referenceType.containedTypeCount());
 }

 @Test
 public void testContainedType() {
 // Test contained type
 JavaType containedType = referenceType.containedType(0);
 assertEquals(IntType.class, containedType.getClass());
 }

 @Test
 public void testGetContainedTypeName() {
 // Test contained type name
 String containedTypeName = referenceType.containedTypeName(0);
 assertEquals("T", containedTypeName);
 }

 @Test
 public void testGetParameterSource() {
 // Test getParameterSource
 assertEquals(String.class, referenceType.getParameterSource());
 }

 @Test
 public void testGetErasedSignature() {
 // Test getErasedSignature
 StringBuilder erasedSignature = new StringBuilder();
 referenceType.getErasedSignature(erasedSignature);
 assertEquals("class java.lang.String", erasedSignature.toString());
 }

 @Test
 public void testGetGenericSignature() {
 // Test getGenericSignature
 StringBuilder genericSignature = new StringBuilder();
 referenceType.getGenericSignature(genericSignature);
 assertEquals("[reference type, class java.lang.String<T>]", genericSignature.toString());
 }

 @Test
 public void testToString() {
 // Test toString
 String toString = referenceType.toString();
 assertEquals("[reference type, class java.lang.String<T>]", toString);
 }

 @Test
 public void testEquals() {
 // Test equals
 ReferenceType referenceType1 = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 ReferenceType referenceType2 = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 assertTrue(referenceType.equals(referenceType1));
 assertTrue(referenceType.equals(referenceType2));

 ReferenceType referenceType3 = new ReferenceType(Integer.class, IntType.getInstance(), null, null, false);
 assertFalse(referenceType.equals(referenceType3));
 }

 @Test
 public void testConstruct() {
 // Test construct method
 JavaType stringType = JavaType.construct(String.class, intType, null, null);
 ReferenceType referenceTypeConstruct = ReferenceType.construct(String.class, intType, null, null);
 assertEquals(referenceType, referenceTypeConstruct);
 }

 @Test
 public void testWithTypeHandler() {
 // Test withTypeHandler method
 Object handler = new Object();
 JavaType withTypeHandler = referenceType.withTypeHandler(handler);
 assertNotSame(referenceType, withTypeHandler);
 assertEquals(referenceType.getReferencedType(), withTypeHandler.getReferencedType());
 }

 @Test
 public void testWithContentTypeHandler() {
 // Test withContentTypeHandler method
 JavaType contentTypeHandlerJavaType = JavaType.construct(String.class, intType, null, null);
 Object contentHandler = new Object();
 JavaType withContentTypeHandler = referenceType.withContentTypeHandler(contentHandler);
 assertNotSame(referenceType, withContentTypeHandler);
 assertEquals(contentTypeHandlerJavaType, withContentTypeHandler.getReferencedType());
 }

 @Test
 public void testWithValueHandler() {
 // Test withValueHandler method
 Object valueHandler = new Object();
 JavaType withValueHandler = referenceType.withValueHandler(valueHandler);
 assertNotSame(referenceType, withValueHandler);
 assertNull(withValueHandler.getReferencedType());
 }

 @Test
 public void testWithContentValueHandler() {
 // Test withContentValueHandler method
 Object contentValueHandler = new Object();
 JavaType withContentValueHandler = referenceType.withContentValueHandler(contentValueHandler);
 assertNotSame(referenceType, withContentValueHandler);
 assertNull(withContentValueHandler.getReferencedType());
 }

 @Test
 public void testWithStaticTyping() {
 // Test withStaticTyping method
 JavaType withStaticTyping = referenceType.withStaticTypening();
 assertNotSame(referenceType, withStaticTyping);
 assertTrue(withStaticTyping.isStatic());
 }
}