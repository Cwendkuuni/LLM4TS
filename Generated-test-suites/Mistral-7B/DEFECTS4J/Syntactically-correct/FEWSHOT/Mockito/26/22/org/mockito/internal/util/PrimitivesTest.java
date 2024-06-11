package org.mockito.internal.util;

import static org.junit.Assert.*;

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
 public void testPrimitiveTypeOf_PrimitiveClass() {
 final Class<Boolean> expected = Boolean.TYPE;
 assertEquals(expected, Primitives.primitiveTypeOf(BOOLEAN_CLASS));
 }

 @Test
 public void testPrimitiveTypeOf_WrapperClass() {
 final Class<Boolean> expected = Boolean.TYPE;
 assertEquals(expected, Primitives.primitiveTypeOf(BOOLEAN_CLASS));
 }

 @Test
 public void testPrimitiveTypeOf_String() {
 final Class<Character> expected = Character.TYPE;
 assertEquals(expected, Primitives.primitiveTypeOf(CHAR_CLASS.getName()));
 }

 @Test
 public void testIsPrimitiveWrapper_BooleanClass() {
 assertTrue(Primitives.isPrimitiveWrapper(BOOLEAN_CLASS));
 }

 @Test
 public void testIsPrimitiveWrapper_BooleanWrapperClass() {
 assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
 }

 @Test
 public void testIsPrimitiveWrapper_NonPrimitiveClass() {
 assertFalse(Primitives.isPrimitiveWrapper(INT_CLASS));
 }

 @Test
 public void testPrimitiveWrapperOf_BooleanClass() {
 final Boolean expected = false;
 assertEquals(expected, Primitives.primitiveWrapperOf(BOOLEAN_CLASS));
 }

 @Test
 public void testPrimitiveWrapperOf_BooleanWrapperClass() {
 final Boolean expected = false;
 assertEquals(expected, Primitives.primitiveWrapperOf(BOOLEAN_CLASS));
 }

 @Test
 public void testPrimitiveWrapperOf_NonPrimitiveClass() {
 assertNull(Primitives.primitiveWrapperOf(INT_CLASS));
 }

 @Test
 public void testPrimitiveValueOrNullFor_BooleanPrimitiveType() {
 final Boolean expected = false;
 assertEquals(expected, Primitives.primitiveValueOrNullFor(boolean.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor_BytePrimitiveType() {
 final Byte expected = 0;
 assertEquals(expected, Primitives.primitiveValueOrNullFor(byte.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor_NonPrimitiveType() {
 assertNull(Primitives.primitiveValueOrNullFor(Integer.class));
 }
}