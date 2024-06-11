package org.mockito.internal.util;

import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_TYPES = {
         Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE,
         Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE
 };

 @Test
 public void testPrimitiveTypeOf() {
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         Class<?> result = Primitives.primitiveTypeOf(primitiveType);
         Assert.assertEquals(primitiveType, result);
     }

     Class<?> stringClass = String.class;
     Class<?> result = Primitives.primitiveTypeOf(stringClass);
     Assert.assertNull(result);
 }

 @Test
 public void testIsPrimitiveWrapper() {
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         boolean result = Primitives.isPrimitiveWrapper(primitiveType.wrap(null));
         Assert.assertTrue(result);

         result = Primitives.isPrimitiveWrapper(primitiveType);
         Assert.assertFalse(result);
     }
 }

 @Test
 public void testPrimitiveWrapperOf() {
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         Object result = Primitives.primitiveWrapperOf(primitiveType);
         if (primitiveType.isInstance(result)) {
             Assert.assertTrue(true);
         } else {
             Assert.fail("Expected a primitive wrapper object, but got: " + result.getClass().getName());
         }
     }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         Object result = Primitives.primitiveValueOrNullFor(primitiveType);
         if (primitiveType.isInstance(result)) {
             Assert.assertEquals(Primitives.primitiveValues.get(primitiveType), result);
         } else {
             Assert.assertNull(result);
         }
     }
 }

 @Test
 public void testWrapperTypesMap() {
     Map<Class<?>, Class<?>> map = Primitives.wrapperTypes;
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         Class<?> expectedWrapperType = Primitives.primitiveTypeOf(primitiveType);
         Class<?> actualWrapperType = map.get(primitiveType);
         Assert.assertEquals(expectedWrapperType, actualWrapperType);
     }
 }

 @Test
 public void testPrimitiveTypesMap() {
     Map<Class<?>, Class<?>> map = Primitives.primitiveTypes;
     for (Class<?> wrapperType : PRIMITIVES_WRAPPER_TYPES) {
         Class<?> expectedPrimitiveType = Primitives.primitiveTypeOf(wrapperType);
         Class<?> actualPrimitiveType = map.get(wrapperType);
         Assert.assertEquals(expectedPrimitiveType, actualPrimitiveType);
     }
 }

 @Test
 public void testWrapperReturnValuesMap() {
     Map<Class<?>, Object> map = Primitives.wrapperReturnValues;
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         Object expectedValue = Primitives.primitiveValues.get(primitiveType);
         Object actualValue = map.get(primitiveType);
         Assert.assertEquals(expectedValue, actualValue);
     }
 }

 @Test
 public void testPrimitiveValuesMap() {
     Map<Class<?>, Object> map = Primitives.primitiveValues;
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         Object expectedValue = Primitives.primitiveValues.get(primitiveType);
         Object actualValue = map.get(primitiveType);
         Assert.assertEquals(expectedValue, actualValue);
     }
 }

 private static final Class<?>[] PRIMITIVES_WRAPPER_TYPES = {
         Boolean.class, Character.class, Byte.class, Short.class,
         Integer.class, Long.class, Float.class, Double.class
 };
}