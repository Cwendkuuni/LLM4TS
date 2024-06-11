package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_TYPES = {
 boolean.class, Character.TYPE, Byte.TYPE, Short.TYPE,
 Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE
 };
 private static final Class<?>[] WRAPPER_TYPES = {
 Boolean.class, Character.class, Byte.class, Short.class,
 Integer.class, Long.class, Float.class, Double.class
 };

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> primitiveType : PRIMITIVE_TYPES) {
 Class<?> expected = primitiveType;
 if (primitiveType.isPrimitive()) {
 Class<?> actual = Primitives.primitiveTypeOf(primitiveType);
 assertEquals(expected, actual);
 } else {
 Class<?> actual = Primitives.primitiveTypeOf(WrapperTypes.class.getDeclaredField(primitiveType.getName()).getType());
 assertEquals(expected.getTypeName(), actual.getTypeName());
 }
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> wrapperType : WRAPPER_TYPES) {
 boolean expected = true;
 boolean actual = Primitives.isPrimitiveWrapper(wrapperType);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> wrapperType : WRAPPER_TYPES) {
 Object expected = Primitives.primitiveValueOrNullFor(Primitives.primitiveTypeOf(wrapperType));
 Object actual = Primitives.primitiveWrapperOf(wrapperType);
 if (expected == null) {
 assertNull(actual);
 } else {
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Class<?> primitiveType : PRIMITIVE_TYPES) {
 Object expected = Primitives.primitiveValues.get(primitiveType);
 Object actual = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals(expected, actual);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullPrimitiveType() {
 Primitives.primitiveTypeOf(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullWrapperType() {
 Primitives.primitiveWrapperOf(null);
 }

}