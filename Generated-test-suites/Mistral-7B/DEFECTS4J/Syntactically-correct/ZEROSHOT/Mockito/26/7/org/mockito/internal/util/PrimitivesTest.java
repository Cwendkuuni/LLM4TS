package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 private Class<?> clazz;
 private Class<?> primitiveType;
 private Class<?> wrapperType;
 private Class<?> expectedWrapperType;
 private boolean isPrimitive;
 private boolean isWrapper;
 private Object primitiveValue;
 private Object wrapperValue;

 @Before
 public void setup() {
 clazz = Double.class;
 primitiveType = Double.TYPE;
 wrapperType = Double.class;
 expectedWrapperType = Double.class;
 isPrimitive = true;
 isWrapper = false;
 primitiveValue = 0.0;
 wrapperValue = new Double(0.0);
 }

 @Test
 public void testPrimitiveTypeOf() {
 Class<Double> result = Primitives.primitiveTypeOf(clazz);
 assertEquals(primitiveType, result);

 clazz = Integer.class;
 result = Primitives.primitiveTypeOf(clazz);
 assertEquals(Integer.TYPE, result);

 clazz = String.class;
 result = Primitives.primitiveTypeOf(clazz);
 assertNull(result);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 isWrapper = Primitives.isPrimitiveWrapper(clazz);
 assertFalse(isWrapper);

 clazz = Boolean.class;
 isWrapper = Primitives.isPrimitiveWrapper(clazz);
 assertTrue(isWrapper);
 }

 @Test
 public void testPrimitiveWrapperOf() {
 wrapperValue = Primitives.primitiveWrapperOf(wrapperType);
 assertEquals(primitiveValue, wrapperValue);
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals(primitiveValue, primitiveValueOrNullFor(primitiveType));
 }

 @Test(expected = NullPointerException.class)
 public void testNullClass() {
 Primitives.primitiveTypeOf(null);
 }

 @Test
 public void testWrapperTypesMapInitialization() {
 Map<Class<?>, Class<?>> actualWrapperTypes = new HashMap<>(Primitives.wrapperTypes);
 assertEquals(8, actualWrapperTypes.size());
 assertSame(Boolean.TYPE, actualWrapperTypes.get(Boolean.class));
 // other assertions for other types
 }

 @Test
 public void testWrapperReturnValuesMapInitialization() {
 Map<Class<?>, Object> actualWrapperReturnValues = new HashMap<>(Primitives.wrapperReturnValues);
 assertEquals(8, actualWrapperReturnValues.size());
 assertSame(new Boolean(false), actualWrapperReturnValues.get(Boolean.class));
 // other assertions for other types
 }

 @Test
 public void testPrimitiveValuesMapInitialization() {
 Map<Class<?>, Object> actualPrimitivesValues = new HashMap<>(Primitives.primitiveValues);
 assertEquals(8, actualPrimitivesValues.size());
 assertSame(Boolean.FALSE, actualPrimitivesValues.get(boolean.class));
 // other assertions for other types
 }
}