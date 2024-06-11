package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 private Class<?> clazz;
 private Class<?> expectedClass;
 private boolean isPrimitiveWrapper;
 private Object wrapperValue;
 private T primitiveValue;

 @Test
 public void testPrimitiveTypeOf() {
 clazz = Boolean.class;
 expectedClass = Boolean.TYPE;
 assertSame(expectedClass, Primitives.primitiveTypeOf(clazz));

 clazz = String.class;
 assertNull(Primitives.primitiveTypeOf(clazz));
 }

 @Test
 public void testIsPrimitiveWrapper() {
 clazz = Boolean.class;
 isPrimitiveWrapper = Primitives.isPrimitiveWrapper(clazz);
 assertTrue(isPrimitiveWrapper);

 clazz = Integer.class;
 isPrimitiveWrapper = Primitives.isPrimitiveWrapper(clazz);
 assertFalse(isPrimitiveWrapper);
 }

 @Test
 public void testPrimitiveWrapperOf() {
 clazz = Boolean.class;
 primitiveValue = Primitives.primitiveWrapperOf(clazz);
 assertInstanceOf(Boolean.class, primitiveValue);
 assertFalse((Boolean) primitiveValue);

 clazz = Double.class;
 primitiveValue = Primitives.primitiveWrapperOf(clazz);
 assertInstanceOf(Double.class, primitiveValue);
 assertNotNull(primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 primitiveValue = Primitives.primitiveValueOrNullFor(boolean.class);
 assertNull(primitiveValue);

 primitiveValue = Primitives.primitiveValueOrNullFor(Boolean.class);
 assertInstanceOf(Boolean.class, primitiveValue);
 assertFalse((Boolean) primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 primitiveValue = Primitives.primitiveValueOrNullFor(char.class);
 assertNull(primitiveValue);

 primitiveValue = Primitives.primitiveValueOrNullFor(Character.class);
 assertInstanceOf(Character.class, primitiveValue);
 assertEq('\u0000', primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 primitiveValue = Primitives.primitiveValueOrNullFor(byte.class);
 assertNull(primitiveValue);

 primitiveValue = Primitives.primitiveValueOrNullFor(Byte.class);
 assertInstanceOf(Byte.class, primitiveValue);
 assertEq((byte) 0, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 primitiveValue = Primitives.primitiveValueOrNullFor(short.class);
 assertNull(primitiveValue);

 primitiveValue = Primitives.primitiveValueOrNullFor(Short.class);
 assertInstanceOf(Short.class, primitiveValue);
 assertEq((short) 0, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForInteger() {
 primitiveValue = Primitives.primitiveValueOrNullFor(int.class);
 assertNull(primitiveValue);

 primitiveValue = Primitives.primitiveValueOrNullFor(Integer.class);
 assertInstanceOf(Integer.class, primitiveValue);
 assertEq(0, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 primitiveValue = Primitives.primitiveValueOrNullFor(long.class);
 assertNull(primitiveValue);

 primitiveValue = Primitives.primitiveValueOrNullFor(Long.class);
 assertInstanceOf(Long.class, primitiveValue);
 assertEq(0L, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 primitiveValue = Primitives.primitiveValueOrNullFor(float.class);
 assertNull(primitiveValue);

 primitiveValue = Primitives.primitiveValueOrNullFor(Float.class);
 assertInstanceOf(Float.class, primitiveValue);
 assertEq(0F, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 primitiveValue = Primitives.primitiveValueOrNullFor(double.class);
 assertNull(primitiveValue);

 primitiveValue = Primitives.primitiveValueOrNullFor(Double.class);
 assertInstanceOf(Double.class, primitiveValue);
 assertEq(0D, primitiveValue);
 }
}