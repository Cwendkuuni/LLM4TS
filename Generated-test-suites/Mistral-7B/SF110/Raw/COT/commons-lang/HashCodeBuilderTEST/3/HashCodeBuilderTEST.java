import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 private HashCodeBuilder hashCodeBuilder;
 private static final int initialNonZeroOddNumber = 17;
 private static final int multiplierNonZeroOddNumber = 37;

 @Test
 public void testHashCodeBuilderConstructor() {
 hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder expectedHashCodeBuilder = new HashCodeBuilder();
 assertEquals(expectedHashCodeBuilder.toHashCode(), hashCodeBuilder.toHashCode());
 }

 @Test
 public void testHashCodeBuilderWithInitialAndMultiplier() {
 hashCodeBuilder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
 HashCodeBuilder expectedHashCodeBuilder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
 assertEquals(expectedHashCodeBuilder.toHashCode(), hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendBoolean() {
 hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(true);
 hashCodeBuilder.append(false);
 int expectedHashCode = 32 * 37 + 1;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendBooleanArray() {
 hashCodeBuilder = new HashCodeBuilder();
 boolean[] booleanArray = {true, false, true};
 hashCodeBuilder.append(booleanArray);
 int expectedHashCode = 17 * 37 * 2 + 1 + 0;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendByte() {
 hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((byte) 127);
 int expectedHashCode = 127 * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendByteArray() {
 hashCodeBuilder = new HashCodeBuilder();
 byte[] byteArray = {1, 2, 3};
 hashCodeBuilder.append(byteArray);
 int expectedHashCode = 1 * 37 * 3 + 2 * 37 + 3 * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendChar() {
 hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append('A');
 int expectedHashCode = 'A' * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendCharArray() {
 hashCodeBuilder = new HashCodeBuilder();
 char[] charArray = {'A', 'B', 'C'};
 hashCodeBuilder.append(charArray);
 int expectedHashCode = 'A' * 37 * 3 + 'B' * 37 * 2 + 'C' * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendDouble() {
 hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.1);
 long expectedHashCode = Double.doubleToLongBits(1.1);
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendDoubleArray() {
 hashCodeBuilder = new HashCodeBuilder();
 double[] doubleArray = {1.1, 2.2, 3.3};
 hashCodeBuilder.append(doubleArray);
 long expectedHashCode = 1 * 37 * 64 + 2 * 37 * 64 + 3 * 37 * 64;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendFloat() {
 hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.1f);
 int expectedHashCode = Float.floatToIntBits(1.1f);
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendFloatArray() {
 hashCodeBuilder = new HashCodeBuilder();
 float[] floatArray = {1.1f, 2.2f, 3.3f};
 hashCodeBuilder.append(floatArray);
 int expectedHashCode = 1 * 37 * 32 + 2 * 37 * 32 + 3 * 37 * 32;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendInt() {
 hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1);
 int expectedHashCode = 1 * 37 + 0;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendIntArray() {
 hashCodeBuilder = new HashCodeBuilder();
 int[] intArray = {1, 2, 3};
 hashCodeBuilder.append(intArray);
 int expectedHashCode = 1 * 37 * 3 + 2 * 37 * 2 + 3 * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendLong() {
 hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1L);
 long expectedHashCode = 1L * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendLongArray() {
 hashCodeBuilder = new HashCodeBuilder();
 long[] longArray = {1, 2, 3};
 hashCodeBuilder.append(longArray);
 long expectedHashCode = 1 * 37 * 8 + 2 * 37 * 8 + 3 * 37 * 8;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendObject() {
 hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append("Test");
 int expectedHashCode = "Test".hashCode();
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendObjectArray() {
 hashCodeBuilder = new HashCodeBuilder();
 String[] stringArray = {"Test1", "Test2", "Test3"};
 hashCodeBuilder.append(stringArray);
 int expectedHashCode = "Test1".hashCode() * 37 * 3 + "Test2".hashCode() * 37 * 2 + "Test3".hashCode() * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendShort() {
 hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((short) 127);
 int expectedHashCode = 127 * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendShortArray() {
 hashCodeBuilder = new HashCodeBuilder();
 short[] shortArray = {1, 2, 3};
 hashCodeBuilder.append(shortArray);
 int expectedHashCode = 1 * 37 * 2 + 2 * 37 + 3 * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendSuperHashCode() {
 hashCodeBuilder = new HashCodeBuilder();
 int superHashCode = 42;
 hashCodeBuilder.appendSuper(superHashCode);
 int expectedHashCode = superHashCode * 37;
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderWithEvenInitialValue() {
 hashCodeBuilder = new HashCodeBuilder(2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderWithEvenMultiplier() {
 hashCodeBuilder = new HashCodeBuilder(3, 2);
 }

 @Test
 public void testHashCodeBuilderReflection() {
 hashCodeBuilder = new HashCodeBuilder();
 Object object = new Object();
 hashCodeBuilder.append(object);
 int expectedHashCode = object.hashCode();
 int actualHashCode = hashCodeBuilder.toHashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test(expected = InternalError.class)
 public void testHashCodeBuilderReflectionWithIllegalAccessException() {
 hashCodeBuilder = new HashCodeBuilder();
 Object object = new Object();
 try {
 Class<?> clazz = object.getClass();
 Field field = clazz.getDeclaredField("privateField");
 AccessibleObject.setAccessible(field, true);
 field.set(object, "Test");
 hashCodeBuilder.append(object);
 }
 catch (NoSuchFieldException e) {
 throw new TestFailureException("Field 'privateField' not found");
 }
 }
}