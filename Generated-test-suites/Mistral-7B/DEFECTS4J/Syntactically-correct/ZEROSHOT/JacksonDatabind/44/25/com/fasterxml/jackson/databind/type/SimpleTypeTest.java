package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstructor() {
 SimpleType simpleType = new SimpleType(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 Class<?> stringClass = String.class;
 SimpleType originalSimpleType = new SimpleType(stringClass, new TypeBindings(), null, null);
 SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
 assertEquals(originalSimpleType, copiedSimpleType);
 }

 @Test
 public void testSimpleTypeUnsafeConstructor() {
 Class<?> stringClass = String.class;
 SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructWithCollection() {
 SimpleType.construct(ArrayList.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructWithMap() {
 SimpleType.construct(HashMap.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructWithArray() {
 SimpleType.construct(int[].class);
 }

 @Test
 public void testSimpleTypeNarrow() {
 SimpleType stringSimpleType = new SimpleType(String.class);
 SimpleType stringSubclassSimpleType = new SimpleType(StringBuilder.class);
 SimpleType narrowedType = stringSimpleType.narrow(StringBuilder.class);
 assertEquals(stringSubclassSimpleType, narrowedType);
 }

 @Test
 public void testSimpleTypeWithContentType() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 simpleType.withContentType(IntegerType.getIntType(null));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(new StdSerializer<String>(String.class));
 assertNotSame(simpleType, simpleTypeWithHandler);
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType simpleTypeWithHandler = simpleType.withValueHandler(new StdDeserializer<String>(String.class));
 assertNotSame(simpleType, simpleTypeWithHandler);
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType staticTypingSimpleType = simpleType.withStaticTyping();
 assertTrue(staticTypingSimpleType.isStatic());
 }

 @Test
 public void testSimpleTypeRefine() {
 SimpleType simpleType = new SimpleType(String.class);
 assertNull(simpleType.refine(Object.class, null, null, null));
 }

 @Test
 public void testSimpleTypeBuildCanonicalName() {
 SimpleType stringSimpleType = new SimpleType(String.class);
 String canonicalName = stringSimpleType.getCanonicalName();
 assertEquals("class java.lang.String", canonicalName);
 }

 @Test
 public void testSimpleTypeGetErasedSignature() {
 SimpleType stringSimpleType = new SimpleType(String.class);
 StringBuilder sb = new StringBuilder();
 stringSimpleType.getErasedSignature(sb);
 assertEquals("java.lang.String", sb.toString());
 }

 @Test
 public void testSimpleTypeGetGenericSignature() {
 SimpleType stringSimpleType = new SimpleType(String.class);
 StringBuilder sb = new StringBuilder();
 stringSimpleType.getGenericSignature(sb);
 assertEquals("java.lang.String", sb.toString());
 sb.append(";");
 stringSimpleType = new SimpleType(List.class, null, null, StringType.instance);
 sb.setLength(sb.length() - 1);
 stringSimpleType.getGenericSignature(sb);
 assertEquals("[Ljava.util.List;", sb.toString());
 }
}