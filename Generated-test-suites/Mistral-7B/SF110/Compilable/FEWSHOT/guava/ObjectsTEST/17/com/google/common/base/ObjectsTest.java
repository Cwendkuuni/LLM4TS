package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

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
 Object a = new Object();
 Object b = new Object();
 Object c = a;

 int hashCodeA = Objects.hashCode(a);
 int hashCodeB = Objects.hashCode(b);
 int hashCodeC = Objects.hashCode(c);
 int hashCodeArray = Objects.hashCode(new Object[] { a, b, c });

 assertEquals(hashCodeA, hashCodeC); // a and c are equal
 assertNotSame(hashCodeA, hashCodeB);
 assertNotSame(hashCodeA, hashCodeArray);
 }

 @Test
 public void testToStringHelper() {
 Class<?> clazz = String.class;
 Objects.ToStringHelper helper = Objects.toStringHelper(clazz);

 helper.add("name1", "value1");
 helper.add("name2", "value2");
 helper.add("name3", null);
 helper.omitNullValues();

 assertEquals("String{name1=value1, name2=value2}", helper.toString());
 }

 @Test
 public void testToStringHelper_omitNullValues_withNullName() {
 Objects.ToStringHelper helper = Objects.toStringHelper("String");
 helper.add("name1", null);
 helper.omitNullValues();

 assertEquals("String{}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("String");
 helper.addValue("value1");
 helper.addValue(1);

 assertEquals("String{value1=value1, 1=1}", helper.toString());
 }

 @Test
 public void testToStringHelper_addValue_withNullValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("String");
 helper.addValue(null);

 assertEquals("String{}", helper.toString());
 }

 @Test
 public void testToStringHelper_add() {
 Objects.ToStringHelper helper = Objects.toStringHelper("String");
 helper.add("name1", "value1");
 helper.add("name2", "value2");
 helper.add("name3", null);
 helper.omitNullValues();

 assertEquals("String{name1=value1, name2=value2}", helper.toString());
 }

 @Test
 public void testToStringHelper_add_withNullName() {
 Objects.ToStringHelper helper = Objects.toStringHelper("String");
 helper.add(null, "value1");

 assertEquals("String{value1=value1}", helper.toString());
 }

 @Test
 public void testToStringHelper_add_withNullValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("String");
 helper.add("name1", null);

 assertEquals("String{name1=null}", helper.toString());
 }
}