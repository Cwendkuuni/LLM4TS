package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_TYPES = {
     boolean.class, char.class, byte.class, short.class,
     int.class, long.class, float.class, double.class
 };

 private static final Class<?>[] WRAPPER_TYPES = {
     Boolean.class, Character.class, Byte.class, Short.class,
     Integer.class, Long.class, Float.class, Double.class
 };

 @Test
 public void testPrimitiveTypeOf() {
     for (Class<?> primitiveType : PRIMITIVE_Types) {
         assertEquals(primitiveType, Primitives.primitiveTypeOf(primitiveType));
         assertEquals(primitiveType, Primitives.primitiveTypeOf(primitiveType.wrapperType()));
     }
 }

 @Test
 public void testIsPrimitiveWrapper() {
     for (Class<?> wrapperType : WRAPPER_Types) {
         assertTrue(Primitives.isPrimitiveWrapper(wrapperType));
     }
 }

 @Test
 public void testPrimitiveWrapperOf() {
     for (Class<?> wrapperType : WRAPPER_Types) {
         assertEquals(Primitives.primitiveValueOrNullFor(Primitives.primitiveTypeOf(wrapperType)),
                 Primitives.primitiveWrapperOf(wrapperType));
     }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
     for (Class<?> primitiveType : PRIMITIVE_Types) {
         assertEquals(Primitives.primitiveValueOrNullFor(primitiveType),
                 Primitives.primitiveValues.get(primitiveType));
     }
 }
}