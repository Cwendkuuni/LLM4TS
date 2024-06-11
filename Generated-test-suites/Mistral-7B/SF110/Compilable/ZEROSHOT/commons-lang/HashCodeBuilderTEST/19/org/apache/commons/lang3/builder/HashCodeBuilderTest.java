package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 @Test
 public void testHashCodeBuilder() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(17, 37);

 // Test default constructor
 assertEquals(0, hashCodeBuilder.toHashCode());

 // Test constructor with initial odd number and multiplier odd number
 assertEquals(17, hashCodeBuilderWithInitialValue.toHashCode());
 }

 @Test
 public void testAppendBoolean() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 boolean booleanValue = true;
 hashCodeBuilder.append(booleanValue);
 int expectedHashCode = 37; // 0 + 1 (true)
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendBooleanArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 boolean[] booleanArray = {true, false};
 hashCodeBuilder.append(booleanArray);
 int expectedHashCode = 37 * 2 + 0 + 1; // 0 (booleanArray is null) + 1 (first boolean)
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByte() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 byte byteValue = 10;
 hashCodeBuilder.append(byteValue);
 int expectedHashCode = 37 * 2 + byteValue; // 17 * 2 + 10
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByteArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 byte[] byteArray = {1, 2, 3};
 hashCodeBuilder.append(byteArray);
 int expectedHashCode = 37 * 2 * 3 + 1 + 2 + 3; // 17 * 2 * 3 + sum of bytes
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendChar() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 char charValue = 'A';
 hashCodeBuilder.append(charValue);
 int expectedHashCode = 37 * 2 + charValue; // 17 * 2 + 'A'
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendCharArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 char[] charArray = {'A', 'B', 'C'};
 hashCodeBuilder.append(charArray);
 int expectedHashCode = 37 * 2 * 3 + 'A' + 'B' + 'C'; // 17 * 2 * 3 + sum of chars
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDouble() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double doubleValue = 1.2;
 hashCodeBuilder.append(doubleValue);
 long doubleValueLongBits = Double.doubleToLongBits(doubleValue);
 int expectedHashCode = 37 * 2 + (int) (doubleValueLongBits ^ doubleValueLongBits >> 32);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDoubleArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double[] doubleArray = {1.2, 2.3, 3.4};
 hashCodeBuilder.append(doubleArray);
 long[] doubleArrayLongBits = new long[doubleArray.length];
 for (int i = 0; i < doubleArray.length; i++) {
 doubleArrayLongBits[i] = Double.doubleToLongBits(doubleArray[i]);
 }
 int expectedHashCode = 1;
 for (long longValue : doubleArrayLongBits) {
 expectedHashCode *= 37 * 2 + (int) (longValue ^ longValue >> 32);
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloat() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float floatValue = 1.2f;
 hashCodeBuilder.append(floatValue);
 int expectedHashCode = 37 * 2 + Float.floatToIntBits(floatValue); // 17 * 2 + int(floatValue)
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloatArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float[] floatArray = {1.2f, 2.3f, 3.4f};
 hashCodeBuilder.append(floatArray);
 int expectedHashCode = 1;
 for (int i = 0; i < floatArray.length; i++) {
 expectedHashCode *= 37 * 2 + Float.floatToIntBits(floatArray[i]);
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendInt() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int intValue = 10;
 hashCodeBuilder.append(intValue);
 int expectedHashCode = 37 * 2 + intValue; // 17 * 2 + 10
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendIntArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int[] intArray = {1, 2, 3};
 hashCodeBuilder.append(intArray);
 int expectedHashCode = 1;
 for (int intValue : intArray) {
 expectedHashCode *= 37 * 2 + intValue;
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLong() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 long longValue = 10L;
 hashCodeBuilder.append(longValue);
 int expectedHashCode = 37 * 2 + (int)(longValue ^ longValue >> 32);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLongArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 long[] longArray = {1L, 2L, 3L};
 hashCodeBuilder.append(longArray);
 int expectedHashCode = 1;
 for (long longValue : longArray) {
 expectedHashCode *= 37 * 2 + (int)(longValue ^ longValue >> 32);
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendObject() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 Object object = new Object();
 hashCodeBuilder.append(object);
 int expectedHashCode = 37 * 2 + object.hashCode();
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendObjectArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 Object[] objectArray = {new Object(), new Object()};
 hashCodeBuilder.append(objectArray);
 int expectedHashCode = 1;
 for (Object object : objectArray) {
 expectedHashCode *= 37 * 2 + object.hashCode();
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendShort() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 short shortValue = 10;
 hashCodeBuilder.append(shortValue);
 int expectedHashCode = 37 * 2 + shortValue; // 17 * 2 + 10
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendShortArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 short[] shortArray = {1, 2, 3};
 hashCodeBuilder.append(shortArray);
 int expectedHashCode = 1;
 for (short shortValue : shortArray) {
 expectedHashCode *= 37 * 2 + shortValue;
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendSuperHashCode() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int superHashCode = 10;
 hashCodeBuilder.appendSuper(superHashCode);
 int expectedHashCode = 37 * 2 + superHashCode;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }
}