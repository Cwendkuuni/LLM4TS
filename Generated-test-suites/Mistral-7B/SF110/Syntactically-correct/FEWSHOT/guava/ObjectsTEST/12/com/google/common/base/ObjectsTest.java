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
 Object[] objects = {new Object(), new Object(), new Object()};
 int hashCode = Objects.hashCode(objects);

 assertNotEquals(hashCode, Objects.hashCode(new Object[]{}));
 assertNotEquals(hashCode, Objects.hashCode(new Object[]{null}));
 }

 @Test
 public void testToStringHelper_omitNullValues_false() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
 .add("name1", "value1")
 .add("name2", "value2")
 .add("name3", null);

 assertEquals("ClassName{name1=value1, name2=value2, name3=null}", helper.toString());
 }

 @Test
 public void testToStringHelper_omitNullValues_true() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
 .add("name1", "value1")
 .add("name2", "value2")
 .add("name3", null)
 .omitNullValues();

 assertEquals("ClassName{name1=value1, name2=value2}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValues() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
 .addValue(1)
 .addValue(2.0)
 .addValue(true)
 .addValue('a');

 assertEquals("ClassName{1, 2.0, true, a}", helper.toString());
 }

 @Test
 public void testToStringHelper_addStringValues() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
 .add("name1", "value1")
 .add("name2", "value2");

 assertEquals("ClassName{name1=value1, name2=value2}", helper.toString());
 }

 @Test
 public void testToStringHelper_addBooleanValue() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
 .add("name", true);

 assertEquals("ClassName{name=true}", helper.toString());
 }

 @Test
 public void testToStringHelper_addCharacterValue() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
 .add("name", 'a');

 assertEquals("ClassName{name=a}", helper.toString());
 }

 @Test
 public void testToStringHelper_addNullValue() {
 Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
 .add("name", null);

 assertEquals("ClassName{name=null}", helper.toString());
 }
}