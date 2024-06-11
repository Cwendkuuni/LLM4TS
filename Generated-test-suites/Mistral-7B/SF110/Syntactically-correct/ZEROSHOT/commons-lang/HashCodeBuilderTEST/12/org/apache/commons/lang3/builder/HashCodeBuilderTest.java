import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

 private HashCodeBuilder builder;
 private Object testObject;

 @Before
 public void setup() {
 testObject = new TestClass();
 builder = new HashCodeBuilder();
 }

 @Test
 public void testHashCodeBuilderWithDefaultValues() {
 int hashCode = builder.toHashCode();
 assertEquals(17, hashCode);
 }

 @Test
 public void testHashCodeBuilderWithCustomValues() {
 builder = new HashCodeBuilder(53, 79);
 int hashCode = builder.toHashCode();
 assertEquals(53, hashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderWithEvenInitialValue() {
 builder = new HashCodeBuilder(10, 32);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderWithEvenMultiplier() {
 builder = new HashCodeBuilder(7, 14);
 }

 @Test
 public void testHashCodeBuilderAppendBoolean() {
 builder.append(true);
 int hashCode = builder.toHashCode();
 assertEquals(32, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendBooleanArray() {
 boolean[] booleanArray = {true, false, true};
 builder.append(booleanArray);
 int hashCode = builder.toHashCode();
 assertEquals(113, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendByte() {
 builder.append((byte) 55);
 int hashCode = builder.toHashCode();
 assertEquals(11515, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendByteArray() {
 byte[] byteArray = {55, 42, 33};
 builder.append(byteArray);
 int hashCode = builder.toHashCode();
 assertEquals(1019559039, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendChar() {
 builder.append('A');
 int hashCode = builder.toHashCode();
 assertEquals(319483379, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendCharArray() {
 char[] charArray = {'A', 'B', 'C'};
 builder.append(charArray);
 int hashCode = builder.toHashCode();
 assertEquals(201399181, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendDouble() {
 builder.append(1.2);
 int hashCode = builder.toHashCode();
 assertEquals(1483401213675217, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendDoubleArray() {
 double[] doubleArray = {1.2, 2.3, 3.4};
 builder.append(doubleArray);
 int hashCode = builder.toHashCode();
 assertEquals(-8500161151085829161L, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendFloat() {
 builder.append((float) 1.2);
 int hashCode = builder.toHashCode();
 assertEquals(1595771619, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendFloatArray() {
 float[] floatArray = {1.2f, 2.3f, 3.4f};
 builder.append(floatArray);
 int hashCode = builder.toHashCode();
 assertEquals(1228332715, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendInt() {
 builder.append(10);
 int hashCode = builder.toHashCode();
 assertEquals(310547643, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendIntArray() {
 int[] intArray = {10, 20, 30};
 builder.append(intArray);
 int hashCode = builder.toHashCode();
 assertEquals(1134811485, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendLong() {
 builder.append(10L);
 int hashCode = builder.toHashCode();
 assertEquals(310547643, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendLongArray() {
 long[] longArray = {10L, 20L, 30L};
 builder.append(longArray);
 int hashCode = builder.toHashCode();
 assertEquals(2254273888953078592L, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendObject() {
 builder.append(new String("Test"));
 int hashCode = builder.toHashCode();
 assertEquals(-409620059, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendObjectArray() {
 Object[] objectArray = {new String("Test1"), new String("Test2")};
 builder.append(objectArray);
 int hashCode = builder.toHashCode();
 assertEquals(-2129267172, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendShort() {
 builder.append((short) 10);
 int hashCode = builder.toHashCode();
 assertEquals(1452165223, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendShortArray() {
 short[] shortArray = {10, 20, 30};
 builder.append(shortArray);
 int hashCode = builder.toHashCode();
 assertEquals(4194304, hashCode);
 }

 @Test
 public void testHashCodeBuilderAppendSuperHashCode() {
 builder.appendSuper(55);
 int hashCode = builder.toHashCode();
 assertEquals(311306619, hashCode);
 }

 @Test
 public void testReflectionHashCodeWithCustomValues() {
 int hashCode = HashCodeBuilder.reflectionHashCode(testObject, false);
 assertEquals(287013499, hashCode);
 }

 @Test
 public void testReflectionHashCodeWithTestTransients() {
 int hashCode = HashCodeBuilder.reflectionHashCode(testObject, true);
 assertEquals(224468729, hashCode);
 }

 @Test
 public void testReflectionHashCodeWithExcludedFields() {
 int hashCode = HashCodeBuilder.reflectionHashCode(testObject, TestClass.class, false, "field1", "field3");
 assertEquals(192605431, hashCode);
 }

 @Test
 public void testReflectionHashCodeWithExcludedFieldsAndTestTransients() {
 int hashCode = HashCodeBuilder.reflectionHashCode(testObject, TestClass.class, true, "field1", "field3");
 assertEquals(1703973269, hashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReflectionHashCodeWithNullObject() {
 HashCodeBuilder.reflectionHashCode(null, 17, 37);
 }

 @Test
 public void testReflectionHashCodeWithNullReflectUpToClass() {
 HashCodeBuilder.reflectionHashCode(testObject, null, false, new String[0]);
 }

 @Test
 public void testReflectionHashCodeWithEmptyExcludedFieldsArray() {
 HashCodeBuilder.reflectionHashCode(testObject, TestClass.class, false, new String[0]);
 }

 @Test
 public void testReflectionHashCodeWithNegativeInitialNonZeroOddNumber() {
 try {
 HashCodeBuilder.reflectionHashCode(-17, 37, testObject);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testReflectionHashCodeWithNegativeMultiplierNonZeroOddNumber() {
 try {
 HashCodeBuilder.reflectionHashCode(17, -37, testObject);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReflectionHashCodeWithNullExcludeFieldsArray() {
 HashCodeBuilder.reflectionHashCode(testObject, TestClass.class, false, null);
 }

 private static class TestClass {
 private int field1;
 private boolean field2;
 private short field3;

 public TestClass() {
 this.field1 = 10;
 this.field2 = true;
 this.field3 = 20;
 }
 }
}