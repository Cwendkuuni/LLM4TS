import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.IDKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 @Test
 public void testHashCodeBuilder() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder hashCodeBuilderWithInitialOddNumber = new HashCodeBuilder(13, 17);

 assertEquals(17, hashCodeBuilder.iTotal);
 assertEquals(37, hashCodeBuilder.iConstant);

 assertEquals(13, hashCodeBuilderWithInitialOddNumber.iTotal);
 assertEquals(17, hashCodeBuilderWithInitialOddNumber.iConstant);
 }

 @Test
 public void testAppendBoolean() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(true);
 hashCodeBuilder.append(false);

 int expectedHashCode = 37 * 1 + 1;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendBooleanArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 boolean[] booleanArray = {true, false, true};
 hashCodeBuilder.append(booleanArray);

 int expectedHashCode = 37 * 1 + 1; // first boolean
 expectedHashCode = expectedHashCode * 37 + 0; // false
 expectedHashCode = expectedHashCode * 37 + 1; // true

 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendByte() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((byte) 10);

 int expectedHashCode = 37 * 1 + 10;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendByteArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 byte[] byteArray = {5, 15, 25};
 hashCodeBuilder.append(byteArray);

 int expectedHashCode = 37 * 1 + 5;
 expectedHashCode = expectedHashCode * 37 + 15;
 expectedHashCode = expectedHashCode * 37 + 25;

 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendChar() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append('A');

 int expectedHashCode = 37 * 1 + 65;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendCharArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 char[] charArray = {'A', 'B', 'C'};
 hashCodeBuilder.append(charArray);

 int expectedHashCode = 37 * 1 + 65;
 expectedHashCode = expectedHashCode * 37 + 66;
 expectedHashCode = expectedHashCode * 37 + 67;

 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendDouble() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double value = 1.23456;
 hashCodeBuilder.append(value);

 long doubleAsLongBits = Double.doubleToLongBits(value);
 int expectedHashCode = 37 * 1 + (int) (doubleAsLongBits ^ (doubleAsLongBits >> 32));
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendDoubleArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double[] doubleArray = {1.23456, 2.34567, 3.45678};
 hashCodeBuilder.append(doubleArray);

 for (double value : doubleArray) {
 long valueAsLongBits = Double.doubleToLongBits(value);
 int hashCode = 37 * 1 + (int) (valueAsLongBits ^ (valueAsLongBits >> 32));
 hashCodeBuilder.iTotal *= 37;
 hashCodeBuilder.iTotal += hashCode;
 }

 int expectedHashCode = hashCodeBuilder.iTotal;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendFloat() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float value = 1.23456f;
 hashCodeBuilder.append(value);

 int expectedHashCode = 37 * 1 + Float.floatToIntBits(value);
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendFloatArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float[] floatArray = {1.23456f, 2.34567f, 3.45678f};
 hashCodeBuilder.append(floatArray);

 for (float value : floatArray) {
 int hashCode = Float.floatToIntBits(value);
 hashCodeBuilder.iTotal *= 37;
 hashCodeBuilder.iTotal += hashCode;
 }

 int expectedHashCode = hashCodeBuilder.iTotal;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendInt() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(10);

 int expectedHashCode = 37 * 1 + 10;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendIntArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int[] intArray = {1, 2, 3};
 hashCodeBuilder.append(intArray);

 int expectedHashCode = 37 * 1 + 1;
 expectedHashCode = expectedHashCode * 37 + 2;
 expectedHashCode = expectedHashCode * 37 + 3;

 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendLong() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(10L);

 int expectedHashCode = 37 * 1 + (int) (10L ^ (10L >> 32));
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendLongArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 long[] longArray = {5, 15, 25};
 hashCodeBuilder.append(longArray);

 int expectedHashCode = 37 * 1 + (int) (5 ^ (5 >> 32));
 expectedHashCode = expectedHashCode * 37 + (int) (15 ^ (15 >> 32));
 expectedHashCode = expectedHashCode * 37 + (int) (25 ^ (25 >> 32));

 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendObject() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 String object = "Hello, World!";
 hashCodeBuilder.append(object);

 int expectedHashCode = 37 * 1 + object.hashCode();
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendObjectArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 Object[] objectArray = {"Hello, World!", 1, 'A'};
 hashCodeBuilder.append(objectArray);

 for (Object obj : objectArray) {
 if (obj instanceof String) {
 int hashCode = ((String) obj).hashCode();
 } else if (obj instanceof Number) {
 int hashCode = obj.hashCode();
 } else {
 int hashCode = obj.getClass().hashCode();
 }
 hashCodeBuilder.iTotal *= 37;
 hashCodeBuilder.iTotal += hashCode;
 }

 int expectedHashCode = hashCodeBuilder.iTotal;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendShort() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((short) 10);

 int expectedHashCode = 37 * 1 + 10;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendShortArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 short[] shortArray = {5, 15, 25};
 hashCodeBuilder.append(shortArray);

 int expectedHashCode = 37 * 1 + 5;
 expectedHashCode = expectedHashCode * 37 + 15;
 expectedHashCode = expectedHashCode * 37 + 25;

 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testAppendSuperHashCode() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int superHashCode = 5;
 hashCodeBuilder.appendSuper(superHashCode);

 int expectedHashCode = 37 * 1 + superHashCode;
 int actualHashCode = hashCodeBuilder.toHashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCode() {
 Class<HashCodeBuilder> hashCodeBuilderClass = HashCodeBuilder.class;
 Object object = new HashCodeBuilder();

 int hashCode = HashCodeBuilder.reflectionHashCode(object, true);

 int expectedHashCode = 37 * 1 + hashCodeBuilderClass.hashCode() * 37 + HashCodeBuilder.class.getDeclaredFields().length * 37;

 Set<IDKey> registry = HashCodeBuilder.getRegistry();
 Field[] fields = hashCodeBuilderClass.getDeclaredFields();
 AccessibleObject.setAccessible(fields, true);

 for (Field field : fields) {
 if (!ArrayUtils.contains(excludedFields, field.getName())) {
 try {
 Object fieldValue = field.get(object);
 hashCode = HashCodeBuilder.reflectionHashCode(hashCode, 17, fieldValue.getClass(), true, null, new String[0]);
 } catch (IllegalAccessException e) {
 throw new InternalError("Unexpected IllegalAccessException");
 }
 }
 }

 Set<IDKey> registryAfterReflection = HashCodeBuilder.getRegistry();
 assertTrue(registryAfterReflection.isEmpty());

 int actualHashCode = hashCode;

 assertEquals(expectedHashCode, actualHashCode);
 }
}