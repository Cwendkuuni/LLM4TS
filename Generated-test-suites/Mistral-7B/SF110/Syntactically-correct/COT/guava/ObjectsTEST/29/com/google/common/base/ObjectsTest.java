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
 assertEquals(obj1.hashCode(), obj5.hashCode());
 assertFalse(Objects.equal(obj1, obj5));
 }

 // Test for hashCode method with null and empty array
 @Test
 public void testHashCodeNullAndEmptyArray() {
 int hashCode = Objects.hashCode();
 int hashCodeWithNull = Objects.hashCode((Object) null);
 int hashCodeWithEmptyArray = Objects.hashCode();

 assertEquals(0, hashCode);
 assertEquals(-1, hashCodeWithNull);
 assertEquals(hashCode, hashCodeWithEmptyArray);
 }

 // Test for hashCode method with single object
 @Test
 public void testHashCodeSingleObject() {
 Object obj1 = new Object();
 int hashCode = Objects.hashCode(obj1);

 assertNotEquals(0, hashCode);
 }

 // Test for hashCode method with multiple objects
 @Test
 public void testHashCodeMultipleObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();

 int hashCode = Objects.hashCode(obj1, obj2, obj3);

 assertNotEquals(0, hashCode);
 }

 // Test for toStringHelper methods
 @Test
 public void testToStringHelper() {
 Object obj = new Object();

 ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.omitNullValues();
 toStringHelper.add("name1", "value1");
 toStringHelper.add("name2", obj);

 String toString = toStringHelper.toString();

 String expectedToString = "Object{" +
         "name1=value1, " +
         "name2=" + obj.getClass().getSimpleName() +
         "}";

 assertEquals(expectedToString, toString);
 }

 // Test for toStringHelper methods with empty string as value
 @Test
 public void testToStringHelperEmptyString() {
 Object obj = new Object();

 ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("name", "");

 String toString = toStringHelper.toString();

 String expectedToString = "Object{" +
         "name=" +
         "}";

 assertEquals(expectedToString, toString);
 }

 // Test for toStringHelper methods with null object
 @Test(expected = NullPointerException.class)
 public void testToStringHelperNullObject() {
 Object obj = null;

 ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("name", "value");
 }
}