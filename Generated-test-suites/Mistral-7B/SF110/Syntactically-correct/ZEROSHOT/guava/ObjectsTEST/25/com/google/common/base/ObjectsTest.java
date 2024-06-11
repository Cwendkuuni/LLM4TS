package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object(); // replace with actual object type and instance
 Object obj2 = obj1;
 assertTrue(Objects.equal(obj1, obj2));

 Object obj3 = new Object(); // different instance
 assertFalse(Objects.equal(obj1, obj3));

 Object obj4 = null;
 assertFalse(Objects.equal(obj1, obj4));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[5];
 for (int i = 0; i < objects.length; i++) {
 objects[i] = new Object(); // replace with actual object type and instance
 }

 int hashCode = Objects.hashCode(objects);

 // Test with same objects in different order
 Objects[] sameObjects = objects.clone();
 Arrays.sort(sameObjects);
 int hashCode2 = Objects.hashCode(sameObjects);
 assertEquals(hashCode, hashCode2);

 // Test with null object
 Object nullObj = null;
 int hashCode3 = Objects.hashCode(nullObj);
 int hashCode4 = Objects.hashCode(new Object[0]);
 assertEquals(hashCode3, hashCode4);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNull() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Object obj = new Object(); // replace with actual object type and instance

 ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add("field1", "value1");
 helper.add("field2", "value2");
 helper.omitNullValues();
 String expected = "Object{field1=value1, field2=value2}";
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperAddNullValue() {
 Object obj = new Object(); // replace with actual object type and instance

 ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add("field1", null);
 helper.add("field2", "value2");
 helper.omitNullValues();
 String expected = "Object{field2=value2}";
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperAddValue() {
 Object obj = new Object(); // replace with actual object type and instance

 ToStringHelper helper = Objects.toStringHelper(obj);
 helper.addValue("value1");
 String expected = "Object{value1}";
 String actual = helper.toString();
 assertEquals(expected, actual);
 }
}