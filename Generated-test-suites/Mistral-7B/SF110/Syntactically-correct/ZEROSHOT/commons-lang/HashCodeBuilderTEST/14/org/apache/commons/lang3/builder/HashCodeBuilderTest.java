package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 @Test
 public void testHashCodeBuilder() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(31, 31);

 assertEquals(0, hashCodeBuilder.hashCode());
 assertEquals(31, hashCodeBuilderWithInitialAndMultiplier.hashCode());
 }

 @Test
 public void testAppendBoolean() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(true);
 hashCodeBuilder.append(false);
 int expectedHashCode = 32 * 31 + 1; // true has hashCode 1
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendByte() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((byte) 1);
 hashCodeBuilder.append((byte) -128);
 int expectedHashCode = 32 * 31 + 1 + 128;
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendChar() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append('a');
 hashCodeBuilder.append('z');
 int expectedHashCode = 32 * 31 * 2 + 'z' + 'a';
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendDouble() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.0);
 hashCodeBuilder.append(3.14159265358979323846);
 long doubleHashCode = Double.doubleToLongBits(1.0) ^ (Double.doubleToLongBits(3.14159265358979323846) >>> 32);
 int expectedHashCode = (int) (32 * 31 * 2 + doubleHashCode);
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendFloat() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.0f);
 hashCodeBuilder.append(3.14159265358979323846f);
 int floatHashCode1 = Float.floatToIntBits(1.0f);
 int floatHashCode2 = Float.floatToIntBits(3.14159265358979323846f);
 int expectedHashCode = (int) (32 * 31 * 2 + floatHashCode1 + floatHashCode2);
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendInt() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1);
 hashCodeBuilder.append(42);
 int expectedHashCode = 32 * 31 * 2 + 1 + 42;
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendLong() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1L);
 hashCodeBuilder.append(4294967295L);
 long longHashCode1 = 1L ^ (1L >>> 32); // 32-bit int representation of 1
 long longHashCode2 = 4294967295L ^ (4294967295L >>> 32); // 32-bit int representation of 4294967295
 int expectedHashCode = (int) (32 * 31 * 2 + longHashCode1 + longHashCode2);
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendObject() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append("hello");
 String string = "world";
 hashCodeBuilder.append(string);
 int expectedHashCode = 32 * 31 * 2 + "hello".hashCode() + string.hashCode();
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendObjectArrays() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 boolean[] booleanArray = {true, false};
 hashCodeBuilder.append(booleanArray);
 int expectedHashCode = 32 * 31 * 2 + booleanArray.length;
 for (boolean booleanValue : booleanArray) {
 expectedHashCode *= 31;
 if (!booleanValue) {
 expectedHashCode++;
 }
 }
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendByteArrays() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 byte[] byteArray = {(byte) 1, (byte) -128};
 hashCodeBuilder.append(byteArray);
 int expectedHashCode = 32 * 31 * 2;
 for (byte byteValue : byteArray) {
 expectedHashCode *= 31;
 expectedHashCode += byteValue;
 }
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendCharArrays() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 char[] charArray = {'a', 'z'};
 hashCodeBuilder.append(charArray);
 int expectedHashCode = 32 * 31 * 2;
 for (char charValue : charArray) {
 expectedHashCode *= 31;
 expectedHashCode += charValue;
 }
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendDoubleArrays() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double[] doubleArray = {1.0, 3.14159265358979323846};
 hashCodeBuilder.append(doubleArray);
 long firstDoubleHashCode = Double.doubleToLongBits(1.0);
 long secondDoubleHashCode = Double.doubleToLongBits(3.14159265358979323846);
 int expectedHashCode = 32 * 31 * 2 + firstDoubleHashCode + secondDoubleHashCode;
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendFloatArrays() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float[] floatArray = {1.0f, 3.14159265358979323846f};
 hashCodeBuilder.append(floatArray);
 int floatHashCode1 = Float.floatToIntBits(1.0f);
 int floatHashCode2 = Float.floatToIntBits(3.14159265358979323846f);
 int expectedHashCode = 32 * 31 * 2 + floatHashCode1 + floatHashCode2;
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendIntArrays() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int[] intArray = {1, 42};
 hashCodeBuilder.append(intArray);
 int expectedHashCode = 32 * 31 * 2 + intArray.length;
 for (int integer : intArray) {
 expectedHashCode *= 31;
 expectedHashCode += integer;
 }
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendLongArrays() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 long[] longArray = {1L, 4294967295L};
 hashCodeBuilder.append(longArray);
 long firstLongHashCode = 1L ^ (1L >>> 32); // 32-bit int representation of 1
 long secondLongHashCode = 4294967295L ^ (4294967295L >>> 32); // 32-bit int representation of 4294967295
 int expectedHashCode = 32 * 31 * 2 + firstLongHashCode + secondLongHashCode;
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendObjectArraysWithExcludedFields() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 String[] stringArray = {"hello", "world"};
 hashCodeBuilder.append(stringArray, "fieldNameToExclude");
 int expectedHashCode = 32 * 31 * 2 + stringArray.length;
 for (String string : stringArray) {
 expectedHashCode *= 31;
 expectedHashCode += string.hashCode();
 }
 int actualHashCode = hashCodeBuilder.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCode() {
 Class<?> clazz = String.class;
 Object string = "hello";
 int expectedHashCode = reflectionHashCode(17, 37, string, false, clazz, new String[0]);
 int actualHashCode = HashCodeBuilder.reflectionHashCode(string);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeWithExcludedFields() {
 Class<?> clazz = String.class;
 Object string = "hello";
 int expectedHashCode = reflectionHashCode(17, 37, string, false, clazz, new String[]{"fieldNameToExclude"});
 int actualHashCode = HashCodeBuilder.reflectionHashCode(string, new String[]{"fieldNameToExclude"});
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeWithTestTransients() {
 Class<?> clazz = String.class;
 Object string = "hello";
 int expectedHashCode = reflectionHashCode(17, 37, string, true, clazz, new String[0]);
 int actualHashCode = HashCodeBuilder.reflectionHashCode(string, true);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeWithUpToClass() {
 Class<?> superClass = String[].class;
 Object stringArray = new String[]{"hello", "world"};
 int expectedHashCode = reflectionHashCode(17, 37, stringArray, false, superClass, new String[0]);
 int actualHashCode = HashCodeBuilder.reflectionHashCode(stringArray);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeWithExcludedFieldsAndUpToClass() {
 Class<?> superClass = String[].class;
 Object stringArray = new String[]{"hello", "world"};
 int expectedHashCode = reflectionHashCode(17, 37, stringArray, false, superClass, new String[]{"fieldNameToExclude"});
 int actualHashCode = HashCodeBuilder.reflectionHashCode(stringArray, "fieldNameToExclude");
 assertEquals(expectedHashCode, actualHashCode);
 }
}