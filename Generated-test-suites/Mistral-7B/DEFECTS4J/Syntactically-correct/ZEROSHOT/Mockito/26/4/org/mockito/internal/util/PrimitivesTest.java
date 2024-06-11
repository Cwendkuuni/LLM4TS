package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 private Class<?> primitiveType;
 private Class<?> wrapperType;
 private Class<?> expectedWrapperType;
 private Class<?> expectedPrimitiveType;
 private Object wrapperValue;
 private Object primitiveValue;

 @Test
 public void testPrimitiveTypeOf() {
 // Testing boolean
 primitiveType = Boolean.class;
 expectedPrimitiveType = Boolean.TYPE;
 wrapperType = null;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));

 primitiveType = Boolean.class;
 expectedWrapperType = Boolean.class;
 wrapperValue = false;
 assertSame(expectedWrapperType, Primitives.primitiveTypeOf(wrapperType));
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));

 // Testing other primitives
 primitiveType = Character.class;
 expectedPrimitiveType = Character.TYPE;
 wrapperType = null;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));

 primitiveType = Byte.class;
 expectedPrimitiveType = Byte.TYPE;
 wrapperType = null;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));

 primitiveType = Byte.class;
 expectedWrapperType = Byte.class;
 wrapperValue = (byte) 0;
 assertSame(expectedWrapperType, Primitives.primitiveTypeOf(wrapperType));
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));

 // Testing other primitives
 // ... similar tests for Short, Integer, Long, Float, Double
 }

 @Test
 public void testIsPrimitiveWrapper() {
 // Testing boolean
 primitiveType = Boolean.class;
 expectedWrapperType = Boolean.class;
 assertTrue(Primitives.isPrimitiveWrapper(primitiveType));

 // Testing other primitives
 primitiveType = Byte.class;
 expectedWrapperType = Byte.class;
 assertTrue(Primitives.isPrimitiveWrapper(primitiveType));

 // Testing Object
 primitiveType = Object.class;
 assertFalse(Primitives.isPrimitiveWrapper(primitiveType));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 // Testing boolean
 primitiveType = Boolean.class;
 expectedWrapperValue = false;
 assertSame(expectedWrapperValue, Primitives.primitiveWrapperOf(primitiveType));

 // Testing other primitives
 primitiveType = Byte.class;
 expectedWrapperValue = (byte) 0;
 assertSame(expectedWrapperValue, Primitives.primitiveWrapperOf(primitiveType));

 // Testing null
 primitiveType = null;
 assertNull(Primitives.primitiveWrapperOf(primitiveType));
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 // Testing boolean
 primitiveType = Boolean.class;
 expectedPrimitiveValue = false;
 assertSame(expectedPrimitiveValue, Primitives.primitiveValueOrNullFor(primitiveType));

 // Testing other primitives
 primitiveType = Byte.class;
 expectedPrimitiveValue = (byte) 0;
 assertSame(expectedPrimitiveValue, Primitives.primitiveValueOrNullFor(primitiveType));

 // Testing null
 primitiveType = null;
 assertNull(Primitives.primitiveValueOrNullFor(primitiveType));
 }
}