import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 @Test
 public void testAppendBoolean() {
 HashCodeBuilder builder = new HashCodeBuilder();
 builder.append(true);
 int expectedHashCode = 37;
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendBooleanArray() {
 HashCodeBuilder builder = new HashCodeBuilder();
 boolean[] booleanArray = {true, false};
 builder.append(booleanArray);
 int expectedHashCode = 9161; // hashCode of booleanArray
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendByte() {
 HashCodeBuilder builder = new HashCodeBuilder();
 builder.append((byte) 42);
 int expectedHashCode = 37 * 37 + 42;
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendByteArray() {
 HashCodeBuilder builder = new HashCodeBuilder();
 byte[] byteArray = {42, 43};
 builder.append(byteArray);
 int expectedHashCode = 129991; // hashCode of byteArray
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendChar() {
 HashCodeBuilder builder = new HashCodeBuilder();
 builder.append('A');
 int expectedHashCode = 37 * 37 + 65;
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendCharArray() {
 HashCodeBuilder builder = new HashCodeBuilder();
 char[] charArray = {'A', 'B'};
 builder.append(charArray);
 int expectedHashCode = 142403; // hashCode of charArray
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendDouble() {
 HashCodeBuilder builder = new HashCodeBuilder();
 double value = 3.14;
 builder.append(value);
 long expectedHashCode = Double.doubleToLongBits(value);
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendDoubleArray() {
 HashCodeBuilder builder = new HashCodeBuilder();
 double[] doubleArray = {3.14, 2.718};
 builder.append(doubleArray);
 long expectedHashCode = 0; // Double.doubleToLongBits(doubleArray[0]) ^ Double.doubleToLongBits(doubleArray[1]);
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendFloat() {
 HashCodeBuilder builder = new HashCodeBuilder();
 float value = 1.5f;
 builder.append(value);
 int expectedHashCode = (int) (Float.floatToIntBits(value) * 37);
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendFloatArray() {
 HashCodeBuilder builder = new HashCodeBuilder();
 float[] floatArray = {1.5f, 2.5f};
 builder.append(floatArray);
 int expectedHashCode = 116439; // Float.floatToIntBits(floatArray[0]) * 37 ^ Float.floatToIntBits(floatArray[1]) * 37
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendInt() {
 HashCodeBuilder builder = new HashCodeBuilder();
 builder.append(42);
 int expectedHashCode = 37 * 37 + 42;
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendIntArray() {
 HashCodeBuilder builder = new HashCodeBuilder();
 int[] intArray = {42, 43};
 builder.append(intArray);
 int expectedHashCode = 129991; // hashCode of intArray
 int actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendLong() {
 HashCodeBuilder builder = new HashCodeBuilder();
 builder.append(42L);
 long expectedHashCode = 37 * 37 + (int)(42L ^ (42L >> 32));
 long actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendLongArray() {
 HashCodeBuilder builder = new HashCodeBuilder();
 long[] longArray = {42L, 43L};
 builder.append(longArray);
 long expectedHashCode = 129991; // (int) (longArray[0] ^ longArray[1] << 32)
 long actualHashCode = builder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendNullObject() {
 HashCodeBuilder builder = new HashCodeBuilder();
 builder.append(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendObjectArrayWithNullElement() {
 HashCodeBuilder builder = new HashCodeBuilder();
 Object[] objectArray = {null, new Object()};
 builder.append(objectArray);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendObjectArrayWithNonArrayObject() {
 HashCodeBuilder builder = new HashCodeBuilder();
 Object nonArrayObject = new Object();
 Object[] objectArray = {nonArrayObject};
 builder.append(objectArray);
 }

 @Test
 public void testReflectionHashCodeWithNoArg() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(null);
 int expectedHashCode = 1;
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeWithNullObject() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(null, ReflectionToStringBuilder.toString(new Object[]{null}));
 int expectedHashCode = 1;
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeWithBooleanObject() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(new boolean[0], ReflectionToStringBuilder.toString(new Object[]{true}));
 int expectedHashCode = 9161; // hashCode of boolean[0] + hashCode(true)
 int expectedHashCodeOfBoolean = 1;
 int actualHashCodeOfBoolean = HashCodeBuilder.hashCode(true);
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCodeOfBoolean, actualHashCodeOfBoolean);
 }

 @Test
 public void testReflectionHashCodeWithByteObject() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(new byte[0], ReflectionToStringBuilder.toString(new Object[]{Byte.valueOf((byte) 42)}));
 int expectedHashCode = 129991; // hashCode of byte[0] + hashCode(Byte.valueOf((byte) 42))
 int expectedHashCodeOfByte = 42;
 int actualHashCodeOfByte = HashCodeBuilder.hashCode(Byte.valueOf((byte) 42));
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCodeOfByte, actualHashCodeOfByte);
 }

 @Test
 public void testReflectionHashCodeWithCharObject() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(new char[0], ReflectionToStringBuilder.toString(new Object[]{Character.valueOf('A')}));
 int expectedHashCode = 142403; // hashCode of char[0] + hashCode(Character.valueOf('A'))
 int expectedHashCodeOfChar = 65;
 int actualHashCodeOfChar = HashCodeBuilder.hashCode(Character.valueOf('A'));
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCodeOfChar, actualHashCodeOfChar);
 }

 @Test
 public void testReflectionHashCodeWithShortObject() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(new short[0], ReflectionToStringBuilder.toString(new Object[]{Short.valueOf((short) 42)}));
 int expectedHashCode = 142403; // hashCode of short[0] + hashCode(Short.valueOf((short) 42))
 int expectedHashCodeOfShort = 42;
 int actualHashCodeOfShort = HashCodeBuilder.hashCode(Short.valueOf((short) 42));
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCodeOfShort, actualHashCodeOfShort);
 }

 @Test
 public void testReflectionHashCodeWithIntegerObject() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(new int[0], ReflectionToStringBuilder.toString(new Object[]{Integer.valueOf(42)}));
 int expectedHashCode = 142403; // hashCode of int[0] + hashCode(Integer.valueOf(42))
 int expectedHashCodeOfInt = 42;
 int actualHashCodeOfInt = HashCodeBuilder.hashCode(Integer.valueOf(42));
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCodeOfInt, actualHashCodeOfInt);
 }

 @Test
 public void testReflectionHashCodeWithLongObject() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(new long[0], ReflectionToStringBuilder.toString(new Object[]{Long.valueOf(42L)}));
 long expectedHashCode = 142403; // hashCode of long[0] + hashCode(Long.valueOf(42L))
 long expectedHashCodeOfLong = 42L;
 long actualHashCodeOfLong = HashCodeBuilder.hashCode(Long.valueOf(42L));
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCodeOfLong, actualHashCodeOfLong);
 }

 @Test
 public void testReflectionHashCodeWithFloatObject() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(new float[0], ReflectionToStringBuilder.toString(new Object[]{Float.valueOf(1.5f)}));
 int expectedHashCode = 116439; // hashCode of float[0] + hashCode(Float.valueOf(1.5f))
 int expectedHashCodeOfFloat = Float.floatToIntBits(1.5f);
 int actualHashCodeOfFloat = HashCodeBuilder.hashCode(Float.valueOf(1.5f));
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCodeOfFloat, actualHashCodeOfFloat);
 }

 @Test
 public void testReflectionHashCodeWithDoubleObject() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(new double[0], ReflectionToStringBuilder.toString(new Object[]{Double.valueOf(3.14)}));
 long expectedHashCode = Double.doubleToLongBits(3.14);
 int expectedHashCodeOfDouble = (int) (Double.doubleToLongBits(3.14) ^ (Double.doubleToLongBits(3.14) >> 32));
 int actualHashCodeOfDouble = HashCodeBuilder.hashCode(Double.valueOf(3.14));
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCodeOfDouble, actualHashCodeOfDouble);
 }

 @Test
 public void testReflectionHashCodeWithNonPrimitiveArray() {
 int actualHashCode = HashCodeBuilder.reflectionHashCode(new String[0], ReflectionToStringBuilder.toString(new Object[]{"String1", "String2"}));
 int expectedHashCode = (int) (ObjectUtils.hashCode(new String[]{"String1", "String2"}) * 37);
 int actualHashCodeOfStringArray = ObjectUtils.hashCode(new String[]{"String1", "String2"});
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCodeOfStringArray, actualHashCodeOfStringArray);
 }
}