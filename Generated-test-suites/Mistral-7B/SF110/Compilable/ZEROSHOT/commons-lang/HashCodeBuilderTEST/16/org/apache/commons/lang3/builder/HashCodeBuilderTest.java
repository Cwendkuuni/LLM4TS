package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 @Test
 public void testHashCodeBuilder() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(13, 29);

 // Test default constructor
 assertEquals(0, hashCodeBuilder.hashCode());

 // Test constructor with initial value and multiplier
 assertEquals(13, hashCodeBuilderWithInitialValue.hashCode());
 }

 @Test
 public void testAppendBoolean() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(true);
 hashCodeBuilder.append(false);

 int expectedHashCode = 32 * 37 + 1 + 0;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendBooleanArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 boolean[] booleanArray = {true, false, true};
 hashCodeBuilder.append(booleanArray);

 int expectedHashCode = 32 * 37 * 3 + 1 + 0 + 32 * 37 * 3 + 0 + 32 * 37 * 3 + 1;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendByte() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((byte) 5);
 hashCodeBuilder.append((byte) 10);

 int expectedHashCode = 32 * 37 * 2 + 5 + 10;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendByteArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 byte[] byteArray = {5, 10, 15};
 hashCodeBuilder.append(byteArray);

 int expectedHashCode = 32 * 37 * 3 + 5 + 10 + 15;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendChar() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append('a');
 hashCodeBuilder.append('z');

 int expectedHashCode = 32 * 37 * 2 + 97 + 122;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendCharArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 char[] charArray = {'a', 'z', 'e'};
 hashCodeBuilder.append(charArray);

 int expectedHashCode = 32 * 37 * 3 + 97 + 122 + 101;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendDouble() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.23);
 hashCodeBuilder.append(2.718);

 long expectedHashCode = 32 * 37 * 2 + Double.doubleToLongBits(1.23) + Double.doubleToLongBits(2.718);
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendDoubleArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double[] doubleArray = {1.23, 2.718, 3.14};
 hashCodeBuilder.append(doubleArray);

 long expectedHashCode = 32 * 37 * 3 + Double.doubleToLongBits(1.23) + Double.doubleToLongBits(2.718) + Double.doubleToLongBits(3.14);
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendFloat() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(3.14f);
 hashCodeBuilder.append(1.23f);

 int expectedHashCode = 32 * 37 * 2 + Float.floatToIntBits(3.14f) + Float.floatToIntBits(1.23f);
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendFloatArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float[] floatArray = {3.14f, 1.23f, 2.718f};
 hashCodeBuilder.append(floatArray);

 int expectedHashCode = 32 * 37 * 3 + Float.floatToIntBits(3.14f) + Float.floatToIntBits(1.23f) + Float.floatToIntBits(2.718f);
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendInt() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(42);
 hashCodeBuilder.append(13);

 int expectedHashCode = 32 * 37 * 2 + 42 + 13;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendIntArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int[] intArray = {42, 13, 7};
 hashCodeBuilder.append(intArray);

 int expectedHashCode = 32 * 37 * 3 + 42 + 13 + 7;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendLong() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(123L);
 hashCodeBuilder.append(456L);

 long expectedHashCode = 32 * 37 * 2 + (int)(123L ^ 123L >> 32) + (int)(456L ^ 456L >> 32);
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendLongArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 long[] longArray = {123L, 456L, 789L};
 hashCodeBuilder.append(longArray);

 long expectedHashCode = 32 * 37 * 3 + (int)(123L ^ 123L >> 32) + (int)(456L ^ 456L >> 32) + (int)(789L ^ 789L >> 32);
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendObject() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 String string = "Hello World";
 hashCodeBuilder.append(string);
 hashCodeBuilder.append("Test");

 int expectedHashCode = 32 * 37 * 2 + string.hashCode() + "Test".hashCode();
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendObjectArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 Object[] objectArray = {"Hello World", "Test", new Integer(42)};
 hashCodeBuilder.append(objectArray);

 int expectedHashCode = 32 * 37 * 3 + "Hello World".hashCode() + "Test".hashCode() + 42;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendShort() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((short) 5);
 hashCodeBuilder.append((short) 10);

 int expectedHashCode = 32 * 37 * 2 + 5 + 10;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendShortArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 short[] shortArray = {5, 10, 15};
 hashCodeBuilder.append(shortArray);

 int expectedHashCode = 32 * 37 * 3 + 5 + 10 + 15;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testAppendSuperHashCode() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int superHashCode = 123;
 hashCodeBuilder.appendSuper(superHashCode);

 int expectedHashCode = 32 * 37 * 2 + 123;
 assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
 }

 @Test
 public void testReflectionHashCode() {
 int expectedHashCode = 37 * 17 + 37 * 17 + HashCodeBuilder.class.hashCode() + 37 * 17 + "HashCodeBuilderTest".hashCode() + 37 * 17;

 assertEquals(expectedHashCode, HashCodeBuilder.reflectionHashCode(this));
 }
}