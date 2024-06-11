package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 @Test
 public void testReferenceTypeConstruction() {
 ReferenceType referenceType = ReferenceType.construct(String.class, IntType.getInstance(), null, null);
 JavaType referencedType = referenceType.getReferencedType();
 assertInstanceOf(IntType.class, referencedType);
 }

 @Test
 public void testGetReferencedType() {
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 JavaType referencedType = referenceType.getReferencedType();
 assertInstanceOf(IntType.class, referencedType);
 }

 @Test
 public void testIsReferenceType() {
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testNarrow() {
 Class<?> subclass = String[].class;
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 JavaType narrowedType = referenceType.narrow(subclass);
 assertInstanceOf(ReferenceType.class, narrowedType);
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 JavaType withTypeHandler = referenceType.withTypeHandler(handler);
 assertNotSame(referenceType, withTypeHandler);
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 JavaType withContentTypeHandler = referenceType.withContentTypeHandler(handler);
 assertNotSame(referenceType, withContentTypeHandler);
 }

 @Test
 public void testWithValueHandler() {
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 JavaType withValueHandler = referenceType.withValueHandler(handler);
 assertNotSame(referenceType, withValueHandler);
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 JavaType withContentValueHandler = referenceType.withContentValueHandler(handler);
 assertNotSame(referenceType, withContentValueHandler);
 }

 @Test
 public void testWithStaticTyping() {
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 JavaType withStaticTyping = referenceType.withStaticTyping();
 assertNotSame(referenceType, withStaticTyping);
 }

 @Test
 public void testCanonicalName() {
 Class<?> stringClass = String.class;
 JavaType intType = IntType.getInstance();
 ReferenceType referenceType = new ReferenceType(stringClass, intType, null, null, false);
 String canonicalName = referenceType.buildCanonicalName();
 String expected = "[reference type, class java.lang.String<T>>]";
 assertEquals(expected, canonicalName);
 }

 @Test
 public void testContainsType() {
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 int containedTypeCount = referenceType.containedTypeCount();
 assertEquals(1, containedTypeCount);
 JavaType containedType = referenceType.containedType(0);
 assertInstanceOf(IntType.class, containedType);
 }

 @Test
 public void testGetParameterSource() {
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 Class<?> parameterSource = referenceType.getParameterSource();
 assertSame(String.class, parameterSource);
 }

 @Test
 public void testGetErasedSignature() {
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 StringBuilder erasedSignature = referenceType.getErasedSignature(new StringBuilder());
 String expected = "Ljava/lang/String;";
 assertEquals(expected, erasedSignature.toString());
 }

 @Test
 public void testGetGenericSignature() {
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);StringBuilder genericSignature = referenceType.getGenericSignature(new StringBuilder()); String expected = "[Ljava/lang/String;<T:Ljava/lang/Object;>"; assertEquals(expected, genericSignature.toString()); } }