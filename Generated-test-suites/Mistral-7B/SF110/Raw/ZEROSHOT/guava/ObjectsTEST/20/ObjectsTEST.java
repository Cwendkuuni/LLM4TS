package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.hashCode;
import static com.google.common.base.Objects.toStringHelper;

public class ObjectsTest {

 @Test
 public void testEqual() {
 // Test equal with null objects
 Assert.assertTrue(Objects.equal(null, null));
 Assert.assertFalse(Objects.equal(null, new Object()));
 Assert.assertFalse(Objects.equal(new Object(), null));

 // Test equal with non-null objects
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = obj1;

 Assert.assertTrue(Objects.equal(obj1, obj1));
 Assert.assertTrue(Objects.equal(obj1, obj3));
 Assert.assertFalse(Objects.equal(obj1, obj2));
 }

 @Test
 public void testHashCode() {
 // Test hashCode with null object
 Assert.assertEquals(0, Objects.hashCode(null));

 // Test hashCode with single object
 Object obj = new Object();
 int hashCodeValue = obj.hashCode();

 // Test hashCode with multiple objects
 Object[] objects = {new Object(), new Object(), new Object()};
 int hashCodeValueList = Objects.hashCode(objects);

 Assert.assertNotEquals(hashCodeValue, hashCodeValueList);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelperWithNullSelf() {
 Objects.toStringHelper(null);
 }

 @Test
 public void testToStringHelperWithClass() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + '}';

 toStringHelper.omitNullValues();
 toStringHelper.add("someField", "someValue");

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithValue() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.addValue("someValue");

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithStringValue() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.add("someField", "someValue");

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithNullValue() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.add("someField", null);
 toStringHelper.omitNullValues();

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithMultipleValues() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.add("someField1", "someValue1");
 toStringHelper.add("someField2", "someValue2");

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithBooleanValue() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.add("someField", true);

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithCharacterValue() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.add("someField", 'A');

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithDoubleValue() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.add("someField", 3.14);

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithFloatValue() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.add("someField", (float) 3.14);

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithIntegerValue() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.add("someField", 42);

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringHelperWithLongValue() {
 String className = "com.google.common.base.Objects";
 ToStringHelper toStringHelper = Objects.toStringHelper(className);
 String expected = className + "{className=" + className + "}";

 toStringHelper.add("someField", 42L);

 String actual = toStringHelper.toString();
 assertEquals(expected, actual);
 }
}