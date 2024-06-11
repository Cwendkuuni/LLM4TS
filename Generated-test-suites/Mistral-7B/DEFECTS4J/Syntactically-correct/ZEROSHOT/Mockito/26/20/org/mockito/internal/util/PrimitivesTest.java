package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PrimitivesTest {

 private Class<?> primitiveTypeOfClass;
 private Class<Boolean> booleanClass;
 private Class<Character> characterClass;
 private Class<Byte> byteClass;
 private Class<Short> shortClass;
 private Class<Integer> intClass;
 private Class<Long> longClass;
 private Class<Float> floatClass;
 private Class<Double> doubleClass;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 booleanClass = (Class<Boolean>) Class.forName("java.lang.Boolean");
 characterClass = Class.forName("char");
 byteClass = Class.forName("byte");
 shortClass = Class.forName("short");
 intClass = Class.forName("int");
 longClass = Class.forName("long");
 floatClass = Class.forName("float");
 doubleClass = Class.forName("double");
 primitiveTypeOfClass = Primitives.primitiveTypeOf(booleanClass);
 }

 @Test
 public void testPrimitiveTypeOf() {
 // Test for primitives
 assertEquals(boolean.class, Primitives.primitiveTypeOf(booleanClass));
 assertEquals(char.class, Primitives.primitiveTypeOf(characterClass));
 assertEquals(byte.class, Primitives.primitiveTypeOf(byteClass));
 assertEquals(short.class, Primitives.primitiveTypeOf(shortClass));
 assertEquals(int.class, Primitives.primitiveTypeOf(intClass));
 assertEquals(long.class, Primitives.primitiveTypeOf(longClass));
 assertEquals(float.class, Primitives.primitiveTypeOf(floatClass));
 assertEquals(double.class, Primitives.primitiveTypeOf(doubleClass));

 // Test for Object
 assertNull(Primitives.primitiveTypeOf(Object.class));

 // Test for wrapper classes
 assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(Boolean.class));
 assertEquals(Character.TYPE, Primitives.primitiveTypeOf(Character.class));
 assertEquals(Byte.TYPE, Primitives.primitiveTypeOf(Byte.class));
 assertEquals(Short.TYPE, Primitives.primitiveTypeOf(Short.class));
 assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(Integer.class));
 assertEquals(Long.TYPE, Primitives.primitiveTypeOf(Long.class));
 assertEquals(Float.TYPE, Primitives.primitiveTypeOf(Float.class));
 assertEquals(Double.TYPE, Primitives.primitiveTypeOf(Double.class));
 }

 @Test
 public void testIsPrimitiveWrapper() {
 // Test for primitives
 assertFalse(Primitives.isPrimitiveWrapper(booleanClass));
 assertFalse(Primitives.isPrimitiveWrapper(characterClass));
 assertFalse(Primitives.isPrimitiveWrapper(byteClass));
 assertFalse(Primitives.isPrimitiveWrapper(shortClass));
 assertFalse(Primitives.isPrimitiveWrapper(intClass));
 assertFalse(Primitives.isPrimitiveWrapper(longClass));
 assertFalse(Primitives.isPrimitiveWrapper(floatClass));
 assertFalse(Primitives.isPrimitiveWrapper(doubleClass));

 // Test for wrapper classes
 assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
 assertTrue(Primitives.isPrimitiveWrapper(Character.class));
 assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
 assertTrue(Primitives.isPrimitiveWrapper(Short.class));
 assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
 assertTrue(Primitives.isPrimitiveWrapper(Long.class));
 assertTrue(Primitives.isPrimitiveWrapper(Float.class));
 assertTrue(Primitives.isPrimitiveWrapper(Double.class));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 // Test for primitives
 assertNull(Primitives.primitiveWrapperOf(booleanClass));
 assertNull(Primitives.primitiveWrapperOf(characterClass));
 assertNull(Primitives.primitiveWrapperOf(byteClass));
 assertNull(Primitives.primitiveWrapperOf(shortClass));
 assertNull(Primitives.primitiveWrapperOf(intClass));
 assertNull(Primitives.primitiveWrapperOf(longClass));
 assertNull(Primitives.primitiveWrapperOf(floatClass));
 assertNull(Primitives.primitiveWrapperOf(doubleClass));

 // Test for wrapper classes
 assertEquals(false, Primitives.primitiveWrapperOf(Boolean.class));
 assertEquals((char) 0, Primitives.primitiveWrapperOf(Character.class));
 assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.class));
 assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.class));
 assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
 assertEquals(0L, Primitives.primitiveWrapperOf(Long.class));
 assertEquals(0F, Primitives.primitiveWrapperOf(Float.class));
 assertEquals(0D, Primitives.primitiveWrapperOf(Double.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 // Test for primitives
 assertNull(Primitives.primitiveValueOrNullFor(booleanClass));
 assertNull(Primitives.primitiveValueOrNullFor(characterClass));
 assertNull(Primitives.primitiveValueOrNullFor(byteClass));
 assertNull(Primitives.primitiveValueOrNullFor(shortClass));
 assertNull(Primitives.primitiveValueOrNullFor(intClass));
 assertNull(Primitives.primitiveValueOrNullFor(longClass));
 assertNull(Primitives.primitiveValueOrNullFor(floatClass));
 assertNull(Primitives.primitiveValueOrNullFor(doubleClass));

 // Test for wrapper classes
 assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.class));
 assertEquals('\u0000', Primitives.primitiveValueOrNullFor(Character.class));
 assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(Byte.class));
 assertEquals((short) 0, Primitives.primitiveValueOrNullFor(Short.class));
 assertEquals(0, Primitives.primitiveValueOrNullFor(Integer.class));
 assertEquals(0L, Primitives.primitiveValueOrNullFor(Long.class));
 assertEquals(0F, Primitives.primitiveValueOrNullFor(Float.class));
 assertEquals(0D, Primitives.primitiveValueOrNullFor(Double.class));
 }

 @Test
 public void testWrapperTypesAndPrimitiveTypesAreTheSame() {
 for (Class<?> wrapperType : new Class<?>[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class }) {
 assertEquals(Primitives.primitiveTypeOf(wrapperType), wrapperType);
 }
 }

 @Test
 public void testWrapperReturnValuesAreInitialisedCorrectly() {
 assertEquals(false, (Boolean) Primitives.primitiveWrapperOf(Boolean.class));
 assertEquals('\u0000', (Character) Primitives.primitiveWrapperOf(Character.class));
 assertEquals((byte) 0, (Byte) Primitives.primitiveWrapperOf(Byte.class));
 assertEquals((short) 0, (Short) Primitives.primitiveWrapperOf(Short.class));
 assertEquals(0, (Integer) Primitives.primitiveWrapperOf(Integer.class));
 assertEquals(0L, (Long) Primitives.primitiveWrapperOf(Long.class));
 assertEquals(0F, (Float) Primitives.primitiveWrapperOf(Float.class));
 assertEquals(0D, (Double) Primitives.primitiveWrapperOf(Double.class));
 }

 @Test
 public void testPrimitiveValuesAreInitialisedCorrectly() {
 assertEquals(false, Primitives.primitiveValueOrNullFor(booleanClass));
 assertEquals('\u0000', Primitives.primitiveValueOrNullFor(characterClass));
 assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byteClass));
 assertEquals((short) 0, Primitives.primitiveValueOrNullFor(shortClass));
 assertEquals(0, Primitives.primitiveValueOrNullFor(intClass));
 assertEquals(0L, Primitives.primitiveValueOrNullFor(longClass));
 assertEquals(0F, Primitives.primitiveValueOrNullFor(floatClass));
 assertEquals(0D, Primitives.primitiveValueOrNullFor(doubleClass));
 }
}