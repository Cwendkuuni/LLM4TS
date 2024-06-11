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
 }

 // Test for hashCode method with null and empty array
 @Test
 public void testHashCode_NullAndEmptyArray() {
 int hashCode = Objects.hashCode();
 int hashCodeWithNull = Objects.hashCode((Object) null);
 int hashCodeWithEmptyArray = Objects.hashCode();

 assertEquals(-1, hashCode);
 assertEquals(0, hashCodeWithNull);
 assertEquals(0, hashCodeWithEmptyArray);
 }

 // Test for hashCode method with single object
 @Test
 public void testHashCode_SingleObject() {
 Object obj = new Object();
 int hashCode = Objects.hashCode(obj);

 assertEquals(hashCode, Objects.hashCode(obj));
 }

 // Test for hashCode method with multiple objects
 @Test
 public void testHashCode_MultipleObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();

 int hashCode = Objects.hashCode(obj1, obj2, obj3);
 int hashCodeWithSameObjects = Objects.hashCode(obj1, obj2, obj3, obj1, obj2, obj3);

 assertEquals(hashCode, Objects.hashCode(obj1, obj2, obj3));
 assertEquals(hashCodeWithSameObjects, Objects.hashCode(obj1, obj2, obj3, obj1, obj2, obj3));
 }

 // Test for firstNonNull method with null and non-null objects
 @Test
 public void testFirstNonNull() {
 Object first = null;
 Object second = new Object();
 Object result = Objects.firstNonNull(first, second);

 assertEquals(second, result);
 }

 // Test for firstNonNull method with both null objects
 @Test(expected = NullPointerException.class)
 public void testFirstNonNull_BothNull() {
 Object first = null;
 Object second = null;
 Objects.firstNonNull(first, second);
 }

 // Test for ToStringHelper class
 @Test
 public void testToStringHelper() {
 Object obj = new Object();
 ToStringHelper helper = new Objects.ToStringHelper("Object")
 .add("name", "value");
 String toString = helper.toString();

 assertEquals("Object{name=value}", toString);
 }

 // Test for ToStringHelper class with omitNullValues set to true
 @Test
 public void testToStringHelper_OmitNullValues() {
 Object obj = new Object();
 ToStringHelper helper = new Objects.ToStringHelper("Object")
 .add("name1", obj)
 .add("name2", null)
 .omitNullValues();
 String toString = helper.toString();

 assertEquals("Object{}", toString);
 }
}