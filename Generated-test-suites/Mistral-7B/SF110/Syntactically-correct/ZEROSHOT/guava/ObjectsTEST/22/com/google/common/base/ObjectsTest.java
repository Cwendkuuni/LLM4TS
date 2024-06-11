package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 assertTrue(Objects.equal(obj1, obj2));
 assertTrue(Objects.equal(obj1, obj2, " Unequal objects should be considered equal when they refer to the same instance."));
 assertFalse(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(obj1, null, "Null objects are never equal."));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object[] objects = {obj1, obj2};

 int hashCode = Objects.hashCode(objects);
 int expectedHashCode = Arrays.hashCode(objects);
 assertEquals("Incorrect hash code.", expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNullArray() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj);

 helper.add("field1", "value1");
 helper.add("field2", 42);
 helper.addValue(43.0);
 helper.omitNullValues();
 helper.addValue(null);

 String actualToString = helper.toString();
 String expectedToString = "Object{field1=value1, field2=42, field1=null}";
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testToStringHelperAddNull() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 helper.add("name", null);
 String toString = helper.toString();
 assertTrue(toString.contains("name=null"));
 }

 @Test
 public void testToStringHelperEmpty() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 String toString = helper.toString();
 assertEquals("Object{}", toString);
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object())
 .omitNullValues();
 Object obj = new Object();
 String name1 = "name1";
 String name2 = "name2";
 Object value1 = new Object();
 Object value2 = null;
 helper.add(name1, value1);
 helper.add(name2, value2);
 String toString = helper.toString();
 assertTrue(toString.contains(name1 + "=" + value1));
 assertFalse(toString.contains(name2));
 }

 @Test
 public void testToStringHelperNested() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 helper.add("outer", new Objects.ToStringHelper(MoreObjects.simpleName(Objects.class))
 .add("inner1", 1)
 .add("inner2", new Object()));
 String toString = helper.toString();
 String expectedToString = "Object{outer=Object{inner1=1, inner2=}, outer=null}";
 assertEquals(expectedToString, toString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringHelperNullName() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 helper.add(null, new Object());
 }
}