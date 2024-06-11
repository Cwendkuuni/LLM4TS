package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_TYPES = {
         Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE,
         Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE
 };

 private static final Class<?>[] WRAPPER_TYPES = {
         Boolean.class, Character.class, Byte.class, Short.class,
         Integer.class, Long.class, Float.class, Double.class
 };

 @Test
 public void testPrimitiveTypeOf() {
     for (Class<?> wrapperType : WRAPPER_TYPES) {
         Class<?> primitiveType = Primitives.primitiveTypeOf(wrapperType);
         Assert.assertSame(PRIMITIVE_TYPES[WrapperTypes.indexOf(wrapperType)], primitiveType);
     }
 }

 @Test
 public void testIsPrimitiveWrapper() {
     for (Class<?> wrapperType : WRAPPER_TYPES) {
         boolean result = Primitives.isPrimitiveWrapper(wrapperType);
         Assert.assertTrue("IsPrimitiveWrapper should return true for " + wrapperType, result);
     }
 }

 @Test
 public void testPrimitiveWrapperOf() {
     for (Class<?> wrapperType : WRAPPER_TYPES) {
         Object result = Primitives.primitiveWrapperOf(wrapperType);
         if (wrapperType == Boolean.class) {
             Assert.assertSame(Boolean.FALSE, result);
         } else if (wrapperType == Character.class) {
             Assert.assertEquals('\u0000', result.charValue());
         } else {
             Assert.assertEquals(0, result.hashCode());
         }
     }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         Object result = Primitives.primitiveValueOrNullFor(primitiveType);
         if (primitiveType == boolean.class) {
             Assert.assertNull(result);
         } else {
             Assert.assertEquals(0, result.hashCode());
         }
     }
 }

 @Test
 public void testPrimitiveTypesInitialization() {
     Map<Class<?>, Class<?>> primitiveTypesMap = new HashMap<Class<?>, Class<?>>();
     Primitives.initStatic();
     Assert.assertEquals(primitiveTypesMap, Primitives.primitiveTypes);
 }

 @Test
 public void testWrapperReturnValuesInitialization() {
     Map<Class<?>, Object> wrapperReturnValuesMap = new HashMap<Class<?>, Object>();
     wrapperReturnValuesMap.putAll(Primitives.wrapperReturnValues);
     Assert.assertEquals(wrapperReturnValuesMap, Primitives.wrapperReturnValues);
 }

 @Test
 public void testPrimitiveValuesInitialization() {
     Map<Class<?>, Object> primitiveValuesMap = new HashMap<Class<?>, Object>();
     primitiveValuesMap.putAll(Primitives.primitiveValues);
     Assert.assertEquals(primitiveValuesMap, Primitives.primitiveValues);
 }
}