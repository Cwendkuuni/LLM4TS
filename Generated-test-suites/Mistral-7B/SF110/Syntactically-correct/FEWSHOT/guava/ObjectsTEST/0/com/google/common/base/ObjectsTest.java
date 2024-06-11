package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;

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
 Object[] objects = new Object[] { new Object(), null, new Object(), new Object(), new Object() };
 int hashCode = Objects.hashCode(objects);

 int expectedHashCode = Arrays.hashCode(new Object[] { new Object(), null, new Object(), new Object(), new Object() });
 assertEquals(expectedHashCode, hashCode);
 }

 @Test
 public void testToStringHelper_omitNullValues_false() {
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
 .add("name1", "value1")
 .add("name2", null)
 .add("name3", "value3");

 assertEquals("ClassName{name1=value1, name2=, name3=value3}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelper_omitNullValues_true() {
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
 .add("name1", "value1")
 .add("name2", null)
 .add("name3", "value3")
 .omitNullValues();

 assertEquals("ClassName{name1=value1, name3=value3}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelper_addValues() {
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
 .addValue(1)
 .addValue(true)
 .addValue("string")
 .addValue(new Object())
 .add("name1", "value1")
 .add("name2", null)
 .add("name3", "value3");

 assertEquals("ClassName{1, true, string, null, name1=value1, name3=value3}", toStringHelper.toString());
 }

}