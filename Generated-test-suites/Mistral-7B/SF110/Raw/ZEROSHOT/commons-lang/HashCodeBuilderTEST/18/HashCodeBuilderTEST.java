import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 @Test
 public void testHashCodeBuilder() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(19, 43);

 assertEquals(17, hashCodeBuilder.iTotal);
 assertEquals(37, hashCodeBuilder.iConstant);
 assertEquals(1, hashCodeBuilder.iTotal);

 assertEquals(19, hashCodeBuilderWithInitialValue.iTotal);
 assertEquals(43, hashCodeBuilderWithInitialValue.iConstant);
 assertEquals(19, hashCodeBuilderWithInitialValue.iTotal);
 }

 @Test
 public void testAppendBoolean() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(true);
 hashCodeBuilder.append(false);

 assertEquals(33, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendBooleanArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 boolean[] booleanArray = {true, false, true};
 hashCodeBuilder.append(booleanArray);

 assertEquals(51, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendByte() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((byte) 1);
 hashCodeBuilder.append((byte) 2);

 assertEquals(69, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendByteArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 byte[] byteArray = {1, 2, 3};
 hashCodeBuilder.append(byteArray);

 assertEquals(211, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendChar() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((char) 'a');
 hashCodeBuilder.append((char) 'b');

 assertEquals(213, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendCharArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 char[] charArray = {'a', 'b', 'c'};
 hashCodeBuilder.append(charArray);

 assertEquals(425, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendDouble() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.2);
 hashCodeBuilder.append(2.3);

 long doubleHashCode = Double.doubleToLongBits(1.2) ^ (Double.doubleToLongBits(2.3) << 32);
 assertEquals(doubleHashCode, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendDoubleArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double[] doubleArray = {1.2, 2.3, 3.4};
 hashCodeBuilder.append(doubleArray);

 long doubleHashCode = 0;
 for (double value : doubleArray) {
 long valueHashCode = Double.doubleToLongBits(value);
 doubleHashCode ^= (valueHashCode << 32) | valueHashCode;
 }
 assertEquals(doubleHashCode, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendFloat() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((float) 1.2);
 hashCodeBuilder.append((float) 2.3);

 long floatHashCode = Float.floatToIntBits(1.2f) ^ (Float.floatToIntBits(2.3f) << 32);
 assertEquals(floatHashCode, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendFloatArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float[] floatArray = {1.2f, 2.3f, 3.4f};
 hashCodeBuilder.append(floatArray);

 long floatHashCode = 0;
 for (float value : floatArray) {
 long valueHashCode = Float.floatToIntBits(value);
 floatHashCode ^= (valueHashCode << 32) | valueHashCode;
 }
 assertEquals(floatHashCode, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendInt() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1);
 hashCodeBuilder.append(2);

 assertEquals(43, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendIntArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int[] intArray = {1, 2, 3};
 hashCodeBuilder.append(intArray);

 assertEquals(73, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendLong() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1L);
 hashCodeBuilder.append(2L);

 assertEquals(51, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendLongArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 long[] longArray = {1L, 2L, 3L};
 hashCodeBuilder.append(longArray);

 assertEquals(151, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendObject() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append("Test");
 Object testObject = new Object() {};
 hashCodeBuilder.append(testObject);

 assertEquals(53, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendObjectArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 Object[] objectArray = {new Object() {}, new Object() {}};
 hashCodeBuilder.append(objectArray);

 assertEquals(107, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendShort() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((short) 1);
 hashCodeBuilder.append((short) 2);

 assertEquals(35, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendShortArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 short[] shortArray = {1, 2, 3};
 hashCodeBuilder.append(shortArray);

 assertEquals(113, hashCodeBuilder.iTotal);
 }

 @Test
 public void testAppendSuperHashCode() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.appendSuper(12345);

 assertEquals(12346, hashCodeBuilder.iTotal);
 }

 @Test
 public void testReflectionHashCodeBoolean() {
 int hashCode = HashCodeBuilder.reflectionHashCode(true);
 assertEquals(33, hashCode);
 }

 @Test
 public void testReflectionHashCodeBooleanArray() {
 boolean[] booleanArray = {true, false, true};
 int hashCode = HashCodeBuilder.reflectionHashCode(booleanArray, false);
 assertEquals(51, hashCode);
 }

 @Test
 public void testReflectionHashCodeDouble() {
 double hashCode = HashCodeBuilder.reflectionHashCode(1.2);
 long doubleHashCode = Double.doubleToLongBits(1.2);
 assertEquals(doubleHashCode, hashCode);
 }

 @Test
 public void testReflectionHashCodeDoubleArray() {
 double[] doubleArray = {1.2, 2.3, 3.4};
 int hashCode = HashCodeBuilder.reflectionHashCode(doubleArray, false, Double.class, new String[0]);
 long doubleHashCode = 0;
 for (double value : doubleArray) {
 long valueHashCode = Double.doubleToLongBits(value);
 doubleHashCode ^= (valueHashCode << 32) | valueHashCode;
 }
 assertEquals(doubleHashCode, hashCode);
 }

 @Test
 public void testReflectionHashCodeFloat() {
 float hashCode = HashCodeBuilder.reflectionHashCode(1.2f);
 long floatHashCode = Float.floatToIntBits(1.2f);
 assertEquals(floatHashCode, hashCode);
 }

 @Test
 public void testReflectionHashCodeFloatArray() {
 float[] floatArray = {1.2f, 2.3f, 3.4f};
 int hashCode = HashCodeBuilder.reflectionHashCode(floatArray, false, Float.class, new String[0]);
 long floatHashCode = 0;
 for (float value : floatArray) {
 long valueHashCode = Float.floatToIntBits(value);
 floatHashCode ^= (valueHashCode << 32) | valueHashCode;
 }
 assertEquals(floatHashCode, hashCode);
 }

 @Test
 public void testReflectionHashCodeInteger() {
 int hashCode = HashCodeBuilder.reflectionHashCode(1);
 assertEquals(1, hashCode);
 }

 @Test
 public void testReflectionHashCodeIntegerArray() {
 int[] intArray = {1, 2, 3};
 int hashCode = HashCodeBuilder.reflectionHashCode(intArray, false);
 assertEquals(73, hashCode);
 }

 @Test
 public void testReflectionHashCodeLong() {
 long hashCode = HashCodeBuilder.reflectionHashCode(1L);
 assertEquals(1, hashCode);
 }

 @Test
 public void testReflectionHashCodeLongArray() {
 long[] longArray = {1L, 2L, 3L};
 int hashCode = HashCodeBuilder.reflectionHashCode(longArray, false);
 assertEquals(151, hashCode);
 }

 @Test
 public void testReflectionHashCodeObject() {
 Object testObject = new Object() {};
 int hashCode = HashCodeBuilder.reflectionHashCode(testObject, false);
 assertEquals(53, hashCode);
 }

 @Test
 public void testReflectionHashCodeObjectArray() {
 Object[] objectArray = {new Object() {}, new Object() {}};
 int hashCode = HashCodeBuilder.reflectionHashCode(objectArray, false);
 assertEquals(107, hashCode);
 }

 @Test
 public void testReflectionHashCodeShort() {
 short hashCode = HashCodeBuilder.reflectionHashCode((short) 1);
 assertEquals(1, hashCode);
 }

 @Test
 public void testReflectionHashCodeShortArray() {
 short[] shortArray = {1, 2, 3};
 int hashCode = HashCodeBuilder.reflectionHashCode(shortArray, false);
 assertEquals(113, hashCode);
 }

 @Test
 public void testReflectionHashCodeWithTestTransients() {
 Class<?> booleanClass = boolean.class;
 int hashCode = HashCodeBuilder.reflectionHashCode(true, true, booleanClass, new String[0]);
 assertEquals(53, hashCode);
 }

 @Test
 public void testReflectionHashCodeWithTestTransientsAndExcludeFields() {
 Class<?> booleanClass = boolean.class;
 String[] excludeFields = {"testField"};
 int hashCode = HashCodeBuilder.reflectionHashCode(true, true, booleanClass, excludeFields);
 assertEquals(33, hashCode);
 }
}