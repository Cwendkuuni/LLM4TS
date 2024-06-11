package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

 private static class IDKey {
      private final Object value;

      IDKey(Object value) {
          this.value = value;
      }

      @Override
      public int hashCode() {
          return value.hashCode();
      }

      @Override
      public boolean equals(Object obj) {
          if (obj instanceof IDKey) {
              return ((IDKey) obj).value.equals(this.value);
          }
          return false;
      }
  }

 @Test
 public void testConstructors() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     assertNotNull(hcb);
     HashCodeBuilder hcbWithParams = new HashCodeBuilder(17, 37);
     assertNotNull(hcbWithParams);
 }

 @Test
 public void testAppendBoolean() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     hcb.append(true);
     int expectedHashCode = 37;
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendBooleanArray() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     boolean[] booleanArray = {true, false, true};
     hcb.append(booleanArray);
     int expectedHashCode = 37 * 3 + 1 + 0 + 37 * 3 + 0 + 37 * 3 + 1;
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendByte() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     hcb.append((byte) 5);
     int expectedHashCode = 37 * 11 + 5;
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendByteArray() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     byte[] byteArray = {1, 2, 3};
     hcb.append(byteArray);
     int expectedHashCode = 37 * 5 + 2 + 3;
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendChar() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     hcb.append('A');
     int expectedHashCode = 37 * 11 + 65;
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendCharArray() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     char[] charArray = {'A', 'B', 'C'};
     hcb.append(charArray);
     int expectedHashCode = 37 * 11 + 65 + 66 + 67;
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendDouble() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     double value = 1.1;
     hcb.append(value);
     long doubleBits = Double.doubleToLongBits(value);
     int expectedHashCode = 37 * 5 + (int) (doubleBits ^ (doubleBits >>> 32));
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendDoubleArray() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     double[] doubleArray = {1.1, 2.2, 3.3};
     hcb.append(doubleArray);
     long[] doubleBits = new long[doubleArray.length];
     for (int i = 0; i < doubleArray.length; i++) {
         doubleBits[i] = Double.doubleToLongBits(doubleArray[i]);
     }
     long expectedHashCode = 1;
     for (long bits : doubleBits) {
         expectedHashCode *= 37;
         expectedHashCode += (int) (bits ^ (bits >>> 32));
     }
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendFloat() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     float value = 1.1f;
     hcb.append(value);
     int expectedHashCode = 37 * 5 + Float.floatToIntBits(value);
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendFloatArray() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     float[] floatArray = {1.1f, 2.2f, 3.3f};
     hcb.append(floatArray);
     int expectedHashCode = 1;
     for (int i = 0; i < floatArray.length; i++) {
         int floatCode = Float.floatToIntBits(floatArray[i]) % 32;
         expectedHashCode *= 37;
         expectedHashCode += floatCode;
     }
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendInt() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     int value = 5;
     hcb.append(value);
     int expectedHashCode = 37 * 11 + value;
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendIntArray() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     int[] intArray = {1, 2, 3};
     hcb.append(intArray);
     int expectedHashCode = 1;
     for (int value : intArray) {
         expectedHashCode *= 37;
         expectedHashCode += value;
     }
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendLong() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     long value = 5L;
     hcb.append(value);
     int expectedHashCode = 37 * 11 + (int) (value ^ (value >>> 32));
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendLongArray() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     long[] longArray = {1, 2, 3};
     hcb.append(longArray);
     long expectedHashCode = 1;
     for (long value : longArray) {
         expectedHashCode *= 37;
         expectedHashCode += (int) (value ^ (value >>> 32));
     }
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendObject() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     Object obj = new Object() {};
     hcb.append(obj);
     int expectedHashCode = 37 * 11 + obj.hashCode();
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendObjectArray() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     Object[] objectArray = {new Object() {}, new Object() {}};
     hcb.append(objectArray);
     int expectedHashCode = 1;
     for (Object obj : objectArray) {
         expectedHashCode *= 37;
         expectedHashCode += obj.hashCode();
     }
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendShort() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     short value = 5;
     hcb.append(value);
     int expectedHashCode = 37 * 11 + value;
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendShortArray() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     short[] shortArray = {1, 2, 3};
     hcb.append(shortArray);
     int expectedHashCode = 1;
     for (short value : shortArray) {
         expectedHashCode *= 37;
         expectedHashCode += value;
     }
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testAppendSuperHashCode() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     int superHashCode = 17;
     hcb.appendSuper(superHashCode);
     int expectedHashCode = 37 * 11 + superHashCode;
     int calculatedHashCode = hcb.toHashCode();
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testReflectionHashCodeWithObjectArray() {
     Object[] objArray = new Object[] {1, "two", 3.0f};
     int expectedHashCode = reflectionHashCode(objArray, false);
     int calculatedHashCode = HashCodeBuilder.reflectionHashCode(objArray, false);
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testReflectionHashCodeWithStringArray() {
     String[] strArray = {"one", "two", "three"};
     int expectedHashCode = reflectionHashCode(strArray, false);
     int calculatedHashCode = HashCodeBuilder.reflectionHashCode(strArray, false);
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testReflectionHashCodeWithExcludedFields() {
     Object obj = new TestClass();
     int expectedHashCode = reflectionHashCode(obj, "excludedField1", "excludedField2");
     int calculatedHashCode = HashCodeBuilder.reflectionHashCode(obj, new String[] {"excludedField1", "excludedField2"});
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReflectionHashCodeWithNullObject() {
     HashCodeBuilder.reflectionHashCode(null, 17, 37, false, null, new String[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReflectionHashCodeWithNullExcludeFields() {
     HashCodeBuilder.reflectionHashCode(new TestClass(), 17, 37, false, null, (String[]) null);
 }

 @Test
 public void testReflectionHashCodeWithTestTransients() {
     Object obj = new TestClass();
     int expectedHashCode = reflectionHashCode(obj, true);
     int calculatedHashCode = HashCodeBuilder.reflectionHashCode(obj, true);
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testReflectionHashCodeWithUpToClass() {
     Object obj = new TestClass();
     int expectedHashCode = reflectionHashCode(obj, TestClass.class);
     int calculatedHashCode = HashCodeBuilder.reflectionHashCode(obj, TestClass.class);
     assertEquals(expectedHashCode, calculatedHashCode);
 }

 @Test
 public void testIsRegistered() {
     Object obj = new Object() {};
     HashCodeBuilder hcb = new HashCodeBuilder();
     reflectionAppend(obj, obj.getClass(), hcb, false, null);
     assertTrue(HashCodeBuilder.isRegistered(obj));
 }

 @Test
 public void testIsRegisteredWithNullObject() {
     assertFalse(HashCodeBuilder.isRegistered(null));
 }

 @Test
 public void testGetRegistry() {
     HashCodeBuilder hcb = new HashCodeBuilder();
     Set<IDKey> registry = HashCodeBuilder.getRegistry();
     assertEquals(registry.size(), 0);
 }

 @Test
 public void testRegister() {
     Object obj = new Object() {};
     HashCodeBuilder hcb = new HashCodeBuilder();
     reflectionAppend(obj, obj.getClass(), hcb, false, null);
     assertTrue(HashCodeBuilder.isRegistered(obj));
 }

 @Test
 public void testUnregister() {
     Object obj = new Object() {};
     HashCodeBuilder hcb = new HashCodeBuilder();
     reflectionAppend(obj, obj.getClass(), hcb, false, null);
     HashCodeBuilder.unregister(obj);
     assertFalse(HashCodeBuilder.isRegistered(obj));
 }

 private void reflectionAppend(Object object, Class<?> clazz, HashCodeBuilder builder, boolean useTransients, String[] excludeFields) {
     try {
         Field[] fields = clazz.getDeclaredFields();
         for (Field field : fields) {
             if (!ArrayUtils.contains(excludeFields, field.getName()) && field.getName().indexOf(36) == -1 && (!Modifier.isTransient(field.getModifiers()) || useTransients) && !Modifier.isStatic(field.getModifiers())) {
                 Object fieldValue = field.get(object);
                 builder.append(fieldValue);
             }
         }
         if (clazz.getSuperclass() != null) {
             reflectionAppend(object, clazz.getSuperclass(), builder, useTransients, excludeFields);
         }
     } catch (IllegalAccessException e) {
         throw new RuntimeException("Unexpected IllegalAccessException", e);
     }
 }

 private int reflectionHashCode(Object object, boolean testTransients, Class<? super Object> reflectUpToClass, String... excludeFields) {
     HashCodeBuilder hcb = new HashCodeBuilder();
     reflectionAppend(object, object.getClass(), hcb, testTransients, excludeFields);
     if (reflectUpToClass != null) {
         reflectionAppend(object, reflectUpToClass, hcb, testTransients, excludeFields);
     }
     return hcb.toHashCode();
 }

 private int reflectionHashCode(Object[] objects, boolean testTransients) {
     int hashCode = 17;
     for (Object obj : objects) {
         hashCode = HashCodeBuilder.reflectionHashCode(obj, testTransients);
     }
     return hashCode;
 }

 private static class TestClass {
      private int excludedField1;
      private int excludedField2;
      private int field1;
      private int field2;

      @Override
      public String toString() {
          return "TestClass{" +
                 "excludedField1=" + excludedField1 +
                 ", excludedField2=" + excludedField2 +
                 ", field1=" + field1 +
                 ", field2=" + field2 +
                 '}';
      }
  }
}