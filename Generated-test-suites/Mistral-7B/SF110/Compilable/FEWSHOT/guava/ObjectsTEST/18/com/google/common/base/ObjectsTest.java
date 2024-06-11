package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = obj1;

 assertTrue(Objects.equal(obj1, obj1));
 assertTrue(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(obj1, obj2));
 assertFalse(Objects.equal(obj1, null));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = obj1;

 int hashCode1 = Objects.hashCode(obj1, obj2);
 int hashCode2 = Objects.hashCode(obj2, obj1);
 int hashCode3 = Objects.hashCode(obj1, obj1);
 int hashCode4 = Objects.hashCode(obj1, null);
 int hashCode5 = Objects.hashCode(null, obj1);

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 assertNotEquals(hashCode1, hashCode5);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNullArray() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 String className = Objects.class.getName();
 Objects.ToStringHelper helper = Objects.toStringHelper(className);

 helper.add("field1", "value1");
 helper.add("field2", 2);
 helper.add("field3", null);
 helper.omitNullValues();

 assertEquals("Objects{" + "field1=value1, field2=2" + '}', helper.toString());
 }

 @Test
 public void testToStringHelperAddNullValue() {
 String className = Objects.class.getName();
 Objects.ToStringHelper helper = Objects.toStringHelper(className);

 helper.add("field1", "value1");
 helper.add("field2", null);

 assertEquals("Objects{" + "field1=value1, field2=null" + '}', helper.toString());
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 String className = Objects.class.getName();
 Objects.ToStringHelper helper = Objects.toStringHelper(className).omitNullValues();

 helper.add("field1", "value1");
 helper.add("field2", null);

 assertEquals("Objects{" + "field1=value1" + '}', helper.toString());
 }

 @Test
 public void testToStringHelperAddMultipleValues() {
 String className = Objects.class.getName();
 Objects.ToStringHelper helper = Objects.toStringHelper(className);

 helper.add("field1", "value1");
 helper.add("field2", 2);
 helper.add("field3", null);
 helper.add("field4", 4);
 helper.add("field5", 5.0);
 helper.add("field6", 6.0f);
 helper.add("field7", 7);
 helper.add("field8", 8L);

 assertEquals("Objects{" +
 "field1=value1, " +
 "field2=2, " +
 "field4=4, " +
 "field5=5.0, " +
 "field6=6.0, " +
 "field7=7, " +
 "field8=8" +
 '}', helper.toString());
 }
}