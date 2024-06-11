import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 @Test
 public void testHashCodeBuilder() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(11, 13);

 assertEquals(17, hashCodeBuilder.toHashCode());
 assertEquals(11, hashCodeBuilderWithInitialAndMultiplier.toHashCode());
 }

 @Test
 public void testAppendBasicTypes() {
 final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

 hashCodeBuilder.append(true);
 hashCodeBuilder.append((byte) 1);
 hashCodeBuilder.append((char) 'a');
 hashCodeBuilder.append(1.0f);
 hashCodeBuilder.append(123);
 hashCodeBuilder.append(456L);

 int expectedHashCode = 37 * 17 + 1 + 37 * 17 + 'a' + 31 + 123 + (int)(456L ^ 456L >> 32);
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendArrays() {
 final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

 final boolean[] booleanArray = new boolean[] { true, false };
 final byte[] byteArray = new byte[] { 1, 2 };
 final char[] charArray = new char[] { 'a', 'b' };
 final double[] doubleArray = new double[] { 1.0, 2.0 };
 final float[] floatArray = new float[] { 1.0f, 2.0f };
 final int[] intArray = new int[] { 1, 2 };
 final long[] longArray = new long[] { 1, 2 };
 final Object[] objectArray = new Object[] { new Integer(1), new String("test") };

 hashCodeBuilder.append(booleanArray);
 hashCodeBuilder.append(byteArray);
 hashCodeBuilder.append(charArray);
 hashCodeBuilder.append(doubleArray);
 hashCodeBuilder.append(floatArray);
 hashCodeBuilder.append(intArray);
 hashCodeBuilder.append(longArray);
 hashCodeBuilder.append(objectArray);

 int expectedHashCode = 37 * 17 * 2 * (37 * 2 + 1) * (37 * 2 + 'a') * 2 * (37 * 2 + 'b') * 2 * (37 * 2 + 1.0) * 2 * (37 * 2 + 1.0) * 2 * (37 * 2 + 1) * 2 * (37 * 2 + 2) * 2 * (37 * 2 + 1) * 37 * 1 + 1 + 37 * 1 + 37 * 1 + 37 * 1 + 37 * 1 + 37 * 1 + 37 * 1;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendObjects() {
 final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

 final Integer integer = new Integer(1);
 final String string = new String("test");
 final StringBuilder stringBuilder = new StringBuilder("test");

 hashCodeBuilder.append(integer);
 hashCodeBuilder.append(string);
 hashCodeBuilder.append(stringBuilder);

 int expectedHashCode = 37 * 17 + integer.hashCode() + string.hashCode() + stringBuilder.hashCode();
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendArraysOfObjects() {
 final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

 final Object[] objectArray = new Object[] { new Integer(1), new String("test") };

 hashCodeBuilder.append(objectArray);

 int expectedHashCode = 37 * 17 * 2 * (37 * 1 + 1) * (37 * 1 + "test".hashCode());
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }
}