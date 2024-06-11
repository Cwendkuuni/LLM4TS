package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Objects;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;

 // Test when both objects are the same
 assertTrue(Objects.equal(obj1, obj2));

 // Test when first object is null
 Object obj3 = null;
 assertFalse(Objects.equal(obj1, obj3));

 // Test when second object is null
 assertFalse(Objects.equal(obj3, obj1));

 // Test when both objects are different
 Object obj4 = new Object();
 assertFalse(Objects.equal(obj1, obj4));
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
 public void testToStringHelperNullObject() {
 Objects.toStringHelper(null).toString();
 }

 @Test
 public void testToStringHelper() {
 Object obj = new Object();
 String className = Object.class.getSimpleName();

 ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 ToStringHelper omitNullValuesToStringHelper = toStringHelper.omitNullValues();

 // Test adding null values
 toStringHelper.add("testField1", null);
 omitNullValuesToStringHelper.add("testField2", null);
 String toString = toStringHelper.toString();
 String omitNullValuesToString = omitNullValuesToStringHelper.toString();

 // Test toString without omitNullValues
 assertTrue(toString.contains("testField1=null"));
 assertTrue(toString.contains("testField2=null"));

 // Test toString with omitNullValues
 assertFalse(omitNullValuesToString.contains("testField1="));
 assertFalse(omitNullValuesToString.contains("testField2="));
 }

 @Test
 public void testToStringHelperBasicTypes() {
 Object obj = new Object();
 String className = Object.class.getSimpleName();

 // Test adding boolean value
 ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("testField1", true);
 String toString = toStringHelper.toString();
 String expectedToString = className + "{testField1=true}";
 assertEquals(expectedToString, toString);

 // Test adding char value
 toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("testField1", 'A');
 expectedToString = className + "{testField1='A'}";
 assertEquals(expectedToString, toString.toString());

 // Test adding double value
 toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("testField1", 3.14);
 expectedToString = className + "{testField1=3.14}";
 assertEquals(expectedToString, toString.toString());

 // Test adding float value
 toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("testField1", 3.14f);
 expectedToString = className + "{testField1=3.14}";
 assertEquals(expectedToString, toString.toString());

 // Test adding int value
 toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("testField1", 1);
 expectedToString = className + "{testField1=1}";
 assertEquals(expectedToString, toString.toString());

 // Test adding long value
 toStringHelper = Objects.toStringHelper(obj);
toStringHelper.add("testField1", 1L);
 expectedToString = className + "{testField1=1}";
 assertEquals(expectedToString, toString.toString());
 }

 @Test
 public void testToStringHelperString() {
 Object obj = new Object();
 String className = Object.class.getSimpleName();

 // Test adding String value
 ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("testField1", "test string");
 String toString = toStringHelper.toString();
 String expectedToString = className + "{testField1='test string'}";
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperValues() {
 Object obj = new Object();
 String className = Object.class.getSimpleName();

 ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("testField1", obj);
 toStringHelper.addValue(obj);
 String toString = toStringHelper.toString();
 String expectedToString = className + "{testField1=" + obj.toString() + "}";
 assertEquals(expectedToString, toString);
 }
}