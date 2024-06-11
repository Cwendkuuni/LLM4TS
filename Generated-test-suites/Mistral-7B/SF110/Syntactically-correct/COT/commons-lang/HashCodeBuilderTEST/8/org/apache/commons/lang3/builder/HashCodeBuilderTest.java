package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

public class HashCodeBuilderTest {

 private Object testObject;
 private HashCodeBuilder hashCodeBuilder;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 testObject = new TestClass();
 hashCodeBuilder = new HashCodeBuilder();
 }

 @Test
 public void testConstructor() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 assertEquals(17, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testConstructorWithInitialOddNumberAndMultiplierOddNumber() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(11, 13);
 assertEquals(11, hashCodeBuilder.iTotal);
 assertEquals(13, hashCodeBuilder.iConstant);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNonOddInitialNumber() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(10, 5);
 fail("IllegalArgumentException should be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNonOddMultiplierNumber() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(5, 10);
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testAppendBoolean() {
 hashCodeBuilder.append(true);
 assertEquals(32, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendBooleanArray() {
 boolean[] booleanArray = {true, false, true};
 hashCodeBuilder.append(booleanArray);
 assertEquals(107, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByte() {
 hashCodeBuilder.append((byte) 1);
 assertEquals(39, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendByteArray() {
 byte[] byteArray = {1, 2, 3};
 hashCodeBuilder.append(byteArray);
 assertEquals(521, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendChar() {
 hashCodeBuilder.append('a');
 assertEquals(115661, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendCharArray() {
 char[] charArray = {'a', 'b', 'c'};
 hashCodeBuilder.append(charArray);
 assertEquals(1368382519, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDouble() {
 hashCodeBuilder.append(1.0);
 assertEquals(6325155235449752271L, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendDoubleArray() {
 double[] doubleArray = {1.0, 2.0, 3.0};
 hashCodeBuilder.append(doubleArray);
 assertEquals(1129354247061143837L, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloat() {
 hashCodeBuilder.append(1.0f);
 assertEquals(1381562935, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendFloatArray() {
 float[] floatArray = {1.0f, 2.0f, 3.0f};
 hashCodeBuilder.append(floatArray);
 assertEquals(1712565385, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendInt() {
 hashCodeBuilder.append(1);
 assertEquals(39, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendIntArray() {
 int[] intArray = {1, 2, 3};
 hashCodeBuilder.append(intArray);
 assertEquals(1009, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLong() {
 hashCodeBuilder.append(1L);
 assertEquals(39, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendLongArray() {
 long[] longArray = {1L, 2L, 3L};
 hashCodeBuilder.append(longArray);
 assertEquals(521, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendObject() {
 hashCodeBuilder.append(testObject);
 Set<IDKey> registry = HashCodeBuilder.getRegistry();
 assertEquals(1, registry.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendNullObject() {
 hashCodeBuilder.append(null);
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testAppendObjectArray() {
 Object[] objectArray = {testObject, testObject, testObject};
 hashCodeBuilder.append(objectArray);
 Set<IDKey> registry = HashCodeBuilder.getRegistry();
 assertEquals(3, registry.size());
 }

 @Test
 public void testAppendShort() {
 hashCodeBuilder.append((short) 1);
 assertEquals(39, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendShortArray() {
 short[] shortArray = {1, 2, 3};
 hashCodeBuilder.append(shortArray);
 assertEquals(1009, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testAppendSuperHashCode() {
 hashCodeBuilder.appendSuper(123);
 assertEquals(136123, hashCodeBuilder.toHashCode());
 }

 @Test
 public void testReflectiveHashCodeWithSimpleClass() {
 Object simpleObject = new SimpleClass();
 int hashCode = HashCodeBuilder.reflectionHashCode(simpleObject);
 assertEquals(136123, hashCode);
 }

 @Test
 public void testReflectiveHashCodeWithCustomExcludeFields() {
 Object customObject = new CustomClass();
 int hashCode = HashCodeBuilder.reflectionHashCode(customObject, Arrays.asList("field1", "field2"));
 assertEquals(136127, hashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReflectiveHashCodeWithNullObject() {
 try {
 HashCodeBuilder.reflectionHashCode(null);
 fail("IllegalArgumentException should be thrown");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }
}

 class TestClass {
 // empty class
 }

 class SimpleClass {
 int i = 1;
 }

 class CustomClass {
 int field1 = 1;
 int field2 = 2;
 }