package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_TYPES = new Class[] {
 Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
 Float.TYPE, Double.TYPE
 };

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> clazz : PRIMITIVE_TYPES) {
 Class<?> expected = clazz;
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals("primitiveTypeOf returned unexpected type for " + clazz, expected, actual);
 }
 }

 @Test
 public void testPrimitiveTypeOfWrapperClass() {
 for (Class<?> wrapperClass : new Class[] { Boolean.class, Character.class, Byte.class,
 Short.class, Integer.class, Long.class, Float.class, Double.class }) {
 Class<?> expected = Primitives.primitiveTypeOf(wrapperClass);
 Assert.assertNotNull("primitiveTypeOf returned null for wrapper class " + wrapperClass, expected);
 Class<?> actual = Primitives.primitiveTypeOf(Primitives.primitiveTypeOf(wrapperClass));
 Assert.assertEquals("primitiveTypeOf did not correctly identify primitive type for wrapper class " + wrapperClass,
 Primitives.primitiveTypeOf(wrapperClass), actual);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> wrapperClass : new Class[] { Boolean.class, Character.class, Byte.class,
 Short.class, Integer.class, Long.class, Float.class, Double.class }) {
 boolean expected = true;
 boolean actual = Primitives.isPrimitiveWrapper(wrapperClass);
 Assert.assertEquals("isPrimitiveWrapper returned unexpected value for " + wrapperClass, expected, actual);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> primitiveType : PRIMITIVE_TYPES) {
 Object expected = Primitives.primitiveValueOrNullFor(primitiveType);
 Object actual = Primitives.primitiveWrapperOf(primitiveType);
 if (expected == null) {
 Assert.assertNull("primitiveWrapperOf returned unexpected value for nullable primitive type " + primitiveType, actual);
 } else {
 Assert.assertEquals("primitiveWrapperOf returned unexpected value for primitive type " + primitiveType, expected, actual);
 }
 }
 }

 @Test
 public void testWrapperReturnValues() {
 for (Class<?> primitiveType : PRIMITIVE_TYPES) {
 Object expected = Primitives.primitiveValueOrNullFor(Primitives.primitiveTypeOf(primitiveType));
 Object actual = Primitives.wrapperReturnValues.get(primitiveType);
 Assert.assertEquals("wrapperReturnValues returned unexpected value for primitive type " + primitiveType, expected, actual);
 }
 }

 @Test
 public void testPrimitiveValues() {
 for (Class<?> primitiveType : PRIMITIVE_TYPES) {
 Object expected = Primitives.primitiveValueOrNullFor(primitiveType);
 Object actual = Primitives.primitiveValues.get(primitiveType);
 Assert.assertEquals("primitiveValues returned unexpected value for primitive type " + primitiveType, expected, actual);
 }
 }
}