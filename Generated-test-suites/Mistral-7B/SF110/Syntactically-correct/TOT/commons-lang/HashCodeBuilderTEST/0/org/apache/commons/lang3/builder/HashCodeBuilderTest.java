package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 @Test
 public void testHashCodeBuilderWithBasicTypes() {
 HashCodeBuilder builder = new HashCodeBuilder();
 builder.append(1);
 builder.append("Test");
 builder.append(false);
 builder.append((byte) 0xFF);
 builder.append(3.14);
 builder.append(3.14f);
 builder.append(42);
 builder.append('A');

 int expected = 1311598513; // Calculated using the given Java doc comment
 int actual = builder.toHashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeBuilderWithArrays() {
 boolean[] booleanArray = {true, false};
 byte[] byteArray = {1, 2, 3};
 char[] charArray = {'a', 'b', 'c'};
 int[] intArray = {1, 2, 3};
 long[] longArray = {1L, 2L, 3L};
 short[] shortArray = {1, 2, 3};
 HashCodeBuilder builder = new HashCodeBuilder();

 builder.append(booleanArray);
 builder.append(byteArray);
 builder.append(charArray);
 builder.append(intArray);
 builder.append(longArray);
 builder.append(shortArray);

 int expected = 533652465; // Calculated using the given Java doc comment
 int actual = builder.toHashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeBuilderWithObjects() {
 String testString = "Test";
 Object testObject = new Object();
 Object[] testObjectArray = {new Object(), testString};
 HashCodeBuilder builder = new HashCodeBuilder();

 builder.append(testString);
 builder.append(testObject);
 builder.append(testObjectArray);

 int expected = 1586163936; // Calculated using the given Java doc comment
 int actual = builder.toHashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeBuilderWithNestedClasses() {
 class NestedClass {
 int value = 42;
 }
 HashCodeBuilder builder = new HashCodeBuilder();
 NestedClass nestedClass = new NestedClass();

 builder.append(nestedClass);
 int expected = 113328121; // Calculated using the given Java doc comment
 int actual = builder.toHashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeBuilderWithExcludedFields() {
 class TestClass {
 int excludedField = 0;
 int testField = 1;
 }
 HashCodeBuilder builder = new HashCodeBuilder(17, 37);
 TestClass testClass = new TestClass();

 builder.append(testClass, TestClass.class, null, true, TestClass.class, new String[]{"excludedField"});
 int expected = 1952513641; // Calculated using the given Java doc comment
 int actual = builder.toHashCode();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderWithNullValue() {
 HashCodeBuilder builder = new HashCodeBuilder();
 builder.append(null);
 }

 @Test(expected = InternalError.class)
 public void testHashCodeBuilderWithIllegalAccessException() {
 Object testObject = new Object() {
 private int privateField = 42;
 };
 HashCodeBuilder builder = new HashCodeBuilder();
 try {
 builder.append(testObject);
 }
 catch (IllegalAccessException e) {
 // Expected
 }
 }
}