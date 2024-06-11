package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

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
 public void testHashCodeNull() {
 Object[] objects = new Object[0];
 int hashCode = Objects.hashCode(objects);
 assertEquals(0, hashCode);
 }

 @Test
 public void testHashCodeSingleObject() {
 Object obj = new Object();
 int hashCode = Objects.hashCode(obj);
 // Hashcodes are not deterministic, just ensure it's not zero
 assertNotEquals(0, hashCode);
 }

 @Test
 public void testHashCodeMultipleObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object[] objects = {obj1, obj2};
 int hashCode = Objects.hashCode(objects);
 // Hashcodes are not deterministic, just ensure it's not zero
 assertNotEquals(0, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNullArray() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 assertEquals("TestClass{}", helper.toString());
 }

 @Test
 public void testToStringHelperAddString() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("name", "value");
 assertEquals("TestClass{name=value}", helper.toString());
 }

 @Test
 public void testToStringHelperAddNullString() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("name", null);
 assertEquals("TestClass{}", helper.toString());
 }

 @Test
 public void testToStringHelperAddNullValue() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("name", new Object());
 helper.omitNullValues();
 assertEquals("TestClass{name=}", helper.toString());
 }

 @Test
 public void testToStringHelperAddMultipleValues() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("name1", "value1");
 helper.add("name2", "value2");
 helper.add("name3", "value3");
 assertEquals("TestClass{name1=value1, name2=value2, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelperAddBoolean() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("booleanValue", true);
 assertEquals("TestClass{booleanValue=true}", helper.toString());
 }

 @Test
 public void testToStringHelperAddNullBoolean() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("booleanValue", null);
 assertEquals("TestClass{}", helper.toString());
 }

 @Test
 public void testToStringHelperAddChar() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("charValue", 'a');
 assertEquals("TestClass{charValue=a}", helper.toString());
 }

 @Test
 public void testToStringHelperAddNullChar() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("charValue", null);
 assertEquals("TestClass{}", helper.toString());
 }

 @Test
 public void testToStringHelperAddDouble() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("doubleValue", 1.2);
 assertEquals("TestClass{doubleValue=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelperAddLongDouble() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("doubleValue", Double.LONG_VALUE);
 assertEquals("TestClass{doubleValue=" + Double.LONG_VALUE + "}", helper.toString());
 }

 @Test
 public void testToStringHelperAddFloat() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("floatValue", 1.2f);
 assertEquals("TestClass{floatValue=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelperAddLongFloat() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("floatValue", Long.FLOAT_VALUE);
 assertEquals("TestClass{floatValue=" + Long.FLOAT_VALUE + "}", helper.toString());
 }

 @Test
 public void testToStringHelperAddInt() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("intValue", 1);
 assertEquals("TestClass{intValue=1}", helper.toString());
 }

 @Test
 public void testToStringHelperAddLongInt() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("intValue", Long.MAX_VALUE);
 assertEquals("TestClass{intValue=" + Long.MAX_VALUE + "}", helper.toString());
 }

 @Test
 public void testToStringHelperAddLong() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("longValue", 1L);
 assertEquals("TestClass{longValue=1}", helper.toString());
 }

 @Test
 public void testToStringHelperAddNullLong() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("TestClass");
 helper.add("longValue", null);
 assertEquals("TestClass{}", helper.toString());
 }
}