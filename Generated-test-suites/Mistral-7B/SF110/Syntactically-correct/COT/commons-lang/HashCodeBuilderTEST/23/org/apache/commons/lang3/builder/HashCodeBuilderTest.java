package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 private int initialHashCode;
 private HashCodeBuilder hashCodeBuilder;

 @Test
 public void testHashCodeBuilderConstructor() {
     hashCodeBuilder = new HashCodeBuilder();
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder(13, 41);
     assertEquals(13, hashCodeBuilder.iConstant);
     assertEquals(41, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendBoolean() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(true);
     hashCodeBuilder.append(false);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(false);
     hashCodeBuilder.append(true);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendBooleanArray() {
     hashCodeBuilder = new HashCodeBuilder();
     boolean[] booleanArray = {true, false};
     hashCodeBuilder.append(booleanArray);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     boolean[] booleanArray2 = {false, true};
     hashCodeBuilder.append(booleanArray2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByte() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append((byte) 5);
     hashCodeBuilder.append((byte) 10);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append((byte) 10);
     hashCodeBuilder.append((byte) 5);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByteArray() {
     hashCodeBuilder = new HashCodeBuilder();
     byte[] byteArray = {5, 10};
     hashCodeBuilder.append(byteArray);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     byte[] byteArray2 = {10, 5};
     hashCodeBuilder.append(byteArray2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendChar() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append('A');
     hashCodeBuilder.append('B');
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append('B');
     hashCodeBuilder.append('A');
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendCharArray() {
     hashCodeBuilder = new HashCodeBuilder();
     char[] charArray = {'A', 'B'};
     hashCodeBuilder.append(charArray);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     char[] charArray2 = {'B', 'A'};
     hashCodeBuilder.append(charArray2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDouble() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(1.2);
     hashCodeBuilder.append(2.5);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(2.5);
     hashCodeBuilder.append(1.2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDoubleArray() {
     hashCodeBuilder = new HashCodeBuilder();
     double[] doubleArray = {1.2, 2.5};
     hashCodeBuilder.append(doubleArray);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     double[] doubleArray2 = {2.5, 1.2};
     hashCodeBuilder.append(doubleArray2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloat() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(1.2f);
     hashCodeBuilder.append(2.5f);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(2.5f);
     hashCodeBuilder.append(1.2f);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloatArray() {
     hashCodeBuilder = new HashCodeBuilder();
     float[] floatArray = {1.2f, 2.5f};
     hashCodeBuilder.append(floatArray);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     float[] floatArray2 = {2.5f, 1.2f};
     hashCodeBuilder.append(floatArray2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendInt() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(1);
     hashCodeBuilder.append(2);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(2);
     hashCodeBuilder.append(1);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendIntArray() {
     hashCodeBuilder = new HashCodeBuilder();
     int[] intArray = {1, 2};
     hashCodeBuilder.append(intArray);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     int[] intArray2 = {2, 1};
     hashCodeBuilder.append(intArray2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLong() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(1L);
     hashCodeBuilder.append(2L);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(2L);
     hashCodeBuilder.append(1L);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLongArray() {
     hashCodeBuilder = new HashCodeBuilder();
     long[] longArray = {1L, 2L};
     hashCodeBuilder.append(longArray);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     long[] longArray2 = {2L, 1L};
     hashCodeBuilder.append(longArray2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendObject() {
     hashCodeBuilder = new HashCodeBuilder();
     String object = "Test";
     hashCodeBuilder.append(object);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     String object2 = new String("Test");
     hashCodeBuilder.append(object2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendObjectArray() {
     hashCodeBuilder = new HashCodeBuilder();
     Object[] objectArray = {"Test", "Test"};
     hashCodeBuilder.append(objectArray);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     Object[] objectArray2 = {"Test", "Test"};
     hashCodeBuilder.append(objectArray2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendShort() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append((short) 5);
     hashCodeBuilder.append((short) 10);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append((short) 10);
     hashCodeBuilder.append((short) 5);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendShortArray() {
     hashCodeBuilder = new HashCodeBuilder();
     short[] shortArray = {5, 10};
     hashCodeBuilder.append(shortArray);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     short[] shortArray2 = {10, 5};
     hashCodeBuilder.append(shortArray2);
     assertEquals(initialHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendSuperInt() {
     hashCodeBuilder = new HashCodeBuilder();
     int superHashCode = 10;
     hashCodeBuilder.appendSuper(superHashCode);
     initialHashCode = hashCodeBuilder.toHashCode();
     hashCodeBuilder = new HashCodeBuilder();
     int superHashCode2 = 20;
     hashCodeBuilder.appendSuper(superHashCode2);
     assertEquals(initialHashCode + superHashCode, hashCodeBuilder.toHashCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderNegativeInitialOddNumber() {
     hashCodeBuilder = new HashCodeBuilder(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderNegativeMultiplierOddNumber() {
     hashCodeBuilder = new HashCodeBuilder(5, -5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendNullArray() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append((Object[]) null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendNullObject() {
     hashCodeBuilder = new HashCodeBuilder();
     hashCodeBuilder.append(null);
 }

}