package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimitivesTest {

 private Class<?>[] types = { boolean.class, char.class, byte.class, short.class,
         int.class, long.class, float.class, double.class, Boolean.class, Character.class,
         Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class };

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> type : types) {
 Class<?> result = Primitives.primitiveTypeOf(type);
 assertEquals(type, result);
 }

 Class<?> wrapperType = Primitives.primitiveTypeOf(Boolean.class);
 assertEquals(Boolean.TYPE, wrapperType);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> type : types) {
 if (type.isPrimitive()) {
 continue;
 }
 assertTrue(Primitives.isPrimitiveWrapper(type));
 }

 assertFalse(Primitives.isPrimitiveWrapper(boolean.class));
 assertFalse(Primitives.isPrimitiveWrapper(Boolean.class));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> type : types) {
 if (type.isPrimitive()) {
 continue;
 }
 assertEquals(Primitives.primitiveWrapperOf(type), Primitives.wrapperReturnValues.get(type));
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Class<?> type : types) {
 if (!type.isPrimitive()) {
 continue;
 }
 assertEquals(Primitives.primitiveValueOrNullFor(type), Primitives.primitiveValues.get(type));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullParameter() {
 Primitives.primitiveTypeOf(null);
 }
}