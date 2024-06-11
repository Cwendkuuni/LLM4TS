package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = a;
 assertTrue(Objects.equal(a, b));
 assertFalse(Objects.equal(a, new Object()));
 assertFalse(Objects.equal(null, a));
 assertFalse(Objects.equal(null, null));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[10];
 for (int i = 0; i < objects.length; i++) {
 objects[i] = new Object();
 }
 int hashCode = Objects.hashCode(objects);
 assertNotEquals(-1, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testNullArgumentsHashCode() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 assertEquals("ClassName{}", helper.toString());
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.omitNullValues();
 String obj1 = new Object();
 String obj2 = null;
 helper.add("field1", obj1).add("field2", obj2);
 assertEquals("ClassName{field1=<object>}", helper.toString());
 }

 @Test
 public void testToStringHelperAddString() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("field1", "value1");
 assertEquals("ClassName{field1=value1}", helper.toString());
 }

 @Test
 public void testToStringHelperAddBoolean() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("field1", true);
 assertEquals("ClassName{field1=true}", helper.toString());
 }

 @Test
 public void testToStringHelperAddChar() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("field1", 'c');
 assertEquals("ClassName{field1=c}", helper.toString());
 }

 @Test
 public void testToStringHelperAddDouble() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("field1", 1.2);
 assertEquals("ClassName{field1=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelperAddFloat() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("field1", 1.2f);
 assertEquals("ClassName{field1=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelperAddInt() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("field1", 1);
 assertEquals("ClassName{field1=1}", helper.toString());
 }

 @Test
 public void testToStringHelperAddLong() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("field1", 1L);
 assertEquals("ClassName{field1=1}", helper.toString());
 }

 @Test
 public void testToStringHelperAddValue() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.addValue(obj);
 assertEquals("ClassName{field1=" + obj.toString() + '}', helper.toString());
 }

 @Test
 public void testToStringHelperAddValueBoolean() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.addValue(true);
 assertEquals("ClassName{field1=true}", helper.toString());
 }

 @Test
 public void testToStringHelperAddValueChar() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.addValue('c');
 assertEquals("ClassName{field1=c}", helper.toString());
 }

 @Test
 public void testToStringHelperAddValueDouble() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.addValue(1.2);
 assertEquals("ClassName{field1=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelperAddValueFloat() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.addValue(1.2f);
 assertEquals("ClassName{field1=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelperAddValueInt() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.addValue(1);
 assertEquals("ClassName{field1=1}", helper.toString());
 }

 @Test
 public void testToStringHelperAddValueLong() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.addValue(1L);
 assertEquals("ClassName{field1=1}", helper.toString());
 }
}