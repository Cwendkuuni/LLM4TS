package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

 @BeforeClass
 static void setUpBeforeClass() {
 // Set up any static variables or objects here if necessary
 }

 @AfterClass
 static void tearDownAfterClass() {
 // Clean up any static variables or objects here if necessary
 }

 @Before
 static void setUp() {
 // Set up any instance variables or objects here if necessary
 }

 @After
 static void tearDown() {
 // Clean up any instance variables or objects here if necessary
 }

 @Test
 public void testHashCodeBuilder() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 // Test default constructor
 assertEquals(17, hashCodeBuilder.toHashCode());

 HashCodeBuilder hashCodeBuilderWithInitialNonZeroOddNumber = new HashCodeBuilder(13, 31);
 // Test constructor with initial non-zero odd number and multiplier non-zero odd number
 assertEquals(13, hashCodeBuilderWithInitialNonZeroOddNumber.iConstant);
 assertEquals(31, hashCodeBuilderWithInitialNonZeroOddNumber.iTotal);

 HashCodeBuilder hashCodeBuilderWithInitialNonZeroOddNumberAndMultiplierNonZeroOddNumber = new HashCodeBuilder(5, 43);
 // Test constructor with initial non-zero odd number, multiplier non-zero odd number, and initial value
 assertEquals(5, hashCodeBuilderWithInitialNonZeroOddNumberAndMultiplierNonZeroOddNumber.iConstant);
 assertEquals(5, hashCodeBuilderWithInitialNonZeroOddNumberAndMultiplierNonZeroOddNumber.iTotal);
 }

 @Test
 public void testAppendBoolean() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(true);
 hashCodeBuilder.append(false);
 int expectedHashCode = 33 * 37 + 1 + (1 << 31);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendBooleanArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 boolean[] booleanArray = {true, false};
 hashCodeBuilder.append(booleanArray);
 int expectedHashCode = 33 * 37 + 1 + (1 << 31); // for the first boolean
 for (boolean booleanValue : booleanArray) {
 expectedHashCode = 33 * expectedHashCode + (booleanValue ? 0 : 1);
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByte() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((byte) 5);
 hashCodeBuilder.append((byte) 10);
 int expectedHashCode = 33 * 37 + 5 + 10;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByteArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 byte[] byteArray = {(byte) 5, (byte) 10};
 hashCodeBuilder.append(byteArray);
 int expectedHashCode = 33 * 37 + 5 + 10;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendChar() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append('a');
 hashCodeBuilder.append('z');
 int expectedHashCode = 33 * 37 + 'a' + 'z';
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendCharArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 char[] charArray = {'a', 'z'};
 hashCodeBuilder.append(charArray);
 int expectedHashCode = 33 * 37 + 'a' + 'z';
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDouble() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double value = 1.5;
 hashCodeBuilder.append(value);
 long doubleValueBits = Double.doubleToLongBits(value);
 int expectedHashCode = 33 * 37 + (int) (doubleValueBits ^ (doubleValueBits >>> 32));
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDoubleArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double[] doubleArray = {1.5, 2.5};
 hashCodeBuilder.append(doubleArray);
 int expectedHashCode = 1;
 for (double doubleValue : doubleArray) {
 long doubleValueBits = Double.doubleToLongBits(doubleValue);
 expectedHashCode = 33 * expectedHashCode + (int) (doubleValueBits ^ (doubleValueBits >>> 32));
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloat() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float value = 1.5f;
 hashCodeBuilder.append(value);
 int expectedHashCode = 33 * 37 + Float.floatToIntBits(value);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloatArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float[] floatArray = {1.5f, 2.5f};
 hashCodeBuilder.append(floatArray);
 int expectedHashCode = 1;
 for (float floatValue : floatArray) {
 int floatValueIntBits = Float.floatToIntBits(floatValue);
 expectedHashCode = 33 * expectedHashCode + floatValueIntBits;
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendInt() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(10);
 hashCodeBuilder.append(5);
 int expectedHashCode = 33 * 37 + 10 + 5;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendIntArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int[] intArray = {10, 5};
 hashCodeBuilder.append(intArray);
 int expectedHashCode = 1;
 for (int intValue : intArray) {
 expectedHashCode = 33 * expectedHashCode + intValue;
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLong() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(10L);
 hashCodeBuilder.append(5L);
 int expectedHashCode = 33 * 37 + (int) (10L ^ (10L >>> 32)) + (int) (5L ^ (5L >>> 32));
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLongArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 long[] longArray = {10L, 5L};
 hashCodeBuilder.append(longArray);
 int expectedHashCode = 1;
 for (long longValue : longArray) {
 expectedHashCode = 33 * expectedHashCode + (int) (longValue ^ (longValue >>> 32));
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendObject() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 Object object = new Object();
 hashCodeBuilder.append(object);
 int expectedHashCode = 33 * 37 + object.hashCode();
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendObjectArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 Object[] objectArray = {new Object(), new Object()};
 hashCodeBuilder.append(objectArray);
 int expectedHashCode = 1;
 for (Object objectValue : objectArray) {
 expectedHashCode = 33 * expectedHashCode + objectValue.hashCode();
 }
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendShort() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((short) 5);
 hashCodeBuilder.append((short) 10);
 int expectedHashCode = 33 * 37 + 5 + 10;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendShortArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 short[] shortArray = {(short) 5, (short) 10};
 hashCodeBuilder.append(shortArray);
 int expectedHashCode = 33 * 37 + 5 + 10;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendSuperInt() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int superHashCode = 10;
 hashCodeBuilder.appendSuper(superHashCode);
 int expectedHashCode = 33 * 37 + superHashCode;
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testReflectionHashCodeWithObject() {
 int expectedHashCode = reflectionHashCode(17, 37, new Object(), false, null, new String[0]);
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(new Object());
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testReflectionHashCodeWithCollectionExcludeFields() {
 String[] excludeFields = {"excludeField"};
 int expectedHashCode = reflectionHashCode(17, 37, new Object(), false, null, excludeFields);
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(new Object(), excludeFields);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testReflectionHashCodeWithCollection() {
 int expectedHashCode = reflectionHashCode(17, 37, new Object(), false, null, new String[0]);
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(new Object());
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());

 Set<String> excludeFields = new HashSet<>();
 excludeFields.add("excludeField");
 int expectedHashCodeWithExcludeFields = reflectionHashCode(17, 37, new Object(), false, null, excludeFields.toArray(new String[0]));
 HashCodeBuilder hashCodeBuilderWithExcludeFields = new HashCodeBuilder();
 hashCodeBuilderWithExcludeFields.append(new Object(), excludeFields.toArray(new String[0]));
 assertEquals(expectedHashCodeWithExcludeFields, hashCodeBuilderWithExcludeFields.toHashCode());
 }

 @Test
 public void testReflectionHashCodeWithTestTransients() {
 int expectedHashCode = reflectionHashCode(17, 37, new Object(), true, null, new String[0]);
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(new Object(), true);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testReflectionHashCodeWithReflectUpToClass() {
 int expectedHashCode = reflectionHashCode(17, 37, new Object(), false, Object.class, new String[0]);
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(new Object(), Object.class);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testReflectionHashCodeWithReflectUpToClassAndExcludeFields() {
 String[] excludeFields = {"excludeField"};
 int expectedHashCode = reflectionHashCode(17, 37, new Object(), false, Object.class, excludeFields);
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(new Object(), Object.class, excludeFields);
 assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
 }
}