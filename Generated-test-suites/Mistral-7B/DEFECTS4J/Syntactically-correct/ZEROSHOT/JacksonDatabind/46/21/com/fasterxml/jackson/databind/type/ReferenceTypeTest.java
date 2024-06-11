package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 private JavaType referenceType;
 private JavaType referencedType;

 @Test
 public void testReferenceTypeConstruction() {
 ReferenceType testType = ReferenceType.construct(String.class, IntType.getInstance(), null, null);
 assertNotNull(testType);
 assertEquals(String.class, testType.getRawClass());
 assertNotNull(testType.getContentType());
 assertInstanceOf(SimpleType.class, testType.getContentType());
 }

 @Test
 public void testGetReferencedType() {
 referenceType = ReferenceType.construct(String.class, IntType.getInstance(), null, null);
 referencedType = referenceType.getReferencedType();
 assertNotNull(referencedType);
 assertEquals(Integer.class, referencedType.getRawClass());
 }

 @Test
 public void testIsReferenceType() {
 referenceType = ReferenceType.construct(String.class, IntType.getInstance(), null, null);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testWithTypeHandler() {
 JavaType originalType = referenceType.withTypeHandler(null);
 JavaType newType = referenceType.withTypeHandler(new StubTypeHandler());
 assertNotSame(originalType, newType);
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType originalType = referenceType.withContentTypeHandler(null);
 JavaType newType = referenceType.withContentTypeHandler(new StubTypeHandler());
 assertNotSame(originalType, newType);
 }

 @Test
 public void testWithValueHandler() {
 JavaType originalType = referenceType.withValueHandler(null);
 JavaType newType = referenceType.withValueHandler(new StubValueHandler());
 assertNotSame(originalType, newType);
 }

 @Test
 public void testWithContentValueHandler() {
 JavaType originalType = referenceType.withContentValueHandler(null);
 JavaType newType = referenceType.withContentValueHandler(new StubValueHandler());
 assertNotSame(originalType, newType);
 }

 @Test
 public void testWithStaticTyping() {
 JavaType originalType = referenceType;
 JavaType newType = referenceType.withStaticTyping();
 assertNotSame(originalType, newType);
 assertTrue(newType.isStatic());
 }

 @Test
 public void testNarrow() {
 JavaType narrowedType = referenceType.narrow(String[].class);
 assertNotNull(narrowedType);
 assertEquals(String[].class, narrowedType.getRawClass());
 }

 @Test
 public void testContainsType() {
 JavaType containedType = referenceType.containedType(0);
 assertNotNull(containedType);
 assertEquals(Integer.class, containedType.getRawClass());
 }

 @Test
 public void testContainsTypeName() {
 assertEquals("T", referenceType.containedTypeName(0));
 }

 @Test
 public void testGetParameterSource() {
 assertEquals(String.class, referenceType.getParameterSource());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder signature = new StringBuilder();
 referenceType.getErasedSignature(signature);
 assertEquals("java.lang.String", signature.toString());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder signature = new StringBuilder();
 referenceType.getGenericSignature(signature);
 assertEquals("java.lang.String<T>", signature.toString());
 }

 @Test
 public void testToString() {
 String expected = "[reference type, class java.lang.String<java.lang.Integer>]";
 assertEquals(expected, referenceType.toString());
 }

 @Test
 public void testEquals() {
 JavaType otherType = ReferenceType.construct(String.class, IntType.getInstance(), null, null);
 assertTrue(referenceType.equals(referenceType));
 assertTrue(referenceType.equals(otherType));
 JavaType differentType = ReferenceType.construct(String.class, StringType.getInstance(), null, null);
 assertFalse(referenceType.equals(differentType));
 }
}

class StubTypeHandler {}
class StubValueHandler {}