package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = a;
 assertTrue(Objects.equal(a, b));
 assertTrue(Objects.equal(a, a));

 Object c = new Object();
 assertFalse(Objects.equal(a, c));
 assertFalse(Objects.equal(null, a));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[10];
 for (int i = 0; i < objects.length; i++) {
 objects[i] = new Object();
 }
 int hashCode = Objects.hashCode(objects);
 int expectedHashCode = Arrays.hashCode(objects);
 assertEquals(expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNull() {
 Object[] objects = {null};
 Objects.hashCode(objects);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
 .add("name", "John Doe")
 .add("age", 30)
 .addValue(true)
 .addValue(1)
 .addValue(1.5)
 .addValue('A')
 .addValue('B')
 .addValue("Hello");
 assertEquals("TestClass{name=John Doe, age=30, omitNullValues=true, value=true, value=1, value=1.5, value=A, value=B, value=Hello}", helper.toString());
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
 .add("name", null)
 .add("age", 30)
 .addValue(true)
 .addValue(1)
 .addValue(1.5)
 .addValue('A')
 .addValue('B')
 .addValue("Hello")
 .omitNullValues();
 assertEquals("TestClass{age=30, value=true, value=1, value=1.5, value=A, value=B}", helper.toString());
 }

 @Test
 public void testToStringHelperAddValueNull() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
 .add("name", null)
 .addValue(null);
 assertEquals("TestClass{name=null}", helper.toString());
 }
}