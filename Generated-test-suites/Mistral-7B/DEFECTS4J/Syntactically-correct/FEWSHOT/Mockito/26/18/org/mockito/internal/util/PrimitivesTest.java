package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {

 private static final Map<Class<?>, Class<?>> wrapperTypesMap = new HashMap<>();
 private static final Map<Class<?>, Class<?>> primitiveTypesMap = new HashMap<>();
 private static final Map<Class<?>, Object> wrapperReturnValuesMap = new HashMap<>();
 private static final Map<Class<?>, Object> primitiveValuesMap = new HashMap<>();

 static {
 initMaps();
 }

 @Test
 public void testPrimitiveTypeOf() {
 Class<?> expectedType;
 Class<?> actualType;

 // Test with primitive types
 expectedType = boolean.class;
 actualType = Primitives.primitiveTypeOf(Boolean.class);
 assertEquals(expectedType, actualType);

 expectedType = char.class;
 actualType = Primitives.primitiveTypeOf(Character.class);
 assertEquals(expectedType, actualType);

 expectedType = byte.class;
 actualType = Primitives.primitiveTypeOf(Byte.class);
 assertEquals(expectedType, actualType);

 expectedType = short.class;
 actualType = Primitives.primitiveTypeOf(Short.class);
 assertEquals(expectedType, actualType);

 expectedType = int.class;
 actualType = Primitives.primitiveTypeOf(Integer.class);
 assertEquals(expectedType, actualType);

 expectedType = long.class;
 actualType = Primitives.primitiveTypeOf(Long.class);
 assertEquals(expectedType, actualType);

 expectedType = float.class;
 actualType = Primitives.primitiveTypeOf(Float.class);
 assertEquals(expectedType, actualType);

 expectedType = double.class;
 actualType = Primitives.primitiveTypeOf(Double.class);
 assertEquals(expectedType, actualType);

 // Test with wrapper classes
 expectedType = Byte.class;
 actualType = Primitives.primitiveTypeOf(byte[].class);
 assertNull(actualType);

 expectedType = Integer.class;
 actualType = Primitives.primitiveTypeOf(Integer[].class);
 assertNull(actualType);

 expectedType = Long.class;
 actualType = Primitives.primitiveTypeOf(long[].class);
 assertNull(actualType);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<?> type;
 boolean expectedResult;

 // Test with primitive types
 type = boolean.class;
 expectedResult = false;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = Boolean.class;
 expectedResult = true;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = char.class;
 expectedResult = false;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = Character.class;
 expectedResult = true;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = byte.class;
 expectedResult = false;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = Byte.class;
 expectedResult = true;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = short.class;
 expectedResult = false;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = Short.class;
 expectedResult = true;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = int.class;
 expectedResult = false;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = Integer.class;
 expectedResult = true;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = long.class;
 expectedResult = false;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = Long.class;
 expectedResult = true;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = float.class;
 expectedResult = false;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = Float.class;
 expectedResult = true;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = double.class;
 expectedResult = false;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));

 type = Double.class;
 expectedResult = true;
 assertEquals(expectedResult, Primitives.isPrimitiveWrapper(type));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<?> expectedType;
 Object expectedValue;
 Object actualValue;

 // Test with primitive types
 expectedType = boolean.class;
 expectedValue = false;
 actualValue = Primitives.primitiveWrapperOf(expectedType);
 assertEquals(expectedValue, actualValue);
 assertEquals(Boolean.class, actualValue.getClass());

 expectedType = char.class;
 expectedValue = '\u0000';
 actualValue = Primitives.primitiveWrapperOf(expectedType);
 assertEquals(expectedValue, actualValue);
 assertEquals(Character.class, actualValue.getClass());

 expectedType = byte.class;
 expectedValue = (byte) 0;
 actualValue = Primitives.primitiveWrapperOf(expectedType);
 assertEquals(expectedValue, actualValue);
 assertEquals(Byte.class, actualValue.getClass());

 expectedType = short.class;
 expectedValue = (short) 0;
 actualValue = Primitives.primitiveWrapperOf(expectedType);
 assertEquals(expectedValue, actualValue);
 assertEquals(Short.class, actualValue.getClass());

 expectedType = int.class;
 expectedValue = 0;
 actualValue = Primitives.primitiveWrapperOf(expectedType);
 assertEquals(expectedValue, actualValue);
 assertEquals(Integer.class, actualValue.getClass());

 expectedType = long.class;
 expectedValue = 0L;
 actualValue = Primitives.primitiveWrapperOf(expectedType);
 assertEquals(expectedValue, actualValue);
 assertEquals(Long.class, actualValue.getClass());

 expectedType = float.class;
 expectedValue = 0F;
 actualValue = Primitives.primitiveWrapperOf(expectedType);
 assertEquals(expectedValue, actualValue);
 assertEquals(Float.class, actualValue.getClass());

 expectedType = double.class;
 expectedValue = 0D;
 actualValue = Primitives.primitiveWrapperOf(expectedType);
 assertEquals(expectedValue, actualValue);
 assertEquals(Double.class, actualValue.getClass());

 // Test with null
 expectedType = null;
 expectedValue = null;
 assertNull(Primitives.primitiveWrapperOf(expectedType));
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 Object expectedValue;
 Object actualValue;

 // Test with primitive types
 expectedValue = false;
 actualValue = Primitives.primitiveValueOrNullFor(boolean.class);
 assertEquals(expectedValue, actualValue);

 expectedValue = '\u0000';
 actualValue = Primitives.primitiveValueOrNullFor(char.class);
 assertEquals(expectedValue, actualValue);

 expectedValue = (byte) 0;
 actualValue = Primitives.primitiveValueOrNullFor(byte.class);
 assertEquals(expectedValue, actualValue);

 expectedValue = (short) 0;
 actualValue = Primitives.primitiveValueOrNullFor(short.class);
 assertEquals(expectedValue, actualValue);

 expectedValue = 0;
 actualValue = Primitives.primitiveValueOrNullFor(int.class);
 assertEquals(expectedValue, actualValue);

 expectedValue = 0L;
 actualValue = Primitives.primitiveValueOrNullFor(long.class);
 assertEquals(expectedValue, actualValue);

 expectedValue = 0F;
 actualValue = Primitives.primitiveValueOrNullFor(float.class);
 assertEquals(expectedValue, actualValue);

 expectedValue = 0D;
 actualValue = Primitives.primitiveValueOrNullFor(double.class);
 assertEquals(expectedValue, actualValue);

 // Test with null
 expectedValue = null;
 actualValue = Primitives.primitiveValueOrNullFor(null);
 assertNull(actualValue);
 }

 private static void initMaps() {
 for (Map.Entry<Class<?>, Class<?>> entry : wrapperTypes.entrySet()) {
 wrapperTypesMap.put(entry.getKey(), entry.getValue());
 primitiveTypesMap.put(entry.getValue(), entry.getKey());
 }

 for (Map.Entry<Class<?>, Object> entry : wrapperReturnValues.entrySet()) {
 wrapperReturnValuesMap.put(entry.getKey(), entry.getValue());
 primitiveValuesMap.put(entry.getValue().getClass(), entry.getValue());
 }

 wrapperTypes = wrapperTypesMap;
 primitiveTypes = primitiveTypesMap;
 wrapperReturnValues = wrapperReturnValuesMap;
 primitiveValues = primitiveValuesMap;
 }
}