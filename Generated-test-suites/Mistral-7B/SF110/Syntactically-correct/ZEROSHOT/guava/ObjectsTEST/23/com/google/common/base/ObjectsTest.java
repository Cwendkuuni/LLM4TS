package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

public class ObjectsTest {

 @Test
 public void testEqualNullNull() {
 boolean result = Objects.equal(null, null);
 assertTrue(result);
 }

 @Test
 public void testEqualNullObject() {
 boolean result = Objects.equal(null, new Object());
 assertFalse(result);
 }

 @Test
 public void testEqualSameObject() {
 Object obj = new Object();
 boolean result = Objects.equal(obj, obj);
 assertTrue(result);
 }

 @Test
 public void testEqualDifferentObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 boolean result = Objects.equal(obj1, obj2);
 assertFalse(result);
 }

 @Test
 public void testEqualNullAndObject() {
 Object obj = new Object();
 boolean result = Objects.equal(null, obj);
 assertFalse(result);
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[]{1, "hello", new Object()};
 int result = Objects.hashCode(objects);
 // Hashcodes are not guaranteed to be the same between JVMs, but we can check for equality
 int expectedHashcode = Arrays.hashCode(objects);
 assertEquals(expectedHashcode, result);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 String result = helper.toString();
 String expectedResult = "ClassName{}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperAdd() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 String result = helper.add("fieldName1", "value1").add("fieldName2", 2).toString();
 String expectedResult = "ClassName{fieldName1=value1, fieldName2=2}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperAddNullValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 String result = helper.add("fieldName", null).toString();
 String expectedResult = "ClassName{fieldName=null}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperAddMultipleNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 String result = helper.add("fieldName1", null).add("fieldName2", null).toString();
 String expectedResult = "ClassName{fieldName1=null, fieldName2=null}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName").omitNullValues();
 String result = helper.add("fieldName1", null).add("fieldName2", "value2").toString();
 String expectedResult = "ClassName{fieldName2=value2}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperAddMultipleNullValuesOmit() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName").omitNullValues();
 String result = helper.add("fieldName1", null).add("fieldName2", null).toString();
 String expectedResult = "";
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testToStringHelperAddDuplicateName() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("fieldName", "value1").add("fieldName", "value2");
 }

 @Test
 public void testToStringHelperAddValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 String result = helper.addValue("value1").toString();
 String expectedResult = "ClassName{value1=value1}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperAddValueArray() {
 Object[] values = new Object[]{"value1", "value2", new Object()};
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 for (Object value : values) {
 helper = helper.addValue(value);
 }
 String result = helper.toString();
 String expectedResult = "ClassName{" + String.join(", ", Arrays.asList(values)) + "}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperAddValueNull() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper = helper.addValue(null);
 String result = helper.toString();
 String expectedResult = "ClassName{value1=null}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperAddValueArrayWithNull() {
 Object[] values = new Object[]{"value1", null, "value3", new Object()};
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 for (Object value : values) {
 helper = helper.addValue(value);
 }
 String result = helper.toString();
 String expectedResult = "ClassName{" + String.join(", ", Arrays.asList(values)) + "}";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperAddValueOmitNull() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName").omitNullValues();
 helper = helper.addValue(null);
 String result = helper.toString();
 String expectedResult = "";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToStringHelperAddValueArrayWithNullOmit() {
 Object[] values = new Object[]{"value1", null, "value3", new Object()};
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName").omitNullValues();
 for (Object value : values) {
 helper = helper.addValue(value);
 }
 String result = helper.toString();
 String expectedResult = "";
 assertEquals(expectedResult, result);
 }
}