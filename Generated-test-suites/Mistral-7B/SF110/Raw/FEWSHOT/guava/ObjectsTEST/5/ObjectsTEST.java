package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 assertTrue(Objects.equal(obj1, obj1));
 assertTrue(Objects.equal(obj1, obj2));
 assertFalse(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(null, obj1));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();

 int hashCode1 = Objects.hashCode(obj1);
 int hashCode2 = Objects.hashCode(obj2);
 int hashCode3 = Objects.hashCode(obj3);
 int hashCodeArray = Objects.hashCode(obj1, obj2, obj3);

 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 assertEquals(hashCode1, Objects.hashCode(obj1));
 assertEquals(hashCodeArray, Objects.hashCode(obj1, obj2, obj3));
 }

 @Test
 public void testToStringHelper_omitNullValues_false() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .add("name1", "value1")
 .add("name2", null)
 .add("name3", "value3");
 assertEquals("HelperClass{name1=value1, name2=, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelper_omitNullValues_true() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .add("name1", "value1")
 .add("name2", null)
 .add("name3", "value3")
 .omitNullValues();
 assertEquals("HelperClass{name1=value1, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .addValue("value1");
 assertEquals("HelperClass{value1=}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValue_boolean() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .addValue(true);
 assertEquals("HelperClass{value=true}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValue_char() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .addValue('a');
 assertEquals("HelperClass{value='a'}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValue_double() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .addValue(1.2);
 assertEquals("HelperClass{value=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValue_float() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .addValue((float) 1.2);
 assertEquals("HelperClass{value=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValue_int() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .addValue(1);
 assertEquals("HelperClass{value=1}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValue_long() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .addValue(1L);
 assertEquals("HelperClass{value=1}", helper.toString());
 }

 @Test
 public void testToStringHelper_add() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .add("name1", "value1")
 .add("name2", "value2")
 .add("name3", "value3");
 assertEquals("HelperClass{name1=value1, name2=value2, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelper_add_boolean() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .add("name1", "value1")
 .add("name2", true)
 .add("name3", "value3");
 assertEquals("HelperClass{name1=value1, name2=true, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelper_add_char() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .add("name1", "value1")
 .add("name2", 'a')
 .add("name3", "value3");
 assertEquals("HelperClass{name1=value1, name2=a, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelper_add_double() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .add("name1", "value1")
 .add("name2", 1.2)
 .add("name3", "value3");
 assertEquals("HelperClass{name1=value1, name2=1.2, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelper_add_float() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .add("name1", "value1")
 .add("name2", (float) 1.2)
 .add("name3", "value3");
 assertEquals("HelperClass{name1=value1, name2=1.2, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelper_add_int() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .add("name1", "value1")
 .add("name2", 1)
 .add("name3", "value3");
 assertEquals("HelperClass{name1=value1, name2=1, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelper_add_long() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("HelperClass")
 .add("name1", "value1")
 .add("name2", 1L)
 .add("name3", "value3");
 assertEquals("HelperClass{name1=value1, name2=1, name3=value3}", helper.toString());
 }
}