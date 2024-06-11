package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 private Class<?> type;
 private Class<?> expectedPrimitiveType;

 @Test
 public void testPrimitiveTypeOf() {
 type = Boolean.class;
 expectedPrimitiveType = Boolean.TYPE;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(type));

 type = Integer.class;
 expectedPrimitiveType = Integer.TYPE;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(type));

 type = Double.class;
 expectedPrimitiveType = Double.TYPE;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(type));

 type = String.class;
 assertNull(Primitives.primitiveTypeOf(type));
 }

 private Class<?> wrapperType;

 @Test
 public void testIsPrimitiveWrapper() {
 wrapperType = Boolean.class;
 assertTrue(Primitives.isPrimitiveWrapper(wrapperType));

 wrapperType = Integer.class;
 assertFalse(Primitives.isPrimitiveWrapper(wrapperType));

 wrapperType = Integer.TYPE;
 assertTrue(Primitives.isPrimitiveWrapper(wrapperType));
 }

 @Test(expected = NullPointerException.class)
 public void testIsPrimitiveWrapperNullType() {
 Primitives.isPrimitiveWrapper(null);
 }

 private Object wrapperValue;

 @Test
 public void testPrimitiveWrapperOf() {
 wrapperValue = Primitives.primitiveWrapperOf(Boolean.class);
 assertSame(false, wrapperValue);

 wrapperValue = Primitives.primitiveWrapperOf(Character.class);
 assertEquals('\u0000', wrapperValue);

 wrapperValue = Primitives.primitiveWrapperOf(Byte.class);
 assertEquals((byte) 0, wrapperValue);

 wrapperValue = Primitives.primitiveWrapperOf(Short.class);
 assertEquals((short) 0, wrapperValue);

 wrapperValue = Primitives.primitiveWrapperOf(Integer.class);
 assertEquals(0, wrapperValue);

 wrapperValue = Primitives.primitiveWrapperOf(Long.class);
 assertEquals(0L, wrapperValue);

 wrapperValue = Primitives.primitiveWrapperOf(Float.class);
 assertEquals(0F, wrapperValue);

 wrapperValue = Primitives.primitiveWrapperOf(Double.class);
 assertEquals(0D, wrapperValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveWrapperOfNullType() {
 Primitives.primitiveWrapperOf(null);
 }

 private Class<?> primitiveType;
 private Object primitiveValue;

 @Test
 public void testPrimitiveValueOrNullFor() {
 primitiveType = boolean.class;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertSame(false, primitiveValue);

 primitiveType = char.class;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals('\u0000', primitiveValue);

 primitiveType = byte.class;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals((byte) 0, primitiveValue);

 primitiveType = short.class;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals((short) 0, primitiveValue);

 primitiveType = int.class;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals(0, primitiveValue);

 primitiveType = long.class;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals(0L, primitiveValue);

 primitiveType = float.class;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals(0F, primitiveValue);

 primitiveType = double.class;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals(0D, primitiveValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveValueOrNullForNullType() {
 Primitives.primitiveValueOrNullFor(null);
 }
}