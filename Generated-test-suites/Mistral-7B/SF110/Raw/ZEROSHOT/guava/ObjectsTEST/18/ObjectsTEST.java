package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 assertTrue(Objects.equal(obj1, obj2));
 assertTrue(Objects.equal(obj1, obj1));
 assertFalse(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(null, obj1));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[]{new Object(), new Object(), new Object()};
 int hashCode = Objects.hashCode(objects);
 int expectedHashCode = Arrays.hashCode(objects);
 assertEquals(expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNullInput() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 String expectedString = "ClassName{ }";
 assertEquals(expectedString, helper.toString());
 }

 @Test
 public void testToStringHelperStringName() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("fieldName", "fieldValue");
 String expectedString = "ClassName{ fieldName=fieldValue }";
 assertEquals(expectedString, helper.toString());
 }

 @Test
 public void testToStringHelperBoolean() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("booleanField", true);
 String expectedString = "ClassName{ booleanField=true }";
 assertEquals(expectedString, helper.toString());
 }

 @Test
 public void testToStringHelperOmmitNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("stringField", null);
 helper.omitNullValues();
 Objects.ToStringHelper helper2 = helper.add("booleanField", null);
 helper2.add("stringField2", "stringValue");
 String expectedString = "ClassName{ booleanField=null, stringField2=stringValue }";
 assertEquals(expectedString, helper2.toString());
 }

 @Deprecated
 @Test
 public void testToStringHelperDeprecated() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class);
 String expectedString = "Objects{ }";
 assertEquals(expectedString, helper.toString());
 }

 @Test
 public void testFirstNonNull() {
 String first = "First";
 String second = null;
 String result = Objects.firstNonNull(first, second);
 assertEquals(first, result);
 }

 @Test
 public void testFirstNonNullNullFirst() {
 String first = null;
 String second = "Second";
 String result = Objects.firstNonNull(first, second);
 assertEquals(second, result);
 }
}