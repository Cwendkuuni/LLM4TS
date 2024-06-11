package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = a;
 Object c = new Object();

 assertTrue(Objects.equal(a, b));
 assertFalse(Objects.equal(a, c));
 assertFalse(Objects.equal(null, a));
 }

 @Test(expected = NullPointerException.class)
 public void testEqualNullA() {
 Object b = new Object();
 Object c = new Object();
 Objects.equal(null, b);
 }

 @Test(expected = NullPointerException.class)
 public void testEqualNullB() {
 Object a = new Object();
 Object c = new Object();
 Objects.equal(a, null);
 }

 @Test
 public void testHashCode() {
 Object a = new Object();
 Object b = new Object();
 Object[] objects = {a, b};
 int hashCode = Objects.hashCode(objects);

 int expectedHashCode = 31 * 1 + 31 * Arrays.hashCode(objects);
 assertEquals(expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNullObjects() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Object self = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(self);
 helper.add("field1", "value1");
 helper.add("field2", 2);
 helper.add("field3", true);

 String toString = helper.toString();
 String expectedToString = "Object{" +
         "field1=value1, " +
         "field2=2, " +
         "field3=true" +
         "}";
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Object self = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(self).omitNullValues();
 helper.add("field1", null);
 helper.add("field2", 2);
 helper.add("field3", null);

 String toString = helper.toString();
 String expectedToString = "Object{" +
         "field2=2" +
         "}";
 assertEquals(expectedToString, toString);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelperNullSelf() {
 Objects.toStringHelper(null);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelperNullClassName() {
 Objects.toStringHelper(null);
 }
}