package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimitivesTest {

 @Test
 public void testPrimitiveTypeOf() {
 Class<Integer> intClass = Integer.class;
 Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
 assertEquals(Integer.TYPE, intType);

 Class<Boolean> booleanClass = Boolean.class;
 Class<Boolean> booleanType = Primitives.primitiveTypeOf(booleanClass);
 assertEquals(Boolean.TYPE, booleanType);

 Class<String> stringClass = String.class;
 Class<?> stringType = Primitives.primitiveTypeOf(stringClass);
 assertNull(stringType);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<Integer> intWrapper = Integer.class;
 assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
 assertTrue(Primitives.isPrimitiveWrapper(intWrapper));

 Class<Character> charWrapper = Character.class;
 assertTrue(Primitives.isPrimitiveWrapper(Character.class));
 assertTrue(Primitives.isPrimitiveWrapper(charWrapper));

 Class<String> stringClass = String.class;
 assertFalse(Primitives.isPrimitiveWrapper(String.class));
 assertFalse(Primitives.isPrimitiveWrapper(stringClass));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<Boolean> booleanClass = Boolean.class;
 Boolean booleanWrapper = Primitives.primitiveWrapperOf(booleanClass);
 assertNotNull(booleanWrapper);
 assertTrue(booleanWrapper instanceof Boolean);

 Class<Integer> intClass = Integer.class;
 Integer intWrapper = Primitives.primitiveWrapperOf(intClass);
 assertNotNull(intWrapper);
 assertTrue(intWrapper instanceof Integer);

 Class<String> stringClass = String.class;
 assertNull(Primitives.primitiveWrapperOf(stringClass));
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 assertTrue(Primitives.primitiveValueOrNullFor(boolean.class));
 }

 @Test
 public void testPrimitiveValueOrNullForChar() {
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
 public void testPrimitiveValueOrNullForInt() {
 assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
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