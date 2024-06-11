package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {

 private static final Map<Class<?>, Class<?>> expectedWrapperTypes = new HashMap<Class<?>, Class<?>>() {{
 put(Boolean.class, Boolean.TYPE);
 put(Character.class, Character.TYPE);
 put(Byte.class, Byte.TYPE);
 put(Short.class, Short.TYPE);
 put(Integer.class, Integer.TYPE);
 put(Long.class, Long.TYPE);
 put(Float.class, Float.TYPE);
 put(Double.class, Double.TYPE);
 }};

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

 private static final Map<Class<?>, Object> expectedWrapperValues = new HashMap<Class<?>, Object>() {{
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
 put(double.class, 0D);
 }};

 @Test
 public void testPrimitiveTypeOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedPrimitiveTypes.entrySet()) {
 assertEquals(entry.getValue(), Primitives.primitiveTypeOf(entry.getKey()));
 }

 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 Class<?> primitiveType = Primitives.primitiveTypeOf(entry.getKey());
 assertEquals(entry.getValue(), primitiveType);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 assertTrue(Primitives.isPrimitiveWrapper(entry.getKey()));
 }

 for (Class<?> nonPrimitiveWrapper : new Class[] { String.class, Integer[].class }) {
 assertFalse(Primitives.isPrimitiveWrapper(nonPrimitiveWrapper));
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 assertEquals(entry.getKey(), Primitives.primitiveWrapperOf(entry.getValue()));
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
 public void testPrimitiveValueOrNullForInteger() {
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
 public void testWrapperReturnValues() {
 for (Map.Entry<Class<?>, Object> entry : expectedWrapperValues.entrySet()) {
 assertEquals(entry.getValue(), Primitives.wrapperReturnValues.get(entry.getKey()));
 }
 }

 @Test
 public void testPrimitiveValues() {
 for (Map.Entry<Class<?>, Object> entry : expectedPrimitiveValues.entrySet()) {
 assertEquals(entry.getValue(), Primitives.primitiveValues.get(entry.getKey()));
 }
 }
}