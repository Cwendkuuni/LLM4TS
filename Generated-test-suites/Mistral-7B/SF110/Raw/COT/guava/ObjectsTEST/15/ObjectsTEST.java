package com.example.junit.tests;

import static org.junit.Assert.*;

import com.google.common.base.Objects;

import java.util.Arrays;

public class ObjectsTest {

 private Object[] getNullObjectsArray() {
 return new Object[] { null, null };
 }

 @Test
 public void testEqual_NullNull() {
 assertTrue(Objects.equal(null, null));
 }

 @Test
 public void testEqual_NullNotNull() {
 assertFalse(Objects.equal(null, new Object()));
 }

 @Test
 public void testEqual_NotNullNull() {
 assertFalse(Objects.equal(new Object(), null));
 }

 @Test
 public void testEqual_NotNullNotNull() {
 assertTrue(Objects.equal(new Object(), new Object()));
 }

 @Test
 public void testEqual_SameInstance() {
 Object obj = new Object();
 assertTrue(Objects.equal(obj, obj));
 }

 @Test
 public void testHashCode_NullArray() {
 int expected = 0;
 int actual = Objects.hashCode(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCode_EmptyArray() {
 int expected = 0;
 int actual = Objects.hashCode(new Object[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCode_SingleObject() {
 Object obj = new Object();
 int expected = Objects.hashCode(new Object[] { obj });
 int actual = Objects.hashCode(new Object[] { obj });
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCode_MultipleObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();
 int expected = Arrays.hashCode(new Object[] { obj1, obj2, obj3 });
 int actual = Objects.hashCode(new Object[] { obj1, obj2, obj3 });
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCode_NullObjects() {
 Objects.hashCode(getNullObjectsArray());
 }

 @Test
 public void testToStringHelper_Constructor() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String expected = className + "{";
 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_OmitNullValues() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className).omitNullValues();
 String value1 = "Value 1";
 String value2 = null;
 Objects.ToStringHelper helper = toStringHelper.add("Property1", value1).add("Property2", value2);
 String expected = className + "{Property1=" + value1 + '}';
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_AddValue() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 Object value = new Object();
 Objects.ToStringHelper helper = toStringHelper.addValue(value);
 String expected = className + "{Value=" + value + '}';
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_Add_String() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String name1 = "Property1";
 String value1 = "Value 1";
 Objects.ToStringHelper helper = toStringHelper.add(name1, value1);
 String expected = className + "{Property1=" + value1 + '}';
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_Add_Boolean() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String name = "Property1";
 boolean value = true;
 Objects.ToStringHelper helper = toStringHelper.add(name, value);
 String expected = className + "{Property1=" + value + '}';
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_Add_Char() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String name = "Property1";
 char value = 'A';
 Objects.ToStringHelper helper = toStringHelper.add(name, value);
 String expected = className + "{Property1=" + value + '}';
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_Add_Double() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String name = "Property1";
 double value = 3.14;
 Objects.ToStringHelper helper = toStringHelper.add(name, value);
 String expected = className + "{Property1=" + value + '}';
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_Add_Float() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String name = "Property1";
 float value = 1.2f;
 Objects.ToStringHelper helper = toStringHelper.add(name, value);
 String expected = className + "{Property1=" + value + '}';
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_Add_Int() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String name = "Property1";
 int value = 42;
 Objects.ToStringHelper helper = toStringHelper.add(name, value);
 String expected = className + "{Property1=" + value + '}';
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_Add_Long() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String name = "Property1";
 long value = 42L;
 Objects.ToStringHelper helper = toStringHelper.add(name, value);
 String expected = className + "{Property1=" + value + '}';
 String actual = helper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelper_ToString() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className).add("Property1", "Value 1").add("Property2", "Value 2");
 String expected = className + "{Property1=Value 1, Property2=Value 2}";
 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelper_Add_NullName() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String name = null;
 Object value = new Object();
 Objects.ToStringHelper helper = toStringHelper.add(name, value);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelper_Add_NullValue() {
 String className = "ClassName";
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper(className);
 String name = "Property1";
 Object value = null;
 Objects.ToStringHelper helper = toStringHelper.add(name, value);
 }
}