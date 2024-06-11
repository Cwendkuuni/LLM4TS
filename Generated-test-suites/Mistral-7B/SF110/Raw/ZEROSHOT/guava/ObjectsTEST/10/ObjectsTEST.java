package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

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
 public void testHashCode() {
 Object[] objects = new Object[]{new Object(), new Object(), new Object()};
 int hashCode = Objects.hashCode(objects);
 int expectedHashCode = Arrays.hashCode(objects);
 assertEquals(expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNull() {
 Objects.hashCode(new Object[] {null});
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());

 helper.add("field1", "value1");
 helper.add("field2", 2);
 helper.add("field3", true);
 helper.addValue(3);
 helper.omitNullValues();

 assertEquals("Object{field1=value1, field2=2, field3=true, 3}", helper.toString());
 }

 @Test
 public void testToStringHelperWithNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());

 helper.add("field1", null);
 helper.add("field2", null);
 helper.addValue(null);
 helper.omitNullValues();

 assertEquals("Object{field1=, field2=,}", helper.toString());
 }

 @Test
 public void testToStringHelperWithoutOmitNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());

 helper.add("field1", null);
 helper.add("field2", null);
 helper.addValue(null);

 assertEquals("Object{field1=null, field2=null, ", helper.toString());
 }
}