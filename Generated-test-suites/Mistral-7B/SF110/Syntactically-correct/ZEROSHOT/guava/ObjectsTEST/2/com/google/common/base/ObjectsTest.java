package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.hashCode;

public class ObjectsTest {

 @Test
 public void testEqual() {
 // Test with null objects
 Assert.assertTrue(Objects.equal(null, null));

 // Test with same object
 Object obj1 = new Object();
 Assert.assertTrue(Objects.equal(obj1, obj1));

 // Test with null and non-null object
 Assert.assertFalse(Objects.equal(null, obj1));

 // Test with non-null and different objects
 Object obj2 = new Object();
 Assert.assertFalse(Objects.equal(obj1, obj2));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[5];
 for (int i = 0; i < objects.length; i++) {
 objects[i] = new Object();
 }

 int hashCodeValue = Objects.hashCode(objects);
 Set<Object> set = new HashSet<>();
 for (Object obj : objects) {
 set.add(obj);
 }
 int setHashCode = set.hashCode();

 Assert.assertEquals(setHashCode, hashCodeValue);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullObjects() {
 Objects.hashCode(new Object[] { null });
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");

 // Test adding values
 helper.add("field1", "value1");
 helper.add("field2", 2);
 helper.addValue(3.0);

 String result = helper.toString();
 String expected = "TestClass{field1=value1, field2=2, field3=3.0}";
 Assert.assertEquals(expected, result);

 // Test omitting null values
 helper = helper.omitNullValues();
 helper.add("field1", null);
 result = helper.toString();
 expected = "TestClass{field2=2, field3=3.0}";
 Assert.assertEquals(expected, result);
 }
}