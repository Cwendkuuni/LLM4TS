package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

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

 @Test
 public void testPrimitiveTypeOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 for (Class<?> clazz : new Class<?>[] { Integer.TYPE, entry.getKey() }) {
 assertEquals(entry.getValue(), Primitives.primitiveTypeOf(clazz));
 }
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 assertTrue(Primitives.isPrimitiveWrapper(entry.getKey()));
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 assertEquals(entry.getValue(), Primitives.primitiveWrapperOf(entry.getKey()));
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
}