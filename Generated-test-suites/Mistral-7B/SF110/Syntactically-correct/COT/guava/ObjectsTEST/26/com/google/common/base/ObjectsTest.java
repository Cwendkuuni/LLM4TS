package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 // Test equal method
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

 // Test hashCode method with null and non-null objects
 @Test
 public void testHashCode() {
 Object[] objects = new Object[10];
 for (int i = 0; i < objects.length; i++) {
 objects[i] = new Object();
 }
 int hashCode = Objects.hashCode(objects);

 Object nullObj = null;
 int nullHashCode = Objects.hashCode(nullObj);

 assertNotEqual(hashCode, nullHashCode);
 }

 // Test toStringHelper methods with various data types
 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");

 helper.add("name1", "value1");
 helper.add("name2", true);
 helper.add("name3", 'a');
 helper.add("name4", 1);
 helper.add("name5", 1L);
 helper.add("name6", 1.0);
 helper.add("name7", 1.5);
 helper.add("name8", new Object());
 helper.addValue("name9", null);
 helper.omitNullValues();

 String result = helper.toString();
 String expected = "ClassName{name1=value1, name2=true, name3=a, name4=1, name5=1, name6=1.0, name7=1.5, name8=, name9=}";
 assertEquals(expected, result);
 }

 // Test toStringHelper methods with empty list
 @Test
 public void testToStringHelperEmptyList() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 String result = helper.toString();
 String expected = "ClassName{}";
 assertEquals(expected, result);
 }

 // Test addValue method with null value
 @Test(expected = NullPointerException.class)
 public void testAddValueNullValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.addValue(null);
 }

 // Test add method with null name
 @Test(expected = NullPointerException.class)
 public void testAddNullName() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add(null, new Object());
 }

 // Test add method with null value
 @Test(expected = NullPointerException.class)
 public void testAddNullValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("name", null);
 }

 // Test add method with duplicate name
 @Test
 public void testAddDuplicateName() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("name", "value");
 helper.add("name", "anotherValue");
 String result = helper.toString();
 String expected = "ClassName{name=anotherValue}";
 assertEquals(expected, result);
 }
}