package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 assertTrue(Objects.equal(obj1, obj2));
 assertFalse(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(null, obj1));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[]{null, new Object(), new Object(), new Object()};
 int hashCode = Objects.hashCode(objects);
 int expectedHashCode = Arrays.hashCode(objects);
 assertEquals(expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNullArray() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");

 helper.add("name", "value1");
 helper.add("anotherName", 1);
 helper.add(1);
 helper.addValue(true);
 helper.addValue('a');
 helper.addValue(1.0);
 helper.addValue(1.5f);
 helper.addValue(2);
 helper.addValue(3L);
 helper.omitNullValues();

 String toString = helper.toString();
 String expectedToString = "TestClass{name=value1, anotherName=1, 1, true, a, 1.0, 1.5, 2, 3}";
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperAddNullValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");

 helper.add("name", "value1");
 helper.add("anotherName", null);
 helper.omitNullValues();

 String toString = helper.toString();
 String expectedToString = "TestClass{name=value1}";
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperAddNullClass() {
 Objects.ToStringHelper helper = Objects.toStringHelper(null);
 String toString = helper.toString();
 assertEquals("null", toString);
 }
}