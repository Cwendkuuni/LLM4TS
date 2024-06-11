package com.google.common.base;

import static org.junit.Assert.*;
import java.util.Arrays;

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
 public void testHashCodeWithNullObjects() {
 int hashCode = Objects.hashCode(null);
 assertEquals(-1, hashCode);
 }

 @Test
 public void testHashCodeWithSingleObject() {
 Object obj = new Object();
 int hashCode = Objects.hashCode(obj);
 assertNotEquals(-1, hashCode);
 }

 @Test
 public void testHashCodeWithMultipleObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();
 int hashCode = Objects.hashCode(obj1, obj2, obj3);
 assertNotEquals(-1, hashCode);
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
 .omitNullValues()
 .add("name1", "value1")
 .add("name2", null)
 .add("name3", "value3");
 assertEquals("ClassName{name1=value1, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelperWithoutOmitNullValues() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
 .add("name1", "value1")
 .add("name2", null)
 .add("name3", "value3");
 assertEquals("ClassName{name1=value1, name2=null, name3=value3}", helper.toString());
 }

 @Test
 public void testToStringHelperWithSingleValue() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
 .add("name", new Object());
 assertEquals("ClassName{name=java.lang.Object@52f3e2}", helper.toString());
 }

 @Test
 public void testToStringHelperWithMultipleValues() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
 .add("name1", "value1")
 .add("name2", "value2")
 .add("name3", "value3");
 assertEquals("ClassName{name1=value1, name2=value2, name3=value3}", helper.toString());
 }
}