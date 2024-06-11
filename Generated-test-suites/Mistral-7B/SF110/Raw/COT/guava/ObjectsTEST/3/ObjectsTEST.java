package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 // Test for equal method
 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 assertTrue(Objects.equal(obj1, obj2));

 Object obj3 = new Object();
 assertFalse(Objects.equal(obj1, obj3));

 Object obj4 = null;
 assertFalse(Objects.equal(obj1, obj4));

 Object obj5 = new Object();
 assertFalse(Objects.equal(obj4, obj5));
 }

 // Test for hashCode method with null input
 @Test
 public void testHashCodeWithNullInput() {
 Object[] objects = new Object[0];
 int actualHashCode = Objects.hashCode(objects);
 int expectedHashCode = 0;
 assertEquals(expectedHashCode, actualHashCode);
 }

 // Test for hashCode method with empty array
 @Test
 public void testHashCodeWithEmptyArray() {
 Object[] objects = new Object[0];
 int actualHashCode = Objects.hashCode(objects);
 int expectedHashCode = 0; // Hashcode for empty array is 0 according to Java docs
 assertEquals(expectedHashCode, actualHashCode);
 }

 // Test for hashCode method with single object input
 @Test
 public void testHashCodeWithSingleObjectInput() {
 Object obj = new Object();
 Object[] objects = {obj};
 int actualHashCode = Objects.hashCode(objects);
 int expectedHashCode = obj.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 // Test for hashCode method with multiple objects input
 @Test
 public void testHashCodeWithMultipleObjectsInput() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object[] objects = {obj1, obj2};
 int actualHashCode = Objects.hashCode(objects);
 int expectedHashCode = obj1.hashCode() ^ obj2.hashCode(); // Hashcodes should be XOR of hashcodes of individual objects
 assertEquals(expectedHashCode, actualHashCode);
 }

 // Test for toStringHelper deprecated methods
 @Test(expected = DeprecationException.class)
 public void testToStringHelperDeprecatedMethods() {
 Object obj = new Object();
 Objects.toStringHelper(obj);
 }

 // Test for toStringHelper add methods with String name and null value
 @Test
 public void testToStringHelperAddNullValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("fieldName", null);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{fieldName=null}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper add methods with String name and non-null value
 @Test
 public void testToStringHelperAddNonNullValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("fieldName", "value");
 String actualToString = helper.toString();
 String expectedToString = "MyClass{fieldName=value}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper add methods with boolean value
 @Test
 public void testToStringHelperAddBooleanValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("fieldName", true);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{fieldName=true}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper add methods with char value
 @Test
 public void testToStringHelperAddCharValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("fieldName", 'A');
 String actualToString = helper.toString();
 String expectedToString = "MyClass{fieldName='A'}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper add methods with double value
 @Test
 public void testToStringHelperAddDoubleValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("fieldName", 3.14);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{fieldName=3.14}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper add methods with float value
 @Test
 public void testToStringHelperAddFloatValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("fieldName", 3.14f);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{fieldName=3.14}"; // Floats are treated as doubles in toStringHelper
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper add methods with int value
 @Test
 public void testToStringHelperAddIntValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("fieldName", 3);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{fieldName=3}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper add methods with long value
 @Test
 public void testToStringHelperAddLongValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("fieldName", 3L);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{fieldName=3}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper addValue methods with null value
 @Test
 public void testToStringHelperAddValueNull() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .addValue(null);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper addValue methods with boolean value
 @Test
 public void testToStringHelperAddValueBoolean() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .addValue(true);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{true}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper addValue methods with char value
 @Test
 public void testToStringHelperAddValueChar() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .addValue('A');
 String actualToString = helper.toString();
 String expectedToString = "MyClass{'A'}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper addValue methods with double value
 @Test
 public void testToStringHelperAddValueDouble() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .addValue(3.14);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{3.14}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper addValue methods with float value
 @Test
 public void testToStringHelperAddValueFloat() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .addValue(3.14f);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{3.14}"; // Floats are treated as doubles in toStringHelper
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper addValue methods with int value
 @Test
 public void testToStringHelperAddValueInt() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .addValue(3);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{3}";
 assertEquals(expectedToString, actualToString);
 }

 // Test for toStringHelper addValue methods with long value
 @Test
 public void testToStringHelperAddValueLong() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .addValue(3L);
 String actualToString = helper.toString();
 String expectedToString = "MyClass{3}";
 assertEquals(expectedToString, actualToString);
 }

}