package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object(); // replace with actual object type
 Object obj2 = obj1;
 assertTrue(Objects.equal(obj1, obj2));

 Object obj3 = new Object(); // different object
 assertFalse(Objects.equal(obj1, obj3));

 Object obj4 = null;
 assertFalse(Objects.equal(obj1, obj4));
 }

 @Test(expected = NullPointerException.class)
 public void testEqualWithNullArgument() {
 Object obj1 = new Object();
 Object obj2 = null;
 Objects.equal(obj1, obj2);
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 int hashCode1 = Objects.hashCode(obj1);
 int hashCode2 = Objects.hashCode(obj2);
 assertNotSame(hashCode1, hashCode2);

 Object[] array = { obj1, obj2 };
 int hashCode3 = Objects.hashCode(array);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToStringHelper() {
 String className = "StringClass";
 Objects.ToStringHelper helper = Objects.toStringHelper(className);
 helper.add("name1", "value1");
 helper.add("name2", "value2");
 String expectedToString = className + "{name1=value1, name2=value2}";
 assertEquals(expectedToString, helper.toString());
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 String className = "StringClass";
 Objects.ToStringHelper helper = Objects.toStringHelper(className).omitNullValues();
 helper.add("name1", null);
 helper.add("name2", "value2");
 String expectedToString = className + "{name2=value2}";
 assertEquals(expectedToString, helper.toString());
 }

 @Test
 public void testToStringHelperAddValue() {
 String className = "StringClass";
 Objects.ToStringHelper helper = Objects.toStringHelper(className);
 helper.addValue(1);
 helper.addValue(2.5);
 helper.addValue(true);
 helper.addValue("Hello");
 String expectedToString = className + "{1, 2.5, true, Hello}";
 assertEquals(expectedToString, helper.toString());
 }

 @Test
 public void testToStringHelperAddNullValue() {
 String className = "StringClass";
 Objects.ToStringHelper helper = Objects.toStringHelper(className);
 helper.add("name1", null);
 String expectedToString = className + "{name1=null}";
 assertEquals(expectedToString, helper.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelperNullClassName() {
 Objects.toStringHelper(null);
 }

 @Test
 public void testToStringHelperEmpty() {
 String className = "StringClass";
 Objects.ToStringHelper helper = Objects.toStringHelper(className).omitNullValues();
 String expectedToString = className + "{}";
 assertEquals(expectedToString, helper.toString());
 }
}