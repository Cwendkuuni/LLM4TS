package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object(); // replace with actual object type and instance
 Object obj2 = obj1;
 assertTrue(Objects.equal(obj1, obj2));

 obj2 = new Object(); // different instance
 assertFalse(Objects.equal(obj1, obj2));

 obj2 = obj1; // same instance again
 assertTrue(Objects.equal(obj1, obj2));
 }

 @Test
 public void testEqualWithNull() {
 Object obj1 = null;
 Object obj2 = null;
 assertTrue(Objects.equal(obj1, obj2));

 obj1 = new Object();
 obj2 = null;
 assertFalse(Objects.equal(obj1, obj2));

 obj1 = null;
 obj2 = new Object();
 assertFalse(Objects.equal(obj1, obj2));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[3];
 objects[0] = new Object();
 objects[1] = new Object();
 objects[2] = new Object();

 int hashCode = Objects.hashCode(objects);
 int expectedHashCode = Arrays.hashCode(objects);
 assertEquals(expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullArray() {
 Object[] objects = null;
 Objects.hashCode(objects);
 }

 @Test
 public void testToStringHelper() {
 Object obj = new Object();

 ToStringHelper helper = Objects.toStringHelper(obj.getClass());
 helper.add("field1", "value1");
 helper.add("field2", obj);
 helper.omitNullValues();

 String expectedToString = "Object{" +
         "field1=value1" +
         "field2=" + obj.getClass().getName() +
         "}";
 String toString = helper.toString();
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithNullValue() {
 Object obj = new Object();

 ToStringHelper helper = Objects.toStringHelper(obj.getClass()).add("field", null);
 String toString = helper.toString();
 String expectedToString = "Object{" +
         "field=" + null + // null value is displayed as such in toString
         "}";
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithOmitNullValues() {
 Object obj = new Object();
 Object nullValue = null;

 ToStringHelper helper = Objects.toStringHelper(obj.getClass())
         .add("field1", "value1")
         .add("field2", nullValue)
         .omitNullValues();

 String expectedToString = "Object{" +
         "field1=value1" +
         "}";
 String toString = helper.toString();
 assertEquals(expectedToString, toString);
 }
}