package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_WRAPPER_TYPES = {
 Boolean.class, Character.class, Byte.class, Short.class, Integer.class,
 Long.class, Float.class, Double.class, Boolean.TYPE, Character.TYPE,
 Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE };

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 Class<?> result = Primitives.primitiveTypeOf(clazz);
 assertEquals(clazz.getComponentType(), result);

 Class<?> wrapper = Primitives.primitiveTypeOf(clazz.getComponentType());
 assertEquals(clazz, Primitives.wrapperTypeOf(wrapper));
 }

 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 Class<?> primitiveType = Primitives.primitiveTypeOf(clazz);
 assertFalse(clazz.isPrimitive());
 assertTrue(primitiveType.isPrimitive());
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 assertTrue(Primitives.isPrimitiveWrapper(clazz));
 }

 Class<?> nonWrapperType = String.class;
 assertFalse(Primitives.isPrimitiveWrapper(nonWrapperType));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 Object result = Primitives.primitiveWrapperOf(clazz);
 assertTrue(result instanceof Object);

 if (clazz.isInstance(false)) {
 assertTrue((Boolean) result);
 } else if (clazz.isInstance(' ')) {
 assertEquals((char) 0, (Character) result);
 } else if (clazz.isInstance((byte) 0)) {
 assertEquals((byte) 0, (Byte) result);
 } else if (clazz.isInstance((short) 0)) {
 assertEquals((short) 0, (Short) result);
 } else if (clazz.isInstance(0)) {
 assertEquals(0, (Integer) result);
 } else if (clazz.isInstance(0L)) {
 assertEquals(0L, (Long) result);
 } else if (clazz.isInstance(0.0f)) {
 assertEquals(0.0f, (Float) result, 0.001f);
 } else if (clazz.isInstance(0.0)) {
 assertEquals(0.0, (Double) result, 0.001);
 }
 }

 Class<?> nonWrapperType2 = String.class;
 assertNull(Primitives.primitiveWrapperOf(nonWrapperType2));
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 Object result = Primitives.primitiveValueOrNullFor(clazz.getComponentType());
 if (clazz.isInstance(false)) {
 assertTrue((Boolean) result);
 } else if (clazz.isInstance(' ')) {
 assertEquals((char) 0, (Character) result);
 } else if (clazz.isInstance((byte) 0)) {
 assertEquals((byte) 0, (Byte) result);
 } else if (clazz.isInstance((short) 0)) {
 assertEquals((short) 0, (Short) result);
 } else if (clazz.isInstance(0)) {
 assertEquals(0, (Integer) result);
 } else if (clazz.isInstance(0L)) {
 assertEquals(0L, (Long) result);
 } else if (clazz.isInstance(0.0f)) {
 assertEquals(0.0f, (Float) result, 0.001f);
 } else if (clazz.isInstance(0.0)) {
 assertEquals(0.0, (Double) result, 0.001);
 } else {
 fail("Unexpected primitive type: " + clazz.getName());
 }
 }

 Class<?> nonWrapperType3 = String.class;
 assertNull(Primitives.primitiveValueOrNullFor(nonWrapperType3));
 }
}