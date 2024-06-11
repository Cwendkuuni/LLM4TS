package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class PrimitivesTest {

 @Test
 public void testPrimitiveTypeOf() {
 Class<Integer> intClass = Integer.class;
 Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
 Assert.assertEquals(intType, Integer.TYPE);

 Class<String> stringClass = String.class;
 Class<String> stringType = Primitives.primitiveTypeOf(stringClass);
 Assert.assertNull(stringType);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<Boolean> booleanWrapperClass = Boolean.class;
 Assert.assertTrue(Primitives.isPrimitiveWrapper(booleanWrapperClass));

 Class<String> stringClass = String.class;
 Assert.assertFalse(Primitives.isPrimitiveWrapper(stringClass));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<Boolean> booleanWrapperClass = Boolean.class;
 Boolean booleanWrapper = Primitives.primitiveWrapperOf(booleanWrapperClass);
 Assert.assertNotNull(booleanWrapper);
 Assert.assertEquals(false, booleanWrapper);

 Class<String> stringClass = String.class;
 Assert.assertNull(Primitives.primitiveWrapperOf(stringClass));
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 Class<Boolean> booleanClass = Boolean.class;
 Boolean booleanValue = Primitives.primitiveValueOrNullFor(booleanClass);
 Assert.assertEquals(false, booleanValue);
 }

 @Test
 public void testPrimitiveValueOrNullForChar() {
 Class<Character> charClass = Character.class;
 Character charValue = Primitives.primitiveValueOrNullFor(charClass);
 Assert.assertEquals('\u0000', charValue);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 Class<Byte> byteClass = Byte.class;
 Byte byteValue = Primitives.primitiveValueOrNullFor(byteClass);
 Assert.assertEquals((byte) 0, byteValue);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 Class<Short> shortClass = Short.class;
 Short shortValue = Primitives.primitiveValueOrNullFor(shortClass);
 Assert.assertEquals((short) 0, shortValue);
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 Class<Integer> intClass = Integer.class;
 Integer intValue = Primitives.primitiveValueOrNullFor(intClass);
 Assert.assertEquals(0, intValue);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 Class<Long> longClass = Long.class;
 Long longValue = Primitives.primitiveValueOrNullFor(longClass);
 Assert.assertEquals(0L, longValue);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 Class<Float> floatClass = Float.class;
 Float floatValue = Primitives.primitiveValueOrNullFor(floatClass);
 Assert.assertEquals(0F, floatValue);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 Class<Double> doubleClass = Double.class;
 Double doubleValue = Primitives.primitiveValueOrNullFor(doubleClass);
 Assert.assertEquals(0D, doubleValue);
 }
}