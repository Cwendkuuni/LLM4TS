package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 // Test equal method
 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = a;
 assertTrue(Objects.equal(a, b));

 Object c = new Object();
 assertFalse(Objects.equal(a, c));

 Object d = null;
 assertFalse(Objects.equal(a, d));
 }

 // Test hashCode method with null and empty array
 @Test
 public void testHashCodeWithNullAndEmptyArray() {
 int hashCodeOfNull = Objects.hashCode();
 int hashCodeOfEmptyArray = Objects.hashCode(new Object[0]);

 assertEquals(0, hashCodeOfNull);
 assertEquals(0, hashCodeOfEmptyArray);
 }

 // Test hashCode method with single object
 @Test
 public void testHashCodeWithSingleObject() {
 Object obj = new Object();
 int hashCodeOfSingleObject = Objects.hashCode(obj);

 assertNotEquals(0, hashCodeOfSingleObject);
 }

 // Test hashCode method with multiple objects
 @Test
 public void testHashCodeWithMultipleObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();

 int hashCodeOfMultipleObjects = Objects.hashCode(obj1, obj2, obj3);

 assertNotEquals(0, hashCodeOfMultipleObjects);
 }

 // Test equal method with null and non-null objects
 @Test(expected = NullPointerException.class)
 public void testEqualWithNullAndNonNullObject() {
 Object a = new Object();
 Object b = null;
 Objects.equal(a, b);
 }

 // Test equal method with null object
 @Test
 public void testEqualWithNullObject() {
 Object a = null;
 Object b = null;
 assertTrue(Objects.equal(a, b));
 }

 // Test toStringHelper method with default configuration
 @Test
 public void testToStringHelperWithDefaultConfiguration() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(new Object());
 assertEquals("Object{}", toStringHelper.toString());
 }

 // Test toStringHelper method with omitNullValues set to true
 @Test
 public void testToStringHelperWithOmitNullValues() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(new Object()).omitNullValues();
 Object obj = new Object();
 Objects.ToStringHelper toStringHelperWithValue = toStringHelper.add("value", obj);
 assertEquals("Object{value=}", toStringHelperWithValue.toString());
 }

 // Test toStringHelper method with multiple values
 @Test
 public void testToStringHelperWithMultipleValues() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(new Object());
 Objects.ToStringHelper toStringHelperWithNameAndValue1 = toStringHelper.add("name1", "value1");
 Objects.ToStringHelper toStringHelperWithNameAndValue2 = toStringHelperWithNameAndValue1.add("name2", "value2");
 Objects.ToStringHelper toStringHelperWithNameAndValue3 = toStringHelperWithNameAndValue2.add("name3", "value3");
 assertEquals("Object{name1=value1, name2=value2, name3=value3}", toStringHelperWithNameAndValue3.toString());
 }
}