package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {

 private static final Map<Class<?>, Class<?>> expectedPrimitiveTypes = new HashMap<Class<?>, Class<?>>() {{
 put(Boolean.class, Boolean.TYPE);
 put(Character.class, Character.TYPE);
 put(Byte.class, Byte.TYPE);
 put(Short.class, Short.TYPE);
 put(Integer.class, Integer.TYPE);
 put(Long.class, Long.TYPE);
 put(Float.class, Float.TYPE);
 put(Double.class, Double.TYPE);
 }};

 private static final Map<Class<?>, Class<?>> expectedWrapperTypes = new HashMap<Class<?>, Class<?>>() {{
 put(Boolean.class, Boolean.class);
 put(Character.class, Character.class);
 put(Byte.class, Byte.class);
 put(Short.class, Short.class);
 put(Integer.class, Integer.class);
 put(Long.class, Long.class);
 put(Float.class, Float.class);
 put(Double.class, Double.class);
 }};

 private static final Map<Class<?>, Object> expectedWrapperReturnValues = new HashMap<Class<?>, Object>() {{
 put(Boolean.class, false);
 put(Character.class, '\u0000');
 put(Byte.class, (byte) 0);
 put(Short.class, (short) 0);
 put(Integer.class, 0);
 put(Long.class, 0L);
 put(Float.class, 0F);
 put(Double.class, 0D);
 }};

 private static final Map<Class<?>, Object> expectedPrimitiveValues = new HashMap<Class<?>, Object>() {{
 put(boolean.class, false);
 put(char.class, '\u0000');
 put(byte.class, (byte) 0);
 put(short.class, (short) 0);
 put(int.class, 0);
 put(long.class, 0L);
 put(float.class, 0F);
 put(double.class, 0.0);
 }};

 @Test
 public void testPrimitiveTypeOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedPrimitiveTypes.entrySet()) {
 Class<?> actual = Primitives.primitiveTypeOf(entry.getKey());
 assertEquals(entry.getValue(), actual);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 assertEquals(true, Primitives.isPrimitiveWrapper(entry.getValue()));
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 Object actual = Primitives.primitiveWrapperOf(entry.getKey());
 assertEquals(entry.getValue(), actual.getClass());
 }
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 Boolean actual = Primitives.primitiveValueOrNullFor(boolean.class);
 assertNotNull(actual);
 assertEquals(false, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForChar() {
 Character actual = Primitives.primitiveValueOrNullFor(char.class);
 assertNotNull(actual);
 assertEquals('\u0000', actual);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 Byte actual = Primitives.primitiveValueOrNullFor(byte.class);
 assertNotNull(actual);
 assertEquals((byte) 0, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 Short actual = Primitives.primitiveValueOrNullFor(short.class);
 assertNotNull(actual);
 assertEquals((short) 0, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 Integer actual = Primitives.primitiveValueOrNullFor(int.class);
 assertNotNull(actual);
 assertEquals(0, actual.intValue());
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 Long actual = Primitives.primitiveValueOrNullFor(long.class);
 assertNotNull(actual);
 assertEquals(0L, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 Float actual = Primitives.primitiveValueOrNullFor(float.class);
 assertNotNull(actual);
 assertEquals(0F, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 Double actual = Primitives.primitiveValueOrNullFor(double.class);
 assertNotNull(actual);
 assertEquals(0.0, actual.doubleValue());
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveValueOrNullForNull() {
 Primitives.primitiveValueOrNullFor(null);
 }

 @Test
 public void testWrapperReturnValues() {
 for (Map.Entry<Class<?>, Object> entry : expectedWrapperReturnValues.entrySet()) {
 Object actual = Primitives.primitiveWrapperOf(entry.getKey()).newInstance();
 assertEquals(entry.getValue(), actual);
 }
 }
}