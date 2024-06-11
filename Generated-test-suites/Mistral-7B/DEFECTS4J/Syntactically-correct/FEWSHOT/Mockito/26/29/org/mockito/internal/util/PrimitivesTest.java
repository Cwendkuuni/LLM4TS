package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimitivesTest {

 @Test
 public void testPrimitiveTypeOf() {
 Class<?> clazz = String.class;
 Class<Object> expected = null;
 Class<Object> actual = Primitives.primitiveTypeOf(clazz);
 assertNull(actual);

 clazz = Boolean.class;
 expected = Boolean.TYPE;
 actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, actual);

 clazz = Character.class;
 expected = Character.TYPE;
 actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, actual);

 clazz = Byte.class;
 expected = Byte.TYPE;
 actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, actual);

 clazz = Short.class;
 expected = Short.TYPE;
 actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, actual);

 clazz = Integer.class;
 expected = Integer.TYPE;
 actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, actual);

 clazz = Long.class;
 expected = Long.TYPE;
 actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, actual);

 clazz = Float.class;
 expected = Float.TYPE;
 actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, actual);

 clazz = Double.class;
 expected = Double.TYPE;
 actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, actual);

 clazz = Object.class;
 actual = Primitives.primitiveTypeOf(clazz);
 assertNull(actual);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<?> type = Boolean.class;
 boolean expected = true;
 boolean actual = Primitives.isPrimitiveWrapper(type);
 assertEquals(expected, actual);

 type = String.class;
 expected = false;
 actual = Primitives.isPrimitiveWrapper(type);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<?> type = Boolean.class;
 Object expected = false;
 Object actual = Primitives.primitiveWrapperOf(type);
 assertEquals(expected, actual);

 type = Character.class;
 expected = Character.MIN_VALUE;
 actual = Primitives.primitiveWrapperOf(type);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 boolean expected = false;
 Object actual = Primitives.primitiveValueOrNullFor(boolean.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForChar() {
 char expected = '\u0000';
 Object actual = Primitives.primitiveValueOrNullFor(char.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 byte expected = 0;
 Object actual = Primitives.primitiveValueOrNullFor(byte.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 short expected = 0;
 Object actual = Primitives.primitiveValueOrNullFor(short.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 int expected = 0;
 Object actual = Primitives.primitiveValueOrNullFor(int.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 long expected = 0L;
 Object actual = Primitives.primitiveValueOrNullFor(long.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 float expected = 0F;
 Object actual = Primitives.primitiveValueOrNullFor(float.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 double expected = 0;
 Object actual = Primitives.primitiveValueOrNullFor(double.class);
 assertEquals(expected, actual);
 }
}