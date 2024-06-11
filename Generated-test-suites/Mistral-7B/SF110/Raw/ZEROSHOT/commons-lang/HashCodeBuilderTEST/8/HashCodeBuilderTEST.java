package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

 @Test
 public void testHashCodeBuilder() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(11, 13);
 // Test default values
 assertEquals(17, hashCodeBuilder.toHashCode());
 // Test custom values
 assertEquals(11 * 13 + 0, hashCodeBuilderWithInitialAndMultiplier.toHashCode());
 }

 @Test
 public void testAppendBoolean() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(true);
 hashCodeBuilder.append(false);
 int expectedHashCode = 37 * 17 + 1 + 0;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendBooleanArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 boolean[] booleanArray = {true, false};
 hashCodeBuilder.append(booleanArray);
 int expectedHashCode = 37 * 17;
 for (boolean b : booleanArray) {
 expectedHashCode += b ? 0 : 1;
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByte() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((byte) 5);
 int expectedHashCode = 37 * 17 + 5;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByteArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 byte[] byteArray = {1, 2, 3};
 hashCodeBuilder.append(byteArray);
 int expectedHashCode = 37 * 17;
 for (byte b : byteArray) {
 expectedHashCode += b;
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendChar() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append('A');
 int expectedHashCode = 37 * 17 + 'A' + 0x10000;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendCharArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 char[] charArray = {'A', 'B', 'C'};
 hashCodeBuilder.append(charArray);
 int expectedHashCode = 37 * 17;
 for (char c : charArray) {
 expectedHashCode += c;
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDouble() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.5);
 long doubleBits = Double.doubleToLongBits(1.5);
 int expectedHashCode = 37 * 17 + (int)(doubleBits ^ doubleBits >> 32);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDoubleArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double[] doubleArray = {1.5, 2.5, 3.5};
 hashCodeBuilder.append(doubleArray);
 long expectedHashCode = 37 * 17;
 for (double d : doubleArray) {
 expectedHashCode += Double.doubleToLongBits(d) ^ (Double.doubleToLongBits(d) >>> 32);
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloat() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.5f);
 int expectedHashCode = 37 * 17 + Float.floatToIntBits(1.5f);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloatArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float[] floatArray = {1.5f, 2.5f, 3.5f};
 hashCodeBuilder.append(floatArray);
 int expectedHashCode = 37 * 17;
 for (float f : floatArray) {
 expectedHashCode += Float.floatToIntBits(f);
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendInt() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(10);
 int expectedHashCode = 37 * 17 + 10;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendIntArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int[] intArray = {1, 2, 3};
 hashCodeBuilder.append(intArray);
 int expectedHashCode = 37 * 17;
 for (int i : intArray) {
 expectedHashCode += i;
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLong() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(10L);
 int expectedHashCode = 37 * 17 + (int)(10L ^ 10L >> 32);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLongArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 long[] longArray = {1, 2, 3};
 hashCodeBuilder.append(longArray);
 long expectedHashCode = 37 * 17;
 for (long l : longArray) {
 expectedHashCode += (int)(l ^ l >> 32);
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendObject() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 String object = "test";
 hashCodeBuilder.append(object);
 int expectedHashCode = 37 * 17 + object.hashCode();
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendObjectArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 Object[] objectArray = {"test1", "test2", "test3"};
 hashCodeBuilder.append(objectArray);
 int expectedHashCode = 37 * 17;
 for (Object o : objectArray) {
 expectedHashCode += o.hashCode();
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendShort() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((short) 5);
 int expectedHashCode = 37 * 17 + 5;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendShortArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 short[] shortArray = {1, 2, 3};
 hashCodeBuilder.append(shortArray);
 int expectedHashCode = 37 * 17;
 for (short s : shortArray) {
 expectedHashCode += s;
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendSuperHashCode() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int superHashCode = 5;
 hashCodeBuilder.appendSuper(superHashCode);
 int expectedHashCode = 37 * 17 + superHashCode;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testReflectionHashCode() {
 int reflectionHashCode = HashCodeBuilder.reflectionHashCode("testObject");
 int expectedHashCode = 37 * 17 + "testObject".hashCode();
 assertEquals(expectedHashCode, reflectionHashCode);
 }

 @Test
 public void testReflectionHashCodeBoolean() {
 Object testObject = new Object() {
 boolean booleanField = true;
 };
 int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject, false);
 int expectedHashCode = 37 * 17 + testObject.hashCode() + testObject.booleanField ? 0 : 1;
 assertEquals(expectedHashCode, reflectionHashCode);
 }

 @Test
 public void testReflectionHashCodeBooleanArray() {
 Object testObject = new Object() {
 boolean[] booleanArray = {true, false};
 };
 int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject, false, Boolean.class, new String[0]);
 int expectedHashCode = 37 * 17 + testObject.hashCode();
 for (boolean b : booleanArray) {
 expectedHashCode += b ? 0 : 1;
 }
 assertEquals(expectedHashCode, reflectionHashCode);
 }

 @Test
 public void testReflectionHashCodeCollectionString() {
 Object testObject = new Object() {
 List<String> list = Arrays.asList("test1", "test2", "test3");
 };
 int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject, ReflectionToStringBuilder.toString(testObject), new String[0]);
 int expectedHashCode = 37 * 17;
 for (String s : (List<String>) testObject.list) {
 expectedHashCode += s.hashCode();
 }
 assertEquals(expectedHashCode, reflectionHashCode);
 }

 @Test
 public void testReflectionHashCodeObject() {
 Object testObject = new Object();
 int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject);
 int expectedHashCode = 37 * 17 + testObject.hashCode();
 assertEquals(expectedHashCode, reflectionHashCode);
 }

 @Test
 public void testReflectionHashCodeObjectArray() {
 Object testObject = new Object() {
 Object[] objectArray = {new Object(), new Object()};
 };
 int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject, false, null, new String[0]);
 int expectedHashCode = 37 * 17;
 for (Object o : (Object[]) testObject.objectArray) {
 expectedHashCode += o.hashCode();
 }
 assertEquals(expectedHashCode, reflectionHashCode);
 }
}