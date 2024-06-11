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
 assertFalse(Objects.equal(obj3, obj1));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();

 int hashCode1 = Objects.hashCode(obj1);
 int hashCode2 = Objects.hashCode(obj2);
 int hashCode3 = Objects.hashCode(obj3);
 int hashCode4 = Objects.hashCode(obj1, obj2, obj3);

 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 assertNotSame(hashCode1, hashCode4);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNull() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj.getClass()).add("field1", "value1");
 String result = helper.toString();

 assertEquals("Object{field1=value1}", result);
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj.getClass()).add("field1", null).omitNullValues();
 String result = helper.toString();

 assertEquals("Object{}", result);
 }

 @Test
 public void testToStringHelperAddValues() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj.getClass())
 .add("field1", "value1")
 .addValue(true)
 .addValue(1)
 .addValue(1.0)
 .addValue(1.0f)
 .addValue(1L);
 String result = helper.toString();

 assertEquals("Object{field1=value1, true, 1, 1.0, 1.0, 1}", result);
 }
}