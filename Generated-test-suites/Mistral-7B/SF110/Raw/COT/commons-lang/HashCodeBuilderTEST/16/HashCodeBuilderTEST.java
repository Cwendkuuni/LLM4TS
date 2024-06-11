package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

 private HashCodeBuilder builder;
 private Object object;

 @Test
 public void testHashCodeBuilderConstructor() {
     builder = new HashCodeBuilder();
     assertEquals(17, builder.iTotal);
     assertEquals(37, builder.iConstant);
 }

 @Test
 public void testHashCodeBuilderConstructorWithInitialAndMultiplier() {
     builder = new HashCodeBuilder(11, 61);
     assertEquals(11, builder.iTotal);
     assertEquals(61, builder.iConstant);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderWithEvenInitialValue() {
     try {
         new HashCodeBuilder(2, 3);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         assertEquals("HashCodeBuilder requires an odd initial value", e.getMessage());
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderWithEvenMultiplier() {
     try {
         new HashCodeBuilder(3, 2);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         assertEquals("HashCodeBuilder requires an odd multiplier", e.getMessage());
     }
 }

 @Test
 public void testAppendBoolean() {
     builder = new HashCodeBuilder();
     builder.append(true);
     assertEquals(32, builder.iTotal);
     builder.append(false);
     assertEquals(33, builder.iTotal);
 }

 @Test
 public void testAppendBooleanArray() {
     builder = new HashCodeBuilder();
     boolean[] booleanArray = {true, false, true, false};
     builder.append(booleanArray);
     assertEquals(133, builder.iTotal);
 }

 @Test
 public void testAppendByte() {
     builder = new HashCodeBuilder();
     builder.append((byte) 10);
     assertEquals(38, builder.iTotal);
 }

 @Test
 public void testAppendByteArray() {
     builder = new HashCodeBuilder();
     byte[] byteArray = {1, 2, 3};
     builder.append(byteArray);
     assertEquals(115, builder.iTotal);
 }

 @Test
 public void testAppendChar() {
     builder = new HashCodeBuilder();
     builder.append('a');
     assertEquals(53, builder.iTotal);
 }

 @Test
 public void testAppendCharArray() {
     builder = new HashCodeBuilder();
     char[] charArray = {'a', 'b', 'c'};
     builder.append(charArray);
     assertEquals(123, builder.iTotal);
 }

 @Test
 public void testAppendDouble() {
     builder = new HashCodeBuilder();
     builder.append(1.5);
     long doubleValue = Double.doubleToLongBits(1.5);
     assertEquals(37 * 32 + (int) (doubleValue ^ doubleValue >> 32), builder.iTotal);
 }

 @Test
 public void testAppendDoubleArray() {
     builder = new HashCodeBuilder();
     double[] doubleArray = {1.5, 2.5, 3.5};
     builder.append(doubleArray);
     for (Double d : doubleArray) {
         long doubleValue = Double.doubleToLongBits(d);
         assertEquals(37 * 32 + (int) (doubleValue ^ doubleValue >> 32), (builder.iTotal - 32) % (1L << 32));
         builder.iTotal = builder.iTotal % (1L << 32) + 32;
     }
     assertEquals(builder.iTotal, 17 * 37 * 32 * 3 + doubleArray.length * 32);
 }

 @Test
 public void testAppendFloat() {
     builder = new HashCodeBuilder();
     builder.append((float) 1.5);
     long floatValue = Float.floatToIntBits(1.5f);
     assertEquals(37 * 32 + (int) (floatValue >> 32), builder.iTotal);
 }

 @Test
 public void testAppendFloatArray() {
     builder = new HashCodeBuilder();
     float[] floatArray = {1.5f, 2.5f, 3.5f};
     builder.append(floatArray);
     for (Float f : floatArray) {
         long floatValue = Float.floatToIntBits(f);
         assertEquals(37 * 32 + (int) (floatValue >> 32), (builder.iTotal - 32) % (1L << 32));
         builder.iTotal = builder.iTotal % (1L << 32) + 32;
     }
     assertEquals(builder.iTotal, 17 * 37 * 32 * 3 + floatArray.length * 32);
 }

 @Test
 public void testAppendInt() {
     builder = new HashCodeBuilder();
     builder.append(5);
     assertEquals(43, builder.iTotal);
 }

 @Test
 public void testAppendIntArray() {
     builder = new HashCodeBuilder();
     int[] intArray = {1, 2, 3};
     builder.append(intArray);
     assertEquals(115, builder.iTotal);
 }

 @Test
 public void testAppendLong() {
     builder = new HashCodeBuilder();
     builder.append(10L);
     assertEquals(47, builder.iTotal);
 }

 @Test
 public void testAppendLongArray() {
     builder = new HashCodeBuilder();
     long[] longArray = {1, 2, 3};
     builder.append(longArray);
     assertEquals(123, builder.iTotal);
 }

 @Test
 public void testAppendObject() {
     builder = new HashCodeBuilder();
     String string = "test";
     builder.append(string);
     assertEquals(15 * 37 + string.hashCode(), builder.iTotal);
 }

 @Test
 public void testAppendObjectArray() {
     builder = new HashCodeBuilder();
     String[] stringArray = {"test1", "test2", "test3"};
     builder.append(stringArray);
     assertEquals(15 * 37 * 3 + stringArray.length * 32, builder.iTotal);
 }

 @Test
 public void testAppendShort() {
     builder = new HashCodeBuilder();
     builder.append((short) 10);
     assertEquals(43, builder.iTotal);
 }

 @Test
 public void testAppendShortArray() {
     builder = new HashCodeBuilder();
     short[] shortArray = {1, 2, 3};
     builder.append(shortArray);
     assertEquals(115, builder.iTotal);
 }

 @Test
 public void testAppendSuperHashCode() {
     builder = new HashCodeBuilder();
     int superHashCode = 10;
     builder.appendSuper(superHashCode);
     assertEquals(44, builder.iTotal);
 }

 @Test
 public void testReflectionHashCodeWithObject() {
     object = new Object();
     assertEquals( reflectionHashCode(17, 37, object, false), builder.toHashCode());
 }

 @Test
 public void testReflectionHashCodeWithObjectAndExcludedFields() {
     object = new Object() {
         int excludedField = 1;
     };
     String[] excludeFields = {"excludedField"};
     assertEquals( reflectionHashCode(17, 37, object, false, Object.class, excludeFields), builder.toHashCode());
 }

 @Test
 public void testReflectionHashCodeWithCollectionOfStringsAsSingleArgument() {
     Collection<String> stringCollection = new ArrayList<>();
     stringCollection.add("test1");
     stringCollection.add("test2");
     assertEquals( reflectionHashCode(17, 37, stringCollection, false), builder.toHashCode());
 }

 @Test
 public void testReflectionHashCodeWithReflectionUpToClass() {
     object = new Object() {
         Object subObject = new SubObject();

         static class SubObject {
             int subField = 1;
         }
     };
     Class<?> reflectUpToClass = Object.class;
     assertEquals( reflectionHashCode(17, 37, object, false, Object.class, new String[0]), builder.toHashCode());
     reflectUpToClass = SubObject.class;
     assertEquals( reflectionHashCode(17, 37, object, false, Object.class, new String[0]), builder.toHashCode());
     reflectUpToClass = Object.class;
     assertEquals( reflectionHashCode(17, 37, object, false, SubObject.class, new String[0]), builder.toHashCode());
 }
}