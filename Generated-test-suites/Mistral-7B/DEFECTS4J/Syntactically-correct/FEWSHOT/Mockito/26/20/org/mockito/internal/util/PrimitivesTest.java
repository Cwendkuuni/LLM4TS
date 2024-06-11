package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_WRAPPER_TYPES = {
        Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class
 };

 @Test
 public void testPrimitiveTypeOf() {
         for (Class<?> clazz : PRIMITIVE_WRAPPER_Types) {
             assertEquals(clazz, Primitives.primitiveTypeOf(clazz));
         }

         for (Class<?> clazz : new Class<?>[] { String.class, Object.class }) {
             assertNull(Primitives.primitiveTypeOf(clazz));
         }
     }

 @Test
 public void testIsPrimitiveWrapper() {
         for (Class<?> clazz : PRIMITIVE_WRAPPER_Types) {
             assertTrue(Primitives.isPrimitiveWrapper(clazz));
         }

         assertFalse(Primitives.isPrimitiveWrapper(String.class));
         assertFalse(Primitives.isPrimitiveWrapper(Object.class));
     }

 @Test
 public void testPrimitiveWrapperOf() {
         Object wrapper;
         for (Class<?> clazz : PRIMITIVE_WRAPPER_Types) {
             wrapper = Primitives.primitiveWrapperOf(clazz);
             assertEquals(clazz, wrapper.getClass());
             if (clazz.isInstance(Boolean.FALSE)) {
                 assertEquals(Boolean.FALSE, (Boolean) wrapper);
             } else if (clazz.isInstance(Character.MIN_VALUE)) {
                 assertEquals(Character.MIN_VALUE, (Character) wrapper);
             } else {
                 assertEquals(0, wrapper);
             }
         }
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

 @Test
 public void testPrimitiveValueOrNullForObject() {
         assertNull(Primitives.primitiveValueOrNullFor(Object.class));
     }

 private static boolean assertFalse(boolean condition) {
         if (condition) {
             fail("Expected false but got true");
         }
         return condition;
     }

 private static boolean assertTrue(boolean condition) {
         if (!condition) {
             fail("Expected true but got false");
         }
         return condition;
     }
}