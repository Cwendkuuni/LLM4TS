package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

public class PrimitivesTest {

 private static final Class<Boolean> BOOLEAN_CLASS = Boolean.class;
 private static final Class<Character> CHAR_CLASS = Character.class;
 private static final Class<Byte> BYTE_CLASS = Byte.class;
 private static final Class<Short> SHORT_CLASS = Short.class;
 private static final Class<Integer> INT_CLASS = Integer.class;
 private static final Class<Long> LONG_CLASS = Long.class;
 private static final Class<Float> FLOAT_CLASS = Float.class;
 private static final Class<Double> DOUBLE_CLASS = Double.class;

 @Test
 public void testPrimitiveTypeOfBoolean() {
 assertEquals(BOOLEAN_CLASS, Primitives.primitiveTypeOf(BOOLEAN_CLASS));
 }

 @Test
 public void testPrimitiveTypeOfCharacter() {
 assertEquals(CHAR_CLASS, Primitives.primitiveTypeOf(CHAR_CLASS));
 }

 @Test
 public void testPrimitiveTypeOfByte() {
 assertEquals(BYTE_CLASS, Primitives.primitiveTypeOf(BYTE_CLASS));
 }

 @Test
 public void testPrimitiveTypeOfShort() {
 assertEquals(SHORT_CLASS, Primitives.primitiveTypeOf(SHORT_CLASS));
 }

 @Test
 public void testPrimitiveTypeOfInteger() {
 assertEquals(INT_CLASS, Primitives.primitiveTypeOf(INT_CLASS));
 }

 @Test
 public void testPrimitiveTypeOfLong() {
 assertEquals(LONG_CLASS, Primitives.primitiveTypeOf(LONG_CLASS));
 }

 @Test
 public void testPrimitiveTypeOfFloat() {
 assertEquals(FLOAT_CLASS, Primitives.primitiveTypeOf(FLOAT_CLASS));
 }

 @Test
 public void testPrimitiveTypeOfDouble() {
 assertEquals(DOUBLE_CLASS, Primitives.primitiveTypeOf(DOUBLE_CLASS));
 }

 @Test
 public void testPrimitiveTypeOfString() {
 assertNull(Primitives.primitiveTypeOf(String.class));
 }

 @Test
 public void testIsPrimitiveWrapperBoolean() {
 assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
 }

 @Test
 public void testIsPrimitiveWrapperCharacter() {
 assertFalse(Primitives.isPrimitiveWrapper(Character.class));
 }

 @Test
 public void testIsPrimitiveWrapperByte() {
 assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
 }

 @Test
 public void testIsPrimitiveWrapperShort() {
 assertTrue(Primitives.isPrimitiveWrapper(Short.class));
 }

 @Test
 public void testIsPrimitiveWrapperInteger() {
 assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
 }

 @Test
 public void testIsPrimitiveWrapperLong() {
 assertTrue(Primitives.isPrimitiveWrapper(Long.class));
 }

 @Test
 public void testIsPrimitiveWrapperFloat() {
 assertTrue(Primitives.isPrimitiveWrapper(Float.class));
 }

 @Test
 public void testIsPrimitiveWrapperDouble() {
 assertTrue(Primitives.isPrimitiveWrapper(Double.class));
 }

 @Test
 public void testPrimitiveWrapperOfBoolean() {
 assertEquals(false, Primitives.primitiveWrapperOf(Boolean.class));
 }

 @Test
 public void testPrimitiveWrapperOfCharacter() {
 assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.class));
 }

 @Test
 public void testPrimitiveWrapperOfByte() {
 assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.class));
 }

 @Test
 public void testPrimitiveWrapperOfShort() {
 assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.class));
 }

 @Test
 public void testPrimitiveWrapperOfInteger() {
 assertEquals(0, Primitives.primitiveWrapperOf(Integer.class).intValue());
 }

 @Test
 public void testPrimitiveWrapperOfLong() {
 assertEquals(0L, Primitives.primitiveWrapperOf(Long.class).longValue());
 }

 @Test
 public void testPrimitiveWrapperOfFloat() {
 assertEquals(0F, Primitives.primitiveWrapperOf(Float.class));
 }

 @Test
 public void testPrimitiveWrapperOfDouble() {
 assertEquals(0D, Primitives.primitiveWrapperOf(Double.class));
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
 }

 @Test
 public void testPrimitiveValueOrNullForInteger() {
 assertEquals(0, Primitives.primitiveValueOrNullFor(int.class).intValue());
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class).longValue());
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
 }
}