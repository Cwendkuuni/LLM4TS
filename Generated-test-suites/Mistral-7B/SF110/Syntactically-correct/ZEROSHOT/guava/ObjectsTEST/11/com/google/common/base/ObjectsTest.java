package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Objects;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = a;
 Object c = new Object();

 assertTrue(Objects.equal(a, b));
 assertFalse(Objects.equal(a, c));
 assertFalse(Objects.equal(null, a));
 assertFalse(Objects.equal(null, null));
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
 public void testHashCodeWithNull() {
 Object[] objects = new Object[1];
 objects[0] = null;

 Objects.hashCode(objects);
 }

 @Test
 public void testToStringHelper() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

 toStringHelper.add("field1", 1);
 toStringHelper.add("field2", "test");
 toStringHelper.omitNullValues();

 String expectedToString = String.format("%s{field1=1, field2=test}", className);
 String toString = toStringHelper.toString();

 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithNull() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

 toStringHelper.add("field1", null);
 toStringHelper.omitNullValues();

 String expectedToString = String.format("%s{field1=}", className);
 String toString = toStringHelper.toString();

 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithBoolean() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

 toStringHelper.add("field1", true);
 toStringHelper.omitNullValues();

 String expectedToString = String.format("%s{field1=true}", className);
 String toString = toStringHelper.toString();

 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithChar() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

 toStringHelper.add("field1", 'a');
 toStringHelper.omitNullValues();

 String expectedToString = String.format("%s{field1='a'}, ", className);
 String toString = toStringHelper.toString();

 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithDouble() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

 toStringHelper.add("field1", 1.0);
 toStringHelper.omitNullValues();

 String expectedToString = String.format("%s{field1=1.0}", className);
 String toString = toStringHelper.toString();

 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithFloat() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

 toStringHelper.add("field1", 1.0f);
 toStringHelper.omitNullValues();

 String expectedToString = String.format("%s{field1=1.0}", className);
 String toString = toStringHelper.toString();

 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithInt() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

 toStringHelper.add("field1", 1);
 toStringHelper.omitNullValues();

 String expectedToString = String.format("%s{field1=1}", className);
 String toString = toStringHelper.toString();

 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithLong() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

 toStringHelper.add("field1", 1L);
 toStringHelper.omitNullValues();

 String expectedToString = String.format("%s{field1=1}", className);
 String toString = toStringHelper.toString();

 assertEquals(expectedToString, toString);
 }
}