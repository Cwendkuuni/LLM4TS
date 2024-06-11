package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

 @Test
 public void testHashCodeBuilder() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder hashCodeBuilderWithArgs = new HashCodeBuilder(17, 37);

 assertEquals(0, hashCodeBuilder.hashCode());
 assertEquals(17, hashCodeBuilderWithArgs.hashCode());
 }

 @Test
 public void testAppendBoolean() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(false);
 hashCodeBuilder.append(true);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendBooleanArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 boolean[] booleanArray = {false, true};
 hashCodeBuilder.append(booleanArray);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendByte() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((byte) 1);
 hashCodeBuilder.append((byte) 2);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendByteArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 byte[] byteArray = {1, 2};
 hashCodeBuilder.append(byteArray);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendChar() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append('a');
 hashCodeBuilder.append('b');

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendCharArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 char[] charArray = {'a', 'b'};
 hashCodeBuilder.append(charArray);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendDouble() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.0);
 hashCodeBuilder.append(2.0);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendDoubleArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 double[] doubleArray = {1.0, 2.0};
 hashCodeBuilder.append(doubleArray);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendFloat() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1.0f);
 hashCodeBuilder.append(2.0f);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendFloatArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 float[] floatArray = {1.0f, 2.0f};
 hashCodeBuilder.append(floatArray);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendInt() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1);
 hashCodeBuilder.append(2);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendIntArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int[] intArray = {1, 2};
 hashCodeBuilder.append(intArray);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendLong() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(1L);
 hashCodeBuilder.append(2L);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendLongArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 long[] longArray = {1L, 2L};
 hashCodeBuilder.append(longArray);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendObject() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 String object = "test";
 hashCodeBuilder.append(object);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendObjectArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 Object[] objectArray = {"test1", "test2"};
 hashCodeBuilder.append(objectArray);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendShort() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append((short) 1);
 hashCodeBuilder.append((short) 2);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendShortArray() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 short[] shortArray = {1, 2};
 hashCodeBuilder.append(shortArray);

 int hashCode = hashCodeBuilder.toHashCode();

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testAppendSuperHashCode() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 int superHashCode = 12345;
 hashCodeBuilder.appendSuper(superHashCode);

 int hashCode = hashCodeBuilder.toHashCode();

 assertEquals(superHashCode, hashCode);
 }

 @Test
 public void testReflectionHashCode() {
 Object object = new Object() {
 int a = 1;
 int b = 2;
 };

 int hashCode = HashCodeBuilder.reflectionHashCode(object);

 assertTrue(hashCode % 37 != 0);
 }

 @Test
 public void testReflectionHashCodeWithExcludeFields() {
 Object object = new Object() {
 int a = 1;
 int b = 2;
 String excludeField = "excludeField";
 };

 int hashCode = HashCodeBuilder.reflectionHashCode(object, new String[]{"excludeField"});

 assertTrue(hashCode % 37 != 0);
 }
}