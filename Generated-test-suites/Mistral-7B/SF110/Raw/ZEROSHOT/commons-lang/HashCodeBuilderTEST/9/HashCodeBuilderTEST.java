package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

 private static final int INITIAL_ODD_NUMBER = 17;
 private static final int MULTIPLIER_ODD_NUMBER = 37;
 private static final boolean TEST_TRANSIENTS = false;
 private static final Class<?> REFLECT_UP_TO_CLASS = Object.class;
 private static final String[] EXCLUDE_FIELDS = new String[0];

 @Test
 public void testHashCodeBuilder() {
 // Test default constructor
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 assertEquals(INITIAL_ODD_NUMBER, hashCodeBuilder.iTotal);
 assertEquals(MULTIPLIER_ODD_NUMBER, hashCodeBuilder.iConstant);

 // Test constructor with odd initial number and multiplier
 HashCodeBuilder hashCodeBuilderWithParams = new HashCodeBuilder(INITIAL_ODD_NUMBER, MULTIPLIER_ODD_NUMBER);
 assertEquals(INITIAL_ODD_NUMBER, hashCodeBuilderWithParams.iTotal);
 assertEquals(MULTIPLIER_ODD_NUMBER, hashCodeBuilderWithParams.iConstant);

 // Test append methods for primitive types
 HashCodeBuilder hashCodeBuilderWithBoolean = new HashCodeBuilder();
 hashCodeBuilderWithBoolean.append(true);
 assertEquals(true, hashCodeBuilderWithBoolean.toHashCode() % 2);

 HashCodeBuilder hashCodeBuilderWithByte = new HashCodeBuilder();
 hashCodeBuilderWithByte.append((byte) 1);
 assertEquals((byte) 1, hashCodeBuilderWithByte.toHashCode() % 256);

 HashCodeBuilder hashCodeBuilderWithChar = new HashCodeBuilder();
 hashCodeBuilderWithChar.append((char) 'a');
 assertEquals((char) 'a', hashCodeBuilderWithChar.toHashCode() % 256);

 HashCodeBuilder hashCodeBuilderWithShort = new HashCodeBuilder();
 hashCodeBuilderWithShort.append((short) 1);
 assertEquals((short) 1, hashCodeBuilderWithShort.toHashCode() % 32768);

 HashCodeBuilder hashCodeBuilderWithInt = new HashCodeBuilder();
 hashCodeBuilderWithInt.append(42);
 assertEquals(42, hashCodeBuilderWithInt.toHashCode());

 HashCodeBuilder hashCodeBuilderWithLong = new HashCodeBuilder();
 hashCodeBuilderWithLong.append(Long.MAX_VALUE);
 assertEquals(Long.MAX_VALUE, hashCodeBuilderWithLong.toHashCode());

 // Test append methods for arrays
 byte[] byteArray = {1, 2, 3};
 HashCodeBuilder hashCodeBuilderWithByteArray = new HashCodeBuilder();
 hashCodeBuilderWithByteArray.append(byteArray);
 assertArrayEquals(byteArray, (byte[]) hashCodeBuilderWithByteArray.toHashCode());

 short[] shortArray = {1, 2, 3};
 HashCodeBuilder hashCodeBuilderWithShortArray = new HashCodeBuilder();
 hashCodeBuilderWithShortArray.append(shortArray);
 assertArrayEquals(shortArray, (short[]) hashCodeBuilderWithShortArray.toHashCode());

 int[] intArray = {1, 2, 3};
 HashCodeBuilder hashCodeBuilderWithIntArray = new HashCodeBuilder();
 hashCodeBuilderWithIntArray.append(intArray);
 assertArrayEquals(intArray, (int[]) hashCodeBuilderWithIntArray.toHashCode());

 long[] longArray = {1L, 2L, 3L};
 HashCodeBuilder hashCodeBuilderWithLongArray = new HashCodeBuilder();
 hashCodeBuilderWithLongArray.append(longArray);
 assertArrayEquals(longArray, (long[]) hashCodeBuilderWithLongArray.toHashCode());

 // Test append method for Object
 Object testObject = new Object() {
 };
 HashCodeBuilder hashCodeBuilderWithObject = new HashCodeBuilder();
 hashCodeBuilderWithObject.append(testObject);
 assertEquals(testObject.hashCode(), hashCodeBuilderWithObject.toHashCode());

 // Test reflectionHashCode methods
 Object reflectionTestObject = new Object() {
 @Override
 public String toString() {
 return "ReflectionTestObject";
 }
 };
 Class<?> reflectionTestObjectClass = reflectionTestObject.getClass();
 int reflectionHashCode = HashCodeBuilder.reflectionHashCode(reflectionTestObject);
 assertEquals(reflectionTestObject.hashCode(), reflectionHashCode);

 int reflectionHashCodeWithTransients = HashCodeBuilder.reflectionHashCode(reflectionTestObject, TEST_TRANSIENTS);
 assertEquals(reflectionHashCode, reflectionHashCodeWithTransients);

 int reflectionHashCodeWithSuperclass = HashCodeBuilder.reflectionHashCode(reflectionTestObject, REFLECT_UP_TO_CLASS, TEST_TRANSIENTS, EXCLUDE_FIELDS);
 assertEquals(reflectionHashCode, reflectionHashCodeWithSuperclass);

 // Test isRegistered method
 Set<IDKey> registry = HashCodeBuilder.getRegistry();
 Object testObjectToRegister = new Object();
 assertTrue(HashCodeBuilder.isRegistered(testObjectToRegister));
 registerTestObject(testObjectToRegister);
 assertTrue(HashCodeBuilder.isRegistered(testObjectToRegister));
 unregisterTestObject(testObjectToRegister);
 assertFalse(HashCodeBuilder.isRegistered(testObjectToRegister));
 }

 private void registerTestObject(Object object) {
 HashCodeBuilder.register(object);
 }

 private void unregisterTestObject(Object object) {
 Set<IDKey> registry = HashCodeBuilder.getRegistry();
 registry.remove(new IDKey(object));
 }

 private static void assertArrayEquals(Object expected, Object actual) {
 if (expected instanceof byte[]) {
 assertEquals(expected.getClass(), actual.getClass());
 assertArrayEquals((byte[]) expected, (byte[]) actual);
 } else if (expected instanceof short[]) {
 assertEquals(expected.getClass(), actual.getClass());
 assertArrayEquals((short[]) expected, (short[]) actual);
 } else if (expected instanceof int[]) {
 assertEquals(expected.getClass(), actual.getClass());
 assertArrayEquals((int[]) expected, (int[]) actual);
 } else if (expected instanceof long[]) {
 assertEquals(expected.getClass(), actual.getClass());
 assertArrayEquals((long[]) expected, (long[]) actual);
 } else {
 super.assertArrayEquals(expected, actual);
 }
 }

}